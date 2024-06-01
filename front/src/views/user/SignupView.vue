<script setup>
import { RouterLink } from 'vue-router'
import { ref } from 'vue';
import router from '@/router';

let inputInvalid = ref(false);
let invalidText = ref('');

let username = ref('')
let password  = ref('');

function login() {
  if (!username.value.trim()) {
    inputInvalid.value = true;
    invalidText.value = "아이디를 입력해주세요";
  } else if (!password.value.trim()) {
    inputInvalid.value = true;
    invalidText.value = "비밀번호를 입력해주세요";
  } else {
    const data = {"username" : username.value, "password": password.value};

    fetch('http://localhost:8090/userjoin', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data)
    })
    .then(response => {
      if (response.status === 201) {
        router.push('/login')
      }
    })
    .catch(error => {
      // inputInvalid.value  = true;
    });
  }

}
</script>

<template>
  <div class="container">
    <h1>회원가입</h1>
    <div class="input-box">
      <input type="text" class="form-control" placeholder="아이디" v-model="username" @keyup.enter="login()">
      <input type="password" class="form-control" placeholder="비밀번호" v-model="password" @keyup.enter="login()">
      <button class="btn btn-primary" @click="login()">회웝가입</button>
      <p v-if="inputInvalid">{{ invalidText }}</p>
      <RouterLink to="/login">로그인</RouterLink>
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