<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <style>
        table{margin: auto;text-align: center;width: 50%;border: solid;border-color: aqua;}
        td{border-bottom: 1px solid #ccc;color: #669;padding: 9px 8px;transition: .3s linear;}
        body{text-align:center;font-size: 16px}
    </style>

 <title>
 <c:if test="${empty user.id}"> Добавление пользователя</c:if>
 <c:if test="${not empty user.id}"> Редактирование пользователя </c:if>
 </title>
</head>
<body>
<h3>
    <c:if test="${empty user.id}"> Добавление пользователя</c:if>
    <c:if test="${not empty user.id}"> Редактирование пользователя </c:if>
</h3>
<form:form method="POST" commandName="user">
 <form:hidden path="id"/>
 <table>

   <tr>
     <td>Имя пользователя</td>
     <td><form:input path="name"/></td>
   </tr>
   <tr>
     <td>Возраст</td>
     <td><form:input path="age"/></td>
   </tr>
    <tr>
     <td>Админ</td>
     <td><form:checkbox path="isAdmin"/></td>
   </tr>
   <tr>
     <td colspan="2"><input type="submit" value="Сохранить"/></td>
   </tr>
 </table>
</form:form>
</body>
</html>