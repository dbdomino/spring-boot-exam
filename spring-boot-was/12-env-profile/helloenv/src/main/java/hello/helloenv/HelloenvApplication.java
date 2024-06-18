package hello.helloenv;

import hello.helloenv.config.MyDataSourceValueConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@Import(MyDataSourceEnvConfig.class) // 설정 파일을 이걸로 제어, 들어갈 파일에 @Configuration 이 있어야함.
@Import(MyDataSourceValueConfig.class)
// 하나씩 설정파일 비교해가며 테스트해볼거라 ComponentScan 경로를 datasource로 지정
@SpringBootApplication(scanBasePackages = "hello.helloenv.datasource")
public class HelloenvApplication {
//  --url=devOMG --username=devUser --password=users1123
    public static void main(String[] args) {
        SpringApplication.run(HelloenvApplication.class, args);
    }

}
