<template>
    <div class="row">
        <div class="col-md-5">
            <input type="text" class="form-control" v-bind:value="address" v-on:click="findAddress" placeholder="지역입력"/>
        </div>
        <div class="col-md-5">
            <input type="text" class="form-control" v-kmodel="keyword" placeholder="검색어" />
        </div>
        <div class="col-md-1">
            <button type="button" class="btn btn-primary" v-on:click="search">검색</button>
        </div>
    </div>
</template>

<script>
import com from '../com.js'
export default {
    name : 'searchForm',
    props : ['onSearch', 'addr', 'keyw'],
    data(){
        return {
            address : '',
            keyword : ''
        }
    },
    created(){
        this.address = this.address;
        this.keyword = this.keyw;
    },
    methods : {
        findAddress(){
            com.getAddress((data)=>{
                this.address = data.address
            })
        },
        search (){
            if(!this.address) {
                alert('지역을 입력해주세요.');
                return;
            }
            if(!this.keyword) {
                alert('검색어를 입력해주세요.');
                return;
            }
            com.getCoord(this.address, (coord)=>{
                this.onSearch({
                    address : this.address,
                    keyword : this.keyword,
                    coordX : coord.x,
                    coordY : coord.y
                });
            });
        }
    }
}
</script>
