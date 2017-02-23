<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
<title>書籍管理 ログイン画面</title>
</head>
<body>
<h1>書籍管理 ログイン画面</h1>

<hr />

<form name="login_form" action="j_spring_security_check" method="POST">
<table>
 <tr>
  <th>ログインID</th>
  <td><input type="text" id="j_username" name="j_username"/></td>
 </tr>
 <tr>
  <th>パスワード</th>
  <td><input type="password" id="j_password" name="j_password"/></td>
 </tr>
</table>
<br>
 <input type="submit" value="ログイン" />
</form>

<c:if test="${param.iserror=='1'}">
<p style="color:red">ログイン認証に失敗しました。</p>
</c:if>

<hr />
</body>
</html>