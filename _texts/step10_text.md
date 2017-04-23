---
layout: handson
step:   "STEP10"
title:  "テストコードの書き方を覚えよう - その2結合テスト"
date:   2017-04-23
---

<h2 class="handson">1. テストコードの書き方を覚えよう - その2結合テスト</h2>

### 1-1. 実習テーマ

1. カバレッジリポートを出力する
2. Webアプリケーション全体を一気にテストする

![step10_image]({{ site.baseurl }}/images/texts/text_step10_01.png "Step10 Image")

### 1-2.今回やること および 学習ポイント

1. カバレッジリポートを出力する
    - やること
        - カバレッジリポートを出力するツール「EclEmma」を導入する
        - カバレッジリポートを表示させる
    - 学習ポイント
        - カバレッジリポートを出力するツールの使い方を覚える
2. Webアプリケーション全体を一気にテストする
    - やること
        - Spring テストランナーおよび Spring MVC MockMvc を使用して、メイン画面を表示するHTTP Request を模倣するテストコードを作成する
        - 同様に、書籍一覧画面、書籍登録画面を表示する HTTP Request を模倣するテストコードを作成する
        - jUnit および カバレッジリポート出力するツールを用いてテストを実行する
    - 学習ポイント
        - Spring テストランナおよび Spring MVC MockMvc を使用したテストコーディング方法を把握する

<h2 class="handson">2. 技術解説 - テストコード</h2>

### 2-1. Spring MVC MockMvc とは

- MockMvc は Spring MVC が提供するテスト用ライブラリ
- MockMvc を使用すると、Spring アプリに対して任意の HTTP Request を送信し、その結果アプリから返却された Response を確認するテストを簡単に作成することができる

---

- MochMvc のドキュメントは[こちらのページを参照してください](http://docs.spring.io/spring/docs/3.2.12.RELEASE/javadoc-api/org/springframework/test/web/servlet/package-summary.html)
- MochMvc の使い方については[こちらのサイトも参考になります](http://kuwalab.hatenablog.jp/entry/20130402/p1)

### 2-2. Spring MVC MockMvc を使用したテストコード例 - その1

- MockMvc を使用したテストコード例 - MockMvc を使用する場合のおまじないと基本的なコーディング作法

    ```java
    // @@@@
    // @@@@ SpringによるJunitテストランナ
    // @@@@
    @RunWith(SpringJUnit4ClassRunner.class)

    // @@@@
    // @@@@ Spring MVC MockMvc のおまじない その1
    // @@@@ WebApplicationContext をロードした環境下でテストコードを実行できるおまじない
    // @@@@
    @WebAppConfiguration

    // Spring定義ファイルの取り込み
    @ContextConfiguration(locations = {"fclasspath:spring-context.xml"})

    public class MainControllerTest {

        // @@@@
        // @@@@ Spring MVC MockMvc のおまじない その2
        // @@@@ WebApplicationContext
        // @@@@
        @Autowired
        private WebApplicationContext wac;

        // @@@@
        // @@@@ Spring MVC MockMvc のおまじない その3
        // @@@@ Spring MVCのテストをするためのMockMvcクラス
        // @@@@
        private MockMvc mockMvc;

        // @@@@
        // @@@@ Spring MVC MockMvc のおまじない その4
        // @@@@ MockMvcクラスインスタンスを取得
        // @@@@
        @Before
        public void setup() {
            mockMvc = webAppContextSetup(wac).build();
        }

        // @@@@
        // @@@@ HTTP Request を模倣し、アプリケーションが処理した結果のレスポンスを
        // @@@@ 受け取って HTTPステータスコードとビュー名をチェックする
        // @@@@
        @Test
        public void testMain_normal_1() throws Exception {
            // メイン画面表示のGETリスクエスト発行
            mockMvc.perform(get("/main"))
              .andExpect(status().isOk())
              .andExpect(view().name("main"));
        }
    }
    ```

### 2-3. Spring MVC MockMvc を使用したテストコード例 - その2

- MockMvc を使用したテストコード例 - 応答結果の確認方法

    ```java
    @Test
    public void testListBook_normal_1() throws Exception {

        // 書籍一覧表示画面のGETリスクエスト発行
        ResultActions retact = mockMvc.perform(get("/listbook"))
                .andExpect(status().isOk()).andExpect(view().name("listbook")); 

        // @@@@
        // @@@@ アプリケーションにリクエストを行い、応答結果として
        // @@@@ コントローラからビューに渡した Model オブジェクトは
        // @@@@ 以下のような感じで取得できる
        // @@@@
        MvcResult mvcResult = retact.andReturn();
        ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
        List<Book> booksList = (List<Book>)modelMap.get("books");
        // @@@@
        // @@@@ 上記で Response で返却された 書籍一覧オブジェクトを
        // @@@@ 取得できたので、その内容が正しいかどうかをチェック
        // @@@@ 可能となる
        // @@@@ 

        assertThat(booksList, CoreMatchers.is(CoreMatchers.not(CoreMatchers.nullValue())));

        // DBに ID=1、ISBN="123456789abcdefgh" NAME="JavaScript" PRICE=1200 
        // のデータが登録されていることが前提のテストケース
        Book book1 = booksList.get(0);
        assertThat(book1.getId(), CoreMatchers.is(1));
        assertThat(book1.getIsbn(), CoreMatchers.is("123456789abcdefgh"));
        assertThat(book1.getName(), CoreMatchers.is("JavaScript"));
        assertThat(book1.getPrice(), CoreMatchers.is(1200));
    }
    ```

### 2-4. Spring MVC MockMvc を使用したテストコード例 - その3

- MockMvc を使用したテストコード例 - リクエストパラメータの設定

    ```java
    @Test
    public void testAddBook_normal_1() throws Exception {
        // テストで登録する書籍情報を設定
        Book book = new Book();
        book.setIsbn("TEST-1234567890");
        book.setName("testAddBookで自動登録しました");
        book.setPrice(8888);

        // @@@@ 
        // @@@@ アプリケーションへのリクエストパラメータは
        // @@@@ 以下のように設定することができる
        // @@@@
        // 書籍登録POSTリクエスト発行
        mockMvc.perform(post("/addbook")
              .param("isbn",  book.getIsbn())
              .param("name", book.getName())
              .param("price", new Integer(book.getPrice()).toString())
            )
          .andExpect(status().isFound())
          .andExpect(view().name("redirect:result"))
          .andExpect(model().hasNoErrors());
    }
    ```

<h2 class="handson">3. ハンズオン実習</h2>

### STEP10 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP10' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - カバレッジリポートの出力方法を覚えましょう
    - MockMvc を使用した Webアプリケーションテスト方法を習得しましょう
