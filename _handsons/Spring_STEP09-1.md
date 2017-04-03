---
layout: handson
title:  Spring ENTRY STEP09-1
date:   2017-04-03
---

# Spring Framework 入門
### STEP 09 / テストコードの書き方を覚えよう！
##### STEP 09-1 / 各クラスを個別にテストする方法を試してみる（単体テスト的）
ここでは作成した各クラスを個別（モジュールごと）にテストする方法を学習します。単体テストでは主にこの方法でテストコードを作成します。
***

## 1. テスト環境を構築する
#### 1-1. Spring Framework テストライブラリの追加
###### 1-1-1. /pom.xml に テストに必要なライブラリ定義を追加
```xml
◆
◆ 省略
◆
        <!-- Test -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
            <version>4.11</version>
        </dependency>

    ◆
    ◆ -- 111行目付近
    ◆ ---- ↓ Spring Framework テストライブラリを追加 ----
    ◆
        <!-- Spring + junit -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>${org.springframework-version}</version>
            <scope>test</scope>
        </dependency>
        <!-- Mockito -->
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>1.10.19</version>
            <scope>test</scope>
        </dependency>
◆
◆ ---- ↑ ここまで、Spring Framework テストライブラリを追加 ----
◆

    </dependencies>
```

★★Maven設定ファイルを更新するとプロジェクトがリビルドする。ビルドの進捗は右下に地味に表示されているだけなので見落とさないこと。ビルドに数秒～数分がかかる場合があるが、終わるまで待っているほうが吉でしょう。

#### 1-2. テスト用Spring MVC設定ファイルを作成
テスト用に AOP を無効化したSpring設定ファイルを作成します。  
ついでに、Eclipse でのファイルコピーやファイル名変更の操作方法も覚えましょう。

###### 1-2-1. Spring MVC 設定ファイル（application-context-web.xml）をテスト用にコピーする
- コピー元： /src/main/webapp/WEB-INF/spring/application-context-web.xml
- コピー先： /src/test/resources/test-context-web.xml

###### 1-2-2. Spring 設定ファイル（application-context-biz.xml）をテスト用にコピーする
- コピー元： /src/main/webapp/WEB-INF/spring/application-context-biz.xml
- コピー先： /src/test/resources/test-context-biz.xml

###### 1-2-3. /src/test/resources/test-context-biz.xml AOPを無効化する
```xml
◆
◆ -- 17行目付近
◆ ---- ↓ AOPを有効にする設定をコメントアウトする ----
◆
    <!-- Enables the Spring AOP -->
    <!-- テストのためAOPを無効化する
    <aop:aspectj-autoproxy />
    --> 
◆
◆ 以下省略
◆
```

## 2. 正常系のテストコードを書いてみる
ＤＡＯクラスを例にとって、正常系テストケースを書いてみましょう。

#### 2-1. 書籍一覧Daoクラスの正常系テストケース作成
###### 2-1-1. /src/test/java/jp.sample.bookmgr.biz.dao.ListBookDaoSpringJdbc.java を新規作成
★★テストコードは、"/src/test" の配下に保存する必要があります。注意してください。
```
/src/test/java で右クリック→[New]→[Class]（もしくは[jUnit Test Case]）

---------------
Java Class 画面
---------------
 [Package]: "jp.sample.bookmgr.biz.dao"
 [Name]:    "ListBookDaoSpringJdbcTest" と入力して[Finish]
---------------
テストケースのひな形が自動作成される
```

###### 2-1-2. /src/test/java/jp.sample.bookmgr.biz.dao.ListBookDaoSpringJdbc.java をコーディング
```java
package jp.sample.bookmgr.biz.dao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧データアクセス実装クラステストケース
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */

// SpringによるJunitテストランナー
@RunWith(SpringJUnit4ClassRunner.class)

// Spring定義ファイルの取り込み
@ContextConfiguration("classpath:test-context-biz.xml")

public class ListBookDaoSpringJdbcTest {

    /**
     * JdbcTemplate オブジェクトのモック
     */
    @Mock(name="template")
    private JdbcTemplate mockJdbc;
    
    /**
     * テスト対象となる ListBookDao クラス
     */
    @InjectMocks
    @Autowired
    private ListBookDao listBookDao;

    /**
     * モック化のおまじない
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * ListBookDao.getBookList() のテストケース
     * 正常系その１
     * 取得データが正しく返却されているかを検証
     */
    @SuppressWarnings("unchecked")
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

        // JdbcTemplate#queryの戻り値をMock動作に差替え
        when(mockJdbc.query(eq(sql), (RowMapper<Book>)anyObject())).thenReturn(expected);
        
        List<Book> actual = listBookDao.getBookList();
        assertThat(actual, CoreMatchers.is(expected));
    }
}
```

