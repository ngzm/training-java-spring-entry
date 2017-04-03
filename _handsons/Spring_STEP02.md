---
layout: handson
title:  "STEP 02 / Spring MVC による画面遷移"
date:   2017-04-03
---

## 1. Hello World 画面からメイン画面への遷移を実装してみる
#### 1-1. hello.jsp にメイン画面へのリンクを追加
###### 1-1-1. /src/main/webapp/WEB-INF/views/hello.jsp 修正
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello Spring 画面</title>
</head>
<body>

<h1>Hello Spring</h1>  
きちんとみれましたでしょうか？

◆
◆ ---- ↓メイン画面へのリンクを追加
◆
<hr>
<a href="main">書籍管理メイン画面</a> 
◆
◆ ---- ↑ここまで、メイン画面へのリンクを追加
◆

</body>
</html>
```

#### 1-2. メイン画面コントローラ作成
###### 1-2-1. /src/main/java/jp.sample.bookmgr.web.controller.MainController.java 作成
```
/src/main/java/ で右クリック→[New]→[Class]
  
Java Class 画面
---------------
Package: "jp.sample.bookmgr.web.controller"
Name: "MainController" と入力して[Finish]
---------------
```

###### 1-2-2. /src/main/java/jp.sample.bookmgr.web.controller.MainController.java コーディング
```java
package jp.sample.bookmgr.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 書籍管理メイン画面コントローラクラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Controller
public class MainController {
  
  /**
   * ロガー
   */
  private static final Logger logger = LoggerFactory.getLogger(MainController.class);
  
  /**
   * 書籍管理メイン画面コントローラ
   * 
   * @return 画面JSP名
   */
  @RequestMapping(value = "/main", method = RequestMethod.GET)
  public String main() throws Exception {
    
    logger.debug("main() start");
    
    // 画面表示に main.jsp を呼び出す
    return "main";
  }
}
```

#### 1-3. メイン画面ビュー作成
###### 1-3-1. /src/main/webapp/WEB-INF/view/main.jsp 作成
```
/src/main/webapp/WEB-INF/views で右クリック→[New]→[JSP File]

New JSP File  画面
---------------
File Name: "main.jsp" と入力して[Finish]
---------------
```

###### 1-3-2. /src/main/webapp/WEB-INF/view/main.jsp コーディング
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
<title>書籍管理メイン画面</title>
</head>
<body>

<h1>書籍管理メイン画面</h1>

<hr />
<a href="listbook">書籍一覧画面</a>
<br />
<a href="addbookform">書籍登録画面</a>

</body>
</html>
```

#### 1-4. 画面表示テスト
プログラムを起動して、うまくコードが実行されるか、うまく画面が表示されるかを確認してください。

![Hello to Main Image](/images/step2-1.png "Hello to Main Image")

書籍一覧画面や書籍登録画面はまだ実装していないため、アクセスしても404エラーとなります。

## 2. メイン画面から書籍一覧と書籍登録フォーム画面に遷移してみる
#### 2-1. 書籍一覧画面コントローラ作成
###### 2-1-1. /src/main/java/jp.sample.bookmgr.web.controller.BookController.java 作成
```
/src/main/java/ で右クリック→[New]→[Class]

Java Class 画面
---------------
Package: "jp.sample.bookmgr.web.controller"
Name: "BookController" と入力して[Finish]
---------------
```

###### 2-1-2. /src/main/java/jp.sample.bookmgr.web.controller.BookController.java コーディング
```java
package jp.sample.bookmgr.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 書籍管理処理コントローラクラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Controller // ”コントローラとしてDI可能" というアノテーションを宣言
public class BookController {

  /**
   * ロガー
   */
  private static final Logger logger = LoggerFactory.getLogger(BookController.class);
  
  /**
   * 書籍一覧画面コントローラ
   * 
   * @return 画面JSP名
   */ 
  @RequestMapping(value = "/listbook", method = RequestMethod.GET)
  public String listBook() throws Exception {
    
    logger.debug("listBook() start");
    
    // 画面表示に listbook.jsp を呼び出す
    return "listbook";
  }
}
```

