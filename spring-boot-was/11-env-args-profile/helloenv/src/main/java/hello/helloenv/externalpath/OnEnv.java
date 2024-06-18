package hello.helloenv.externalpath;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class OnEnv {
    // 윈도우에 등록된 환경변수 확인해보는 소스
    public static void main(String[] args) {
        Map<String, String> envMap = System.getenv();
        System.out.println("_----------------------------------_");
        for (String s : envMap.keySet()) {
            log.info("env {} = {}", s, envMap.get(s));
        }
        System.out.println("_----------------------------------_");
        // DBURL=dev.db.com    <개발서버에서 환경변수>
        // DBURL=prd.db.com    <운영서버에서 환경변수>
    }
}
