package com.minod.servlet.web.frontcontroller.v2;

import com.minod.servlet.web.frontcontroller.MyView;
import com.minod.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import com.minod.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import com.minod.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name="FrontControllerServletV2", urlPatterns = "/front-controller/v2/*")  // urlPattern 이 중요,
public class FrontControllerServletV2 extends HttpServlet {

    private Map<String, ControllerV2> controllerMap = new HashMap<>();
    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV2.service");

        String requestURI = request.getRequestURI(); // 호스트뒤에 자원이 리턴됨.
        System.out.println("URI : "+requestURI);

        ControllerV2 controller= controllerMap.get(requestURI); // hoho
        if (controller == null ) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 프론트 컨트롤러의 도입으로 service 메소드로 모든 서블릿 실행 가능
        // 서블릿의 실행또한 MyView로 설계하여 service에서 실행한다.(각각 서블릿에서 페이지로맵핑 하는게아니라, 프론트 컨트롤러에서 응답시킬 주소를 선언시킨다. 이게더효율적)
        MyView myView = controller.process(request,response);
        myView.render(request, response); // 이게 핵심
    }
}
