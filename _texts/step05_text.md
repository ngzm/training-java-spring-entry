---
layout: handson
step:   "STEP05"
title:  "Spring JDBC による書籍テーブル更新"
date:   2017-04-18
---

<h2 class="handson">1. Spring JDBC による書籍テーブル更新</h2>

### 1-1. 実習テーマ

1. 書籍登録フォーム画面をエセでなくちゃんと作成する
2. Spring JDBC を使用して書籍情報登録処理を実装する
3. 書籍情報登録結果を画面に表示する

![step05_image]({{ site.baseurl }}/images/texts/text_step05_01.png "Step05 Image")

### 1-2.今回やること および 学習ポイント

1. 書籍登録フォーム画面をエセでなくちゃんと作成する
    - やること
        - 書籍登録フォームコントロールクラスの実装
        - 書籍登録フォームJSPの実装
    - 学習ポイント
        - Spring カスタムタグを学習する
        - フォーム入力値をコントローラで取得する方法を習得する
2. Spring JDBC を使用して書籍情報登録処理を実装する
    - やること
        - 書籍登録Daoクラスの実装
        - 書籍登録サービスクラスの実装
        - 書籍登録コントロールクラスの実装
    - 学習ポイント
        - データベース登録・更新処理の実装方法を把握する
        - データ登録SQLテンプレートの設定方法
3. 書籍情報登録結果を画面に表示する
    - やること
        - 登録処理を表示する画面コントロールクラスを実装する
        - 同様のJSPを作成する
    - 学習ポイント
        - 書籍登録処理結果の表示（とりあえず正常系のみ。異常系はこの後）

<h2 class="handson">2. 技術解説 - フォームから入力データを取得する - Spring MVC</h2>

### 2-1. Spring MVC によるデータ登録の流れ

- フォーム画面より入力したデータは、Spring MVC の Model オブジェクトにマッピングされる
- 入力データをマッピングした Model オブジェクトは、Controller メソッドの引数として渡される

![mvc-logical1]({{ site.baseurl }}/images/texts/tech_step05_01.png "MVC Logic 01")

### 2-2. Spring Form Tag を使用した入力フォーム

- Spring MVC でフォーム画面を実装するには、Spring MVC に用意されている Spring Form Tag が便利

    ```jsp
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
    <!-- @@@
         @@@ 上のtaglibディレクティブで Spring Form taglibrary を取り込む
         @@@ -->

    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <html>
    <head><title>書籍登録画面</title></head>
    <body>
    <h1>書籍登録画面</h1>

    <form:form action="addbook" method="POST" modelAttribute="book">
                <!-- @@@
                     @@@ modelAttribute で Model とドメインオブジェクトがマッピングされる
                     @@@ 上記の場合、"book" という名前の Book オブジェクトとして取得される
                     @@@ -->

      <table><tr>
        <th>ISBNコード</th>
        <td><form:input path="isbn"/></td>
      </tr><tr>
        <th>書籍名</th>
        <td><form:input path="name"/></td>
      </tr><tr>
        <th>価格</th>
        <td><form:input path="price"/></td>
      </tr></table>
                <!-- @@@
                     @@@ 取り出したBookオブジェクトの各プロパティには
                     @@@ isbn や name などのプロパティ名だけでアクセスできる
                     @@@ -->

      <input type="submit" value="登録" />
    </form:form>
    </body>
    </html>
    ```

- 上記フォームは、input タグの例であるが、この他にも、SELECT や CHECKBOX など比較的複雑なタグもSpring Form Tab で記述できる

### 2-3. フォームで入力されたデータを Controller で受け取る

- フォームで入力されたデータは、HTTP POST でサーバに送信されるが、そのデータは Spring MVC で Model の modelAttribute に追加される。
- modelAttribute に追加された Domain は、Controller メソッドの引数として受け取ることが可能
- modelAttribute の登録名はドメインクラス名から自動的に決定されるが、Controller に引数に @ModelAttribute アノテーションを付与すると任意の登録名でもデータを取得することができる

    ```java
    @RequestMapping(value = "/addbook", method = RequestMethod.POST)
    public String addBook(@ModelAttribute("book") Book book) throws Exception {
                    // @@@@   ↑ 
                    // @@@@ フォーム入力値（リクエストパラメータ）は自動的に
                    // @@@@ ドメインオブジェクトに格納され、コントローラの引数
                    // @@@@ として受け取ることができる
                    // @@@@ 


      // Something to do
      // ...
    }
    ```

