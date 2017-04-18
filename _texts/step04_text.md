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

<h2 class="handson">2. 技術解説 - Spring JDBC</h2>

### 2-1. Spring JDBC とは

- JDBC を Spring Framework で使いやすくオーバラップしたライブラリ
- Spring JDBC は下記の機能を提供する
    1. データベースから取得したデータのドメインクラスへのマッピング
    2. ＳＱＬ発行時のプレースホルダ指定
    3. データアクセス時の例外処理
    4. トランザクション制御 （但し本研修では意識していない）

  これらの機能を利用することにより、プレーンなJDBCを使う場合に比べて、
  非常にシンプルなコードでデータベースアクセスを実装できるようになりました

### 2-2. DI の適用箇所

- DI はアプリケーション全ての層に適用できる技術である

![DIArchtecture]({{ site.baseurl }}/images/texts/tech_step03_01.png "Spring DI Archtecture")

| レイヤ | コンポーネント | 主な Spring 提供機能 |
|:--|:--|:--|
| プレゼンテーション層 | コントローラ | Spring MVC、DI、AOP |
| ビジネスロジック層 | サービス、ドメイン | Validation、Spring Transaction、DI、AOP |
| データアクセス層 | DAO | Spring JDBC、ORM、DI、AOP |

### 2-3. DI の仕組み

- DI対象のオブジェクトは、DIを実現するための「DIコンテナ」と呼ばれる領域（箱）に格納される

    ![DI-logical1]({{ site.baseurl }}/images/texts/tech_step03_02.png "DI Logic 01")

- インジェクションを要求するクラス変数があると、変数の型（もしくは名前）に合致するオブジェクトをDIコンテナから探し出して、該当変数にインジェクションする

    ![DI-logical2]({{ site.baseurl }}/images/texts/tech_step03_03.png "DI Logic 02")

### 2-4. アノテーションを使用した DI コンテナ格納

- オブジェクトを生成して DI コンテナに格納するためには、下記の何れかのアノテーションをクラス定義に付与すれば良い。
    1. @Controller
    2. @Service
    3. @Repository
    4. @Component


  これらのアノテーションを付与するだけでオブジェクト生成とDIコンテナ格納が自動的に実行される
 

- コントローラをDIコンテナに格納するサンプル

    ```java
    @Controller  // @@@@ コントローラをDIコンテナに格納するアノテーション @@@@
    public class BookController {
      // Something to do ..

    }
    ```

- サービスをDIコンテナに格納するサンプル

    ```java
    @Service  // @@@@ サービスをDIコンテナに格納するアノテーション @@@@
    public class ListBookServiceImpl implements ListBookService {
      // Something to do ..
      //書籍一覧取得サービス
    }
    ```

- Dao をDIコンテナに格納するサンプル

    ```java
    @Repository  // @@@@ Dao をDIコンテナに格納するアノテーション @@@@
    public class ListBookDaoImpl implements ListBookDao {
      // Something to do ..

    }
    ```

### 2-5. アノテーションを使用したインジェクション

- DIコンテナに格納されているオブジェクトのうち、指定するオブジェクトをDIさせるには、クラス変数に、以下の何れかのアノテーションを付加すればよい

    1. @Inject
    2. @Autowired
    3. @Resource

    これらのアノテーションを付与することで指定オブジェクトの検索とインジェクションが実行される
    **現在 @resource は殆ど使用されない、本トレーニングでは @Autowired を使用します**

- 指定オブジェクトの検索方法は、型をベースに検索する方法（byType）と、名前による検索（byName）の2つがある、デフォルトは、byType による検索を行う

- Dao をDIコンテナに格納するサンプル

    ```java
    @Controller 
    public class BookController {
      // @@@@
      // @@@@ ByType で一致するオブジェクトをインジェクションする
      // @@@@
      @Autowired
      ListBookService  listBookService; // @@@@ <-- 型が一致するオブジェクトが自動で代入される
      
      @RequestMapping(value = "/listbook", method = RequestMethod.GET)
      public String listBook(Model model) throws Exception {
        List<Book> books = listBookService.getBookList();
        model.addAttribute("books", books);
        return "listbook";
      }
    }
    ```

