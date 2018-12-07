<template>
    
    <nav class="navbar navbar-expand-md bg-secondary ">
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav nav-tabs text-md-center nav-justified w-100">
                    <li class="nav-item" v-bind:class="{active : cateNo == cate.cateNo}" v-for="cate in categories" v-bind:key="cate.cateNo">
                        <router-link class="nav-link text-white" v-bind:to="`/member/popular/${cate.cateNo}/1`">
                        <span class="check-item" v-if="cateNo == cate.cateNo">&#x2714;</span>
                        {{cate.name}}</router-link>
                    </li>
                </ul>
            </div>
        </nav>
</template>

<script>
import com from '../com.js'
export default {
    name : 'categoryForm',
    props : ['cateNo'],
    data() {
        return {
            categories : [{cateNo : 0, name : '전체'}].concat(com.categories())
        }
    },
    created() {
    },
    methods : {

    }
}
</script>
