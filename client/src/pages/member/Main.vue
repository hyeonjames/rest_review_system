<template>
    <div>
        <session-form></session-form>
        <router-view></router-view>
    </div>
</template>

<script>
import SessionForm from '../../components/SessionForm.vue'
export default {
    name : 'memberMain',
    components: {
        SessionForm
    }
}
</script>
