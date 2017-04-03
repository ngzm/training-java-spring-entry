---
layout: handson
title:  Spring ENTRY STEP07
date:   2017-04-03
---

# Spring Framework 入門
### STEP 07 / 例外をハンドリングする
***

## 1. 例外発生時にきちんとハンドリングする
#### 1-1. Spring定義ファイルで例外ハンドラを設定する
ここではアプリケーション全体で統一した例外ハンドリング方法について学習します。  
Spring MVC の Bean 定義ファイルに、例外ハンドラ（SimpleMappingExceptionResolver）の設定を追記してください。

###### 1-1-1. /src/main/webapp/WEB-INF/spring/application-context-web.xml
```xml
◆
◆ 省略
◆
	<!-- Resolves views selected for rendering by @Controllers to .jsp resources in the /WEB-INF/views directory -->
	<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/views/" />
		<property name="suffix" value=".jsp" />
	</bean>

◆
◆ -- 28行目付近
◆ ---- ↓ 例外ハンドラー設置を追加 ----
◆
	<!-- Exception Handler -->
	<bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
		<property name="exceptionMappings">
			<props>
				<!-- 必要に応じて prop を追加すること -->
				<prop key="org.springframework.dao.DataAccessException">error/dberror</prop>
				<prop key="java.lang.Exception">error/syserror</prop>
			</props>
		</property>
	</bean>
◆
◆ ---- ↑ ここまで、例外ハンドラー設置を追加 ----
◆
</beans>
```

SimpleMappingExceptionResolverを利用した例外ハンドリングについての詳細は「Spring3入門」の240ページ（第6章）が参考になるでしょう。なお SimpleMappingExceptionResolver を使用せず、コントローラクラスごとに例外処理を自分で実装する方法もありますが、この方法については「Spring3入門」の237ページ（第6章）を参照してください。

#### 1-2. 例外発生に表示するエラー画面JSPを用意
###### 1-2-1. /src/main/webapp/WEB-INF/views/error フォルダを新規作成
エラー画面JSPを格納するフォルダ（/src/main/webapp.WEB-INF/views/error）を新規作成

###### 1-2-2. /src/main/webapp/WEB-INF/views/error/dberror.jsp を新規作成
データベースエラー画面JSP（error/dberror.jsp ★パスに注意★）を新規作成して、次のようにコーディング。
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
<title>データベースエラー画面</title>
</head>
<body>
<h1>データベースエラー発生！！</h1>
<hr />
書籍管理プログラムでデータベースに関するエラーが発生しました！！！

<dl style="color:red">
<dt>例外クラス名</dt><dd><c:out value="${exception['class'].name}"/></dd>
<dt>メッセージ</dt><dd><c:out value="${exception.message}"/></dd>
</dl>

<hr />
<a href="main">書籍管理メイン画面</a> 
</body>
</html>
```
###### 1-2-3. /src/main/webapp/WEB-INF/views/error/syserror.jsp を新規作成
システムエラー画面JSP（error/syserror.jsp ★パスに注意★）を新規作成して、次のようにコーディング。
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>	
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
<title>システムエラー画面</title>
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
```

#### 1-3. 画面表示テスト
###### 1-3-1. /src/main/resource/jdbc.properties 
ここで、あえてデータベース関連の例外を発生させるため、jdbc.properties のパスワードに誤った値を設定します。

```
jdbc.driverClassName=org.postgresql.Driver
jdbc.url=jdbc:postgresql://localhost:5432/springdb
jdbc.username=postgres
jdbc.password=wrong_password  ◆◆◆<--- DB接続パスワードを不正なものに変更してみます！
```

###### 1-3-2. 画面表示テスト
書籍管理プログラムを起動し、書籍一覧画面を表示してみましょう。データベース接続に失敗するため、データベースエラー画面が表示されるはずです。

![dbError Image](/images/step7-1.png "dbError Image")

★★★**テスト完了後は、jdbc.properties のパスワードを正しい値「nttat123」に直しておいてください。**