### 2-4. Controller メソッドの引数について

- Controller メソッドの引数として渡される情報は @ModelAttribute なオブジェクトに限らない
- 以下の情報がメソッド引数として渡されてくるためアプリで受け取ることができる

    1. URIテンプレートを使用した時の ＠PathVariable を付けた変数
    1. HTTPリクエストパラメータ @RequestParam を付けた変数
    1. アップロードファイル
    1. HTTPリクエストヘッダ @RequestHeader を付けた変数
    1. クッキー @Cookie を付けた変数
    1. HTTPリクエストのメッセージボディ @RequestBody を付けた変数
    1. HTTPEntityオブジェクト
    1. Model オブジェクト
    1. ModelAttributeオブジェクト @ModelAttribute を付けた変数
    1. Session管理オブジェクト
    1. エラーオブジェクト
    1. WebRequestオブジェクト
    1. Servlet API の各種オブジェクト
    1. ロケール
    1. その他

<h2 class="handson">3. 技術解説 - Spring JDBC を使用したデータ更新処理</h2>

### 3-1. Spring JDBC によるデータベース更新の流れ

- Spring JDBC を使用したデータベース更新処理でも、JdbcTemplate オブジェクトを使用する
- 今回は、JdbcTemplate をさらに使いやすくした NamedParameterJdbcTemplate クラスを使用してみる

    ![jdbc-logical1]({{ site.baseurl }}/images/texts/tech_step05_02.png "jdbc Logic 01")


### 3-2. Spring JDBC を使用したDB更新プログランミング

#### 3-2-1. プレースホルダでパラメータを指定するサンプル

1. INSERT 文

    ```java
    jdbcTemplate.update("INSERT INTO book (id, isbn, name) VALUES (?, ?, ?)",
                  book.getId(), book.getIsdn(), book.getName());
    ```

2. UPDATE 文

    ```java
    jdbcTemplate.update("UPDATE book isbn = ?, name = ? WHERE id = ?",
                  book.getIsdn(), book.getName(), book.getId());
    ```

3. DELETE 文

    ```java
    jdbcTemplate.update("DELETE FROM book WHERE id = ?", book.getId());
    ```

#### 3-2-2. NamedParameterJdbcTemplate を使用してパラメータを指定するサンプル

1. INSERT 文

    ```java
    MapSqlParameterSource pmap = new MapSqlParameterSource();
    pmap.addValue("ID", book.getId());
    pmap.addValue("ISBN", book.getIsbn());
    pmap.addValue("NAME", book.getName());

    namedJdbcTemplate.update(
        "INSERT INTO book (id, isbn, name) VALUES (:ID, :ISBN, :NAME)", pmap);
    ```

2. UPDATE 文

    ```java
    MapSqlParameterSource pmap = new MapSqlParameterSource();
    pmap.addValue("ID", book.getId());
    pmap.addValue("ISBN", book.getIsbn());
    pmap.addValue("NAME", book.getName());

    namedJdbcTemplate.update(
        "UPDATE book isbn = :ISBN, name = :NAME WHERE id = :ID", pmap);
    ```

3. DELETE 文

    ```java
    MapSqlParameterSource pmap = new MapSqlParameterSource();
    pmap.addValue("ID", book.getId());

    namedJdbcTemplate.update("DELETE FROM book WHERE id = :ID", pmap);
    ```

<h2 class="handson">4. ハンズオン実習</h2>

### STEP05 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP05' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - Spring JDBC の概要を学習しましょう
    - Spring JDBC を利用するための環境を構築しましょう
    - Spring JDBC を使用してデータベースから書籍情報一覧を取得しましょう
        - JdbcTemplate の使い方を覚えましょう
        - RowMapper でドメインにマッピングする方法を把握しましょう

### STEP05 実装イメージ - 処理シーケンス

![step05-flow1]({{ site.baseurl }}/images/texts/text_step05_02.png "text05 Flow1")

### STEP05 実装イメージ - Spring JDBC によるデータ登録・更新処理

![step05-flow2]({{ site.baseurl }}/images/texts/text_step05_03.png "text05 Flow2")
