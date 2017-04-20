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
