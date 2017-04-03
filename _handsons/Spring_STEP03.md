---
layout: handson
title:  Spring ENTRY STEP03
date:   2017-04-03
---

# Spring Framework 入門
### STEP 03 / DIによる（えせ）書籍一覧画面
***

## 1. 書籍情報を保持するドメインクラスを作成する
#### 1-1. 書籍情報ドメインクラス Book.java 作成
###### 1-1-1. /src/main/java/jp.sample.bookmgr.biz.domain.Book.java 作成
```
/src/main/java/ で右クリック→[New]→[Class]
  
Java Class 画面
---------------
Package: "jp.sample.bookmgr.biz.domain"
Name: "Book" と入力して[Finish]
---------------
```

###### 1-1-2. /src/main/java/jp.sample.bookmgr.biz.domain.Book.java コーディング
```java
package jp.sample.bookmgr.biz.domain;

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
    private String isbn = "";
    
    /**
     * 書籍名
     */
    private String name = "";
    
    /**
     * 書籍価格
     */
    private int price = 0;
    
    /**
     * 書籍IDゲッタ・セッタ
     */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    /**
     *  ISBNゲッタ・セッタ
     */
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    /**
     *  書籍名ゲッタ・セッタ
     */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    /**
     *  価格ゲッタ・セッタ
     */
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}
```
ドメインクラスは、アプリケーションで取り扱う各種情報を格納するクラスです。ドメインクラスは、情報を格納するためのプライベートなクラス変数（プロパティと呼ばれます）と、プロパティに格納された情報を取得するパブリックなクラスメソッド（俗にゲッタと呼ばれます）、およびプロパティに情報をセットするパブリックなクラスメソッド（俗にセッタと呼ばれます）で構成されます。

## 2. DI ... （えせ）書籍一覧サービスをインジェクションしてみる
#### 2-1. 書籍一覧サービス インターフェース 新規作成
###### 2-1-1. /src/main/java/jp.sample.bookmgr.biz.service.ListBookService.java 作成
```
/src/main/java/ で右クリック→[New]→[interface]
  
Java Class 画面
---------------
Package: "jp.sample.bookmgr.biz.service"
Name: "ListBookService" と入力して[Finish]
---------------
```

###### 2-1-2. /src/main/java/jp.sample.bookmgr.biz.service.ListBookService.java コーディング
```java
package jp.sample.bookmgr.biz.service;

import java.util.List;
import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧サービスクラスインターフェース
 * @author 長住@NTT-AT
 * @version 1.0
 */
public interface ListBookService {
    /**
     * 書籍一覧取得サービス
     * @return 書籍一覧情報
     */ 
    public List<Book> getBookList() throws Exception;
}
```

#### 2-2. 書籍一覧サービスクラス新規作成
###### 2-2-1. /src/main/java/jp.sample.bookmgr.biz.service.ListBookServiceImple.java 作成
```
/src/main/java/ で右クリック→[New]→[class]
  
Java Class 画面
---------------
Package: "jp.sample.bookmgr.biz.service"
Interfaces: "jp.sample.bookmgr.biz.service.ListBookService" を[ADD]
Name: "ListBookServiceImple" と入力して[Finish]
---------------
```

###### 2-2-2. /src/main/java/jp.sample.bookmgr.biz.service.ListBookServiceImple.java コーディング
```java
package jp.sample.bookmgr.biz.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧サービス実装クラス
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Service // サービスクラスとしてDI可能というアノテーションを宣言
public class ListBookServiceImpl implements ListBookService {
    
    /**
     * 書籍一覧取得サービス
     * @return 書籍一覧情報
     */ 
    @Override
    public List<Book> getBookList() throws Exception {

    ◆
    ◆ --- ↓ここから、現段階ではえせリストを返す仮実装 
    ◆
        // 本当はデータベースからデータを取得する必要があるが現段階では簡易えせリストを作成する
        List<Book> bookList = new ArrayList<Book>();
        Book book1 = new Book();
        book1.setId(1);
        book1.setIsbn("ISBN-0001");
        book1.setName("テスト書籍１");
        book1.setPrice(1000);
        bookList.add(book1);
        
        Book book2 = new Book();
        book2.setId(2);
        book2.setIsbn("ISBN-0002");
        book2.setName("テスト書籍２");
        book2.setPrice(2000);
        bookList.add(book2);
        
        Book book3 = new Book();
        book3.setId(3);
        book3.setIsbn("ISBN-0003");
        book3.setName("テスト書籍３");
        book3.setPrice(3000);
        bookList.add(book3);
    ◆
    ◆ -- ↑ここまで仮実装
    ◆
        
        return bookList;
    }
}
```

