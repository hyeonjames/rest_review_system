<template>
    <div>
        <main class="container-fluid" role="main">
            {{totalElements}}명의 가입한 회원이 있습니다.
            <table class="table">
                <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">이름</th>
                        <th scope="col">이메일</th>
                        <th scope="col">작성한 리뷰 수</th>
                        <th scope="col">신고된 리뷰 수</th>
                        <th scope="col"></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="item in members" v-bind:key="item.userNo">
                        <th scope="row">
                        {{item.userId}}</th>
                        <td>{{item.name}}</td>
                        <td>{{item.email}}</td>
                        <td>{{item.reviewCount}}</td>
                        <td class="text-danger">{{item.reportedCount}}</td>
                        <td><button type="button" v-on:click="askDrop(item)" class="align-self-center btn btn-danger" data-toggle="modal" data-target="#confirmModal">탈퇴</button> </td>
                    </tr>
                    <tr v-if="members.length == 0">
                        <td colspan="6" style="text-align:center;">가입한 회원이 없습니다.</td>
                    </tr>

                </tbody>
            </table>
            <pagination v-bind:max-page="totalPage" v-bind:cur-page="$route.params.page" href="/admin/member/manage"></pagination>
        
        </main>

        
        <div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="confirmModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                <h5 class="modal-title">안내</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                </div>
                <div class="modal-body">
                <p>해당 회원을 정말로 탈퇴시키시겠습니까?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal" v-on:click="drop">동의</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">거절</button>
                </div>
            </div>
            </div>
        </div>
        <div class="modal fade" id="notifyModal" tabindex="-1" role="dialog" aria-labelledby="NotifyModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">안내</h5>
                  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <p>해당 회원이 정상적으로 탈퇴되었습니다.</p>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
              </div>
            </div>
        </div>
    </div>
</template>

<script>
import com from '../../com.js'
import $ from 'jquery'
import Pagination from '../../components/Pagination.vue'
export default {
    name :'memberManagement',
    components : {
        Pagination
    },
    data(){
        return {
            members : [],
            totalElements : 0,
            totalPage : 0,
            currentItem : null,
        }
    },
    created(){
        this.update();
    },
    methods : {
        update() {
            
            com.get(`api/manage/member/list/10/${this.$route.params.page}`)
            .then((d)=>{
                this.members = d.content;
                this.totalPage = d.totalPage;
                this.totalElements = d.totalElements;
            })
        },
        askDrop(item) {
            this.currentItem = item;
            $('#confirmModal').modal({
                show : true,
                backdrop : 'static'
            })
        },
        drop(){
            com.post(`api/manage/member/drop/${this.currentItem.userNo}`)
            .then(()=>{
                
                this.update();
                $('#notifyModal').modal({
                    show : true
                });
            });
        }
    }
}
</script>
