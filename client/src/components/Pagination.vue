<template>
     <ul class="pagination justify-content-center">
        <li class="page-item" v-bind:class="{disabled : curPage == 1}">
            <a class="page-link" v-bind:href="`${href}/${Number(curPage)-1}`" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
                <span class="sr-only">&lt;&lt;</span>
            </a>
        </li>
        <li class="page-item" v-bind:class="{disabled : pg < 1 || pg > maxPage, active : pg==curPage}" v-for="pg in range(Math.floor((curPage-1)/10)*10 + 1, Math.floor((curPage-1)/10)*10 + 5)" v-bind:key="pg">
            <a class="page-link" v-bind:href="`${href}/${pg}`">{{pg}}</a>
        </li>
                
        <li class="page-item" v-bind:class="{disabled : curPage == maxPage}">
            <a class="page-link" v-bind:href="`${href}/${Number(curPage)+1}`" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
                <span class="sr-only">&gt;&gt;</span>
            </a>
        </li>
    </ul>

</template>

<script>
export default {
    name : 'pagination',
    props : ['curPage','maxPage', 'href'],
    data() {
        return {

        }
    },
    methods : {
        range(start , end) {
            
            var p = [];
            for(var i=start;i<=end;i++) {
                p.push(i);
            }
            return p;
        }
    }
}
</script>
