---
layout: handson
title:  Spring ENTRY STEP06
date:   2017-04-03
---

# Spring Framework 入門
### STEP 06 / 日本語文字化けとかの対策 ＋ Validation Check の実装
***

## 1. 日本語文字化け対策と HTML ESCAPE を設定する
#### 1-1. 日本語文字化け対策とHTML ESCAPE対策
日本語文字が文字化けする対策については、Spring MVC により提供されているフィルタを設定する方法が最も確実で簡単で一般的です。また、通常は HTML ESCAPE の設定も併せて行います。

###### 1-1-1. /src/main/webapp/WEB-INF/web.xml
```xml
◆
◆ 省略
◆ --34行目付近
◆
    <servlet-mapping>
        <servlet-name>appServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    
  ◆
  ◆ --39行目付近
  ◆ ---- ↓ HTML ESCAPEの設定を追加 ----
  ◆
    <!-- HTML ESCAPEの設定 -->
    <context-param>    
        <param-name>defaultHTMLEscape</param-name>
        <param-value>true</param-value>
    </context-param>
  ◆
  ◆ ---- ↑ HTML ESCAPEの設定ここまで ----
  ◆ ---- ↓ 続いて、日本語文字化け対策設定を追加 ----
  ◆
    <!-- 日本語文字化け対策の設定 -->
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
  ◆
  ◆ ---- ↑ 日本語文字化け対策設定ここまで ----
  ◆
</web-app>
```

#### 1-2. 画面表示テスト
書籍登録画面化から下記の日本語を含むデータを投入して登録してみてください。

![AddBookForm Image](/images/step6-1.png "AddBookForm Image")

書籍一覧画面で、日本語が文字化けすることなく正常に登録されていることが確認できましたでしょうか？

![ListBook Image](/images/step6-2.png "ListBook Image")

## 2. バリデーションチェックのための準備をする
#### 2-1. 日本語文字でプロパティファイルを編集できる Eclide プラグインを導入
本ツールは必須ではないが、これがないと日本語によるプロパティファイル編集が面倒（いちいちnative2ascii などで変換が必要）なので導入しておくと便利です。

```
a. Spring Tool Suite のインストール
  [Help]→[Eclipse MarketPlace] 検索画面で [Properties Editor]を検索し同ツールをインストールする。
```
★★ インストール途中でワーニングが出る場合がありますがインストールを続行して問題ありません。  
インストール後再起動します。

#### 2-2. Maven設定ファイル pom.xml にValidatorライブラリ（Hybernate Validator）を追加する
###### 2-2-1. /pom.xxml
```xml
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>9.4.1208.jre7</version>
        </dependency>
        
      ◆
      ◆ --64行目付近
      ◆ ---- ↓ Vaidation ライブラリ情報を追加 ----
      ◆
        <!-- Validation -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>5.2.4.Final</version>
        </dependency>
      ◆
      ◆ ---- ↑ ここまで Vaidation ライブラリ情報 ----
      ◆

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${org.slf4j-version}</version>
        </dependency>
```

★Maven設定ファイル（pom.xml）を更新するとワークスペースのビルドが始まります。ビルドに数分がかかる場合がありますが、終わるまで待っているほうが良いでしょう。

#### 2-3. Validation メッセージファイル（プロパティファイル）を新規作成
###### 2-3-1. /src/main/resource/messages_ja.properties を新規作成、以下のようにコーディング
```java
typeMismatch.int=数値を入力してください。
javax.validation.constraints.NotNull.message={0}は必須です。
javax.validation.constraints.Size.message=サイズは{min}から{max}の間の値を入力してください。
org.hibernate.validator.constraints.NotEmpty.message={0}は必須です。
org.hibernate.validator.constraints.NotBlank.message={0}は必須です。
book.isbn=ISBNコード
book.name=書籍名
book.price=価格
```

メッセージプロパティの記述方法は、Spring3入門の212ページ（第6章）が参考になります。

#### 2-4.Spring定義ファイルにメッセージソースとValidator Bean定義を追加
###### 2-4-1. /src/main/webapp/WEB-INF/spring/application-context-biz.xml
```xml
    <!-- JDBC Named Template Bean -->
    <bean id="namedJdbcTemplate" class="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate">
        <constructor-arg ref="dataSource" />
    </bean>
    
  ◆
  ◆ --36行目付近
  ◆ ---- ↓ メッセージリソース定義を追加 ----
  ◆
    <!-- Message Source -->
    <bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
        <property name="basename" value="classpath:/messages" />
    </bean>
  ◆
  ◆ ---- ↑ メッセージリソース定義ここまで
  ◆ ---- ↓ 続けて、Validator Bean 定義
  ◆
    <!-- Validator -->
    <bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
        <property name="validationMessageSource" ref="messageSource" />
    </bean>
  ◆
  ◆ ---- ↑ Validator Bean 定義ここまで
  ◆
  
</beans> 
```

