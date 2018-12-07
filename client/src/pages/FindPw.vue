<template>
    <div>
        <nav class="navbar navbar-expand-lg navbar-light bg-dark">
              <div class="container-fluid">
                  <a class="navbar-brand text-white" href="/">식당리뷰시스템</a>
              </div>
        </nav>
        <header class="header" role="header">
            <div class="container-fluid bg-primary" style="height:auto;">
                <router-link to="/find-pw">
                    <h3 class="text-white">PW 찾기</h3>
                </router-link>
            </div>
        </header>
        <main v-if="changed" class="container-fluid" role="main">
            <div class="row mb-5"></div>
            <div class="row mb-5"></div>
            <div class="row mb-5"></div>
            <h5 style="text-align:center">회원님의 비밀번호가 아래의 비밀번호로 변경되었습니다.</h5>
            <br />
            <h5 class="mb-5" style="text-align:center">원하시는 비밀번호로 변경해 주십시오.</h5>
            <div class="jumbotron ml-5 mr-5" style="text-align:center">
                <h6>{{generatedPw}}</h6>
            </div>
                    
            <div class="form-group text-center mt-5">
                <button type="button" @click="$router.push('/')" class="btn btn-primary" style="height:80px;width:300px" onclick="location.href='#'">
                    확인
                </button>
            </div>

        </main>
        <main v-if="!changed" class="container-fluid" role="main">
            <div role="form">
                <div class="form-group row mb-4 ml-5 mt-5">
                    <label for="inputID" class="col-sm-1 col-form-label">ID</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" v-model="item.id" id="inputID" placeholder="ID를 입력해주세요." />
                    </div>
                </div>
                <div class="form-group row mb-4 ml-5">
                    <label for="inputName" class="col-sm-1 col-form-label">이름</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" v-kmodel="item.name" id="inputName" placeholder="이름을 입력해주세요." />
                    </div>
                </div>
                <div class="form-group row mb-5 ml-5 ">
                    <label for="inputEmail" class="col-sm-1 col-form-label">E-mail</label>
                    <div class="col-sm-6">
                        <input type="email" class="form-control" v-model="item.email" id="inputEmail" placeholder="이메일 주소를 입력해주세요." />
                    </div>
                </div>
            </div>
            <div class="form-group text-center mt-5">
                <button type="submit" id="join-submit" class="btn btn-primary" style="height:80px;width:300px" @click="$router.push('/')">
                    취소
                </button>
                <button type="submit" class="btn btn-outline-dark" style="height:80px;width:300px" @click="find">
                    확인
                </button>
            </div>
        </main>


    </div>

</template>

<script>
import valid from '../valid.js'
import com from '../com.js'
export default {
    name : 'find-pw',
    methods : {
        find() {
            if(valid.isValid(this.item, valid.findPw))
             {
                com.post('api/user/find-pw', this.item)
                .then((r)=>{
                    this.changed = true;
                    this.generatedPw = r.pw;
                })
             }
        }
    },
    data() {
        return {
            item : {
                id : '',
                name :'',
                email : ''
            },
            generatedPw : '',
            changed : false
        }
    }
}
</script>
