<script setup>
import router from '@/router';
import { ref } from 'vue';

let inputInvalid = ref(false);
let username;
let password;

function login() {
  const data = {"username" : username, "password": password};
  fetch('http://localhost:8090/login', {
    method: 'POST',
    credential: "include",
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data)
  })
  .then(response => {
    const accessToken = response.headers.get("Authorization");
    if (accessToken !== null) {
      localStorage.setItem("accessToken", response.headers.get("Authorization"));
      router.replace('/');
    } else {
      inputInvalid.value  = true;
    }
  })
  .catch(error => {
    inputInvalid.value  = true;
  });
}
</script>

<template>
  <div class="container">
    <h1>로그인</h1>
    <div class="input-box">
      <input type="text" class="form-control" placeholder="아이디" v-model="username" @keyup.enter="login()">
      <input type="password" class="form-control" placeholder="비밀번호" v-model="password" @keyup.enter="login()">
      <button class="btn btn-primary" @click="login()">로그인</button>
      <p v-if="inputInvalid">아이디 또는 비밀번호를 잘못 입력했습니다.</p>
      <RouterLink to="/signup">회원가입</RouterLink>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 40px;
}

.input-box {
  width: 400px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

button {
  width: 100%;
}

a{
  text-decoration: none;
  align-self: self-end;
  color: black
}

p{
  color: tomato
}
</style>