package hello.helloenv.externalpath;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class JavaSystemProperties {
    public static void main(String[] args) {
        // 자바 시스템 속성(JVM 안에서 접근 가능한 외부 설정들)
        // java -Durl=dev -jar app.jar  에서 Durl 속성 같은 것을 말한다.
        // 순서에 주의해야 한다. -D 옵션이 - jar 보다 앞에 와야 한다.
        // -D라는 VM옵션을 통해서 key=value형식을 띤다. 실제로 url=dev 인 것이다.
        Properties properties = System.getProperties();
        // 프로퍼티의 키 들은 Object타입으로 나온다고 함
        for (Object key : properties.keySet()) {
            log.info("prop {} = {}", key, System.getProperty(String.valueOf(key)));

        }
        // VM 옵션에 -Durl=prd.kor.kr -Dusername=masiru -Dpassword=kimo  해서 실행해보면 값이 잘 매겨진다.
        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");

        log.info("url = {}", url);
        log.info("username = {}", username);
        log.info("password = {}", password);

        // 코드에서 시스템 속성을 자바코드로 추가하기도 가능하다.
        // 코드 안에서 사용하는 것이며, 여기까지 로만 사용하자.
//        System.setProperty(propertyName,"propertyValue");
        System.setProperty("muezzin","propertyValue");
        String muezzin = System.getProperty("muezzin");
        log.info("muezzin = {}", muezzin);
    }
}
