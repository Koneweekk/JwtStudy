<script setup>
import router from "@/router";
import { onMounted, ref } from "vue";
import { Client } from "@stomp/stompjs";

// 참조변수
const userName = ref("");
const userList = ref([]);
let toUser = ref("all");
let message = ref("");
let stomp;

// 마운트시 동작
onMounted(() => {
  getUserInfo();
  connectStomp();
  getUserList();
})

// 함수 - 유저정보 불러오기
function getUserInfo() {
  fetch("http://localhost:8090/user", {
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
      localStorage.removeItem("accessToken");
      router.replace("/login");
    });
}

// 함수 - 유저리스트 불러오기
function getUserList() {
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
      localStorage.removeItem("accessToken");
      router.replace("/login");
    });
}

// 함수 - STOMP 커넥트
function connectStomp() {
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
      stomp.subscribe("/topic/alarm", (message) => {
        console.log("Received message:", message.body);
        // 여기에 메시지를 처리하는 코드를 추가할 수 있습니다.
      });
    },
    // debug: (message) => {
    //   console.log(message)
    // },
    onWebSocketError: (message) => {
      console.log(message)
    },
  });

  stomp.activate();
}

// 함수 - 메시지 전송
function sendMessage() {
  let destination;
  let messageBody;
  if (message.value === "all") {
    destination = "/alarm"
    messageBody = {
      "title" : "",
      "message" : message.value
    }
  } else {

  }

  stomp.publish({
    destination: "app/alarm",
    // body: JSON.stringify(messageBody),
    body: "Awgaewrhaerthaet",
    skipContentLengthHeader: true,
    // headers: { 
    //   'Authorization' : localStorage.getItem('accessToken')
    // },
  });
}
</script>

<template>
    <div class="root-container">
      <div class="left-container">
        <div class="left-header">
          <h1>{{ userName }}</h1>
          <button type="button" class="btn btn-danger">로그아웃</button>
        </div>
        <select class="form-select" v-model="toUser">
            <option value="all" selected>전체</option>
            <option v-for="user in userList" :key="user.username":value="user.username">{{ user.username }}</option>
          </select>
        <textarea class="form-control message-text" rows="5" placeholder="메시지를 입력해주세요" v-model="message"></textarea>
        <button type="button" class="btn btn-primary message-btn" @click="sendMessage()">보내기</button> 
      </div>
      <div class="right-container">
        
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

.left-header > h1 {
  font-size: 48px;
}

.message-btn {
  width: 100%;
}


.right-container {
  height: 100vh;
}



textarea{
  width: 400px;
}
</style>