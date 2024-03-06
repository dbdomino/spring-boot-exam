package com.minod.proxy.데코레이터패턴;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
@Slf4j
public class DecoratorPatternTest {
    @Test
    void noDecorator() {
        Component realComponent = new RealComponent(); // 주입 위해 선언

        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);

        client.execute();
    }

    @Test
    void ecorator1() {
        Component realComponent = new RealComponent(); // 주입 위해 선언

        // client -> messageDecorator -> realComponent 의 객체 의존 관계를 만들고 client.execute() 를 호출한다.
        Component messageDecorator = new MessageDecorator(realComponent); // 프록시가 만들어주는 객체1,

        DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);

        client.execute();
    }

    @Test
    void ecorator2() {
        Component realComponent = new RealComponent(); // 주입 위해 선언

        // client -> timeDecorator-> messageDecorator -> realComponent 의 객체 의존 관계를 만들고 client.execute() 를 호출한다.
        Component messageDecorator = new MessageDecorator(realComponent); // 프록시가 만들어주는 객체1,

        Component timeDecorator = new TimeDecorator(messageDecorator); // 프록시가 만들어주는 객체2,

        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);

        client.execute();
    }
}
