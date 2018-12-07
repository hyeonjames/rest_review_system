import Vue from 'vue'
import Login from './pages/Login.vue'
import $ from 'jquery'
import com from './com.js'
import VueRouter from 'vue-router'

Vue.config.productionTip = false
Vue.use(VueRouter);

function setValue (context, expression, value) {

  let x = expression.split('.');
  for(var i=0;i <x.length-1; i++) {
    context = context[x[i]]
  }
  context[x[x.length-1]] = value;
}
Vue.directive('kmodel', {
  bind(el, binding,vnode) {
    $(el).on('input.kmodel', (e)=>{
      setValue(vnode.context, binding.expression, e.target.value);
    })
  },
  update(el, binding){
    $(el).val(binding.value)
  },
  unbind(el) {
    $(el).off('input.kmodel');
  }
});

window.com = com;
window.$ = $;
window.jQuery = $;
import 'bootstrap'

import AdminMain from './pages/admin/Main.vue'
import AdminReview from './pages/admin/ReviewManagement.vue'
import AdminMember from './pages/admin/MemberManagement.vue'
import OwnerManage from './pages/owner/RestaurantManagement.vue'
import OwnerMain from './pages/owner/Main.vue'
import RestaurantView from './pages/RestaurantView.vue'
import MemberMain from './pages/member/Main.vue'
import MemberRestList from './pages/member/List.vue'
import AgreeView from './pages/join/AgreeView.vue'
import JoinView from './pages/join/JoinView.vue'
import ChooseView from './pages/join/ChooseView.vue'
import JoinMain from './pages/join/Main.vue'
import ModifyView from './pages/Modify.vue'
import DeleteView from './pages/Delete.vue'
import FindId from './pages/FindId.vue'
import FindPw from './pages/FindPw.vue'
const routes = [
  {path : '/', component : Login},
  {path : '/admin', 
  component : AdminMain,
  children : [
    { path : 'review/:page', component : AdminReview},
    { path : 'member/:page', component : AdminMember},
    {path : '*', query : {redirect : '/admin/review/1'}}
  ]},
  {path : '/owner',
    component: OwnerMain,
    children : [
      { path : 'manage', component : OwnerManage },
      { path : 'view/:restNo', component : RestaurantView},
      { path : '*', query : {redirect : '/owner/manage'}}
    ]
  },
  {
    path : '/member',
    component : MemberMain,
    children : [
      {path : 'popular/:cateNo/:page', component: MemberRestList},
      {path : 'search/:coordX/:coordY/:address/:keyword/:page', component:MemberRestList}
    ]
  },
  {
    path : '/modify', component:ModifyView
  },
  {
    path : '/join',
    component : JoinMain,
    children : [
      { path : 'agree/member', component : AgreeView },
      { path : 'agree/owner', component : AgreeView },
      { path : 'member', component : JoinView },
      { path : 'owner', component : JoinView },
      { path : 'choose' , component : ChooseView},
      { path : '*', query : {redirect : '/'}}
    ]
  },
  {path : '/find-id', component : FindId},
  {path : '/find-pw', component : FindPw},
  {path : '/restaurant/:restNo/:page', component :RestaurantView},
  {path : '/delete', component:DeleteView},
  {path :'*', query : {redirect : '/'}}
];
const router = new VueRouter({
  routes : routes
})
new Vue({
  router
}).$mount('#app')