###### 2-1-3. 書籍一覧Daoクラスのテストケースを実行してみる
```
1.クラス単位でテストケース実行する
  - テストケースのクラスを選択し右クリック→[Run as]→[JUnit Test]
  - EclipseのJUnitビューが表示され、テストが実行されるはず！

2.テストケース実行結果を確認する
  - テストが完了するとその結果がJUnitビューに表示される
```

テストコードにエラーがある場合やテスト結果にバグがある場合は、JUnitビューのアイコンで識別できます。さらに、エラーをダブルクリックすると該当テストケースのコードが表示されます。

#### 2-2. 書籍登録Daoクラスの正常系テストケース作成
###### 2-2-1. /src/test/java/jp.sample.bookmgr.biz.dao.AddBookDaoSpringJdbc.java を新規作成
★★テストコードは、"/src/test" の配下に保存する必要があります。注意してください。

```
/src/test/java で右クリック→[New]→[Class]（もしくは[jUnit Test Case]）

---------------
Java Class 画面
---------------
 [Package]: "jp.sample.bookmgr.biz.dao"
 [Name]:    "AddBookDaoSpringJdbcTest" と入力して[Finish]
---------------
テストケースのひな形が自動作成される
```

###### 2-2-2. /src/test/java/jp.sample.bookmgr.biz.dao.AddBookDaoSpringJdbc.java をコーディング
```java
package jp.sample.bookmgr.biz.dao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍登録データアクセス実装クラステストケース
 *  
 * @author 長住@NTT-AT
 * @version 1.0
 */

// SpringによるJunitテストランナー
@RunWith(SpringJUnit4ClassRunner.class)

//Spring定義ファイルの取り込み
@ContextConfiguration("classpath:test-context-biz.xml")

public class AddBookDaoSpringJdbcTest {

    /**
     * NamedParameterJdbcTemplate オブジェクトのモック
     */
    @Mock(name="template")
    private NamedParameterJdbcTemplate mockJdbc;
        
    /**
     * テスト対象となる AddBookDao クラス
     */
    @InjectMocks
    @Autowired
    private AddBookDao addBookDao;
    
    /**
     * モック化のおまじない
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * addBookDao.addBook() のテストケース
     * 正常系その１
     * NamedParameterJdbcTemplate の update メソッドリターン値による挙動確認
     */
    @Test
    public void testAddBook_normal_1() {
        Book book1 = new Book();
        book1.setIsbn("978-4-7741-5380-3");
        book1.setName("Spring3入門");
        book1.setPrice(3900);

        MapSqlParameterSource pmap = new MapSqlParameterSource();
        pmap.addValue("ISBN", book1.getIsbn());
        pmap.addValue("NAME", book1.getName());
        pmap.addValue("PRICE", book1.getPrice());
        
        String sql = "INSERT INTO book (ISBN, NAME, PRICE) VALUES (:ISBN, :NAME, :PRICE)";

        when(mockJdbc.update(eq(sql), refEq(pmap))).thenReturn(1);
        assertThat(addBookDao.addBook(book1), CoreMatchers.is(1));
    }
}
```

###### 2-2-3. 書籍登録Daoクラスのテストケースを実行してみる
テストケース（AddBookDaoSpringJdbcTest.java）をjUnitで実行して、結果を確認してください。

