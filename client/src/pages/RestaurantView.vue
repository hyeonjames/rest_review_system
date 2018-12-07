<template>
    <div>
        <session-form></session-form>
        <main class="container-fluid">
            <div class="media">
                <img v-bind:src="item.imageUrl" alt="" class="mr-3 img200 img-thumbnail">
                    <div class="align-self-center media-body">
                        <div class="row-sm">
                            {{item.name}} ( {{item.categoryName}} )
                        </div>
                        <div class="row-sm">
                            평점 <span class="text-warning">{{item.score}} {{'★'.repeat(Math.floor(item.score)) + '☆'.repeat(5-Math.floor(item.score))}} </span> / 리뷰수 {{item.reviewCount}}
                        </div>
                        <div class="row-sm">
                            {{item.address}} <br/>
                            {{item.phoneNumber}}
                        </div>
                    </div>
            </div>
            <textarea id="storeDetail" style="min-width: 100%" class ="textarea" rows="6" v-bind:value="item.description" readonly></textarea>
            <hr/>
                <div class="media" v-if="isOwner == false">
                    <img v-bind:src="newItem.imageUrl" v-if="newItem.imageUrl" class="mr-3 img100 img-thumbnail">
                    <div class = "media-body align-self-center">
                        <div class="d-flex justify-content-end">
                            <div class="mr-auto">
                            <select class="form-control text-warning" v-model="newItem.score">
                                <option value="1">★☆☆☆☆</option>
                                <option value="2">★★☆☆☆</option>
                                <option value="3">★★★☆☆</option>
                                <option value="4">★★★★☆</option>
                                <option value="5">★★★★★</option>
                            </select>
                        </div>
                            <label style="margin-bottom:5px; margin-right:5px;" class="btn btn-primary" for="my-file-selector">
                                <input id="my-file-selector" @change="upload" type="file" style="display:none;">
                                이미지 업로드
                            </label>
                            <button style="margin-bottom:5px;" type="button" @click="addReview" class="btn btn-primary">리뷰 남기기</button>
                        
                            </div>
                        <textarea rows="2" class="textarea col-sm-10" style="min-width:100%" v-kmodel="newItem.content"></textarea>
                    </div>
                </div>
            <hr/>
            <div class="media">
                
                <div class="align-self-center media-body">
                    <review-item v-bind:on-click="()=>{showDialog(item)}" v-bind:owner="isOwner" v-for="item in reviews" v-bind:key="item.reviewNo" v-bind:item="item"></review-item>
                    
                </div>
            </div>
            <div class="footer">
                <pagination v-bind:cur-page="$route.params.page" v-bind:max-page="totalPage" v-bind:href="`#/restaurant/${$route.params.restNo}`"></pagination>
            </div>
        </main>

    
        <div class="modal fade" id="reviewModal" tabindex="1" role="dialog" aria-labelledby="reviewModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
              <div class="modal-content">
                <div class="modal-body">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <div class="media">
                        <img v-if="currentItem.imageUrl" v-bind:src="currentItem.imageUrl" alt="" class="mr-3 img100 img-thumbnail">
                            <div class="align-self-center media-body">
                                <div class="row-sm">
                                    리뷰 아이디 : {{currentItem.writerName}}
                                </div>
                                <div class="row-sm">
                                    리뷰 점수 : <span class="text-warning"> {{'★'.repeat(currentItem.score) + '☆'.repeat(5-currentItem.score)}} </span>
                                </div>
                                <div class="row-sm">
                                    리뷰 작성일 : {{currentItem.createdAt}}
                                </div>
                            </div>
                    </div>
                    <textarea style="min-width: 100%" class ="textarea" rows="6" readonly v-bind:value="currentItem.content"></textarea>
                </div>
                <div class="modal-footer" v-if="isOwner == true && currentItem.reportState == 'NONE'">
                    <div class="container" >
                        <div class = "form-group row mr-auto ">
                            <textarea v-kmodel="reason" rows="2" class="textarea col-sm-10"></textarea>
                            <button class="btn btn-primary col" type="button" @click="report">신고</button>
                        </div>
                    </div>
                </div>
              </div>
            </div>
        </div>

    </div>
</template>

<script>
import com from '../com.js'
import $ from 'jquery'
import ReviewItem from '../components/ReviewItem.vue'
import Pagination from '../components/Pagination.vue'
import SessionForm from '../components/SessionForm.vue'
export default {
    name : 'restaurantView',
    created() {
        this.get();
        
        com.session.info()
        .then((r)=>{
            this.isOwner = r.type == 'O'
        })
    },
    components : {
        SessionForm,
        ReviewItem,
        Pagination
    },
    data() {
        return {
            item : {},
            reviews : [],
            newItem : {
                imageUrl : '',
                score : 5,
                content : ''
            },
            currentItem : {},
            isOwner : false,
            totalPage : 1,
            reason : ''
        }
    },
    watch : {
        '$route'() {
            this.get();
        }
    },
    methods : {
        get() {
            com.get(`/api/restaurant/get/${this.$route.params.restNo}`)
            .then((i)=>{
                this.item = i;
                this.getReview();
            })
        },
        getReview(){
            com.get(`/api/review/list/${this.$route.params.restNo}/10/${this.$route.params.page}`)
            .then((list)=>{
                this.reviews = list.content;
                this.totalPage = list.totalPage;
            })
        },
        upload() {
            com.upload($('#my-file-selector')[0].files[0])
            .then((url)=>{
                this.newItem.imageUrl = url;
                $('#my-file-selector').val('');
            })
        },
        showDialog(item) {
            if(this.isOwner == false && item.reportState != 'NONE') {
                return;
            }
            this.currentItem = item;
            this.reason = '';
            $('#reviewModal').modal({
                show : true
            })
        },
        report(){
            if(!this.reason) {
                alert('신고 사유를 입력해주세요.');
                return;
            }
            com.post(`api/review/report/${this.currentItem.reviewNo}`, {
                reason : this.reason
            })
            .then(()=>{
                alert('해당 리뷰가 신고 요청되었습니다.');
                this.currentItem.reportState = 'REPORTED';
                $('#reviewModal').modal('hide')
            })
        },
        addReview(){
            if(!this.newItem.content) {
                alert('내용을 입력해주세요');
                return;
            }
            com.post(`api/review/add/${this.$route.params.restNo}`, {
                content : this.newItem.content,
                imageUrl : this.newItem.imageUrl,
                score : this.newItem.score
            }).then(()=>{
                this.newItem = {
                    content : '',
                    imageUrl : '',
                    score : 5
                };
                if(this.$route.params.page == 1) {
                    this.get();
                } else {
                this.$router.push(`/restaurant/${this.$route.params.restNo}/1`);
                }
            });
        }
    }
}
</script>
