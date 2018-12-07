<template>
    <div>
        <session-form></session-form>
        <header class="header" role="header">
            <div class="container-fluid bg-primary" style="height:auto;">
                <a href="#">
                    <h3 class="text-white">{{type == 'M' ? '회원' : '식당주'}}정보수정</h3>
                </a>
            </div>
        </header>
        <main class="container-fluid" role="main">
            <form role="form">
                <div class="form-group row mb-4 ml-5 mt-5">
                    <label for="inputID" class="col-sm-1 col-form-label">ID</label>
                    <div class="col-sm-6">
                        <input type="text" id="inputID" class="form-control" v-bind:value="id" placeholder="(사용자ID)"  readonly/>
                    </div>
                    <div class="col-sm-1 offset-sm-1 ml-5">
                        <button type="button" class="btn btn-primary" style="height:40px;width:100px" @click="$router.push('/delete')">{{type == 'M' ? '회원' : '식당주'}}탈퇴</button>
                    </div>
                </div>
                <div class="form-group row mb-4 ml-5 mt-5">
                    <label for="inputName" class="col-sm-1 col-form-label">이름</label>
                    <div class="col-sm-6">
                        <input type="text" id="inputName" class="form-control" v-kmodel="name"/>
                    </div>
                </div>
                <div class="form-group row mb-4 ml-5">
                    <label for="inputPW" class="col-sm-1 col-form-label">새 비밀번호</label>
                    <div class="col-sm-6 col-sm-offset-3">
                        <input type="password" id="inputPW" class="form-control" v-model="pw" placeholder="비밀번호를 입력해주세요." />
                    </div>
                </div>
                <div class="form-group row mb-4 ml-5">
                    <label for="inputPWC" class="col-sm-1 col-form-label">비밀번호 확인</label>
                    <div class="col-sm-6">
                        <input type="password" id="inputPWC" class="form-control" v-model="pwConfirm" placeholder="비밀번호 확인을 위해 다시 입력해주세요." />
                    </div>
                </div>
                <div class="form-group row mb-5 ml-5 ">
                    <label for="inputEmail" class="col-sm-1 col-form-label">E-mail</label>
                    <div class="col-sm-6">
                        <input type="email" id="inputEmail" class="form-control" v-model="email" placeholder="이메일 주소를 입력해주세요." />
                    </div>
                </div>
            </form>
            <div class="form-group text-center mt-5">
                <button type="button" @click="$router.go(-1)" class="btn btn-primary" style="height:80px;width:300px">
                    취소
                </button>
                <button type="button" @click="update" class="btn btn-outline-dark" style="height:80px;width:300px">
                    확인
                </button>
            </div>
        </main>

    </div>
</template>


<script>
import com from '../com.js'
import valid from '../valid.js'
import SessionForm from '../components/SessionForm.vue'
export default {
    name : 'modifyView',
    components: {
        SessionForm
    },
    data() {
        return {
            id : '',
            pw : '',
            name : '',
            pwConfirm : '',
            email : '',
            type : '',
            valid : {}
        }
    },
    created() {
        com.session.info()
        .then((r)=>{
            this.type = r.type;
            this.id = r.userId;
            this.name = r.name;
            this.email = r.email;
        })
    },
    methods : {
        update(){
            if(this.pw != this.pwConfirm) {
                alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
                return;
            }
            if(!valid.isValid({
                name : this.name,
                pw : this.pw,
                pwConfirm : this.pwConfirm,
                email : this.email
            }, valid.user)) return;

            com.post(`api/${this.type == 'M' ? 'member' : 'owner'}/update`, {
                pw : this.pw,
                email : this.email,
                name : this.name
            }).then(()=>{
                alert('정보 수정이 완료되었습니다.')
                
                com.session.info(true)
                .then((r)=>{
                    let path = {
                        M : '/member/popular/0/1',
                        O : '/owner/manage'
                    }
                    this.$router.push(path[r.type]);
                })
            })
        }
    }
}
</script>
