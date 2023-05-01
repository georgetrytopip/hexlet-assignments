<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Edit user</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
            crossorigin="anonymous">
    </head>
    <body>
        <div class="container">
            <a href="/users">Все пользователи</a>
            <form action="/users/edit?id=${user.get("id")}" method="post">
                <div class="mb-3">
                    <label>Имя</label>
                    <input class="form-control" type="text" name="firstName" value='${user.get("firstName")}'>
                    <label>Фамилия</label>
                    <input class="form-control" type="text" name="lastName" value='${user.get("lastName")}'>
                    <label>Email</label>
                    <input class="form-control" type="email" name="email" value='${user.get("email")}'>
                </div>
                <button class="btn btn-primary" type="submit">Редактировать</button>
                <div class="mt-3">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">${errorMessage}</div>
                    </c:if>
                </div>
            </form>
        </div>
    </body>
</html>
