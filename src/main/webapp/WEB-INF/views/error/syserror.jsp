<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
<title>システムスエラー画面</title>
</head>
<body>
<h1>システムエラー発生！！</h1>
<hr />
書籍管理プログラムでシステムエラーが発生しました！！！

<dl style="color:red">
<dt>例外クラス名</dt><dd><c:out value="${exception['class'].name}"/></dd>
<dt>メッセージ</dt><dd><c:out value="${exception.message}"/></dd>
</dl>

<hr />
<a href="main">書籍管理メイン画面</a> 
</body>
</html>