<script setup>
import router from "@/router";
import { onMounted, ref } from "vue";
import { Client } from "@stomp/stompjs";
import { Toast } from "bootstrap";

// 참조변수
const userName = ref("");
const userList = ref([]);

let toUser = ref("all");
let message = ref("");
let stomp;

let toastBootstrap;
let receivedName = ref("");
let receivedMessage = ref("");

// 마운트시 동작
onMounted(async () => {
  await getUserInfo();
  await getUserList();
  await connectStomp();
  const liveToast = document.getElementById('liveToast')
  toastBootstrap = Toast.getOrCreateInstance(liveToast)
})

// 함수 - 유저정보 불러오기
async function getUserInfo() {
  await fetch("http://localhost:8090/user", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": localStorage.getItem("accessToken")
    },
  })
    // 토큰 만료 확인
    .then(response => {
      if (response.status === 403) {
        throw new Error();
      }
      return response.text()
    })
    // 토큰 만료되지 않았으면 사용자 이름 표시
    .then(data => {
      userName.value = data
    })
    // 토큰 만료시 로그인 페이지로 이동
    .catch(error => {
      logout();
    });
}

// 함수 - 유저리스트 불러오기
async function getUserList() {
  fetch("http://localhost:8090/userlist", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": localStorage.getItem("accessToken")
    },
  })
    // 토큰 만료 확인
    .then(response => {
      if (response.status === 403) {
        throw new Error();
      }
      return response.text()
    })
    // 토큰 만료되지 않았으면 사용자 이름 표시
    .then(data => {
      userList.value = JSON.parse(data);
    })
    // 토큰 만료시 로그인 페이지로 이동
    .catch(error => {
      logout();
    });
}

// 함수 - STOMP 커넥트
async function connectStomp() {
  const accessToken = localStorage.getItem("accessToken"); // 토큰을 여기서 가져옵니다.

  stomp = new Client({
    brokerURL: "ws://localhost:8090/ws",
    connectHeaders: {
      Authorization: accessToken,
    },
    reconnectDelay: 0,
    heartbeatIncoming: 0,
    heartbeatOutgoing: 0,
    onConnect: () => {
      // 전체 알람 구독
      stomp.subscribe("/topic/alarm", (message) => {
        receiveMessage(message)
      });
      // 개인 알람 구독
      stomp.subscribe(`/queue/alarm/${userName.value}`, (message) => {
        receiveMessage(message)
      });
    },
    debug: (message) => console.log(message)
  });
  
  await stomp.activate();
}

// 함수 - 메시지 전송
function sendMessage() {

  let destination = toUser.value === "all" ? "/app/send-alarm" : `/app/send-alarm-to-user/${toUser.value}`;
  let messageBody = {
    from: userName.value,
    to: toUser.value,
    message: message.value
  };

  stomp.publish({
    "destination": destination,
    "body": JSON.stringify(messageBody),
    skipContentLengthHeader: true,
    headers: {
      'Authorization': localStorage.getItem('accessToken')
    },
  });
}

// 함수 - 메시지 수신
async function receiveMessage(message) {
  const messageBody = JSON.parse(message.body);
  if (messageBody.from === userName.value) return;

  receivedName.value = messageBody.from;
  receivedMessage.value = messageBody.message;
  toastBootstrap.show();
}

// 함수 - 로그아웃
function logout() {
  localStorage.removeItem("accessToken");
  router.replace("/login");
}
</script>

<template>
  <div class="root-container">
    <!-- 입력창 -->
    <div class="left-container">
      <div class="left-header">
        <h1>{{ userName }}</h1>
        <button type="button" class="btn btn-danger" @click="logout()">로그아웃</button>
      </div>
      <select class="form-select" v-model="toUser">
        <option value="all" selected>전체</option>
        <option v-for="user in userList" :key="user.username" :value="user.username">{{ user.username }}</option>
      </select>
      <textarea class="form-control message-text" rows="5" placeholder="메시지를 입력해주세요" v-model="message"></textarea>
      <button type="button" class="btn btn-primary message-btn" @click="sendMessage()">쪽지 보내기</button>
    </div>
    <!-- 토스트 -->
    <div class="toast-container position-fixed bottom-0 end-0 p-3">
      <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
          <strong class="me-auto">{{ receivedName }}</strong>
          <!-- <small>11 mins ago</small> -->
          <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
          {{ receivedMessage }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.root-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100vw;
  height: 100vh;
  margin: 0px;
  padding: 0px;
}

.left-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  gap: 20px;
}

.left-header {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.left-header>h1 {
  font-size: 48px;
}

.message-btn {
  width: 100%;
}

textarea {
  width: 400px;
}
</style>