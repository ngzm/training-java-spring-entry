---
layout: handson
step:   "STEP00"
title:  "本資料の記述方法について"
date:   2017-04-03
---
## 1. 作業手順の記述例
作業手順は以下の感じで記述します。

#### 1-1. インストール

```
・次サイトからダウンロード ⇒ http://www.oracle.com/technetwork/java/javase/downloads/index.html
・JDKを選択し普通にインストール
```

#### 1-2. Java実行テスト
環境変数の設定後，コマンドプロンプトでJavaが実行できるか確認する

```
$ java -version
  java version "1.7.0_67"
  Java(TM) SE Runtime Environment (build 1.7.0_67-b01)
  Java HotSpot(TM) Client VM (build 24.65-b04, mixed mode, sharing)
```
***
## ２. ソースコード（環境設定ファイル含む）の記述例
"◆"で始まる文は、コーディングする上で必要となる注釈や、重要な箇所を示すものとなります。  
このため、**この部分をソースコードに入力することはできません。ソースに含めるとエラーになります。** 十分ご注意ください。

#### 例１. 環境設定ファイル（一部）のコーディング例
###### /pox.xml
```xml
◆
◆ 省略
◆ -- 12行目付近
◆ -- ↓主要なライブラリのバージョンを指定 --
◆
<properties>
  <java-version>1.7</java-version>
  <org.springframework-version>3.2.8.RELEASE</org.springframework-version>
  <org.aspectj-version>1.7.4</org.aspectj-version>
  <org.slf4j-version>1.7.6</org.slf4j-version>
</properties>
◆
◆ -- ↑ここまで、主要なライブラリのバージョンを指定 --
◆ 以下省略
◆
```

#### 例２. JSPファイルのコーディング例
###### /src/main/webapp/WEB-INF/views/ addbookform.jsp

```jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="css/bookmgr.css" />
	<title>書籍登録画面</title>
</head>
<body>
<h1>書籍登録画面</h1>

◆
◆ -- ↓とりあえずエセ実装しておく --
◆
<p>
工事中！！
</p>
◆
◆ -- ↑とりあえずエセ実装しておく --
◆

<hr />
<a href="main">書籍管理メイン画面</a>

</body>
</html>
```

#### 例３. ソースコードのコーディング例
###### /src/main/jp.sample.bookmgr.service.ListBookServiceImpl.java

```java
package jp.sample.bookmgr.service;

// import java.util.ArrayList;    ◆◆◆ ←ここ削除
import java.util.List;

import jp.sample.bookmgr.dao.ListBookDao;
import jp.sample.bookmgr.model.Book;

import org.springframework.stereotype.Service;

/**
 * 書籍一覧サービス実装クラス
 * @author	長住@NTT-AT
 * @version	1.0
 */
@Service  ◆◆◆◆ ←重要 サービスクラスとしてDI可能というアノテーションを宣言
public class ListBookServiceImpl implements ListBookService {

	/**
	 *  書籍一覧取得DAO
	 */
	@Autowired	◆◆◆◆ ←重要 ListBookDaoオブジェクトをインジェクション
	ListBookDao listBookDao;

	/**
	 * 書籍一覧取得サービス
	 * @return	書籍一覧情報
	 */
	@Override
	public List<Book> getBookList() throws Exception {
		// 書籍一覧を取得
		return listBookDao.getBookList();
	}
}
```
