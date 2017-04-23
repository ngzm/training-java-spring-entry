---
layout: handson
step:   "STEP09"
title:  "テストコードの書き方を覚えよう - その1単体テスト"
date:   2017-04-23
---

<h2 class="handson">1. テストコードの書き方を覚えよう - その1単体テスト</h2>

### 1-1. 実習テーマ

1. テストコード作成のための環境設定
2. アプリケーションクラスメソッドごとに単体テストする

### 1-2.今回やること および 学習ポイント

1. テストコード作成のための環境設定
    - やること
        - テストを実施するために必要なライブラルを追加する
        - テスト用のSpring定義ファイルを作成する
    - 学習ポイント
        - テスト用の開発環境設定手順を把握する
2. アプリケーションクラスメソッドごとに単体テストする
    - やること
        - Spring テストランナー、および Mockito ライブラリを用いて、書籍一覧 Dao クラスのテストコードを作成する
        - 同様に、書籍登録 Dao クラスのテストコードを作成する
        - jUnit を用いて作成したテストコードを実行してみる
    - 学習ポイント
        - jUnit を使用したテストコード作成方法を学習する
        - Mockito ライブラリを用いたモックの利用方法を把握する
        - Spring テストランナーによるテスト環境について理解する

<h2 class="handson">2. 技術解説 - テストコード</h2>

### 2-1. テストコード作成・実行で使用するライブラリ

- jUnit
    - Javaによるテストフレームワーク
- Mockito
    - java オブジェクトのモックを作るときなどに使用するテスト用ライブラリ
- Spring テストランナ－
    - jUnit テストで、Spring Framework が提供する機能（DI や AOP）を利用したコードを動かすためのテストランナー

### 2-2. jUnit の基本

- jUnit を使用したテストコードの作法を覚える必要がある
- 基本的には、jUnit アノテーションを用いて実装する

    ```java
    package jp.sample.bookmgr.domain;

    import static org.junit.Assert.*;
    import static org.hamcrest.CoreMatchers.*;
    import org.junit.Before;
    import org.junit.Test;

    public class BookTest {

        Book book;

        // @@@@
        // @@@@ テストの前に実行する必要がある処理（変数の初期化など）は
        // @@@@ @Before アノテーション を付けた メソッドに定義する
        // @@@@
        @Before
        public void setup() {
            book = new Book();
        }

        // @@@@
        // @@@@ テストコードであることを示す @Test アノテーション
        // @@@@
        @Test
        public void testIsbn_成功する() {
            book.setIsbn("ISBN-1234567890");
            assertThat(book.getIsbn(), notNullValue());
            assertThat(book.getIsbn(), instanceOf(String.class));
            assertThat(book.getIsbn(), isA(String.class)); 
            assertThat(book.getIsbn(), is("ISBN-1234567890"));
        }

        // @@@@
        // @@@@ java のテストコードでは、テストメソッド名に「・・する」といった名前を
        // @@@@ 付けることが多い。昔はメソッド名に日本語文字を使用することに異論を唱える
        // @@@@ 人もいたが、現在は、テストメソッド名を見ただけでどのようなテストなのか？
        // @@@@ が分かりやすいことから、日本語のネーミングが一般化している
        // @@@@
        @Test
        public void testIsbn_失敗する１() {
            book.setIsbn("ISBN-1234567890");
            assertThat(book.getIsbn(), nullValue());
        }

        @Test
        public void testIsbn_失敗する２() {
            book.setIsbn("ISBN-1234567890");
            assertThat(book.getIsbn(), not("ISBN-1234567890"));
        }

        @Test
        public void testIsbn_失敗する３() {
            book.setIsbn(new String("ISBN-1234567890"));
            assertThat(book.getIsbn(), sameInstance(new String("ISBN-1234567890")));
        }

        // @@@@
        // @@@@ @Test アノテーションで発生するはずの例外クラスを指定することもできる
        // @@@@ 下記テストメソッドの場合は、 Exception オブジェクトが throw されれば
        // @@@@ テスト成功、throw されなければテスト失敗となる
        // @@@@
        @Test(expected=Exception.class)
        public void testIsbn_失敗する４() {
            book.setIsbn("ISBN-1234567890");
            book.getIsbn();
        }

        // @@@@
        // @@@@ jUnit アノテーションは、この他に @BeforeClass, @After, @Afterclass
        // @@@@ などがある
        // @@@@
    }
    ```

- jUnit3 と JUnit4 でテストコードの書き方が大きく変わった
- 本トレーニングでは JUnit4 の作法に従う

### 2-3. jUnit テストコードの書き方

- jUnit4 では asserThat と CoreMatchers をセットで使用しながらテストコードを作成する

    ```java
    // @@@@ 等価であること @@@@
    assertThat("Hello", is("Hello"));
    
    // @@@@ 等価でないこと @@@@
    assertThat(true), not(false));
    
    // @@@@ NULLであること @@@@
    assertThat(null, nullValue());
    
    // @@@@ NULLでないこと @@@@
    assertThat(new String("Hello"), notNullValue());
    
    // @@@@ 指定クラスのインスタンスであること @@@@
    assertThat(new String("Hello"), instanceOf(String.class));
    
    // @@@@ instanceOf()と同じ @@@@
    assertThat(new Integer(123), isA(Integer.class)); 
    
    // @@@@ 同一インスタンスであること @@@@
    String hello = new String("Hello");
    assertThat(hello, sameInstance(hello));
    ```

