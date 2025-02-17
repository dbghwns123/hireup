# 🚀 HireUp 취업준비생 커뮤니티 플랫폼

## 📌 프로젝트 소개
**HireUp은 Hire + Level Up, 취업을 목표로 성장하길 원하는 취업 준비생들을 위한 정보 공유 및 소통 커뮤니티 플랫폼**입니다.  
유저들은 게시글을 작성하고, 해시태그를 이용한 검색이 가능하며,  
좋아요, 댓글 등의 소셜 기능을 활용할 수 있습니다.  

---

## ✨ 주요 기능  

### 🔑 **회원 기능**  
- 회원가입 및 이메일 인증 (랜덤 6자리 코드 전송)  
- 로그인/로그아웃  
- 회원 정보 조회/수정
- 일반 유저 & 관리자 구분 (관리자 코드를 입력하여 권한 부여)  
- 팔로우 기능 및 팔로워/팔로잉 목록 조회
- 회원 탈퇴

### 📝 **게시글 기능**  
- 게시글 작성 (제목, 내용, 해시태그, 카테고리 지정 가능)  
- 게시글 공개 범위 설정 (전체 공개, 팔로워 공개, 비공개)  
- 좋아요, 댓글 기능 (수정 및 삭제 포함)  
- ElasticSearch 기반 **해시태그 검색 최적화**  

### 🚨 **신고 및 관리자 기능**  
- 게시글 신고 기능 (10회 이상 신고 시 자동 비공개 처리)  
- 관리자는 게시글/댓글을 삭제 가능  
- 카테고리 생성/조회/수정/삭제 가능  

### 🔔 **알림 기능**  
- **게시글에 좋아요 또는 댓글이 달리면 이메일 알림 발송**  
- `JavaMailSender` 활용한 알림 기능 구현  

### ☀️ **날씨 정보 제공**  
- `OpenWeatherMap API`를 활용하여 3시간 단위 날씨 정보 제공  
- `Spring Scheduler`를 이용한 자동 업데이트  

---

## 🏗️ ERD 구조  
![ERD](https://github.com/user-attachments/assets/d334fb8f-7bb1-41d0-9cfa-ede5717d0d3f)

---

## 🛠️ 기술 스택  

### **백엔드**
- ![Java](https://img.shields.io/badge/Java-17-blue)  
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-green)  
- Spring Security, JPA, Hibernate  

### **데이터베이스**
- MySQL 9.2.0

### **검색 최적화**
- ElasticSearch

### **스토리지**
- AWS S3

### **API**
- OpenWeatherMap API  

### **인증**
- JWT (JSON Web Token)  
- Spring Security  
