package hello.helloenv.commandline;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;

import java.util.List;
import java.util.Set;

@Slf4j
//@Component
public class CommandLineBean {
    // 자동설정으로 외부 환경변수 읽어들이는 bean, 스프링이 제공
    // argument에서 List로 읽어들임.
    private final ApplicationArguments arguments;
    // 생성자주입
    public CommandLineBean(ApplicationArguments arguments) {
        this.arguments = arguments;
    }

    @PostConstruct
    public void init() {
        log.info("source {}", List.of(arguments.getSourceArgs()));
        log.info("optionName {}", arguments.getOptionNames());
        Set<String> optionNames = arguments.getOptionNames();
        // set으로 옵션 key들 가져오기

        for(String optionName : optionNames) {
            // key에 맞는 value찾아오기
            log.info("option args {} = {}", optionName, arguments.getOptionValues(optionName));
        }
    }
}
