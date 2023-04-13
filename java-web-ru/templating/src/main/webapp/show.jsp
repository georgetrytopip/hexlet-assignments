<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>User details</title>
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
<table>
    <tr>
        <th>id</th>
        <th>firstName</th>
        <th>lastName</th>
        <th>email</th>
    </tr>
    <tr>
        <td><%= user.get("id")%></td>
        <td><%= user.get("firstName")%></td>
        <td><%= user.get("lastName")%></td>
        <td><%= user.get("email")%></td>
    </tr>
</table>
<div><button type="button" class="btn btn-danger" onclick="location.href='/users/delete?id=<%= user.get("id")%>'">Delete User</button></div>
<div><button type="button" class="btn btn-primary" onclick="location.href='/users'">Come back to users</button></div>
<%
        } else {

%>

<p>User not found </p>

<%
        }
%>
</body>
</html>






