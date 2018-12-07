<template>
    <div>
        <header class="header" role="header">
                <div class="container-fluid bg-primary" style="height:auto;">
                    <a href="#/owner/manage">
                        <h3 class="text-white">식당 관리</h3>
                    </a>
                </div>
            </header>
        <main role="main" class="container-fluid bg-light">
            
            
            <div class="row" style="padding-bottom:10px; height:150px;" v-if="list.length == 0">
                <div class="col">
                    <center>
                        <h3>관리하고 있는 식당이 없습니다.</h3>
                    </center>
                </div>
            </div>

            <div class="row" style="padding-bottom:10px;" v-for="item in list" v-bind:key="item.restNo">
                <div class="col" @click="$router.push(`/restaurant/${item.restNo}/1`)">
                    <div class="media">
                        <img v-bind:src="item.imageUrl" alt="" class="mr-3 img-thumbnail img150">
                        <div class="media-body">
                            <div class="row-sm">
                                {{item.name}}
                            </div>
                            <div class="row-sm">
                                {{item.score}} ({{item.reviewCount}} 개의 리뷰)
                            </div>
                        </div>
                    </div>
                </div>
                <div class="align-self-center col-2">
                    <button type="button" class="btn btn-primary" @click="showUpdate(item)">수정</button>
                    <button type="button" class="btn btn-secondary" @click="del(item)">삭제</button>
                </div>
            </div>
        </main>
        <footer class="bg-primary text-white">
            <span style="cursor:pointer;" class="align-self-center" @click="showUpdate()">
                <h3 class="text-white text-center">식당 추가 버튼</h3>
            </span>
        </footer>

        <div class="modal fade" id="modifyModal" tabindex="-1" role="dialog" aria-labelledby="ModifyModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                <h5 class="modal-title">식당 {{currentItem.restNo == 0 ? '추가' : '수정'}}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="inputName">식당 이름</label>
                                <input type="text" class="form-control" id="inputName" aria-describedby="inputName" placeholder="식당 이름" v-kmodel="currentItem.name">
                            </div>
                            <div class="form-group col-md-6">
                                <label for="inputPic">식당 사진</label><br>
                                <label class="btn btn-secondary" for="my-file-selector">
                                        <input id="my-file-selector" @change="upload" type="file" style="display:none">
                                        업로드
                                </label>
                                <span class='label label-info' id="upload-file-info">{{currentItem.imageUrl}}</span>            
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputAddr">식당 주소</label>
                            <div class="form-row">
                                <div class="col">
                                    <input type="text" v-bind:value="currentItem.address" class="form-control" placeholder="식당 주소" readonly>
                                </div>
                                <div class="col-3">
                                    <button type="button" @click="findAddress" class="btn btn-secondary mb-2">주소 찾기</button>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputFood">음식 종류</label>
                            <select class="form-control" v-model="currentItem.categoryNo">
                                <option v-for="item in categories" v-bind:key="item.cateNo" v-bind:value="item.cateNo">{{item.name}}
                                </option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="inputTel">전화번호</label>
                            <input type="phone" class="form-control" v-model="currentItem.phoneNumber" placeholder="02-1234-5678">
                        </div>
                        <div class="form-group">
                            <label for="inputDetail">상세소개</label>
                            <textarea class="form-control" v-model="currentItem.description" rows="3"></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" v-on:click="update">{{currentItem.restNo > 0 ? '수정'  :'등록'}}</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                </div>
            </div>
            </div>
        </div>
        


    </div>

</template>

<script>
import $ from 'jquery'
import valid from '../../valid.js'
import com from '../../com.js'
export default {
    name : 'restaurantManagement',
    data() {
        return {
            categories : com.categories(),
            list : [],
            currentItem : {}
        }
    },
    created() {
        this.getList();
    },
    methods : {
        findAddress(){
            com.getAddress((data)=>{
                this.currentItem.address = data.address;
                
            })
        },
        del(item) {
            com.post(`api/restaurant/delete/${item.restNo}`)
            .then(()=>{
                this.getList();
                alert('해당 식당이 삭제 되었습니다.');
            })
        },
        getList() {
            com.get('api/restaurant/get/own-list')
            .then((l)=>{
                this.list = l;
            })
        },
        upload(e) {
            var file = e.target.files[0];
            com.upload(file)
            .then((url)=>{
                this.currentItem.imageUrl = url;
            })
        },
        update() {
            let i = this.currentItem;
            let dt = {
                name : i.name,
                description : i.description,
                address : i.address,
                imageUrl : i.imageUrl,
                phoneNumber: i.phoneNumber,
                categoryNo : i.categoryNo
            };
            if(!valid.isValid(dt, valid.restaurant)) return;

            com.getCoord(i.address, (c)=>{
                i.coordX = c.x;
                i.coordY = c.y;
                dt.coordX = c.x;
                dt.coordY = c.y;
                
                
                let p = null;
                if(i.restNo == 0) {
                    p=com.post(`api/restaurant/add`, dt);
                } else {
                    p = com.post(`api/restaurant/update/${i.restNo}`, dt)
                }
                p.then(()=>{
                    this.getList();
                    $('#modifyModal').modal('hide');
                    alert('식당 추가/수정이 완료되었습니다.');
                });
            }, ()=>{
                alert('해당 주소를 찾을 수 없습니다.');
            });
            
        },
        showUpdate(item) {
            this.currentItem = item || {
                restNo : 0,
                name : '',
                address : '',
                imageUrl : '',
                categoryNo : this.categories[0].cateNo
            };
            $('#modifyModal').modal({
                show : true,
                backdrop : 'static'
            });

        }
    }
}
</script>