- 上記の Is や not や nullValue ... 等は CoreMatchers ライブラリが提供する比較メソッドである。
- CoreMatchers とは 「org.hamcrest.CoreMatchers」 のことで、Unit4.4 から jUnit に内包されている、詳細は[こちらのページを参照してください](http://hamcrest.org/JavaHamcrest/javadoc/1.3/)


    jUnit3 までは assertThat() がなかったので、以下の様な比較メソッドを使用していた。これらは今でも使用できるが、古い書き方なので新規で作成する時は jUnit4 の作法に従ってください

    ```java
    assertEquals(smp1, smp2)
    assertEquals(msg, smp1, smp2)
    assertTrue(boolean)
    assertFalse(boolean)
    assertNotNull(Object)
    assertNull(Object)
    assertSame(Object1, Object2)
    assertNotSame(Object1, Object2)
    fail()
    ```

### 2-4. Spring テストランナー

- Spring Frameworkを使用したアプリケーショは、DIやAOPなどSpring独自の機能を使用しているため、一般的な jUnit によるテストコードでは動作しない
- このため、Springのアプリケーションテストには、Springが動作できるSpring用のテストランナー（SpringJUnit4ClassRunner）を使用したテストコードを書く必要がある
- ちなみに、標準のテストランナは、BlockJUnit4ClassRunner というものとなる

    ```java
    // @@@@
    // @@@@ Spring テストランナーを使用するというおまじないアノテーション
    // @@@@  ↓
    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration("classpath:spring-sample-applicationContext.xml")
                            // @@@@  ↑
                            // @@@@ テスト環境として参照する Spring 定義ファイルの指定
                            // @@@@
    public class SampleClassTest {
     
        // @@@@
        // @@@@ Spring テストランナーを使用しているため
        // @@@@ DI など Spring 独自の機能が使用できる
        // @@@@ 
        @Autowired
        SampleClass sampleClass;
         
        @Test
        public void test_hello() {
            String messege = sampleClass.getMessage();
            assertThat(message, is(“hoge hoge hoge“));
        }
    }
    ```

### 2-5. Mockito を使用したテストコード例 - その1

- Mockito は、Javaプログラムで使用できるモックフレームワーク
- 任意のクラスインスタンスをモック化（スタブ化）することができる

![mockito_image]({{ site.baseurl }}/images/texts/tech_step09_01.png "Mockito Image")

- Mockito を使用したテストコード例 - Mockito ライブラリを使用する場合のおまじないコード および when ... thenReturn メソッドを使用する例

    ```java
    public class ListBookDaoSpringJdbcTest {

        // @@@@
        // @@@@ おまじないその1
        // @@@@ JdbcTemplate オブジェクトのモック
        // @@@@
        @Mock(name="template")
        private JdbcTemplate mockJdbc;

        // @@@@
        // @@@@ おまじないその2
        // @@@@ テスト対象となる ListBookDao クラス
        // @@@@
        @InjectMocks  @Autowired
        private ListBookDao listBookDao;

        // @@@@
        // @@@@ おまじないその3
        // @@@@ モック化のおまじない
        // @@@@
        @Before
        public void setUp() {
            MockitoAnnotations.initMocks(this);
        }

        // ListBookDao.getBookList() のテストケース 正常系その１
        // 取得データが正しく返却されているかを検証
        @Test
        public void testGetBookList_normal_1() {
            List<Book> expected = new ArrayList<Book>();
            Book book1 = new Book();
            book1.setId(1);
            book1.setIsbn("978-4-7741-5380-3");
            book1.setName("Spring3入門");
            book1.setPrice(3900);
            expected.add(book1);
            String sql = "SELECT id, isbn, name, price FROM book order by id";

            // @@@@
            // @@@@ モック化したインスタンスのメソッド挙動を定義する
            // @@@@
            // @@@@ 以下の場合は、指定した引数で query() がコールされたら
            // @@@@ expected オブジェクトをリターンする挙動を定義
            // @@@@
            when(mockJdbc.query(eq(sql), any(RowMapper.class))).thenReturn(expected);

            List<Book> actual = listBookDao.getBookList();
            assertThat(actual, CoreMatchers.is(expected));
        }
    }
    ```


- 上記の eq() や any() ... は Mockito.Matchers というクラスが提供する検証用メソッド
- 他にも沢山の種類がある、詳細は[こちらのページを参照してください](http://docs.mockito.googlecode.com/hg/org/mockito/Matchers.html)

### 2-6. Mockito を使用したテストコード例 - その2

- Mockito を使用したテストコード例 - verify メソッドを使用したテスト

    ```java
    @Test
    public void testAddBook_normal_2() {
        Book book1 = new Book();
        book1.setId(1);
        book1.setIsbn("978-4-7741-5380-3");
        book1.setName("Spring3入門");
        book1.setPrice(3900);
        String sql = "INSERT INTO book (ID, ISBN, NAME, PRICE)”
          +  “ VALUES (NEXTVAL('bookid_seq'), :ISBN, :NAME, :PRICE)";

        MapSqlParameterSource pmap = new MapSqlParameterSource();
        pmap.addValue("ISBN", book1.getIsbn());
        pmap.addValue("NAME", book1.getName());
        pmap.addValue("PRICE", book1.getPrice());

        addBookDao.addBook(book1);

        // @@@@
        // @@@@ JdbcTemplate.queryメソッドが
        // @@@@ 指定の引数で何回コールされたかをチェックする
        // @@@@
        verify(mockJdbc, times(1)).update(eq(sql), refEq(pmap));
    }
    ```
- Mockitoのドキュメントは[こちらを参照してください（英語）](http://docs.mockito.googlecode.com/hg/org/mockito/Mockito.html)
- Mockito の使い方については[こちらのサイトも参考になります](http://momijiame.tumblr.com/post/36888571523/java-mock-mockito)

<h2 class="handson">3. ハンズオン実習</h2>

### STEP09 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP09' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - テストコード作成のための環境設定手順を把握しましょう
    - アプリ単体テストコードの書き方と実行方法をマスターしましょう
