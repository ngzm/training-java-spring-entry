---
layout: handson
step:   "STEP11"
title:  "ログイン認証とトランザクション管理"
date:   2017-04-03
---

<h2 class="handson">1. ログイン認証を実装する</h2>
### 1-1. 認証ライブラリ Spring Security を利用するための準備
#### 1-1-1. /pom.xml

ここでは、Spring フレームワークが提供する Spring Security を利用した認証方法を学習します。  
以下の通りに、pom.xml の2箇所に Spring Security の定義を追加してください。

```xml
<!--
  @@@@ 省略
  @@@@ 9行目付近
  @@@@ <org.springsecurity-version> の定義を追加
  @@@@ -->

    <properties>
        <org.springframework-version>4.2.8.RELEASE</org.springframework-version>

<!-- @@@@ この行追加 @@@@ -->
        <org.springfsecurity-version>4.0.4.RELEASE</org.springfsecurity-version>

        <org.slf4j-version>1.7.21</org.slf4j-version>
    </properties>

<!-- @@@@ 途中省略 @@@@ 77行目付近 @@@@ -->

        <!-- Validation -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>5.1.3.Final</version>
        </dependency>
        
<!--
  @@@@ 84行目付近
  @@@@ Spring Security ライブラリの定義を追加
  @@@@ -->

        <!-- Spring Security -->
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-core</artifactId>
            <version>${org.springfsecurity-version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-web</artifactId>
            <version>${org.springfsecurity-version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-config</artifactId>
            <version>${org.springfsecurity-version}</version>
        </dependency>

<!-- @@@@ ここまで @@@@ -->

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${org.slf4j-version}</version>
        </dependency>

<!-- @@@@ 以下省略 @@@@ -->
```

Maven設定ファイル（pom.xml）を更新するとワークスペースのビルドが始まります。ビルドに数分がかかる場合がありますが、終わるまで待っているほうが良いでしょう。

### 1-2. Spring Security 定義ファイルを用意する
#### 1-2-1. /src/main/webapp/WEB-INF/spring/application-context-security.xml を新規作成

/src/main/webapp.WEB-INF/spring/application-context-security.xml を新規作してください。

#### 1-2-2. /src/main/webapp/WEB-INF/spring/application-context-security.xml をコーディング

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

### 1-3. サーブレット設定ファイル（web.xml）から Spring Security 定義 ファイルをロード
#### 1-3-1. /src/main/webapp/WEB-INF/web.xml に定義を追加

```xml
<!--
  @@@@ 省略
  @@@@ XX 行目付近
  @@@@ /WEB-INF/spring/application-context-web.xml を追加
  @@@@ -->

    <!-- The definition of the Root Spring Container shared by all Servlets and Filters -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            /WEB-INF/spring/application-context-biz.xml

<!-- @@@@ この行追加 @@@@ -->
            /WEB-INF/spring/application-context-security.xml

        </param-value>
    </context-param>

<!--
  @@@@ 省略
  @@@@ Spring Security フィルタの定義を追加
  @@@@ -->

    <!-- Spring Securityフィルタの設定 -->
    <filter>        
        <filter-name>springSecurityFilterChain</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>springSecurityFilterChain</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

<!-- @@@@ ここまで @@@@ -->

</web-app>
```

### 1-4. ログインフォーム画面とアクセス不可エラー画面を用意する
#### 1-4-1. /src/main/java/jp.sample.bookmgr.web.controller.MainController.java

ログインフォーム画面とログイン処理のコントロールメソッドをMainController に追加します。

