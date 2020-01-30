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
      ListBookService  listBookService;
      // @@@@ ↑
      // @@@@ DI コンテナから型が一致するオブジェクトを検索
      // @@@@ 一致するオブジェクトが見つかったら該当の変数に代入される
      
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
      // @@@@ ↑
      // @@@@ インターフェースの型で宣言することがミソ
      // @@@@ これにより、より抽象化した型で合致するオブジェクトが検索される

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
                  // @@@@   ↑
                  // @@@@ Domain オブジェクト book を Model に登録
                  // @@@@ 上記の場合 "books" という名前で登録している
                  // @@@@

      // 画面表示に listbook.jsp を呼び出す
      return "listbook";
    }
    ```

### 3-2. 渡されたドメインオブジェクト情報を画面に表示する

- Modelオブジェクトに格納した Domain は、Modelオブジェクト格納時に設定した名前でJSPに引き渡される
- つまり自動的にHttpServletReqestに設定されることと同意

    ```jsp
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <html>
    <head><title>書籍一覧画面</title></head>
    <body>
    <h1>書籍一覧画面</h1>
    <table>
      <tr><th>id</th><th>isbn</th><th>name</th><th>price</th></tr>

        <!-- @@@@
             @@@@ Model から Domain オブジェクトを取り出す
             @@@@ 下記例の場合、"books" という名前でアクセスできる
             @@@@ -->

      <c:forEach items="${books}" var="book" varStatus="status">
        <tr>
          <td><c:out value="${book.id}" /></td>
          <td><c:out value="${book.isbn}" /></td>
          <td><c:out value="${book.name}" /></td>
          <td><c:out value="${book.price}" /></td>
        </tr>
      </c:forEach>

        <!-- @@@@ 上の forEach について @@@@
             @@@@ ${books} はList型なので、一要素ずつbookドメインオブジェクトを取り出し
             @@@@ "book" という変数に格納しながら要素が無くなるまでループさせる
             @@@@ -->

    </table>
    </body>
    </html>
    ```

<h2 class="handson">4. ハンズオン実習</h2>

### STEP03 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP03' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - DI を使用して Controller に Service オブジェクトをインジェクションしてみましょう
    - DI を使用して Service に Dao オブジェクトをインジェクションしてみましょう
    - インターフェースを使用して、インジェクションするオブジェクトを抽象化しましょう
    - 書籍一覧画面に Book (Domain) オブジェクトの情報を表示させましょう

### STEP03 実装イメージ

![step03-flow]({{ site.baseurl }}/images/texts/text_step03_02.png "text03 Flow")


<h4 class="handson">次のステップ</h4>

{% for txt in site.texts %}
  {% if txt.step == 'STEP04' %}
    {% capture turl %}{{ site.baseurl }}{{ txt.url }}{% endcapture %}
    {% capture tnam %}{{ txt.step }} - {{ txt.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

次のステップは「 [{{ tnam }}]({{ turl }}) 」です。いい感じです。
