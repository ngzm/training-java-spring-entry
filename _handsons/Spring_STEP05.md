---
layout: handson
title: Spring ENTRY STEP05
---

# Spring Framework 入門
### STEP 05 / Spring JDBC による書籍テーブル更新
***

## 1. 書籍登録フォーム画面を実装する
#### 1-1. 書籍登録フォーム画面コントローラをきちんと実装
###### 1-1-1. /src/main/java/jp.sample.bookmgr.web.controller.BookController.java
```java
◆
◆ 省略
◆ 53行目付近
◆
    /**
     * 書籍登録フォーム画面コントローラ
     * 
     * @param model Spring MVC Modelオブジェクト
     * @return 画面JSP名
     */ 
    @RequestMapping(value = "/addbookform", method = RequestMethod.GET)
    public String addBookForm(Model model) throws Exception {     ◆◆◆<-- 引数 (Model model) を追加 ◆◆◆
        
        logger.debug("addbookform() start");

      ◆
      ◆---- ↓今回追加 ----
      ◆
        // 空の書籍情報をモデルに登録
        Book book = new Book();
        model.addAttribute("book", book);
      ◆
      ◆---- ↑今回追加 ----
      ◆
        
        // 画面表示に addbookform.jsp を呼び出す
        return "addbookform";
    }
}
```

#### 1-2. 書籍登録フォーム画面JSP をきちんと修正
###### 1-2-1. /src/main/webapp/VEB-INF/view/addbookform.jsp
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  ◆◆◆ <--この行追加 ◆◆◆
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
<title>書籍登録フォーム画面</title>
</head>
<body>

<h1>書籍登録フォーム画面</h1>

<p>
◆
◆ ---- ↓書籍登録フォームをきちんと実装 ----
◆
<form:form action="addbook" method="POST" modelAttribute="book">
    <table>
    <tr>
        <th>ISBNコード</th>
        <td><form:input path="isbn"/></td>
    </tr><tr>
        <th>書籍名</th>
        <td><form:input path="name"/></td>
    </tr><tr>
        <th>価格</th>
        <td><form:input path="price"/></td>
    </tr>
    </table>
    <br>
    <input type="submit" value="登録" />
</form:form>
◆
◆ ---- ↑書籍登録フォームをきちんと実装 ----
◆
</p>

<hr />
<a href="main">書籍管理メイン画面</a>

</body>
</html>
```

★★Spring Formタグの説明は次のサイトがヒントになるかもしれません http://kuwalab.hatenablog.jp/entry/20130118/p1

#### 1-3. 画面表示テスト
書籍一覧画面を表示して書籍登録フォーム画面がきちんと表示されるかどうかをチェックする。

![AddBookForm Image](/images/step5-1.png "AddBookForm Image")

なお、「登録」ボタンを押すと書籍情報登録リクエストが発生しますが、まだ当処理を実装していないので 404エラーとなってしまいます。（このあと実装します）


## 2. 書籍登録処理を実装する
#### 2-1. 書籍登録Daoを新規作成
###### 2-1-1. /src/main/java/jp.sample.bookmgr.biz.dao.AddBookDao.java インターフェースを新規作成し、次のようにコーディング
```java
package jp.sample.bookmgr.biz.dao;

import org.springframework.dao.DataAccessException;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍登録データアクセスクラスインターフェース
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
public interface AddBookDao {
    
    /**
     * 指定された書籍データをデータベースに登録して永続化するメソッド
     *     
     * @param Book 書籍情報
     * @return int 登録された書籍データ数
     */ 
    public int addBook(Book book) throws DataAccessException;
}
```

###### 2-1-2. /src/main/java/jp.sample.bookmgr.biz.dao.AddBookDaoSpringJdbc.java 実装クラスを、上記インターフェースを継承しながら新規作成し、以下のようにコーディング
```java
package jp.sample.bookmgr.biz.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍登録データアクセス実装クラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Repository        // DAOとしてDI可能というアノテーションを宣言
public class AddBookDaoSpringJdbc implements AddBookDao {