```java
// @@@@ import 文省略

@Controller
public class MainController {
    
    /**
     * ロガー
     */
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    
    // @@@@ 
    // @@@@ xxx 行目付近
    // @@@@ ログインフォーム画面コントローラを追加
    // @@@@ 

    /**
     * ログインフォーム画面コントローラ
     *    
     * @return 画面JSP名
     */
    @RequestMapping(value = "/loginform", method = RequestMethod.GET)
    public String loginForm() {
        // 画面表示にloginform.jsp を呼び出す
        return "loginform";
    }

    // @@@@ 
    // @@@@ ここまで ログイン画面コントローラ
    // @@@@ 以下、アクセス不可エラー画面コントローラを追加
    // @@@@ 

    /**
     * アクセス不可エラー画面コントローラ
     *
     * @return 画面JSP名
     */
    @RequestMapping(value = "/accesserror", method = RequestMethod.GET)
    public String accessError() {
        return "error/accesserror";
    }

    // @@@@ 
    // @@@@ ここまで アクセス不可エラー画面コントローラ
    // @@@@ 以下省略
    // @@@@ 
```

#### 1-4-2. /src/main/webapp/WEB-INF/views/loginform.jsp

ログインフォーム画面View（loginform.jsp）新規作成

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

#### 1-4-3. /src/main/webapp/WEB-INF/views/error/accesserror.jsp

アクセス不可エラー画面View（error/accesserror.jsp）新規作成

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
<title>アクセス不可エラー画面</title>
</head>
<body>
<h1>アクセス不可エラー</h1>
<hr />

<p style="color:red">アクセス権限がありません。</p>

<hr />
<a href="main">書籍管理メイン画面</a> 
</body>
</html>
```

#### 1-4-4. /src/main/webapp/WEB-INF/views/main.jsp

main 画面View（main.jsp）にログアウトのリンクを追加

```jsp
<!-- @@@@ 省略 @@@@ -->

<a href="listbook">書籍一覧画面</a>
<br />
<a href="addbookform">書籍登録画面</a>
<br />                            <!-- @@@@ 17行目付近 左記を追加 @@@@ -->
<a href="logout">ログアウト</a>   <!-- @@@@ 18行目付近 左記を追加 @@@@ -->
</body>
```

### 1-5. 画面表示テスト
#### 1-5-1. ログイン画面

1) プログラムを起動すると、ログイン画面が表示されることを確認してください。

![login1 Image]({{ site.baseurl }}/images/step10-1.png "login1 Image")

2) ログインIDに user1／パスワードに password1 を入力るとHome画面に遷移することを確認してください。  
同様に、user2/password2  user3/password3 でもログインできるはずです。

![login2 Image]({{ site.baseurl }}/images/step10-2.png "login2 Image")

3) 正常にログインしたら main画面が表示されますが、main画面にある [ログアウト]リンクをクリックするとログイン画面に戻ることを確認してください。

![main Image]({{ site.baseurl }}/images/step10-3.png "main Image")

![login3 Image]({{ site.baseurl }}/images/step10-4.png "login3 Image")

このとき、既にログアウト処理が走っていますので、戻るボタンなどで main 画面に戻ることはできません。

4) 不正な ID/Password を入力した場合は、エラーメッセージが表示されたログイン画面に戻ることを確認してください。

![login4 Image]({{ site.baseurl }}/images/step10-5.png "login4 Image")

5) ログインしていない状態で、アドレスバーからダイレクトにメイン画面や書籍一覧画面のURLを入力してみます。  
すると、その画面に遷移することなく、ログイン画面が表示することを確認してください。

![login5 Image]({{ site.baseurl }}/images/step10-6.png "login5 Image")

この状態でログインすると、ダイレクトに指定したURLに対する画面に遷移するはずです。

![bookList Image]({{ site.baseurl }}/images/step10-7.png "bookList Image")

<h2 class="handson">2. トランザクション管理を実装する</h2>
### 2-1. トランザクションマネージャを利用できるようにする
#### 2-1-1. /src/main/webapp/WEB-INF/spring/application-context-biz.xml

Spring定義ファイルにトランザクションマネージャの定義を追加する

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:tx="http://www.springframework.org/schema/tx"              @@@@ この行追加 @@@@
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd    @@@@ 最後の">を削除 @@@@
        http://www.springframework.org/schema/tx                    @@@@ この行追加 @@@@
        http://www.springframework.org/schema/tx/spring-tx.xsd">    @@@@ この行追加 @@@@
        
    <!-- Defines shared resources visible to all other web components -->
    <context:component-scan base-package="jp.sample.bookmgr.biz" />
    
    <!-- Enables the Spring AOP -->
    <aop:aspectj-autoproxy />
    
    <!-- データソース用のリソース -->
    <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="location" value="classpath:/jdbc.properties" />
    </bean>
    
    <!-- データソース設定 (for tomcat dbcp) -->
    <bean id="dataSource" class="org.apache.tomcat.jdbc.pool.DataSource" destroy-method="close">
        <property name="driverClassName" value="${jdbc.driverClassName}" />
        <property name="url" value="${jdbc.url}" />
        <property name="username" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
    </bean>
    
    <!-- JDBC Template Bean -->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource" />
    </bean>
    <!-- JDBC Named Template Bean -->
    <bean id="namedJdbcTemplate" class="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate">
        <constructor-arg ref="dataSource" />
    </bean>
    
    <!-- Message Source -->
    <bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
        <property name="basename" value="classpath:/messages" />
    </bean>
    <!-- Validator -->
    <bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
        <property name="validationMessageSource" ref="messageSource" />
    </bean>

<!--
  @@@@ xx 行目付近
  @@@@ トランザクションマネージャの定義を追加
  @@@@ -->

    <!-- Transaction Manager -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!-- アノテーションベースのトランザクションを有効にする -->
    <tx:annotation-driven />

<!-- @@@@ ここまで @@@@ -->

</beans>
```