### 2-6. インターフェースで抽象化すること

- DI でインジェクションさせるオブジェクトは、必ずインターフェースで実装を抽象化しておくことが非常に重要である！
- インターフェースで抽象化することにより、ソースコードを変更することなく、実行時に実装クラスを差し替えることが可能となる

  例えば、データ永続化の処理について、データ検索や更新などのインターフェースだけ決まっていれば、実際の永続化手段がデータベースなのか、ファイルなのかは実装クラスを差し替えれば対応できるようになる。

- すなわち、これが依存しないということである

    ![di-interface]({{ site.baseurl }}/images/texts/tech_step03_04.png "DI Interface")

- インターフェースのサンプルコード

    ```java
    // 書籍一覧サービスクラスインターフェース
    public interface ListBookService {
      //書籍一覧取得サービス
      public List<Book> getBookList() throws Exception;
    }
    ```

- インジェクション対象の実装クラスのサンプルコード

    ```java
    @Service
    public class ListBookServiceImpl implements ListBookService {
      //書籍一覧取得サービス
      @Override
      public List<Book> getBookList() throws Exception {
        // Something to do
        return  hoge;
      }
    }
    ```

- インジェクションして利用するクラスのサンプルコード

    ```java
    @Controller 
    public class BookController {
      @Autowired
      ListBookService  listBookService;

      // Something to do
    }
    ```

### 2-7. Spring 設定ファイルによる DI

- Spring設定ファイルで、ＤＩ設定する方法もある（ただし本研修ではここで紹介する程度）
- この方法は、ＤＩを集中的に設計・管理できるメリットが有る
- このため、比較的大規模なアプリケーションの場合に有効である
- そもそもSpringは 設定ファイルによるＤＩのみサポートしていたが、Spring2.5から、アノテーションによるＤＩがサポートされ、利便性が大幅に向上した経緯がある

<h2 class="handson">3. 技術解説 - ドメイン情報を画面表示 - Spring MVC</h2>

### 3-1. ドメインオブジェクトの情報を画面に渡す

- Domain を画面（JSP）に渡すには、Spring MVC に用意されている Model オブジェクトを使う
- Modelオブジェクトは、Model - View - Controller の Model に該当
- すなわち、Controller からView に引き渡すデータを格納するオブジェクト

    ![MVC model]({{ site.baseurl }}/images/texts/tech_step03_05.png "MVC Model")

- Book ドメインを Model に登録するサンプルコード

    ```java
    @RequestMapping(value = "/listbook", method = RequestMethod.GET)
    public String listBook(Model model) throws Exception {
                  // @@@@   ↑
                  // @@@@ Modelオブジェクトをコントローラメソッドの引数で取得
                  // @@@@

      // 書籍一覧取得ロジック処理、Domain オブジェクト取得
      List<Book> books = listBookService.getBookList();

      // 書籍一覧情報をモデルに登録
      model.addAttribute("books", books);
              // @@@@      ↑
              // @@@@ Domain オブジェクト book を Model に登録
              // @@@@ 上記の場合 "books" という名前で登録している
              // @@@@

      // 画面表示に listbook.jsp を呼び出す
      return "listbook";
    }
    ```


<h2 class="handson">4. ハンズオン実習</h2>

### STEP02 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP02' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - メイン画面を作成して遷移させましょう
    - 書籍一覧画面を作成して遷移させましょう
    - 書籍登録フォーム画面を作成して遷移させましょう
    - 書籍管理アプリ用のスタイルシートを作成しましょう

### STEP02 実装イメージ

![step02-flow]({{ site.baseurl }}/images/texts/text_step02_02.png "text02 Flow")
