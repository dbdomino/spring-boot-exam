@SessionAttribute
스프링은 세션을 더 편리하게 사용할 수 있도록 @SessionAttribute 을 지원한다.

이미 로그인 된 사용자를 찾을 때는 다음과 같이 사용하면 된다. 참고로 이 기능은 세션을 생성하지 않는다.
@SessionAttribute(name = "loginMember", required = false) Member loginMember