<template>
    <div>
        <main class="container-fluid" role="main">
            {{totalElements}}건의 신고된 리뷰가 있습니다.
            <table class="table">
                <tr>
                    <th>식당/평점</th>
                    <th>작성자/리뷰 내용</th>
                    <th>신고 사유</th>
                    <th>상태</th>
                </tr>
                <tr v-if="reports.length == 0">
                    <td colspan="4" style="text-align:center;">신고된 리뷰가 없습니다.</td>
                </tr>
                <tr  v-for="item in reports" v-bind:key="item.reportNo">
                    <th width="20%">
                        {{item.restaurant.name}}<br/>
                        평점 {{item.restaurant.score}}<br/>
                        (총 {{item.restaurant.reviewCount}} 개)
                    </th>
                    <td>
                        작성자 {{item.writer}}<br/>
                        {{ item.content}}
                    </td>
                    <td>
                        {{item.reason}}
                    </td>
                    <td width="15%">
                        <div v-if="item.state == 'REPORTED'">
                            <button type="button" class="btn btn-primary" v-on:click="setState(item, 'APPROVED')">승인</button>
                            <button type="button" class="btn btn-danger" v-on:click="setState(item, 'DENYED')">거절</button>
                        </div>
                        <div v-if="item.state != 'REPORTED'">
                            <span v-show="item.state == 'APPROVED'">승인 됨</span>
                            <span v-show="item.state == 'DENYED'">거절 됨</span>
                        </div>
                    </td>
                </tr>
            </table>
            
            <pagination v-bind:max-page="totalPage" v-bind:cur-page="$route.params.page" href="/admin/review/manage"></pagination>
        </main>

    
        <div class="modal fade" id="stateNotifyModal" tabindex="-1" role="dialog" aria-labelledby="stateNotifyModal" aria-hidden="true">
            <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                <h5 class="modal-title">안내</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                </div>
                <div class="modal-body">
                <p>해당 리뷰 신고가 {{this.recentlyState == 'APPROVED' ? '승인' : '거절'}}되었습니다.</p>
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
    components : {
        Pagination
    },
    data() {
        return {
            totalPage : 1,
            reports : [],
            totalElements : 0,
            recentlyState : ''
        }
    },
    created() {
        com.get('api/manage/report/list/10/' + this.$route.params.page)
        .then((data)=>{
            this.reports = data.content;
            this.totalPage = data.totalPage;
            this.totalElements = data.totalElements;
        });
    },
    methods : {
        setState(item, state) {
            com.post(`api/manage/report/update/state/${item.reportNo}`,{
                state : state
            })
            .then(()=>{
                this.recentlyState = state;
                $('#stateNotifyModal').modal({
                    show : true
                });
                item.state = state;
            });
        }
    }
}
</script>