**(参考)＜Import挿入方法＞**  
Import文を記述せずに、とりあえずクラス定義を先に作成してみましょう。当然エラーとなりますが、Eclipse の機能を使えば Import 文を後から自動的に挿入させることができます！ぜひこのワザを覚えましょう！

#### 2-3. 書籍管理コントロールクラスに、書籍一覧サービスをインジェクションして使う
BookController に書籍一覧サービスオブジェクトをDIして使用するコードを追加します。

###### 2-3-1. /src/main/java/jp.sample.bookmgr.web.controller.BookController.java 修正
```java
package jp.sample.bookmgr.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.sample.bookmgr.biz.domain.Book;
import jp.sample.bookmgr.biz.service.ListBookService;

/**
 * 書籍管理処理コントローラクラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Controller // "コントローラとしてDI可能" というアノテーションを宣言
public class BookController {

    /**
     * ロガー
     */
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    
    ◆
    ◆ ---- ↓書籍一覧サービスオブジェクトをDIするコードを追加 ----
    ◆
    /**    
     *  書籍一覧処理を実装したビジネスロジックサービス
     */
    @Autowired        // ListBookServiceオブジェクトをインジェクション
    ListBookService listBookService;
    ◆
    ◆ ---- ↑ここまで、書籍一覧サービスオブジェクトをDIするコード ----
    ◆
    
    /**
     * 書籍一覧画面コントローラ
     * 
     * @param model Spring MVC Modelオブジェクト
     * @return 画面JSP名
     */ 
    @RequestMapping(value = "/listbook", method = RequestMethod.GET)
    public String listBook(Model model) throws Exception { ◆◆◆ <--Model model 追加 ◆◆◆
        
        logger.debug("listBook() start");
        
    ◆
    ◆ ---- ↓DIした書籍一覧サービスロジック処理を実行するコードを追加 ----
    ◆
        // 書籍一覧取得ロジック処理
        List<Book> books = listBookService.getBookList();

        // 書籍一覧情報をモデルに登録
        model.addAttribute("books", books);
    ◆
    ◆ ---- ↑DIした書籍一覧サービスロジック処理を実行するコードを追加 ----
    ◆
        
        // 画面表示に listbook.jsp を呼び出す
        return "listbook";
    }

◆
◆ 以下省略
◆
```

#### 2-4. 書籍一覧画面 View でドメイン情報を表示させる
コントローラでModelに設定した書籍一覧情報を表示できるように実装します。

###### 2-4-1. /src/main/webapp/WEB-INF/view/listbook.jsp 修正
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  ◆◆◆← この行追加 ◆◆◆
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
◆ ---- ↓書籍一覧を表示できるよう修正 ----
◆
<table>
<tr><th>id</th><th>isbn</th><th>name</th><th>price</th></tr>

<c:forEach items="${books}" var="book" varStatus="status">
    <tr>
        <td><c:out value="${book.id}"/></td>
        <td><c:out value="${book.isbn}"/></td>
        <td><c:out value="${book.name}"/></td>
        <td><c:out value="${book.price}"/></td>
    </tr>
</c:forEach>
</table>
◆
◆ ---- ↑書籍一覧を表示できるよう修正 ----
◆

<hr />
<a href="main">書籍管理メイン画面</a>

</body>
</html>
```

#### 2-5. 画面表示テスト
プログラムを起動して、書籍一覧画面を表示。  
うまくコードが実行されるか、うまく画面が表示されるかを確認してください。

![BookList Image](/images/step3-1.png "BookList Image")

## 3.  ＤＩ ・・・ （えせ）書籍一覧Dao をインジェクションしてみる
#### 3-1. 書籍一覧Dao インターフェース 新規作成
###### 3-1-1. /src/main/java/jp.sample.bookmgr.biz.dao.ListBookDao.java 作成
```
/src/main/java/ で右クリック→[New]→[interface]
  
