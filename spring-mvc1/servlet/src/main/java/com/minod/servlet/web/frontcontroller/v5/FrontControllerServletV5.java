package com.minod.servlet.web.frontcontroller.v5;

import com.minod.servlet.web.frontcontroller.ModelView;
import com.minod.servlet.web.frontcontroller.MyView;
import com.minod.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.minod.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.minod.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import com.minod.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.minod.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.minod.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import com.minod.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import com.minod.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="FrontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

//    private Map<String, ControllerV4> controllerMap = new HashMap<>(); v4에서 쓰던방식
    private Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyhandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap(); // 초기화정보 넣어줌.
        initHandlerAdapters(); // 어뎁터에다가 V3 ControllerV3HandlerAdapter 만들어 넣어줌.
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        //V4 추가
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }
    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter()); //V4 추가
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 매핑 정보로 핸들러 맵을 찾는 것을 메소드로 만듬.
        // 1. 핸들러 매핑정보에서 핸들러 조회
        Object handler = getHandler(request);

        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 2. 핸들러 어뎁터 조회
//        getHanderAdapter(handler);  // 소스 적어놓고 ctrl + alt + m 눌러서 별도 함수로 빼는것도 리펙토링 이라고함.
        MyhandlerAdapter adapter = getHanderAdapter(handler);  // 소스 적어놓고 ctrl + alt + m 눌러서 별도 함수로 빼는것도 리펙토링 이라고함.

        // 3. 어뎁터가 handle로 paramMap 만들고 modelView를 반환.
        ModelView mv = adapter.handle(request, response, handler);
        String viewName = mv.getViewName();

        // 4. View Resolver 기능을 만들자. viewResolver는 myView를 반환한다.
        MyView view = viewResolver(viewName);

        // 5. myView를 통해 render를 호출시키자. render를 위해 request, response에 Model(paramMap)을 추가해서 render 하자
//        view.render(model, request, response);
        view.render(mv.getModel(), request, response);
    }


    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Object handler = handlerMappingMap.get(requestURI);
        return handler;
    }

    private MyhandlerAdapter getHanderAdapter(Object handler) {
        MyhandlerAdapter myhandlerAdapter;
        for (MyhandlerAdapter handlerAdapter : handlerAdapters) {    // 컬렉션.iter 누르면 for문으로 바꿔줌
            if(handlerAdapter.supports(handler)) { // uri에 해당하는 핸들러가 어느 컨트롤러어뎁터랑 맞는지 구분함.
                myhandlerAdapter = handlerAdapter;//
                return myhandlerAdapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler = " +handler);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName +".jsp");
    }
}
