---
layout: handson
step:   "STEP04"
title:  "Spring JDBC を使った書籍テーブル検索"
date:   2017-04-18
---

<h2 class="handson">1. Spring JDBC を使った書籍テーブル検索</h2>

### 1-1. 実習テーマ

1. Spring JDBC を使用してデータベースに接続する
2. データベースを検索して書籍テーブルから書籍一覧情報を取得する

![step04_image]({{ site.baseurl }}/images/texts/text_step04_01.png "Step04 Image")

### 1-2.今回やること および 学習ポイント

1. Spring JDBC を使用してデータベースに接続する
    - やること
        - PostgreSQL データベース上に書籍テーブルを作成する
        - Spring JDBC に必要なライブラリの取り込み（pom.xml）
        - Spring JDBC で PostgreSQL に接続するための設定を追加
    - 学習ポイント
        - Spring JDBC に必要なライブラリや環境設定手順を把握する
2. データベースを検索して書籍テーブルから書籍一覧情報を取得する
    - 書籍一覧 DAO クラスに書籍テーブル検索処理を実装する
        - Spring JDBC を使用したデータベース検索方法を習得
        - データベース検索結果をドメインオブジェクトにマッピングする方法を把握する

<h2 class="handson">2. 技術解説 - Spring JDBC を使用したデータ検索処理</h2>

### 2-1. Spring JDBC とは

- JDBC を Spring Framework で使いやすくオーバラップしたライブラリ
- Spring JDBC は下記の機能を提供する
    1. データベースから取得したデータのドメインクラスへのマッピング
    2. ＳＱＬ発行時のプレースホルダ指定
    3. データアクセス時の例外処理
    4. トランザクション制御 （但し本研修では意識していない）

  これらの機能を利用することにより、プレーンなJDBCを使う場合に比べて、
  非常にシンプルなコードでデータベースアクセスを実装できるようになりました

### 2-2. Spring JDBC の適用箇所

- Spring JDBC はデータアクセス層に適用される

![JDBCArchtecture]({{ site.baseurl }}/images/texts/tech_step04_01.png "Spring JDBC Archtecture")

| レイヤ | コンポーネント | 主な Spring 提供機能 |
|:--|:--|:--|
| プレゼンテーション層 | コントローラ | Spring MVC、DI、AOP |
| ビジネスロジック層 | サービス、ドメイン | Validation、Spring Transaction、DI、AOP |
| データアクセス層 | DAO | Spring JDBC、ORM、DI、AOP |

### 2-3. Spring JDBC によるデータベース検索の流れ

- Spring JDBC では、JdbcTemplate オブジェクトを使用してデータベースにアクセスする

    ![jdbc-logical1]({{ site.baseurl }}/images/texts/tech_step04_02.png "jdbc Logic 01")

- JdbcTemplate を使用することにより、データベースコネクション、ステートメント、リザルトセット等、従来プログラミングする必要があった煩雑な処理を意識することなく、より簡単にデータベースアクセスを実装できるようになった

### 2-4. Spring JDBC を使用したDB検索処理

#### 2-4-1. 単一データの取得コードサンプル

1. 一つの数値を取得する場合

    ```java
    int number = jdbcTemplate.queryForInt("SELECT ....");
    ```

 2. 一つのオブジェクト（主にStringやDateなど）を取得する場合

    ```java
    String text = jdbcTemplate.queryForObject("SELECT ....");
    ```

3. ある単一行のデータをMap（Stringにカラム名、Objectにデータ）に格納する場合

    ```java
    int number = jdbcTemplate.queryForInt("SELECT ....");
    ```

 4. ある単一行のデータをドメインクラスにマッピングする場合

    ```java
    class myRowMapper implements RowMapper<Book> {
      public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("ID");
        book.setIsbn(rs.getString("ISBN");
        book.setName(rs.getString("NAME");
        return book;
      }
    }
    Book book = jdbcTemplate.queryForObject (
            "SELECT * FROM BOOK WEHRE ID = ?", new myRowMapper(), id);
    ```

#### 2-4-2. 複数データの取得コードサンプル

1. 複数レコードのMap（Stringにカラム名、Objectにデータ）をListに格納する場合

    ```java
    List<Map<String, Object>> mapList = jdbcTemplate.queryForList("SELECT ...");
    ```

 2. 複数レコードのデータをドメインクラスのListにマッピングする場合

    ```java
    implements RowMapper<Book> {
      public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("ID");
        book.setIsbn(rs.getString("ISBN");
        book.setName(rs.getString("NAME");
        return book;
      }
    }
    List<Book> bookList = jdbcTemplate.query("SELECT * FROM BOOK", new myRowMapper());
    ```

<h2 class="handson">3. ハンズオン実習</h2>

### STEP04 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP04' %}
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

### STEP04 実装イメージ

![step04-flow]({{ site.baseurl }}/images/texts/text_step04_02.png "text04 Flow")
