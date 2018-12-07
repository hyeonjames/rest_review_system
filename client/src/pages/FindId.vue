<template>
    <div>
        <nav class="navbar navbar-expand-lg navbar-light bg-dark">
              <div class="container-fluid">
                  <a class="navbar-brand text-white" href="/">식당리뷰시스템</a>
              </div>
        </nav>

        <header class="header" role="header">
            <div class="container-fluid bg-primary" style="height:auto;">
                <router-link to="/find-id">
                    <h3 class="text-white">ID 찾기</h3>
                </router-link>
            </div>
        </header>
        <main v-if="!found" class="container-fluid" role="main">
            <form role="form">
                <div class="form-group row mb-4 ml-5 mt-5">
                    <label for="inputName" class="col-sm-1 col-form-label">이름</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="inputName" v-kmodel="item.name" placeholder="이름을 입력해주세요." />
                    </div>
                </div>
                <div class="form-group row mb-5 ml-5 ">
                    <label for="inputEmail" class="col-sm-1 col-form-label">E-mail</label>
                    <div class="col-sm-6">
                        <input type="email" class="form-control" id="inputEmail" v-model="item.email" placeholder="이메일 주소를 입력해주세요." />
                    </div>
                </div>
            </form>
            <div class="form-group text-center mt-5">
                <button type="button" id="join-submit" @click="$router.push('/')" class="btn btn-primary" style="height:80px;width:300px">
                    취소
                </button>
                <button type="button" @click="find" class="btn btn-outline-dark" style="height:80px;width:300px">
                    확인
                </button>
            </div>
        </main>
        <main v-if="found" class="container-fluid" role="main">
            <div class="row mb-5"></div>
            <div class="row mb-5"></div>
            <div class="row mb-5"></div>
            <h5 style="text-align:center">고객님의 정보와 일치하는 ID입니다.</h5>
            <br />
            <div class="jumbotron ml-5 mr-5" style="text-align:center">
                <h6>{{foundId}}</h6>
            </div>
            <div class="form-group text-center mt-5">
                <button type="submit" class="btn btn-primary" style="height:80px;width:300px" @click="$router.push('/')">
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
    name : 'find-id',
    components : {

    },
    data() {
        return {
            item : {
                name : '',
                email : ''
            },
            found : false,
            foundId : ''
        }
    },
    methods : {
        find () {
            if(valid.isValid(this.item, valid.findId)) {
                com.post('api/user/find-id', this.item)
                .then((r)=> {
                    this.foundId = r.id
                    this.found = true
                })
            }
        }
    }
}
</script>
