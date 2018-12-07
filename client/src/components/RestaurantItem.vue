<template>
    <div class="row rest-item" v-on:click="detail">
        <img class="mr-3 img150 img-thumbnail" v-bind:src="item.imageUrl" alt="">
        <div class="col-md-5">
            <h3>{{item.name}} ( {{item.categoryName}} )</h3>
            <span>평점 <span class="text-warning">{{item.score}} {{'★'.repeat(Math.floor(item.score)) + '☆'.repeat(5-Math.floor(item.score))}} </span> / 리뷰수 {{item.reviewCount}}</span>
            <br/>
            <span>주소 {{item.address}}</span><br/>
            <span>전화번호 {{item.phoneNumber}}</span><br/>
            <span>설명 {{item.description}}</span>
            
        </div>
    </div>

</template>

<script>
export default {
  name: "restaurantItem",
  props: ["item"],
  data() {
    return {};
  },
  methods: {
      detail(){
          this.$router.push(`/restaurant/${this.item.restNo}/1`)
      }
  }
};
</script>
