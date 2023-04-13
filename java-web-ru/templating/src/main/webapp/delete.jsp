<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Confirm delete</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
          crossorigin="anonymous">
</head>
<body>
<%
    Map<String,String> user = (Map<String, String>) request.getAttribute("user");
    if (user != null){
%>
<h1>Are you sure?</h1>
<p>You're going to delete user with name <%= user.get("firstName")%> and id <%= user.get("id")%></p>
<div class="button-container">
    <form method="post" action="/users/delete">
        <input type="hidden" name="id" value="<%= user.get("id") %>">
        <button class="btn btn-danger" type="submit">Delete</button>
    </form>
    <button class="btn btn-secondary" onclick="location.href='/users/show?id=<%= user.get("id")%>'">Decline</button>
</div>
<%
} else {

%>

<p>User not found </p>

<%
    }
%>
</body>
</html>