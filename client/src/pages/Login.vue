<template>
    <div>
            
        <nav class="navbar navbar-expand-lg navbar-light bg-dark">
            <div class="container-fluid">
                <router-link to="/">
                    <span class="navbar-brand text-white">식당리뷰시스템</span>
                </router-link>
            </div>        
        </nav>
        <div class="rounded border form" style="margin-top:10px;">
            <div class="form-group">
                <label for="textId">아이디</label>
                <input type="text" class="form-control" id="textId" name="txt-id" v-model="userid">
            </div>
            <div class="form-group">
                <label for="textPw">패스워드</label>
                <input type="password" class="form-control" id="textPw" name="txt-pw" v-model="userpw">
            
            </div>

            <button type="button" class="btn btn-primary" v-on:click="login">확인</button>
            <div class="form-group right">
                <button type="button" class="btn btn-primary" style="margin-right:5px;" v-on:click="join">회원 가입</button>
                
                <button type="button" class="btn btn-primary" style="margin-right:5px;" v-on:click="findId">아이디 찾기</button>
                <button type="button" class="btn btn-primary" v-on:click="findPw">비밀번호 찾기</button>
                
            </div>
        </div>
    </div>
</template>

<script>
import com from '../com.js'
export default {
    name : 'login',
    components: {

    },
    data() {
        return {
            userid : '',
            userpw : ''
        }
    },
    created(){
        com.get2('api/auth/info')
        .then((us)=>{
            com.session.cached = us;
            com.session.main()
            .then((url)=>{
                this.$router.push(url);
            });
            
        })
    },
    methods : {
        login() {
            com.post('api/auth/login', {
                id : this.userid,
                pw : this.userpw
            })
            .then((us)=>{
                com.session.cached = us;
                com.session.main()
                .then((url)=>{
                    this.$router.push(url);
                });
            })
        },
        findId() {
            this.$router.push('/find-id');
        },
        findPw() {
            this.$router.push('/find-pw');
        },
        join() {
            this.$router.push('/join/choose')
        }
    }
}
</script>