package com.minod.servlet.web.frontcontroller.v4;

import com.minod.servlet.web.frontcontroller.MyView;
import com.minod.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.minod.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.minod.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        ControllerV4 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 1. request로 받은 걸 먼저 다 읽어와서 paramMap에 넣는다.
        // ModelView가 빠지고 model 객체를 프론트 컨트롤러에서 생성해서 View에 넘겨준다.
        // 컨트롤러에서 모델 객체에 값을 담으면 여기에 그대로 담겨있게된다.
        //       request  ->  paramMap    -> model -> request 로 가네
        // 위치 프론트컨트롤러  프론트컨트롤러    뷰리졸버   myView
        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>(); //추가

        // 2. paramMap의 정보를 ModelView에 넣어준다.
        // 이번엔 ModelView가  빠진다.
        // ModelView mv = controller.process(paramMap, model);
        // String viewName = modelView.getViewName(); // 논리이름 new-form 같은거

        // 3. View Resolver 기능을 만들자. viewResolver는 myView를 반환한다.
        String viewName = controller.process(paramMap, model);
        MyView view = viewResolver(viewName);

        // 4. myView를 통해 render를 호출시키자. render를 위해 request, response에 Model(paramMap)을 추가해서 render 하자
        view.render(model, request, response);
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName,
                        request.getParameter(paramName)));
        return paramMap;
    }
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
