서블릿에서 세션을 다루기 위해 HttpSession 이라는 기능을 제공합니다.

우리가 직접 구현한 세션매니저가 이미 구현되어있고, 많은 기능을 제공합니다.

HttpSession 소개
서블릿이 제공하는 HttpSession 도 결국 우리가 직접 만든 SessionManager 와 같은 방식으로 동작합니다.
서블릿을 통해 HttpSession 을 생성하면 다음과 같은 쿠키를 생성. 쿠키 이름이 JSESSIONID 이고, 값은 추정불가능한 랜덤 값이 나옵니다.
Cookie: JSESSIONID=5B78E23B513F50164D6FDD8C97B0AD05

HttpSession 사용
서블릿이 제공하는 HttpSession 을 사용하도록 개발.

![TrackingModes.png](TrackingModes.png)
HttpSession으로 로그인시 
TrackingModes 를 지원합니다. 
로그인을 처음 시도하면 URL이 다음과 같이 jsessionid 를 포함하고 있는 것을 확인할 수 있다.
쿠키와 동시에 url에도 session정보를 뿌려주는 기능입니다.
끄러면 application.properties에 설정 추가해야 합니다.(끄는게 나아보입니다.)

권장 해결방법: application.properties에 추가
server.servlet.session.tracking-modes=cookie

만약 URL에 jsessionid가 꼭 필요하다면 application.properties에 다음 옵션을 추가해주세요.
spring.mvc.pathmatch.matching-strategy=ant_path_matcher

설마 스프링이 별도로 제공하진 않겠지요?
그런소리하면 안돼요 제공해주니까....

@SessionAttribute