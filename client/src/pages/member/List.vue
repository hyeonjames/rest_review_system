<template>
    <main class="container-fluid" role="main">
        <div class="row mb-5"></div>
        <search-form v-bind:addr="this.$route.params.address" v-bind:keyw="this.$route.params.keyword" v-bind:on-search="search"></search-form>
        <hr />
        
        <category-form v-if="pageType == 'P'" v-bind:cate-no="this.$route.params.cateNo"></category-form>
        <!-- Page Content -->
        <div class="container-fluid">
            <div class="row mt-3 ml-2" v-if="this.pageType == 'S'">
                '{{this.$route.params.keyword}}'로 검색한 결과 총 {{this.totalCount}}개
            </div>
            <div class="row mt-3"></div>
            <restaurant-item v-for="item in list" v-bind:key="item.restNo" v-bind:item="item"></restaurant-item>

            <hr>

            <!-- Pagination -->
            
                <pagination
                    v-if="pageType == 'P'"
                    v-bind:cur-page="$route.params.page"
                    v-bind:max-page="totalPage"
                    v-bind:href="`#/member/popular/${$route.params.cateNo}`"></pagination>
                <pagination
                    v-if="pageType == 'S'"
                    v-bind:cur-page="$route.params.page"
                    v-bind:max-page="totalPage"
                    v-bind:href="`#/member/search/${$route.params.coordX}/${$route.params.coordY}/${$route.params.address}/${$route.params.keyword}`">
                </pagination>
        </div>

    </main>

</template>

<script>
import com from '../../com.js'
import SearchForm from '../../components/SearchForm.vue'
import CategoryForm from '../../components/CategoryForm.vue'
import RestaurantItem from '../../components/RestaurantItem.vue'
import Pagination from '../../components/Pagination.vue'
export default {
    name : 'popularList',
    components : {
        SearchForm,
        CategoryForm,
        RestaurantItem ,
        Pagination
    },
    data() {
        return {
            list : [],
            pageType : 'P', // P : popular list , S: search
            totalCount : 0,
            totalPage : 1
        }
    },
    created() {
        if(this.$route.params.keyword) {
            this.pageType = 'S';
        }
        this.get();  
    },
    watch : {
        '$route'() {
            if(this.$route.params.keyword) {
                this.pageType = 'S';
            }
            this.get();
        }
    },
    methods : {
        search(data){
            this.$router.push(`/member/search/${data.coordX}/${data.coordY}/${data.address}/${data.keyword}/${this.$route.params.page}`);
        },
        get() {
            let p = null;
            if(this.pageType == 'S') {
                p = com.post(`api/restaurant/search`, {
                    coordX : this.$route.params.coordX,
                    coordY : this.$route.params.coordY,
                    text : this.$route.params.keyword,
                    page : this.$route.params.page,
                    count : 10
                });
            } else {
                p = com.get(`api/restaurant/popular/${this.$route.params.cateNo}/10/${this.$route.params.page}`);
            }
            p.then((l)=>{
                this.setData(l);
            })
        },
        setData(l) {
            this.list = l.content;
            this.totalCount = l.totalElements;
            this.totalPage = l.totalPage;
        }
    }
}
</script>