    /**
     * JDBC制御クラス
     */
    @Autowired        // インジェクション
    private NamedParameterJdbcTemplate template;
    
    /**
     * 指定された書籍データをデータベースに登録して永続化するメソッド
     * 
     * @param book 書籍情報
     * @return int 登録された書籍データ数
     */ 
    @Override
    public int addBook(Book book) throws DataAccessException {
        
        // 書籍登録SQLのプレースホルダーを設定
        MapSqlParameterSource pmap = new MapSqlParameterSource();
        pmap.addValue("ISBN", book.getIsbn());
        pmap.addValue("NAME", book.getName());
        pmap.addValue("PRICE", book.getPrice());

        // 書籍登録SQL
        String sql = "INSERT INTO book (isbn, name, price) VALUES (:ISBN, :NAME, :PRICE)";
        
        // データベースに登録する
        int count = template.update(sql, pmap);
        return count;
    }
}
```

#### 2-2. 書籍登録サービスを新規作成
###### 2-2-1. /src/main/java/jp.sample.bookmgr.biz.service.AddBookService.java インターフェースを新規作成して、以下のようにコーディング
```java
package jp.sample.bookmgr.biz.service;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍登録サービスインターフェース
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
public interface AddBookService {
    
    /**
     * 書籍登録サービスを実行する
     * 
     * @param Book 書籍情報
     */ 
    public void addBook(Book book) throws Exception ;
} 
```

###### 2-2-2. /src/main/java/jp.sample.bookmgr.biz.service.AddBookServiceImpl.java 実装クラスを、上記インターフェースを継承して新規作成し、下記のようにコーディングする
```java
package jp.sample.bookmgr.biz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.sample.bookmgr.biz.dao.AddBookDao;
import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍登録サービス実装クラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Service    // サービスクラスとしてDI可能というアノテーションを宣言
public class AddBookServiceImpl implements AddBookService {
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
    public void addBook(Book book) throws Exception {
        // 書籍登録を行うDAOクラスを使用して書籍情報を永続化する
        addBookDao.addBook(book);
    }
}
```

#### 2-3. 書籍登録リクエストを受け付けるコントローラを追加
###### 2-3-1. /src/main/java/jp.sample.bookmgr.web.controller.BookController.java に書籍登録リクエストを受け付けるコントローラ(メソッド)追加
```java
◆
◆ Package宣言およびimport宣言は省略
◆

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
     *  書籍一覧処理を実装したビジネスロジックサービス
     */
    @Autowired        // ListBookServiceオブジェクトをインジェクション
    ListBookService listBookService;

◆
◆ ---- ↓書籍登録サービスオブジェクトのインジェクション宣言を追記 ----
◆
    /**
     *  書籍登録処理を実装したビジネスロジックサービス
     */
    @Autowired        // インジェクション
    AddBookService addBookService;
◆
◆ ---- ↑書籍登録サービスオブジェクトのインジェクション宣言を追記 ----
◆

    /**
     * 書籍一覧画面コントローラ
     * 
     * @param model Spring MVC Modelオブジェクト
     * @return 画面JSP名
     */ 
    @RequestMapping(value = "/listbook", method = RequestMethod.GET)
    public String listBook(Model model) throws Exception {
        
        logger.debug("listBook() start");
        
        // 書籍一覧取得ロジック処理
        List<Book> books = listBookService.getBookList();

        // 書籍一覧情報をモデルに登録
        model.addAttribute("books", books);
        
        // 画面表示に listbook.jsp を呼び出す
        return "listbook";
    }

    /**
     * 書籍登録フォーム画面コントローラ
     * 
     * @param model Spring MVC Modelオブジェクト
     * @return 画面JSP名
     */ 
    @RequestMapping(value = "/addbookform", method = RequestMethod.GET)
    public String addBookForm(Model model) throws Exception {
        
        logger.debug("addbookform() start");
        
        // 空の書籍情報をモデルに登録
        Book book = new Book();
        model.addAttribute("book", book);
        
        // 画面表示に addbookform.jsp を呼び出す
        return "addbookform";
    }

