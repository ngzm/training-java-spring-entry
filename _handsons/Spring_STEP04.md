---
layout: handson
step:   "STEP04"
title:  "Spring JDBC による書籍テーブル検索"
date:   2017-04-03
---
## 1. PostgreSQL データベースを構築する
#### 1-1. データベースのセットアップ
###### 1-1-1. PostgreSQL インストール
```
以下のサイトから Installer version Version 9.6.0 (9.6 の最新版でOK)
http://www.enterprisedb.com/products-services-training/pgdownload

・Win x86-32 もしくは x86-64を選択し普通にインストール
・今回の管理者パスワードは： nttat123 とすること！
・上記以外全てデフォルトで良い。
・最後の「Stack Builder may be…」のチェックは外してインストールを完了させる。
```

###### 1-1-2. データベースの設定
```
1.PostgreSQLの「psql SHELL」を起動してPostgreSQLに接続

2.psql SHELLから次のSQLを発行して新規データベースを作成

  postgres=# CREATE DATABASE springdb encoding 'UTF8';
```

###### 1-1-3. 研修用サンプルアプリで使用するテーブルの作成
```
以下のファイルを実行して書籍テーブルを作成 (研修に必要なテーブル作成とサンプルデータ登録を行うSQLを記述)

  postgres=# create_table.sql

```

**(参考)**  
Windows版 PostgreSQL には pgAdminⅢ というグラフィカルなツールが添付されており、これを利用してデータベースの各種管理を行うことも可能です。

#### 1-2. 研修用データベースの構成
###### 1-2-1. PostgreSQLの接続情報

| 管理ユーザ | パスワード | データベース |
|----|----|----|----|----|
| postgres | nttat123 | springdb |

・書籍テーブルについて

| テーブル名 |
|------|
| book |

| # |カラム名 | 型 | 制約 | 備考 |
|:--:|:--|:--|:--|:--|
| 1 | id | SERIAL | PK | SEQUENCE book_id_seq を用いて自動付番する |
| 2 | isbn | VARCHAR(20) | NOT NULL | - |
| 3 | name | VARCHAR(64) | NOT NULL | - |
| 4 | price | INTEGER | NOT NULL | - |
| 5 | lastup | TIMESTAMP | - | default CURRENT_TIMESTAMP |

###### 1-2-2. データベース接続テスト
上記で示した接続情報でDB接続できること、および書籍テーブル（book）にアクセス可能なことを確認してください。

## 2. アプリケーションのDB接続設定
#### 2-1. Maven設定ファイル pom.xml にDB接続ドライバ（PostgreSQL + JDBC）情報を追加する
###### 2-1-1. /pom.xml 編集
```xml
◆
◆省略
◆-- 36行目付近
◆
        <!-- Spring -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>

    ◆
    ◆ -- 48行目付近
    ◆ ---- ↓ここからDB接続ドライバ情報を追加 ----
    ◆
        <!-- PostgreSQL + JDBC -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jdbc</artifactId>
            <version>8.0.37</version>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>9.4.1211.jre7</version>
        </dependency>
    ◆
    ◆ ---- ↑ここまでDB接続ドライバ情報を追加 ----
    ◆

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${org.slf4j-version}</version>
        </dependency>
◆
◆以下省略
◆
```

★Maven設定ファイル（pom.xml）を更新するとプロジェクトのビルドが始まります。ビルドの進捗は右下に地味に表示されているだけなので見落とさない様に注意してください。ビルドに数分かかる場合がありますが、終わるまで待っている方が良いかもしれません。

#### 2-2. DB接続情報定義プロパティファイルを作成
###### 2-2-1. /src/main/resources/jdbc.properties ファイル作成して、次のようにコーディング
```java
jdbc.driverClassName=org.postgresql.Driver
jdbc.url=jdbc:postgresql://localhost:5432/springdb
jdbc.username=postgres
jdbc.password=nttat123
```

#### 2-3. Spring定義ファイルにデータソースとSpring JDBCデータアクセスBean定義を追加
###### 2-3-1. /src/main/webapp/WEB-INF/spring/application-context-biz.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">
        
    <!-- Defines shared resources visible to all other web components -->
    <context:component-scan base-package="jp.sample.bookmgr.biz" />

