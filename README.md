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

* 문제 상황 발생

  
  Spring Security와 JWT를 사용하여 사용자 인증을 구현하는 과정에서 Access 토큰 만료 처리와 재발급 로직 설계에 어려움이 있었습니다. 만료된 Access 토큰으로 API 요청을 보낼 경우 401 에러가 발생했으며, Axios Interceptor를 통해 자동으로 재발급 요청을 보내도록 설계했지만 재발급 요청조차 JWT 필터에서 401 에러로 처리되어 무한 루프가 발생했습니다.

* 원인 추론


  문제의 원인은 Spring Security의 JWT 필터가 모든 요청에 대해 토큰 유효성을 검사하도록 설정되어 있었기 때문입니다. 이로 인해 재발급 API 요청조차 토큰 검증 대상에 포함되었고, 만료된 토큰으로 보낸 재발급 요청이 계속 실패하는 상황이 발생했습니다.

* 해결 방안


  JWT 필터에서 재발급 API 경로를 예외 처리하여 해당 경로에 대해서 토큰 검증을 생략하도록 설계하였습니다. 이를 통해 재발급 요청이 JWT 검증 없이 다음 필터로 넘어갈 수 있게 되었고 결과적으로 만료된 토큰 상황에서도 재발급 로직이 정상적으로 작동하게 되었습니다.
```
//  Axios Interceptor
//  401 에러 && 재시도가 아닌 경우 --> 토큰 재발급 요청
if (status === 401 && !originalRequest._retry) {
  originalRequest._retry = true;

  try {
    const reissueResponse = await axios.post(`/api/main/reissue`);

    (생략)
  }
  catch {
    (생략)
  }
}
```
```
//  JWT 필터
//  요청 URL가 "/api/main/reissue"인 경우 --> 다음 필터로 넘김
String requestURI = request.getRequestURI();

if ("/api/main/reissue".equals(requestURI)) {

  filterChain.doFilter(request, response);

  return;
}
```
* 결과


  토큰이 만료된 상황에서도 안정적으로 토큰 재발급이 가능해졌습니다. 이 과정에서 JWT 기반 인증의 작동 원리와 Spring Security 필터 체인 설계에 대해 보다 깊이 이해할 수 있었습니다.


## 📋 프로젝트 구조


## 📌 주요 기능