#### 2-2. 書籍一覧画面ビュー作成
###### 2-2-1. /src/main/webapp/WEB-INF/views/listbook.jsp 作成
```
/src/main/webapp/WEB-INF/views で右クリック→[New]→[JSP File]

New JSP File  画面
---------------
File Name: "listbook.jsp" と入力して[Finish]
---------------
```

###### 2-2-2. /src/main/webapp/WEB-INF/views/listbook.jsp コーディング
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
<title>書籍一覧画面</title>
</head>
<body>

<h1>書籍一覧画面</h1>

◆
◆ 中身はまだ実装できないので、とりあえず工事中と表示する
◆
<p>
工事中！！
</p>

<hr />
<a href="main">書籍管理メイン画面</a>

</body>
</html>
```

#### 2-3. 書籍登録フォーム画面コントローラ作成
###### 2-3-1. /src/main/java/jp.sample.bookmgr.web.controller.BookController.java
```java
◆ 省略
◆ -- 34行目付近 (listBookメソッドの次)
◆ ---- ↓書籍登録フォーム画面コントローラのコードを追加
◆
  /**
   * 書籍登録フォーム画面コントローラ
   * 
   * @return 画面JSP名
   */ 
  @RequestMapping(value = "/addbookform", method = RequestMethod.GET)
  public String addBookForm() throws Exception {
    
    logger.debug("addbookform() start");
    
    // 画面表示に addbookform.jsp を呼び出す
    return "addbookform";
  }
◆
◆ ---- ↑ここまで、書籍登録フォーム画面コントローラのコードを追加
◆ 以下省略
```

#### 2-4. 書籍登録フォーム画面ビュー作成
###### 2-4-1. /src/main/webapp/WEB-INF/views/addbookform.jsp 新規作成
```
/src/main/webapp/WEB-INF/views で右クリック→[New]→[JSP File]

New JSP File  画面
---------------
File Name: "addbookform.jsp" と入力して[Finish]
---------------
```

###### 2-4-2. /src/main/webapp/WEB-INF/views/addbookform.jsp コーディング
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
工事中！！
</p>

<hr />
<a href="main">書籍管理メイン画面</a>

</body>
</html>
```

#### 2-5. 画面表示テスト
プログラムを起動して、うまくコードが実行されるか、うまく画面が表示されるかを確認してください。

![Main to BookList and BookAddForm Image](/images/step2-2.png "Main to BookList and BookAddForm Image")

## 3. スタイルシート準備
#### 3-1. スタイルシート（CSSファイル）作成
###### 3-1-1. /src/main/webapp/css フォルダ新規作成
```
/src/main/webapp で右クリック→[New]→[Folder]

New Folder  画面
---------------
Folder Name: "css" と入力して[Finish]
---------------
```

###### 3-1-2. /src/main/webapp/css/bookmgr.css ファイル新規作成
```
/src/main/webapp/css で右クリック→[New]→[File]

New File  画面
---------------
File Name: "bookmgr.css" と入力して[Finish]
---------------
```

###### 3-1-3. /src/main/webapp/css/bookmgr.css コーディング
```css
h1 {
    color: #3399CC;
}
th {
    background-color: #CCCCFF;
    padding: 5px;
}
td {
    background-color: #CCCCCC;
    padding: 5px;
}
```

#### 3-2. Spring定義ファイルの修正
###### 3-2-1. /src/main/webapp/WEB-INF/spring/application-context-web.xml 追記
```xml
  ◆ 省略
  ◆ -- 20行目付近
  ◆ ---- ↓cssファイル格納ディレクトリを直接参照できるように設定を追加
  ◆
    <!-- Resource mappings -->
    <mvc:resources mapping="/resources/**" location="/resources/" />
    <mvc:resources mapping="/css/**" location="/css/" />  ◆◆◆←この行を追加
  ◆
  ◆ 以下省略
```

#### 3-3. 画面表示テスト
プログラム起動。メイン画面遷移するとタイトル文字が青色（水色？）に変わっていることを確認してください。

![Main Image](/images/step2-3.png "Main Image")

