<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>
성공
<!-- request.getAttribute("member")로 모델에 저장한 member 객체를 꺼낼 수 있지만, 너무 복잡해진다. 주석에 달러같은거도 조심해서넣어야됨.
JSP는 \${} 문법을 제공하는데, 이 문법을 사용하면 request의 attribute에 담긴 데이터를 편리하게 조회할 수 있다 -->
<ul>
    <li>id=${member.id}</li>
    <li>username=${member.username}</li>
    <li>age=${member.age}</li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>