◆
◆ ---- ↓ここからDBアクセスに関する定義を追加 ----
◆
    <!-- DB接続情報定義プロパティファイルの読み込み -->
    <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="location" value="classpath:/jdbc.properties" />
    </bean>

    <!-- データソース設定 (for tomcat dbcp) -->
    <bean id="dataSource" class="org.apache.tomcat.jdbc.pool.DataSource" destroy-method="close">
        <property name="driverClassName" value="${jdbc.driverClassName}" />
        <property name="url" value="${jdbc.url}" />
        <property name="username" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
    </bean>

    <!-- JDBC Template Bean -->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource" />
    </bean>
    <!-- JDBC Named Template Bean -->
    <bean id="namedJdbcTemplate" class="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate">
        <constructor-arg ref="dataSource" />
    </bean>
◆
◆ ---- ↑ここまでDBアクセスに関する定義を追加 ----
◆

</beans> 
```

## 3. データベースにアクセスして書籍一覧を取得してみる
#### 3-1. 書籍一覧Daoクラスをきちんと実装する
書籍一覧Ｄａｏは、これまで（えせ）書籍一覧を返していたが、ここで、PostgreSQLデータベースを検索して書籍一覧を取得できるよう、きちんと実装します。

###### 3-1-1. 書籍一覧Ｄａｏ実装クラスのクラス名を適切なものに変更
リファクタリングを使って "ListBookDaoImpl" を "ListBookDaoSpringJdbc" に変更してください。  
**★★リファクタリングの仕方も覚えましょう**

```
ListBookDaoImpl.javaを選択して右クリック [Refacter]-[Rename]
Rename Compilation Unitウインドウ New Nameに変更後の名称を入力して[Finish]
```

###### 3-1-2. /src/main/java/jp.sample.bookmgr.biz.dao.ListBookDao.java インターフェース修正
Throwする例外オブジェクトを正しいクラスに変更します。

```java
package jp.sample.bookmgr.biz.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

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
    public List<Book> getBookList() throws DataAccessException; ◆◆◆<-- ここの例外クラス名を変更するだけ！
}
```

###### 3-1-3. /src/main/java/jp.sample.bookmgr.biz.dao.ListBookDaoSpringJdbc.java クラスをきちんと再実装
これまで、えせ実装だったものを、DBからデータを取得できる形に、きちんと実装し直します。

```java
package jp.sample.bookmgr.biz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
// import java.util.ArrayList; ◆◆◆→ 不要となるので、この宣言は削除してください。
import java.util.List;

◆
◆ import文は適宜追加すること
◆
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧データアクセス実装クラス
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Repository    // リポジトリ（Dao）クラスとしてDI可能というアノテーションを宣言
public class ListBookDaoSpringJdbc implements ListBookDao {

◆
◆ ---- ↓以下は大幅に書き替え ----
◆
    /**
     * JDBC制御クラス
     */
    @Autowired
    private JdbcTemplate template;

    /**
     * 書籍データベースから書籍一覧データを取得するメソッド
     * @return 書籍一覧情報
     */ 
    @Override
    public List<Book> getBookList() throws DataAccessException {
        
        // DBマッピングインナークラスの定義
        class BookRowMapper implements RowMapper<Book> {
            @Override
            public Book mapRow(ResultSet result, int row) throws SQLException {
                Book book = new Book();
                book.setId(result.getInt("ID"));
                book.setIsbn(result.getString("ISBN"));
                book.setName(result.getString("NAME"));
                book.setPrice(result.getInt("PRICE"));
                return book;
            }
        }
        RowMapper<Book> rowMapper = new BookRowMapper();
        String sql = "SELECT id, isbn, name, price FROM book ORDER BY id";

        // DBを検索して書籍情報一覧を取得する
        List<Book> bookList = template.query(sql, rowMapper);
        return bookList;
    }
◆
◆ ---- ↑以上、大幅に書き替え ----
◆
}
```

★★★Spring JDBC を利用したDB接続および検索方法についての詳細は「Spring3入門」第4章を参照してください。

#### 3-2. 画面表示テスト
書籍一覧画面を表示して、データベースに登録された書籍情報が表示されるかどうかをチェックします。

![BookList Image](/images/step4-1.png "BookList Image")