Java Class 画面
---------------
Package: "jp.sample.bookmgr.biz.dao"
Name: "ListBookDao" と入力して[Finish]
---------------
```

###### 3-1-2. /src/main/java/jp.sample.bookmgr.biz.dao.ListBookDao.java コーディング
```java
package jp.sample.bookmgr.biz.dao;

import java.util.List;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧データアクセスクラスインターフェース
 * @author 長住@NTT-AT
 * @version 1.0
 */
public interface ListBookDao {

    /**
     * 書籍データベースから書籍一覧データを取得するメソッド
     * @return 書籍一覧情報
     */ 
    public List<Book> getBookList() throws Exception;
}
```

#### 3-2. 書籍一覧Dao クラス新規作成
###### 3-2-1. /src/main/java/jp.sample.bookmgr.biz.dao.ListBookDaoImple.java 作成
```
/src/main/java/ で右クリック→[New]→[class]
  
Java Class 画面
---------------
Package: "jp.sample.bookmgr.biz.dao"
Name: "ListBookDaoImple" と入力して[Finish]
---------------
```

###### 3-2-2. /src/main/java/jp.sample.bookmgr.biz.dao.ListBookDaoImple.java コーディング
```java
package jp.sample.bookmgr.biz.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧データアクセス実装クラス
 * @author     長住@NTT-AT
 * @version 1.0
 */
@Repository    // リポジトリ（Dao）クラスとしてDI可能というアノテーションを宣言
public class ListBookDaoImpl implements ListBookDao {

    /**
     * 書籍データベースから書籍一覧データを取得するメソッド
     * @return     書籍一覧情報
     */ 
    @Override
    public List<Book> getBookList() throws Exception {
    ◆
    ◆ --- ↓ここから、現段階ではえせリストを返す仮実装 
    ◆
        // 本当はデータベースからデータを取得する必要があるが現段階では簡易えせリストを作成する
        List<Book> bookList = new ArrayList<Book>();
        Book book1 = new Book();
        book1.setId(1);
        book1.setIsbn("ISBN-0001");
        book1.setName("テスト書籍１");
        book1.setPrice(1000);
        bookList.add(book1);
        
        Book book2 = new Book();
        book2.setId(2);
        book2.setIsbn("ISBN-0002");
        book2.setName("テスト書籍２");
        book2.setPrice(2000);
        bookList.add(book2);
        
        Book book3 = new Book();
        book3.setId(3);
        book3.setIsbn("ISBN-0003");
        book3.setName("テスト書籍３");
        book3.setPrice(3000);
        bookList.add(book3);
    ◆
    ◆ --- ↑ここまで仮実装
    ◆
        
        return bookList;
    }
}
```

#### 3-3. 書籍一覧サービスクラスに、書籍一覧Daoオブジェクトをインジェクションして使う
###### 3-3-1. /src/main/java/jp.sample.bookmgr.biz.service.ListBookServiceImple.java を大修正
ListBookServiceImpl は大きく修正します。

```java
package jp.sample.bookmgr.biz.service;

// import java.util.ArrayList; ◆◆◆<-- ここ削除
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.sample.bookmgr.biz.dao.ListBookDao;
import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧サービス実装クラス
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Service // サービスクラスとしてDI可能というアノテーションを宣言
public class ListBookServiceImpl implements ListBookService {
  ◆
  ◆ ---- ↓書籍一覧DaoオブジェクトをDIする
  ◆
    /**
     *  書籍一覧取得DAO
     */
    @Autowired        // ListBookDaoオブジェクトをインジェクション
    ListBookDao listBookDao;
  ◆
  ◆ ---- ↑書籍一覧DaoオブジェクトをDIする
  ◆

    /**
     * 書籍一覧取得サービス
     * @return    書籍一覧情報
     */ 
    @Override
    public List<Book> getBookList() throws Exception {
      ◆
      ◆ ---- ↓さっきコーディングしたえせリスト作成部分を全て削除
      ◆ ---- ↓書籍一覧取得処理はDaoオブジェクトに任せる
      ◆
        // 書籍一覧を取得
        return listBookDao.getBookList();
      ◆
      ◆ ---- ↑ここまで修正
      ◆
    }
}
```

#### 3-4. 画面表示テスト
プログラムを起動して書籍一覧画面を表示させてみる。うまく画面が表示されるかをご確認ください。

![BookList Image](/images/step3-2.png "BookList Image")

