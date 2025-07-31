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
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요" />
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15" />
  </div>

  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="write()">작성완료</el-button>
    </div>
  </div>
</template>

<style>

</style>
