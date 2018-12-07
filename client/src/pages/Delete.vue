<template>
    <div>
        <session-form></session-form>
        <header class="header" role="header">
            <div class="container-fluid bg-primary" style="height:auto;">
                <a href="#">
                    <h3 class="text-white">{{this.type == 'M' ? '회원' : '식당주'}}탈퇴</h3>
                </a>
            </div>
        </header>
        <main class="container-fluid" role="main">
            <div class="row mb-5"></div>
            <form role="form">
                <div class="form-group row mb-4 ml-5 mt-5">
                    <label for="inputID" class="col-sm-1 col-form-label">ID</label>
                    <div class="col-sm-6">
                        <input type="text" id="inputID" class="form-control" v-model="id" placeholder="ID를 입력해주세요." />
                    </div>
                </div>
                <div class="form-group row mb-4 ml-5">
                    <label for="inputPW" class="col-sm-1 col-form-label">비밀번호</label>
                    <div class="col-sm-6 col-sm-offset-3">
                        <input type="password" id="inputPW" class="form-control" v-model="pw" placeholder="비밀번호를 입력해주세요." />
                    </div>
                </div>
            </form>
            <div class="row mb-5"></div>
            <div class="row mb-5"></div>
            <div class="row mb-5"></div>
            <h5 class="ml-5">※ 주의</h5>
            <br />
            <h5 class="mb-5 ml-5">고객님의 정보는 삭제되지만 고객님께서 작성하신 리뷰 및 평점은 삭제되지 않습니다.</h5>
            <div class="row mb-5"></div>
            <div class="form-group text-center mt-5">
                <button type="button" id="join-submit" class="btn btn-primary" style="height:80px;width:300px" @click="$router.go(-1)">
                    취소
                </button>
                <button type="button" class="btn btn-outline-dark" style="height:80px;width:300px" @click="del">
                    확인
                </button>
            </div>
        </main>

    </div>
</template>

<script>
import com from '../com.js';
import SessionForm from '../components/SessionForm.vue'
export default {
    
    name : 'deleteView',
    data() {
        return {
            id : '',
            pw : '',
            type: ''
        }
    },
    components : {
        SessionForm
    },
    created(){
        com.session.info()
        .then((r)=>{
            this.type = r.type;
        }) 
    },
    methods : {
        del() {
            com.post(`api/${this.type == 'M' ? 'member' : 'owner'}/delete`, {
                id : this.id,
                pw : this.pw
            }).then(()=>{
                alert('탈퇴가 완료 되었습니다. 로그인 화면으로 돌아갑니다.');
                location.href = '/#/';
            })
        }
    }
}
</script>
