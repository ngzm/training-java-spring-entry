---
layout: handson
step:   "STEP00"
title:  "はじめに"
date:   2017-04-15
---

<h2 class="handson">1. はじめに</h2>

- 本研修は、小規模なWebアプリケーションを実際にプログラミングしながら Spring Framework の基礎を学習します。
- 本研修では Spring4系 を使用して学習します（Spring4系は現行版です）。

### 1-1. 本トレーニングのゴール

- Eclipse ＋ Spring Tool Suite ＋ Maven を使用した Webアプリケーション開発環境の構築ができる
    - Spring Framework が提供する以下の機能を利用した開発ができる
    - DI （Dependency Injection ： 依存性の注入）
    - AOP （Aspect Oriented Programming ： アスペクト指向プログラミング ）
    - Spring MVC （Webアプリケーション）
    - Spring JDBC （DBアクセス）
    - 入力データのValidation
    - その他（Annotation、Scope、Securiy、Transaction、etc・・） 
- Spring Framework を使用しアプリケーションのテストケースをプログラミングできる
    - jUnit4、Mockito などを使用したテストコードを書くことができる
    - Spring Test Runner の基本を把握する

### 1-2. 今回勉強しないこと

- Spring の少し応用的な機能
  - 設定ファイルによる DI と AOP
  - REST API、ファイルアップロード・ダウンロード等
  - ORMインテグレーション、JPA や MyBatis 連携

- Spring4 で新たにサポートされたモダンな機能
  - REST機能強化
  - WebSocketサポート
  - Lamda式サポート
  - Bean Validationn1.1(JSR-349)対応
  - Java8、Java EE7 対応
  - Spring Boot
  - その他・・

### 1-3. こころえ

- 本トレーニングはあくまで 『入門』 （きっかけ） に過ぎません。
- トレーニングが終わった後も、自主的に発展的に学習を継続することが最も重要です。
- もうひとつ、習うより慣れろ！ です。
- とにかく触れてみる、試してみる、書いてみることがテクノロジ習得の近道です。

**さあお勉強しましょう。  
Let's study! Enjoy study Spring Framework.**

<h2 class="handson">2. これから作成するWebアプリケーション</h2>

### 2-1. 書籍管理アプリケーション

![bookmgr_image]({{ site.baseurl }}/images/texts/text_step00_01.png "Book Manager App Image")

- ログイン／ログアウト機能 （ログイン画面）
- 登録された書籍情報一覧表示機能（書籍一覧画面）
- 書籍情報の追加登録機能（書籍登録画面）

### 2-2. 学習ステップ

- STEP00： はじめに **（本テキスト）**
- STEP01： 開発環境構築 ＋ 『Hello World』
- STEP02： Spring MVC による画面遷移
- STEP03： DIによる（えせ）書籍一覧画面
- STEP04： Spring JDBCによる書籍テーブル検索
- STEP05： Spring JDBCによる書籍テーブル更新
- STEP06： Validation 実装と日本語などの対策
- STEP07： 例外のハンドリング
- STEP08： AOPによる走行ログ出力
- STEP09： jUnit/Spring/Mockitoを使用したテスト その1
- STEP10： jUnit/Spring/Mockitoを使用したテスト その2
- STEP11： ログイン認証とトランザクション管理

<h2 class="handson">3. ハンズオン資料</h2>

本トレーニングでは、ハンズオン資料を参照しながらコーディングの仕方を学習します。


{% for handson in site.handsons %}
  {% if handson.step == 'STEP00' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

**ここでは先ず、ハンズオン資料の読み方を把握してください。  
ハンズオン資料の読み方は「 [{{ hnam }}]({{ hurl }}) 」にてご確認ください。**

<h4 class="handson">次のステップ</h4>

{% for txt in site.texts %}
  {% if txt.step == 'STEP01' %}
    {% capture turl %}{{ site.baseurl }}{{ txt.url }}{% endcapture %}
    {% capture tnam %}{{ txt.step }} - {{ txt.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

次のステップは「 [{{ tnam }}]({{ turl }}) 」です。さっそく始めましょう
