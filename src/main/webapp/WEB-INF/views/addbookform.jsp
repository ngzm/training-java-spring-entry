<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
<title>書籍登録フォーム画面</title>
</head>
<body>

<h1>書籍登録フォーム画面</h1>

<p>
<form:form action="addbook" method="POST" modelAttribute="book">
	<table>
	<tr>
		<th>ISBNコード</th>
		<td><form:input path="isbn"/></td>
		<td><form:errors path="isbn" cssStyle="color: red" /></td>
	</tr><tr>
		<th>書籍名</th>
		<td><form:input path="name"/></td>
		<td><form:errors path="name" cssStyle="color: red" /></td>
	</tr><tr>
		<th>価格</th>
		<td><form:input path="price"/></td>
		<td><form:errors path="price" cssStyle="color: red" /></td>
	</tr>
	</table>
	<br>
	<input type="submit" value="登録" />
</form:form>
</p>

<hr />
<a href="main">書籍管理メイン画面</a>

</body>
</html>