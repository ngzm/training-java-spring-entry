---
layout: handson
step:   "STEP03"
title:  "DI を使って書籍一覧画面を出す"
date:   2017-04-17
---

<h2 class="handson">1. DI を使って書籍一覧画面を出す</h2>

### 1-1. 実習テーマ

1. 書籍情報ドメインクラス作成
2. （えせ）書籍一覧サービスクラスを DI する
3. （えせ）書籍一覧Daoクラスを DI する

![step03_image]({{ site.baseurl }}/images/texts/text_step03_01.png "Step03 Image")

### 1-2.今回やること および 学習ポイント

1. 書籍情報ドメインクラス作成
    - やること
        - 書籍情報ドメインクラスを作成する
2. （えせ）書籍一覧サービスクラスを DI する
    - やること
        - （えせ）書籍一覧サービスクラス作成
        - 書籍画面コントローラに書籍一覧サービスをDIして使用する
        - 書籍一覧サービスで生成した書籍情報ドメインの情報を、書籍一覧JSP画面で表示させる
    - 学習ポイント
        - DI の基本メカニズムの理解
        - Spring DI コンテナの理解
3. （えせ）書籍一覧Daoクラスを DI する
    - やること
        - （えせ）書籍一覧Daoクラス作成
        - 書籍画面サービスに書籍一覧DaoをDIして使用する
    - 学習ポイント
        - DI の必要性を考える

<h2 class="handson">2. 技術解説 - DI (Dependency Injection</h2>
### 2-1. DI とは

- Dependency Injection の略

    日本語では「依存性の注入」と呼ばれる

- オブジェクト同士の依存関係を外部から作成できる機構のこと

    すなわち、プログラムを構成するオブジェクト間の依存性をソースコードから排除し、フレームワークが後から依存性を注入（設定）できる機能を提供するもの

- DI は Web アプリケーションに限らず、Spring を使用した様々なアプリケーションで使用できる

  ```
  そもそも DI は Spring に限らず、様々な言語のフレームワークで実装されています
  ```

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

<h2 class="handson">3. ハンズオン実習</h2>

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
