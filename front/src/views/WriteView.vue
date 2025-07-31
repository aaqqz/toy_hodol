<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const title = ref("");
const content = ref("");

const write = function() {
  axios.post("/api/posts",{
    title: title.value,
    content: content.value
  })
  .then(() => {
    router.replace({ name:"home" })
  })
}
</script>

<template>
  <div class="mt-2">
    <el-input v-model="title" placeholder="제목을 입력해주세요."/>
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15"/>
  </div>

  <div class="mt-2">
    <el-button type="primary" @click="write()">글 작성</el-button>
  </div>
</template>

<style>

</style>
