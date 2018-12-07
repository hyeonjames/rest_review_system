<template>
    
    <div>
        <session-form></session-form>
        <header class="header" role="header">
            <div class="container-fluid bg-light">
                <div class="row">
                    <div class="col" v-bind:class="{'bg-primary' : currentMenu() == 'review', 'bg-secondary' : currentMenu() != 'review'}">  
                        <a href="#/admin/review/1" style="text-decoration: none">
                            <H3 class="text-center text-white">리뷰 신고 관리</H3>
                        </a>
                    </div>
                    <div class="col" v-bind:class="{'bg-primary' : currentMenu() == 'member', 'bg-secondary' : currentMenu() != 'member'}">
                        <a href="#/admin/member/1" style="text-decoration: none">
                            <H3 class="text-center text-white">회원 관리</H3>
                        </a>
                    </div>
                </div>
            </div>
        </header>
        <router-view>
        </router-view>
    </div>
</template>
<script>
import SessionForm from '../../components/SessionForm.vue'
export default {
    name : 'adminMain',
    components: {
        SessionForm
    },
    methods : {
        currentMenu(){
            if(this.$route.path.indexOf('/admin/review') >= 0) {
                return 'review';
            }
            else if(this.$route.path.indexOf('/admin/member') >= 0) {
                return 'member';
            }
        }
    }
}
</script>
