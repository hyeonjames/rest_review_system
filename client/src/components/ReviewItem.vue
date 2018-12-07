<template>
    <div class="row-sm review-item">
        <div @click="onClick" v-if="item.reportState != 'APPROVED' || owner == true">
            <div>
            <img v-if="item.imageUrl" v-bind:src="item.imageUrl" class="mr-3 img-thumbnail img50">
            {{item.writerName}} <span class="text-warning"> {{'★'.repeat(item.score) + '☆'.repeat(5-item.score)}} </span>   {{item.createdAt}}
            <span class="text-danger right" v-if="owner == true && item.reportState == 'REPORTED'">신고처리대기</span>
            <span class="text-danger right" v-if="owner == true && item.reportState == 'APPROVED'">신고처리완료</span>
            <span class="text-danger right" v-if="owner == true && item.reportState == 'DENYED'">신고처리거절</span>
            
            </div>
            <textarea readonly rows="3" style="min-width: 100%" v-bind:value="item.content"></textarea>
        </div>
        <div v-if="owner == false && item.reportState == 'APPROVED'">
            <img v-if="item.imageUrl" v-bind:src="item.imageUrl" class="mr-3 img-thumbnail img50">
            <div>
                 <span class="text-danger"> {{item.writerName}} {{item.createdAt}} 작성</span>   
            </div>
            <textarea readonly rows="3" style="min-width: 100%; font-color:red;">신고된 리뷰 입니다.</textarea>
        </div>
    </div>
</template>
<script>
export default {
    name : 'reviewItem',
    props : [
        'item', 'owner','onClick'
    ],
    methods : {

    }
}
</script>