### 2-2. トランザクション管理を実装する
#### 2-2-1. /src/main/java/jp.sample.bookmgr.biz.service.ListBookServiceImple.java

書籍一覧サービスに readOnly モードでトランザクションを設定します。  
書籍一覧サービス「ListBookServiceImple」はDB更新がないため、トランザクションモードは readOnly で設定します。

```java
// @@@@ 省略
// @@@@ 17行目付近

@Service // サービスクラスとしてDI可能というアノテーションを宣言
public class ListBookServiceImple implements ListBookService {
    /**
     *  書籍一覧取得DAO
     */
    @Autowired        // ListBookDaoオブジェクトをインジェクション
    ListBookDao listBookDao;

    /**
     * 書籍一覧取得サービス
     * @return    書籍一覧情報
     */ 
    @Override
    @Transactional(readOnly=true)     // @@@@ Transactional アノテーションを追加 @@@@
    public List<Book> getBookList() throws Exception {
        // 書籍一覧を取得
        return listBookDao.getBookList();
    }
}
```

#### 2-2-2. /src/main/java/jp.sample.bookmgr.biz.service.AddBookServiceImple.java

書籍登録サービスに、ReadCommitted モードでトランザクションを設定します。  
書籍追加サービス「AddBookServiceImple」はDB更新系なので、ReadCommitted モードでトランザクションを設定します。

```java
// @@@@ 省略
// @@@@ 15行目付近

@Service    // サービスクラスとしてDI可能というアノテーションを宣言
public class AddBookServiceImple implements AddBookService {
    /**
     * データベースに書籍登録を行うDAOクラス
     */
    @Autowired        // インジェクション
    private AddBookDao addBookDao;
    
    /**
     * 書籍登録サービスを実行する
     * 
     * @param Book 書籍情報
     */ 
    @Override

    // @@@@ xx 行目
    // @@@@ 更新系のトランザクション制御を設定する
    @Transactional(
        propagation=Propagation.REQUIRED,
        isolation=Isolation.READ_COMMITTED,
        timeout=10,
        readOnly=false,
        rollbackFor=RuntimeException.class)

    // @@@@ ここまで @@@@

    public void addBook(Book book) throws Exception {
        // 書籍登録を行うDAOクラスを使用して書籍情報を永続化する
        addBookDao.addBook(book);
    }
}
```

#### 2-3. トランザクション制御のテスト
コードを追加しないとできないため講師の方でデモを行います。
