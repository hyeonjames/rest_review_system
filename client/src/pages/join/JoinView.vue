<template>
    <div>
        <header class="header" role="header">
            <div class="container-fluid bg-primary" style="height:auto;">
                <a href="#">
                    <h3 class="text-white">{{type == 'M' ? '회원' : '식당주'}} 가입</h3>
                </a>
            </div>
        </header>
        <main class="container-fluid" role="main">
            <form role="form">
                <div class="form-group row mb-4 ml-5 mt-5">
                    <label for="inputID" class="col-sm-1 col-form-label">ID</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="inputID" v-model="userid" @change="confirmed=false" placeholder="ID를 입력해주세요." />
                    </div>
                    <button type="button" class="btn btn-primary" v-on:click="checkId" >ID중복확인</button>
                </div>
                <div class="form-group row mb-4 ml-5">
                    <label for="inputName" class="col-sm-1 col-form-label">이름</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="inputName" v-kmodel="name" placeholder="이름을 입력해주세요." />
                    </div>
                </div>
                <div class="form-group row mb-4 ml-5">
                    <label for="inputPW" class="col-sm-1 col-form-label">비밀번호</label>
                    <div class="col-sm-6 col-sm-offset-3">
                        <input type="password" class="form-control" id="inputPW" v-model="pw" placeholder="비밀번호를 입력해주세요." />
                    </div>
                </div>
                <div class="form-group row mb-4 ml-5">
                    <label for="inputPWC" class="col-sm-1 col-form-label">비밀번호 확인</label>
                    <div class="col-sm-6">
                        <input type="password" class="form-control" id="inputPWC" v-model="pwConfirm" placeholder="비밀번호 확인을 위해 다시 입력해주세요." />
                    </div>
                </div>
                <div class="form-group row mb-4 ml-5">
                    <label for="inputEmail" class="col-sm-1 col-form-label">E-mail</label>
                    <div class="col-sm-6">
                        <input type="email" class="form-control" id="inputEmail" v-model="email" placeholder="이메일 주소를 입력해주세요." />
                    </div>
                </div>
            </form>
            <div class="form-group text-center mt-5">
                <button type="submit" id="join-submit" class="btn btn-primary" style="height:80px;width:300px" onclick="location.href='#/'">
                    취소
                </button>
                <button type="submit" class="btn btn-outline-dark" style="height:80px;width:300px" v-on:click="join">
                    확인
                </button>
            </div>
        </main>
        

    </div>
</template>

<script>
import com from '../../com.js'
import valid from '../../valid.js'
export default {
    name : 'joinView',
    data(){
        return {
            userid : '',
            name : '',
            email : '',
            pw : '',
            pwConfirm : '',
            type : 'M',
            confirmed : false,
            valid : {
            }
        }
    },
    created(){
        if(location.hash.indexOf('/join/member') >= 0){
            this.type = 'M';
        } else {
            this.type = 'O';
        }
    },
    methods : {
        checkId(){
            if(!this.userid) {
                alert(
                    '아이디를 입력해주세요.'
                );
                return;
            }
            com.get(`api/user/check-id/${encodeURIComponent(this.userid)}`)
            .then(( {value} )=>{
                if(value) {
                    this.confirmed = true;
                    alert('사용 가능한 아이디입니다.')
                } else {
                    alert('사용이 불가능한 아이디입니다.')
                }
            })
        },
        join(){
            if(!this.confirmed) {
                alert('아이디 중복확인이 필요합니다.');
                return;
            }
            if(this.pw != this.pwConfirm) {
                alert('비밀번호와 비밀번호 확인이 다릅니다.');
                return;
            }
            if(!valid.isValid({
                id : this.userid,
                pw : this.pw,
                pwConfirm : this.pwConfirm,
                email : this.email,
                name : this.name
            }, valid.user)) return;

            com.post(`/api/${this.type == 'M' ? 'member' : 'owner'}/join`, {
                id : this.userid,
                pw : this.pw,
                email : this.email,
                name : this.name
            }).then(()=>{
                alert(`${this.type == 'M' ? '회원' : '식당주'} 가입이 완료되었습니다.`)
                this.$router.push('/');
            });

        }
    }
}
</script>