package com.typeconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TypeconverterApplication {

    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();

        // 운영체제에 따라 프로퍼티 외부에서 불러오도록 하는 방법
/*        if (os.contains("win")) {
            new SpringApplicationBuilder(TypeconverterApplication.class)
                    .properties(
                            "spring.config.location=" +
                                    "C:\\Users\\gyuse\\Desktop\\application-dev.yml"
                    )
                    .run(args);
        }

        else {
            new SpringApplicationBuilder(TypeconverterApplication.class)
                    .properties(
                            "spring.config.location=" +
                                    "\\home\\ec2-user\\app\\application-dev.yml"
                    )
                    .run(args);
        }*/

        SpringApplication.run(TypeconverterApplication.class, args);
    }

}