## 3. 書籍登録時のバリデーションチェックを実装する
#### 3-1. 書籍クラスにバリデーション用のアノテーションを追加
###### 3-1-1. /src/main/java/jp.sample.bookmgr.biz.domain.Book.java
```java
◆
◆ Import文
◆

/**
 * 書籍クラス
 * @author 長住@NTT-AT
 * @version 1.0
 */
public class Book {

    /**
     * 書籍ID
     */
    private int id = 0;
    
    /**
     * 書籍ISBN
     */
    @NotBlank                   ◆◆◆ 空白不可を示すアノテーション ◆◆◆
    @Size(min=10, max=16)       ◆◆◆ 文字サイズを示すアノテーション ◆◆◆
    private String isbn = "";
    
    /**
     * 書籍名
     */
    @NotBlank                   ◆◆◆ 空白不可を示すアノテーション ◆◆◆
    private String name = "";
    
    /**
     * 書籍価格
     */
    @NotNull                    ◆◆◆ NULL不可を示すアノテーション ◆◆◆
    private int price = 0;

◆
◆ 以下変更なし
◆
```

バリデーション用のアノテーションの種類は Spring3入門の208ページ（第6章）が参考になります。

#### 3-2. 書籍登録コントローラに Validation Check 結果をハンドリングするコードを追加
###### 3-2-1. /src/main/java/jp.sample.bookmgr.web.controller.BookController.java 修正・追加
```java
◆
◆ 省略
◆ --- 85行目付近
◆
    /**
     * 書籍登録処理コントローラ
     * 
     * @param book Spring MVC ModelオブジェクトからバインドされたBookインスタンス
     * @param result バインディング処理時のバリデーションチェック結果
     * @return 登録結果表示JSP名
     */ 
    @RequestMapping(value = "/addbook", method = RequestMethod.POST)
    public String addBook(@Valid @ModelAttribute("book") Book book,   ◆◆◆ @Valid アノテーションを追加 ◆
            BindingResult result) throws Exception {                  ◆◆◆ BindingResult result を追加 ◆
        
        logger.debug("addbook() start");
        logger.debug("book isbn:" + book.getIsbn());
        logger.debug("book name:" + book.getName());
        logger.debug("book price:" + book.getPrice());
        
      ◆
      ◆ ---- ↓ Validation Check エラー時のハンドリング処理を追加
      ◆
        // Validation エラーが発生しているときは登録フォームに戻る
        if(result.hasErrors()){
            return "addbookform";
        }
      ◆
      ◆ ---- ↑ ここまで、Validation Check エラー時のハンドリング
      ◆
        
        // 書籍情報登録処理
        addBookService.addBook(book);
        
        // 登録結果画面 result.jsp を呼び出す
        return "redirect:result";
    }
◆
◆ 以降変更なし 
◆
```

バリデーションの実装に関する説明は Spring入門の第6章が参考になります。

#### 3-3. 書籍登録フォーム画面に Validation エラーメッセージ表示コードを追加
###### 3-3-1. /src/main/webapp/WEB-INF/views/addbookform.jsp
```jsp
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
<h1>書籍登録画面</h1>

<p>
<form:form action="addbook" method="POST" modelAttribute="book">
    <table>
    <tr>
        <th>ISBNコード</th>
        <td><form:input path="isbn"/></td>
◆ ---- ↓ Validation Check エラーメッセージの表示を追加
        <td><form:errors path="isbn" cssStyle="color:red"/></td>
◆ ---- ↑ この1行追加
    </tr><tr>
        <th>書籍名</th>
        <td><form:input path="name"/></td>
◆ ---- ↓ Validation Check エラー時のハンドリング処理を追加
        <td><form:errors path="name" cssStyle="color:red"/></td>
◆ ---- ↑ この1行追加
    </tr><tr>
        <th>価格</th>
        <td><form:input path="price"/></td>
◆ ---- ↓ Validation Check エラー時のハンドリング処理を追加
        <td><form:errors path="price" cssStyle="color:red"/></td>
◆ ---- ↑ この1行追加
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
```
Spring Formタグの説明は、以下のサイトが詳しいかもしれません。  
http://kuwalab.hatenablog.jp/entry/20130118/p1

#### 3-4. 画面表示テスト
ここで、書籍登録画面（フォーム）に下記のデータを入力して登録してみましょう。

![AddBookForm1 Image](/images/step6-3.png "AddBookForm1 Image")

バリデーション処理が正常にコーディングできていれば、以下のエラーが表示されるはずです！  
いかがでしょうか？

![AddBookForm2 Image](/images/step6-4.png "AddBookForm2 Image")
