package com.minod.core;

import lombok.*;

@Getter // getter 자동생성
@Setter // setter 자동생성
//@NoArgsConstructor // 기본 생성자 자동생성 해줍니다.
@RequiredArgsConstructor //  초기화 되지않은 final 필드, Notnull 표시가 된 필드에 대한 정보를 세팅하는 생성자를 만들어줍니다. 자동생성되는것 확인 ctrl+F12
@AllArgsConstructor //  모든 필드에 대한 정보를 세팅하는 생성자를 만들어줍니다. 자동생성되는것 확인 ctrl+F12
public class HelloLombokApp {
    private String name;
    private int age;
    private final int ago;
    @NonNull private String val;
    private final int ago2;

    public static void main(String[] args) {
        HelloLombokApp helloLombok = new HelloLombokApp(15,"value",25);


        helloLombok.setName("asdf");

        System.out.println("helloLombok name = " + helloLombok.getName());

    }
}
