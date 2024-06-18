package hello.helloenv.commandline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;
// 커맨드 라인에 전달하는 값은 형식이 없고, 단순히 띄어쓰기로 구분한다.
// --url=devOMG --username=devUser --password=users1123 mod=V2
// 이걸 Program arguments 옵션에 넣어보자.
@Slf4j
public class CommandLineV2 {
    public static void main(String[] args) {
        for (String arg : args) {
            log.info(arg);
        }
        // DefaultApplicationArguments 스프링부트에서 제공하는 기능
        // 이걸로 args를 인자 형식이지만 key=value 형식으로 받을 수 있다.
        ApplicationArguments appArgs = new DefaultApplicationArguments(args);
        log.info("SourceArgs = {}", List.of(appArgs.getSourceArgs()));
        log.info("NonOptionsArgs = {}", appArgs.getNonOptionArgs());
        log.info("OptionsName = {}", appArgs.getOptionNames());
        /* 결과가 아래처럼 나온다. args를 받아서 활용 하기 위한 준비가 가능하다.
        이는 서버마다 실행 옵션을 다르게 줌으로써, 하나의 어플리케이션이
        다른 조건으로 실행되도록 제어할 수 있게 되는 요소이다.
16:55:06.146 [main] INFO hello.helloenv.other.CommandLineV2 -- SourceArgs = [--url=devOMG, --username=devUser, --password=users1123, mod=V2]
16:55:06.147 [main] INFO hello.helloenv.other.CommandLineV2 -- NonOptionsArgs = [mod=V2]
16:55:06.149 [main] INFO hello.helloenv.other.CommandLineV2 -- OptionsName = [password, url, username]

         */
        // 결과들을 보면, key 에 맞춰 value가 List에 감싸져서 나온다.
        List<String> url = appArgs.getOptionValues("url");
        List<String> username = appArgs.getOptionValues("username");
        List<String> password = appArgs.getOptionValues("password");
        List<String> mod = appArgs.getOptionValues("mod");
        log.info("Url = {}", url);
        log.info("Username = {}", username);
        log.info("Password = {}", password);
        log.info("Mod = {}", mod);
        /* mod 빼곤 다 List로 불러와졌다. mod는 --가 없이 mod=V2 라고 args에 들어가다보니 list로 인식하지 않겠다라고 인식한다고 한다.
        즉 --가 붙어야 = 기준으로 잘라서 파싱한다고 하니 참고하자.
        스프링 부트 실행 문법으로 args를 list로 각각 가져와서 key value로 쓰려면 --key=value 형식으로 args에 넣어야 한다.
17:01:19.841 [main] INFO hello.helloenv.other.CommandLineV2 -- Url = [devOMG]
17:01:19.841 [main] INFO hello.helloenv.other.CommandLineV2 -- Username = [devUser]
17:01:19.841 [main] INFO hello.helloenv.other.CommandLineV2 -- Password = [users1123]
17:01:19.841 [main] INFO hello.helloenv.other.CommandLineV2 -- Mod = null

         결론 args를 key value로 나눠 파싱해서 자동으로 받으려면 대시2개 --key=value 를 인자로 주면 된다.
         특히 args하나에 여러개의 값이 들어갈 수도 있기에 List로 반환된다고 함.
         */

    }
}
