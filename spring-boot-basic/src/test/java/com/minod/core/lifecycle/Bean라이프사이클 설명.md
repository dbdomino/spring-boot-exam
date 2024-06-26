싱글턴 패턴으로 사용하는 빈 중에서
dbconnection처럼 어플리케이션이 동작할 때 커넥션을 맺고 나서,
어플리케이션이 종료될 때 connection을 끊는 작업이 들어가야 한다.

이를위한 connection bean의 라이프사이클을 관리하는 방법을 테스트로 정리

- 스프링 빈은 "객체생성" -> "의존관계 주입" 관계를 가진다. 수동 Bean 생성하는 Config 보면 기억난다.
- 다만 생성자를 통한 의존관계 주입은 예외적으로 "객체생성"단계에서 의존관계가 주입된다. -> 이 또한 의존관계 주입 순서를 위해 spring 이 객체생성 순서를 알아서 조절해줌

-- 스프링이 객체 생성- > 의존관계 주입 -> 의존관계 주입이 끝난 뒤에 데이터를 사용할 준비가 끝난것을 알수 있는데, 이 시점이 언제인지 어떻게 알 수 있는가?
-> 스프링은 의존관계 주입이 완료되면, 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려주는 다양한 기능을 제공한다. (콜백 메서드로 알 수 있다.)
-> 또한 스프링은 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 준다. 따라서 안전하게 종료 작업을 진행할 수 있다.

-> "스프링 컨테이너 생성" -> "스프링 Bean 생성" -> "의존관계 주입" -> "초기화 콜백" -> " Bean 사용" -> "소멸 전 콜백" -> "스프링 종료"

초기화 콜백 : 빈이 생성된 뒤, 의존관계 주입이 완료된 후 호출
소멸전 콜백 : 빈이 소멸되기 직전에 호출
생성자의 역할과, 초기화의 역할을 구분해서 구현하는게 좋다.!
생성자의 역할(파라미터를 받고 메모리에 할당해서 객체 생성)
초기화의 역할(생성된 객체와 값들로 외부 커넥션 연결 같은 초기화) -> 생성자 안에넣고 하지말고 초기화 메서드로 분리하는게 나을수도 있다.

스프링은 크게 3가지 방법으로 빈 생명주기 콜백을 지원한다. 
- 인터페이스(InitializingBean, DisposableBean) 
- 설정 정보에 초기화 메서드, 종료 메서드 지정 
- @PostConstruct, @PreDestroy 애노테이션 지원