◆
◆ ---- ↓書籍登録コントローラを追記 ----
◆
    /**
     * 書籍登録処理コントローラ
     *     
     * @param book Spring MVC ModelオブジェクトからバインドされたBookインスタンス
     * @return 登録結果表示JSP名
     */ 
    @RequestMapping(value = "/addbook", method = RequestMethod.POST)
    public String addBook(@ModelAttribute("book") Book book) throws Exception {
        
        logger.debug("addbook() start");
        logger.debug("book isbn:" + book.getIsbn());
        logger.debug("book name:" + book.getName());
        logger.debug("book price:" + book.getPrice());
        
        // 書籍情報登録処理
        addBookService.addBook(book);
        
        // とりあえず、メイン画面に戻るようにする
        return "main";
    }
◆
◆ ---- ↑書籍登録コントローラを追記 ----
◆
}
```

#### 2-4. 画面表示テスト
書籍登録フォーム画面から下記の書籍データを投入してデータベースに登録させてみましょう。

![AddBookForm Image](/images/step5-2.png "AddBookForm Image")

正常に登録できたでしょうか？  
登録できたら書籍一覧画面に移動して、今登録したデータが表示されることを確認してください。

![Bookist Image](/images/step5-3.png "BookList Image")

★★★注意★★★ **ここでは、英数字のみでテストしてください。（漢字、ひらがな等は文字化けするため）**  
日本語を入力すると文字化けの対策は、STEP6で行います。

## 3. 書籍登録結果を表示する画面に遷移する
#### 3-1. 書籍登録コントローラから処理結果画面Viewにリダイレクト
###### 3-1-1. /src/main/java/jp.sample.bookmgr.web.controller.BookController.java 修正・追加
```java
◆
◆ 省略
◆ 78行目付近
◆
    /**
     * 書籍登録処理コントローラ
     * 
     * @param book Spring MVC ModelオブジェクトからバインドされたBookインスタンス
     * @return 登録結果表示JSP名
     */ 
    @RequestMapping(value = "/addbook", method = RequestMethod.POST)
    public String addBook(@ModelAttribute("book") Book book) throws Exception {
        
        logger.debug("addbook() start");
        logger.debug("book isbn:" + book.getIsbn());
        logger.debug("book name:" + book.getName());
        logger.debug("book price:" + book.getPrice());
        
        // 書籍情報登録処理
        addBookService.addBook(book);
        
◆
◆ ---- ↓リターンで呼び出すJSPを変更、さらにリダイレクトで遷移するように修正 ----
◆
        // 登録結果画面 result.jsp を呼び出す
        return "redirect:result";
    }

◆
◆ ---- ↓続けて、処理結果画面コントローラを追加 ----
◆
    /**
     * 処理結果画面表示コントローラ
     * 
     * @return 登録結果表示JSP名
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public String result() throws Exception {
        logger.debug("result START");

        return "result";
    }
◆
◆ ---- ↑処理結果画面コントローラを追加 ----
◆
}
```

#### 3-2. 書籍登録結果画面JSPを新規作成
###### 3-2-1. /src/main/webapp/WEB-INF/views/result.jsp ファイルを新規作成して、以下のようにコーディング
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
<title>更新結果</title>
</head>
<body>

<h1>更新に成功しました!</h1>

<hr />
<a href="main">書籍管理メイン画面</a>
<br/>
<a href="listbook">書籍一覧画面</a>

</body>
</html>
```

#### 3-3. 画面表示テスト
書籍登録画面→登録結果画面 に遷移することを確認します。

![AddBookResult Image](/images/step5-4.png "AddBookResult Image")

★★★注意★★★ **ここでは、英数字のみでテストしてください。（漢字、ひらがな等は文字化けするため）**  
日本語を入力すると文字化けする！の対策は、STEP6で行います。
