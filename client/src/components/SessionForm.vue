<template>
    <nav class="navbar navbar-expand-lg navbar-light bg-dark">
        <div class="container-fluid">
            <router-link v-bind:to="mainUrl">
                <span class="navbar-brand text-white">식당리뷰시스템</span>
            </router-link>
            <div class="d-flex justify-content-end">
                <img src="img/images.jpg" height="32px" class="align-self-center img" alt="">
                <span class="align-self-center text-white" v-text="name"></span>
                <button type="button" @click="$router.push('/modify')" class="btn btn-outline-success my-sm-0" v-if="type == 'M' || type == 'O'" >정보수정</button>
                <button type="button" class="btn btn-outline-danger my-sm-0" v-on:click="logout" >로그아웃</button>
            </div>
        </div>        
    </nav>

</template>

<script>
import com from '../com.js'
export default {
    name : 'sessionForm',
    data() {
        return {
            name : '',
            type : 'G',
            mainUrl : ''
        }
    },
    created() {
        com.session.info(true)
        .then((d)=>{
            this.name = d.name
            this.type = d.type
            com.session.main()
            .then((url)=>{
                this.mainUrl = url;
            })
        }, ()=>{
            this.$router.push('/');
        })
    },
    methods : {
        logout() {
            com.session.expire()
            .then(()=>{
                alert('성공적으로 로그아웃 되었습니다.')
                location.href = '/';
            })
        },
        update() {
            
        }
    }
}
</script>
