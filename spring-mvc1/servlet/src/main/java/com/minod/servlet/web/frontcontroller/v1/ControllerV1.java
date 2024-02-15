package com.minod.servlet.web.frontcontroller.v1;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ControllerV1 {
    // 서블릿과 비슷한 모양의 컨트롤러 인터페이스를 도입한다. 각 컨트롤러들은 이 인터페이스를 구현하면 된다.
    // 프론트 컨트롤러는 이 인터페이스를 호출해서 구현과 관계없이 로직의 일관성을 가져갈 수 있다.

    // 기존 servlet을 유지하면서 컨트롤러로 호출하는 것을 연습하기위해, 그럴려면 컨트롤러 동작을 인터페이스로 설계후 진행하는게
    // 이후 여러 컨트롤러를 만드는데 있어 일관성이 있게할 수 있으므로 인터페이스를 만들어서 시작한다... 쉽지않군
    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
