<template>
    <div>
        <ViewPaperHeaderPage/>
        <div class="main">
            <ViewPaperMainPage/>
        </div>
    </div>
</template>
<script lang="ts" setup>
import ViewPaperHeaderPage from "./ViewGZPaperHeaderPage.vue";
import ViewPaperMainPage from "./ViewGZPaperMainPage.vue";
</script>

<style lang="scss" scoped>
.main {
  background-color: #edfade;
  overflow: hidden;
  display: flex;
  flex-direction: row;
}

</style>