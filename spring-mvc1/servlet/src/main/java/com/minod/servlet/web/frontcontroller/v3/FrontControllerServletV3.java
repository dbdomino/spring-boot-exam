package com.minod.servlet.web.frontcontroller.v3;

import com.minod.servlet.web.frontcontroller.ModelView;
import com.minod.servlet.web.frontcontroller.MyView;
import com.minod.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.minod.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.minod.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name="FrontControllerServletV3", urlPatterns = "/front-controller/v3/*")  // urlPattern 이 중요,
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();
    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV3.service");

        String requestURI = request.getRequestURI(); // 호스트뒤에 자원이 리턴됨.
        System.out.println("URI : "+requestURI);

        ControllerV3 controller= controllerMap.get(requestURI); // hoho
        if (controller == null ) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // ModelView에  Map<String, Object> model           모델을 쓰는이유? jsp경로를 각 서블릿 컨트롤러에 넣어야하는 서블릿 종속성을 없애기 위해, 프론트 컨트롤러에서 jsp경로까지 컨트롤시키려고
        // ControllerV3     process (Map<String, String> paramMap)
        // 이 두개를 활용해서 컨트롤러를 싱행시키려면?
        // 1. request로 받은 걸 먼저 다 읽어와서 paramMap에 넣는다.
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()   // request의 값 모두 빼는방법 Iterator 쓰기
                .forEachRemaining(paramName -> paramMap.put(paramName,request.getParameter(paramName)));

        // 2. paramMap의 정보를 ModelView에 넣어준다.
//        ModelView modelView0 = new ModelView(); // 이건 안된다. controller통해서만 ModelView를 쓰도록 약속했기 때문. 제한 제약이다.
        ModelView modelView = controller.process(paramMap);

        // 3. View Resolver 기능을 만들자. viewResolver는 myView를 반환한다.
        String viewName = modelView.getViewName(); // 논리이름 new-form 같은거
        System.out.println(viewName);
        MyView myView = new MyView("/WEB-INF/views/" + viewName+".jsp"); // jsp 경로를 프론트컨트롤러 에서 지원한다.

        // 4. myView를 통해 render를 호출시키자. render를 위해 request, response에 Model(paramMap)을 추가해서 render 하자
        // 뷰가 렌더링 하려면 모델정보(modelView)가 필요해졌기 때문이다.
        // request 와 response를 paramMap으로 받아왔다. 이를 적용하기위해 render를 overloading 하자
//        myView.render(request, response);
        myView.render(modelView.getModel(), request, response);

    }
}
