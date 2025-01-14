# :dress: 오늘의 옷차림 가이드 | 오늘 뭐 입지?

![제목을 입력하세요 7](https://github.com/user-attachments/assets/5189e4ea-e239-4a82-8abb-a8a3679446ff)


## 💁 프로젝트 소개

'오늘 뭐 입지?'는 날씨에 적합한 옷차림을 고민하는 사람들을 위한 웹 애플리케이션으로, 특히 날씨 변화가 잦은 간절기에 유용하게 쓸 수 있도록 개발하였습니다. 사용자는 오늘의 옷차림, 지역과 함께 간단한 리뷰를 남길 수 있습니다. 그리고 다른 사용자가 공유한 리뷰를 참고해 오늘의 옷차림을 정할 수 있습니다. 


* URL : https://whattowear.store/
* Test ID : guest@gmail.com
* Test PW : 123123 
* Blog : https://beginnerdevdiary.tistory.com/

  
## ⏰ 개발 기간

2024.07 ~ 2024.11


## ⚙️ 개발 환경

- 언어 : Java21, JavaScript, HTML/css
- 서버 : Amazon EC2, Docker
- 프레임워크 : Spring Boot, Vue.js, Spring Security
- 데이터베이스 : MariaDB, Amazon RDS
- API/라이브러리 : 기상청 API, JWT


## 🔍 ERD

![스크린샷 2024-12-05 212618](https://github.com/user-attachments/assets/d825a75c-02d6-4cbf-906f-43e9822e9bc6)


## 🚀 트러블 슈팅

1. 문제 상황 발생
Spring Security와 JWT를 사용하여 사용자 인증을 구현하는 과정에서 Access Token 만료 처리 및 재발급 로직 설계에서 아래와 같은 문제가 발생하였다.
* 만료된 Access Token으로 API 요청 시 401 에러가 계속해서 발생
* Axios 인터셉터에서 401 에러가 감지되면 자동으로 재발급 요청을 보내도록 설계했지만, 재발급 요청도 401 에러로 처리되며 무한 루프가 발생


2. 원인 추론
* Spring Security의 JWT 필터가 모든 요청의 토큰 유효성을 검사하도록 설계되어 있었다.
* 재발급 API 요청조차 토큰 검증 대상에 포함되었고, 이로 인해 만료된 토큰으로 보낸 재발급 요청이 계속 실패하였다.
* 결과적으로 401에러 상황에서 재발급 요청을 처리하지 못하고 무한 루프가 발생하였다.


3. 해결 방안
* 재발급 API 경로에 대해 JWT 필터에서 예외 처리하여 토큰 검증 대상에서 제외하였다.
* 재발급 API 경로에 대해 JWT 검증을 생략하고 다음 필터로 넘어가도록 처리함으로써 만료된 토큰을 사용해도 재발급 로직이 정상적으로 작동하도록 설계하였다.


4. 결과
* 만료된 토큰 상황에서도 정상적으로 토큰 재발급이 가능해졌다.
* 인증과 관련된 요청 흐름이 명확하게 정리되었다.
* JWT 기반 인증의 작동 원리와 Spring Security 필터 체인 설계에 대해 보다 깊은 이해를 얻게 되었다.



## 📋 프로젝트 구조


## 📌 주요 기능

