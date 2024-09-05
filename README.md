![image](https://github.com/user-attachments/assets/80fc49d7-565c-4d10-9104-88b5792414ab)
<div align=center>
  
  **🦅[Play Data] 한화시스템 BEYOND SW캠프 7기/불사조🦅**
</div>

<br><br>
## 👥 팀원 소개
<div align=center>
  
  **🐷 황정하 [![Github](https://img.shields.io/badge/Github-Link-1f425f.svg)](https://github.com/NaturalHwang)**<br>
  **🐯 홍예지 [![Github](https://img.shields.io/badge/Github-Link-1f425f.svg)](https://github.com/H-Yeji)**<br>
  **🐯 최무리 [![Github](https://img.shields.io/badge/Github-Link-1f425f.svg)](https://github.com/Choi-Moori)**<br>
  **🐉 이혜진 [![Github](https://img.shields.io/badge/Github-Link-1f425f.svg)](https://github.com/hjin111)**<br>
</div>
<br><br><br>

## 🏨 프로젝트 소개
```
  우리가 여행을 가거나 관광을 할 때 꽤 많은 비중을 차지하는 것이 숙박입니다. 
  숙박 중에 고객들이 편안하게 머물다 갈 수 있는 호텔이 될 수 있도록 이러한 서비스를 제작하게 되었습니다.

  관리자 페이지를 따로 생성하여 고객들 뿐만 아니라 객실이나 다이닝 등,
  호텔 전반적인 서비스를 실시간으로 관리하여 보다 더 나은 서비스를 제공합니다.
  또한 각 부서 마다 권한을 부여함으로써 보안 역시 철저하게 관리됩니다.

  저희 서비스를 통해 빠르고 철저한 호텔 예약을 함으로써 여행의 효율성을 높일 수 있습니다. 😊 
```

<br><br><br>

## 📢 요구사항 명세서

### 1️⃣ 고객 
<details>
  <summary> 상세내용 </summary>
  
 - 회원관련<br>
  (1) 새로운 고객은 회원가입을 할 수 있다.<br>
  (2) 회원은 본인의 정보를 조회할 수 있다.<br>
  (3) 회원은 본인의 정보(비밀번호)를 수정할 수 있다.<br>
  (4) 회원은 탈퇴를 할 수 있다.<br>
<br>

 - 예약관련<br>
  (1) 회원은 객실을 예약할 수 있다.<br>
  (2) 회원은 객실 예약을 조회할 수 있다.<br>
  (3) 회원은 객실 예약을 취소할 수 있다. <br>
  (4) 회원은 다이닝을 예약할 수 있다.<br>
  (5) 회원은 다이닝 예약을 조회할 수 있다.<br>
  (6) 회원은 다이닝 예약을 취소할 수 있다.<br>
<br>

- Q&A 게시판<br>
  (1) 회원은 Q&A를 통해 문의할 수 있다.<br>
  (2) 회원은 자신이 작성한 Q&A를 조회할 수 있다.<br>
  (3) 회원은 자신이 작성한 Q&A를 수정할 수 있다.<br>
  (4) 회원은 자신이 작성한 Q&A를 삭제할 수 있다.<br>
  (5) 회원은 자신이 작성한 Q&A의 답변에 대한 관리자의 알림을 받을 수 있다.<br>
</details>


### 2️⃣ 관리자 

<details>
  <summary> 상세내용 </summary>
  
 - 회원 및 예약 관리 <br>
  (1) 관리자(직원)는 고객의 정보를 조회할 수 있다.<br>
  (2) 관리자는 각 부서마다 해당하는 고객의 예약 정보(객실, 다이닝)를 조회할 수 있다.<br>
  (3) 관리자는 요청 시 각 부서에 해당하는 고객의 예약 정보를 수정할 수 있다. <br>
  (4) 관리자는 요청 시 각 부서에 해당하는 고객의 예약 정보를 취소할 수 있다.<br>
  (5) 관리자는 Q&A 게시판의 목록을 조회할 수 있다. <br>
  (6) 관리자는 Q&A 게시판에 답변을 달 수 있다. <br>
  (7) 각 부서 관리자는 해당 부서의 실시간 알림(예약: 다이닝, 객실 )을 받을 수 있다.<br>
<br>

 -  세부 관리자 권한 <br>
  (1) 관리자는 객실팀과 식음팀, 오피스로 나뉜다. <br>
  (2) 관리자는 본인의 정보를 조회할 수 있다.<br>
  (3) 관리자는 본인의 정보(비밀번호)를 수정할 수 있다.<br>
<br>

- 서비스 관리  <br>
  (0) Office 부서 관리자는 새로 입사한 회원을 등록할 수 있다.<br>
  (1) Office 부서 관리자는 퇴사자의 ID를 삭제할 수 있다.<br>
  (2) Dining 부서 관리자는 dining 메뉴를 추가할 수 있다.<br>
  (3) Dining 부서 관리자는 dining 메뉴를 수정할 수 있다.<br>
  (4) Dining 부서 관리자는 dining 메뉴를 삭제할 수 있다.<br>
  (5) Room 부서 관리자는 객실 요금을 수정할 수 있다.<br>

</details>


<br><br><br>

## ⚙️ 기술 스택

**Frontend**<br>
<img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white">
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
<img src="https://img.shields.io/badge/html-E34F26?style=for-the-badge&logo=html5&logoColor=white">
<img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
<br>

**Backend**<br>
<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white"> 
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=spring boot&logoColor=white">
<img src="https://img.shields.io/badge/rabbitmq-%23FF6600.svg?&style=for-the-badge&logo=rabbitmq&logoColor=white">
<br><br>
**Database**<br>
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
<img src="https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white">
<br><br>
**Tools**<br>
<img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
<img src="https://img.shields.io/badge/Made%20for-VSCode-1f425f.svg">
<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/Notion-black?style=for-the-badge&logo=Notion&logoColor=white"/>


<br><br><br>

## ✈️ WBS
![image](https://github.com/user-attachments/assets/b62cbf6f-c7f1-4a00-aa94-06701a09f7af)

<div align=center>

  [⬆️WBS바로가기](https://docs.google.com/spreadsheets/d/1X0fafalLJS0A_FwmI4IMyJMZsv2F3uoe/edit?usp=sharing&ouid=106399302288862850251&rtpof=true&sd=true)
</div>
<br><br><br>

## 😊 ERD
![image](https://github.com/user-attachments/assets/03bd6cc7-4ecc-4e9a-961e-20c05261f2e3)

<br><br><br> 

## 🧪 테스트 결과 
### 1️⃣ 고객 페이지
<details>
  <summary>(1) 회원가입</summary>
  <br>

  * 이메일 인증을 통해 회원가입<br>
    <img src="https://github.com/user-attachments/assets/731144a2-68cc-40fb-89da-97a3f85bdb51" width=700><br>
    <img src="https://github.com/user-attachments/assets/6b0a5ecc-dba2-4421-b738-fd6bc329676d" width=700>
    <br><br>
    
</details>
<details>
  <summary>(2) 이메일 및 비밀번호 찾기</summary>
  <br>
  
  * 이메일 찾기<br>
    <img src="https://github.com/user-attachments/assets/11ff4999-a218-4a74-8487-f0816c13b6b9" width=700>
    <br><br>
    
  * 비밀번호 찾기<br>
    <img src="https://github.com/user-attachments/assets/0abe1a7f-6481-4126-be3e-746c76499666" width=700>
    <br><br>
</details>
<details>
  <summary>(3) QnA CRUD</summary>
  <br>
  
  * QnA 작성 및 조회<br> 
  <img src="https://github.com/user-attachments/assets/6882017d-9372-466b-a43e-be3acd2c2930" width=700>
    <br><br>
    
  * QnA 수정 및 삭제<br>
  <img src="https://github.com/user-attachments/assets/5adf8b5c-bb2d-4cfb-8e1e-db90d0b395d4" width=700>
  <img src="https://github.com/user-attachments/assets/2df897e5-2cc2-4546-8b89-3eaab102b6a7" width=700>
    <br><br>
</details>
<details>
  <summary>(4) My Page</summary>
  <br>

  * 내가 예약한 Dining 목록/상세내역 조회 및 예약 삭제<br>
  <img src="https://github.com/user-attachments/assets/6d83b8bc-1be0-44cd-b16d-1e5907c5bba3" width=700>
  <br><br>
  
  * 내가 예약한 Room 목록/상세내역 조회 및 예약 삭제<br>
  <img src="https://github.com/user-attachments/assets/2db27547-f063-4762-8466-300ce9a8296a" width=700>
  <br><br>

  * 비밀번호 변경 - 변경하는 비밀번호가 일치하지 않을 때<br>
  <img src="https://github.com/user-attachments/assets/be07c9b0-695a-49a3-9a2a-2d90c968aebb" width=700>
  <br><br>

  * 비밀번호 변경에 성공<br>
  <img src="https://github.com/user-attachments/assets/3efce678-76fc-4c77-ac71-81199e30e114" width=700>
  <br><br>

  * 회원 탈퇴하기 <br>
  <img src="https://github.com/user-attachments/assets/c8686ae7-45dc-49b3-beb5-c5982d2674ce" width=700>
    <br><br>
  
</details>

### 2️⃣ 고객 페이지 + 관리자 페이지 
<details>
  <summary>(1) Room(객실) 예약 </summary>
  <br>

  * 💡 로직 설명 <br>
  <img src="https://github.com/user-attachments/assets/a29fff66-792b-4a58-89a8-7723560955b6" width=700>
  
  * 홈페이지를 통해 고객이 객실 예약을 진행<br>
  <img src="https://github.com/user-attachments/assets/9651c2f9-b69d-4fa3-b548-41541554972e" width=700>
    <br><br>
    
  * 관리자 페이지에서 객실 예약이 들어오면 실시간 알림(SSE) 조회<br>
  <img src="https://github.com/user-attachments/assets/8ca28e96-601f-46f8-861d-92e5c5cdee57" width=700>
    <br><br>

  * 💡 예약 완료 <br>
  <img src="https://github.com/user-attachments/assets/47a475f2-339d-480c-a869-8d4d007dd240" width=700>
  <br><br>
    
  * ➕ 여러 개의 객실 예약이 들어온 경우 실시간 알림(SSE) 조회
  <img src="https://github.com/user-attachments/assets/5863d170-7e02-4840-a0a1-80da30f9ce88" width=700>
    <br><br>
</details>
<details>
  <summary>(2) Dining 예약 </summary>
  <br>
  
  * 홈페이지를 통해 고객이 다이닝 예약을 진행 > 관리자 페이지에서 실시간 알림 (SSE) 조회<br>
  <img src="https://github.com/user-attachments/assets/4068b2b0-b224-43b3-bbb0-a9ec2297a14b" width=700>
  <br><br>

  * 💡 부서마다 예약 알림 접근 제한 - ex) KorDinig의 예약이 들어왔을 때 ChiDining 부서의 직원에게는 알림이 가지 않음<br>
  <img src="https://github.com/user-attachments/assets/6aa7dd0e-38f8-400f-9f7d-fb8bd625a046" width=700>
  <br><br>
</details>
<details>
  <summary>(3) QnA에 답변 달기 </summary>
  <br>
  
  * 관리자 페이지를 통해 들어온 QnA에 답변 달기 <br>
  <img src="https://github.com/user-attachments/assets/2c683c20-20ec-4f31-9f13-97c699b77cfe" width=700>
    <br><br>
    
  * 답변이 달리면 해당 고객의 페이지에 실시간 알림 숫자 업데이트<br>
  <img src="https://github.com/user-attachments/assets/be2034e9-8d3a-4ee2-9b05-cea62f6565b2" width=700>
    <br><br>
</details>


### 3️⃣ 관리자 페이지 
<details>
  <summary>(0) 😊 모든 직원의 공통 작업 </summary>
  <br>

  * 로그인 및 본인의 정보 확인
   <img src="https://github.com/user-attachments/assets/2e83f59f-dac5-4901-b15f-e8e0997c7844" width=700>
    <br><br>
    
  * 비밀번호 변경<br>
  <img src="https://github.com/user-attachments/assets/69818d09-b57f-40b7-b5ea-5dce928e0a23" width=700>
    <br><br>
    
  * 고객의 목록 및 상세 내역 조회<br>
  <img src="https://github.com/user-attachments/assets/d0417a88-56ac-4f9c-af1e-c1c613f87028" width=700>
    <br><br>

</details>
<details>
  <summary>(1) Office 부서 직원 [최상위 직원] </summary>
  <br>
  
  * 모든 직원들의 목록 조회 (검색 기능)<br>
  <img src="https://github.com/user-attachments/assets/99115b4a-0442-4c53-b731-4974de06be23" width=700>
    <br><br>
    
  * 직원의 상세 내역 조회 및 직급 변경 / 퇴사처리<br>
  <img src="https://github.com/user-attachments/assets/4d1335e9-86b2-4c48-9d88-da6b229cc0e4" width=700>
    <br><br>
    
  * 새로 입사한 직원을 등록 : 초기 비밀번호 12341234<br>
  <img src="https://github.com/user-attachments/assets/d27e50be-3a29-4de3-aff7-c98c9b53c20f" width=700>
    <br><br>

    
  * ➕ Room/Dining 예약 내역 조회 불가 (권한 없음) : Office관리자는 Room 예약 내역 조회 기능에 접근할 수 없음<br>
  <img src="https://github.com/user-attachments/assets/0a8d9e14-0882-4e3a-8a42-ffadd2d65a13" width=700>
    <br><br>
</details>
<details>
  <summary>(2) Room(객실) 부서 직원 </summary>
  <br>
  
  * 조회하고자 하는 고객의 이메일을 검색하여 객실 예약 내역 조회<br>
  <img src="https://github.com/user-attachments/assets/df672833-b420-4c46-a79f-fc061f83ac13" width=700>
    <br><br>
    
  * 객실 예약 수정 / 삭제<br>
  <img src="https://github.com/user-attachments/assets/04a468a8-468a-4439-8f5e-3945eb4a8075" width=700>
    <br><br>
    
  * 객실의 base price 변경 <br>
  <img src="https://github.com/user-attachments/assets/1f65f519-85a7-4ee0-97da-3804d2a349e5" width=700>
    <br><br>
</details>
<details>
  <summary>(3) Dining 부서 직원 </summary>
  <br>
  
  * 조회하고자 하는 고객의 이메일을 검색하여 다이닝 예약 내역 조회<br>
  <img src="https://github.com/user-attachments/assets/94b33c24-7bab-4deb-84e1-bbcb357d681d" width=700>
    <br><br>
    
  * 다이닝 예약 상세 내역 조회 / 수정 / 삭제<br>
  <img src="https://github.com/user-attachments/assets/8ecb40d4-e2fb-4391-a68b-0c4fad933368" width=700>
    <br><br>
    
  * 각 다이닝별 메뉴 추가 / 가격 수정 / 삭제<br>
  <img src="https://github.com/user-attachments/assets/64feb06a-90a7-4183-9586-bc633e5f7557" width=700>
  <img src="https://github.com/user-attachments/assets/1b795823-d14c-4458-ac3c-27197c176051" width=700>
    <br><br>
</details>

### 4️⃣ Rabbit mq를 활용한 대기열 구현  

<details>
  <summary>✈️ 객실 예약 시도</summary>
  <br>

  * 💡 설명 
  <img src="https://github.com/user-attachments/assets/4c1ea456-e666-4c0f-bea7-bd6d1d2efea4" width=700>
  <img src="https://github.com/user-attachments/assets/71caf0f2-9c8a-4a44-9e2c-6abc7546144c" width=700>
  <br>

  * 🖥️ 화면 
  <img src="https://github.com/user-attachments/assets/27f607ac-370e-4291-a282-8a968ee1d5e9" width=700>  
</details>




