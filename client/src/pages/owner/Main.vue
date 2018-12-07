<template>
    <div>

        <session-form></session-form>
        <router-view></router-view>
    </div>
</template>

<script>
import SessionForm from '../../components/SessionForm.vue'
export default {
    name : 'ownerMain',
    data() {
        return {

        }
    },
    components : {
        SessionForm
    },
    created() {

    }
}
</script>
