---
layout: handson
step:   "STEP11"
title:  "ユーザ認証とトランザクション管理"
date:   2017-04-24
---

<h2 class="handson">1. ユーザ認証とトランザクション管理</h2>

### 1-1. 実習テーマ

1. Spring Security を用いてユーザ認証を実装する
2. Spring Transaction Manager を利用してトランザクションを実現する

![step11_image]({{ site.baseurl }}/images/texts/text_step11_01.png "Step11 Image")

### 1-2.今回やること および 学習ポイント

1. Spring Security を用いてユーザ認証を実装する
    - やること
        - Spring Security を利用するための環境設定を行う
        - ログインフォーム画面、認証エラー画面 Controller を作成
        - ログインフォーム画面、認証エラー画面 JSP を作成
    - 学習ポイント
        - Spring Security を利用した認証の仕組みを理解する
        - Spring Security を利用した認証処理の実装手順を把握する
2. Spring Transaction Manager を利用してトランザクションを実現する
    - やること
        - Spring Transaction Manager を使用するための環境設定を行う
        - Transaction Manager アノテーションを用いてトランザクションを実装する
    - 学習ポイント
        - Sprint Transaction Manager を利用したトランザクション実現方法を把握する

<h2 class="handson">2. 技術解説 - Spring Security によるユーザ認証</h2>

### 2-1. Spring Security とは

hogehoge

### 2-2. Spring Security でユーザ認証する Spring 定義ファイルの例

- Spring 定義ファイル

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
                xmlns:sec="http://www.springframework.org/schema/security"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="
                    http://www.springframework.org/schema/beans
                    http://www.springframework.org/schema/beans/spring-beans.xsd 
                    http://www.springframework.org/schema/security
                    http://www.springframework.org/schema/security/spring-security.xsd">

        <!-- 以下のURLに対してはSpring Securityの対象外とする-->
        <sec:http pattern="/loginform" security="none" />
        <sec:http pattern="/accesserror" security="none" />
        <sec:http pattern="/resources/**" security="none" />
        <sec:http pattern="/css/**" security="none" />

        <!-- HTTP-Security  auto-configをtrueにしてデフォルト設定を有効とする -->
        <sec:http auto-config="true">
            <!-- アクセス権限がない場合のエラーハンドラ -->
            <sec:access-denied-handler error-page="/accesserror" />
            
            <!-- Cross-Site Request Forgery protection を無効にしておく -->
            <sec:csrf disabled="true"/>

            <!-- 全てのBookmgrサービスには ROLE_USER 権限を必要とする-->
            <sec:intercept-url pattern="/**" access="hasRole('ROLE_USER')" />
            
            <!-- ログイン画面を独自の画面に切り替える -->
            <sec:form-login login-page="/loginform"
                        username-parameter="j_username"
                        password-parameter="j_password"
                        login-processing-url="/j_spring_security_check"
                        default-target-url="/main"
                        authentication-failure-url="/loginform?iserror=1"/>
            
            <!-- ログアウト処理の設定 -->
            <sec:logout logout-url="/logout" logout-success-url="/loginform"
                    invalidate-session="true"/>
        </sec:http>

        <!-- ユーザ認証の設定 -->
        <sec:authentication-manager>
            <sec:authentication-provider>
                <sec:user-service>
                    <!-- ここでは簡単に認証情報を定義する -->
                    <!-- DBにアクセスして認証することも、LDAPとかを利用することも可能とのこと -->
                    <sec:user name="user1" password="password1" authorities="ROLE_USER" />
                    <sec:user name="user2" password="password2" authorities="ROLE_USER" />
                    <sec:user name="user3" password="password3" authorities="ROLE_USER" />
                </sec:user-service>
            </sec:authentication-provider>
        </sec:authentication-manager>

    </beans>
    ```

### 2-2. Spring Security でユーザ認証する Spring 定義ファイルの例

- ログインフォーム画面View（loginform.jsp）新規作成

    ```jsp
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
    ```

<h2 class="handson">3. 技術解説 - Spring transaction Manager によるトランザクション</h2>

### 3-1. Spring Transaction Manager によるトランザクション制御

hogehoge

### 3-2. データ検索処理のトランザクション例

- hogehoge

    ```java
    @Transactional(readOnly=true)     // @@@@ Transactional アノテーションを追加 @@@@
    public List<Book> getBookList() throws Exception {
        // Something to do ..

    }
    ```

### 3-3. データ更新処理のトランザクション例

- hogehoge

    ```java
    // @@@@ 更新系のトランザクション制御を設定する
    @Transactional(
        propagation=Propagation.REQUIRED,
        isolation=Isolation.READ_COMMITTED,
        timeout=10,
        readOnly=false,
        rollbackFor=RuntimeException.class)

    public void addBook(Book book) throws Exception {
        // Something to do ..

    }
    ```

<h2 class="handson">4. ハンズオン実習</h2>

### STEP11 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP11' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - Spring Security を用いたユーザ認証の実現方法を学習しましょう
    - Spring Transaction Manager を利用してトランザクションを実現しましょう