#### 2-3. 書籍登録Daoクラスの正常系テストケース（別パターン）を追加
###### 2-3-1. /src/test/java/jp.sample.bookmgr.biz.dao.AddBookDaoSpringJdbc.java に次のテストケースを追加
```java
◆
◆ 省略
◆ -- 81行目付近 testAddBook_normal_1() の次に挿入
◆ ---- ↓ 正常系テストケースを追加 ----
◆
    /**
     * addBookDao.addBook() のテストケース
     * 正常系その２
     * パラメータが正しく NamedParameterJdbcTemplate の update メソッドにわたっているか？
     */
    @Test
    public void testAddBook_normal_2() {
        Book book1 = new Book();
        book1.setId(1);
        book1.setIsbn("978-4-7741-5380-3");
        book1.setName("Spring3入門");
        book1.setPrice(3900);

        MapSqlParameterSource pmap = new MapSqlParameterSource();
        pmap.addValue("ISBN", book1.getIsbn());
        pmap.addValue("NAME", book1.getName());
        pmap.addValue("PRICE", book1.getPrice());

        String sql = "INSERT INTO book (ISBN, NAME, PRICE) VALUES (:ISBN, :NAME, :PRICE)";

        addBookDao.addBook(book1);
        verify(mockJdbc, times(1)).update(eq(sql), refEq(pmap));
    }
    
    /**
     * addBookDao.addBook() のテストケース
     * 正常系その３
     * パラメータが正しく NamedParameterJdbcTemplate の update メソッドにわたっているか？
     */
    @Test
    public void testAddBook_normal_3() {
        Book book1 = new Book();
        book1.setIsbn("978-4-7741-5380-3");
        book1.setName("Spring3入門");
        book1.setPrice(3900);

        MapSqlParameterSource pmap = new MapSqlParameterSource();
        pmap.addValue("ISBN", "978-4-7741-5380-4");
        pmap.addValue("NAME", "Spring4入門");
        pmap.addValue("PRICE", 4900);
        
        String sql = "INSERT INTO book (ISBN, NAME, PRICE) VALUES (:ISBN, :NAME, :PRICE)";

        addBookDao.addBook(book1);
        verify(mockJdbc, never()).update(eq(sql), refEq(pmap));
    }
◆
◆ ---- ↑ ここまで、正常系テストケース追加 ----
◆ 省略
◆
```

###### 2-3-2. 書籍登録Daoクラスのテストケースを実行してみる
テストケース（AddBookDaoSpringJdbcTest.java）をjUnitで実行して、結果を確認してください。

## 3. 異常系のテストコードを書いてみる
続いて、異常系テストケースの書き方を学習しましょう。

#### 3-1. 書籍一覧Daoクラスの異常系テストケース追加
###### 3-1-1. /src/test/java/jp.sample.bookmgr.biz.dao.ListBookDaoSpringJdbc.java に異常系テストケースを追加
```java
◆
◆ 省略
◆ -- 85行目付近
◆ ---- ↓書籍一覧Daoクラスの異常系テストケース追加 ----
◆
    /**
     * ListBookDao.getBookList() のテストケース
     * 異常系その１
     * 例外発生時の挙動確認
     */
    @SuppressWarnings({ "unchecked", "serial" })
    @Test(expected=DataAccessException.class)
    public void testGetBookList_abnormal_1() {

        // JdbcTemplate モックの query メソッドの挙動を決める
        when(mockJdbc.query(anyString(),
          any(RowMapper.class))).thenThrow(new DataAccessException(""){});

        // モック化されたJdbcTemplateオブジェクトでDaoクラスを実行する
        // DataAccessExceptionがThrowされるはず
        listBookDao.getBookList();
    } 
◆
◆ ---- ↑ここまで、書籍一覧Daoクラスの異常系テストケース追加 ----
◆

}
```

###### 3-1-2. 書籍一覧Daoクラスのテストケースを実行してみる
テストケース ListBookDaoSpringJdbcTest.java を[Run as]→[jUnit]で実行し結果を確認してください。

#### 3-2. 書籍登録Daoクラスの異常系テストケース追加
###### 3-2-1. /src/test/java/jp.sample.bookmgr.biz.dao.AddBookDaoSpringJdbc.java に異常系テストケースを追加
```java
◆
◆ 省略
◆ -- 128行目付近
◆ ---- ↓書籍登録Daoクラスの異常系テストケース追加 ----
◆
    /**
     * addBookDao.addBook() のテストケース
     * 異常系その１
     * 例外発生時の挙動確認
     */
    @SuppressWarnings("serial")
    @Test(expected=DataAccessException.class)
    public void testAddBook_abnormal_1() {
        Book book1 = new Book();
        book1.setId(1);
        book1.setIsbn("978-4-7741-5380-3");
        book1.setName("Spring3入門");
        book1.setPrice(3900);

        // NamedParameterJdbcTemplate モックの update メソッドの挙動を決める
        when(mockJdbc.update(anyString(),
          (MapSqlParameterSource)anyObject())).thenThrow(new DataAccessException(""){});

        // モック化されたNamedJdbcTemplateオブジェクトでDaoクラスを実行
        // DataAccessExceptionがThrowされるはず
        addBookDao.addBook(book1);
    }
◆
◆ ---- ↑ここまで、書籍登録Daoクラスの異常系テストケース追加 ----
◆

}
```

###### 3-2-2. 書籍登録Daoクラスのテストケースを実行してみる
テストケース AddBookDaoSpringJdbcTest.java を[Run as]→[jUnit]で実行し結果を確認してください。
