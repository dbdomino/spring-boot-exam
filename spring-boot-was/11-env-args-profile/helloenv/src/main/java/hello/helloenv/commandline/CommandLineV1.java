package hello.helloenv.commandline;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// CommandLine 에서 사용하는 인수 알아보기
// 실행옵션에서 Program arguments에 "dataA dataB" 라고 넣어보기, 그럼 반복으로 뜨게된다.
// java -jar project.jar dataA dataB
// 커맨드로 실행할 경우 위처럼 인수를 추가할 수 있다.

// 커맨드로 인수 입력은,, key=value 형식이 아니다. 배열처럼 index를 가지며, 순서로 출력해야 한다.
// 굳이 key=value 형식으로 만드려면 args하나씩 파싱하여 = 로 짤라서 map에 집어넣어야 한다.(직접 다 구현해야한다.)
// V2에서 스프링에서 제공하는 기능으로 인수를 key=value 형식으로 편하게 바꿔보자.
public class CommandLineV1 {
    public static void main(String[] args) {
        for (String arg : args) {
            log.info("arg {}", arg);
        }
    }
}
