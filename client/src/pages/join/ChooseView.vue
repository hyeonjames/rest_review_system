<template>
    <div>
        <header class="header" role="header">
            <div class="container-fluid bg-primary" style="height:auto;">
                <a href="#">
                    <h3 class="text-white">회원 가입 선택</h3>
                </a>
            </div>
        </header>
        <main class="container-fluid" role="main">
            <div class="row align-items-center"><hr /></div>
            <div class="row align-items-center"><hr /></div>

            <div class="row align-items-start">
                <div class="col text-center">
                    <button type="button" class="btn btn-primary" style="height:400px;width:400px" onclick="location.href='#/join/agree/member'"><h1>개인 회원</h1></button>
                </div>
                <div class="col text-center">
                    <button type="button" class="btn btn-primary" style="height:400px;width:400px" onclick="location.href='#/join/agree/owner'"><h1>식당주회원</h1></button>
                </div>
            </div>
            <div class="row align-items-center"><hr /></div>
            <div class="row align-items-center"><hr /></div>
            <div class="row align-items-end">
                <div class="col text-center">
                    <button type="button" class="btn btn-outline-dark" style="height:80px;width:380px" onclick="location.href='#/' "><h2>취소</h2></button>
                </div>
            </div>
        </main>

    </div>
</template>

<script>
export default {
    name : 'chooseView'
}
</script>
