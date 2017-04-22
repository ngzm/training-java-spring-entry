---
layout: handson
step:   "STEP07"
title:  "例外のハンドリング"
date:   2017-04-22
---

<h2 class="handson">1. 例外のハンドリング</h2>

### 1-1. 実習テーマ

1. Spring定義ファイルに例外処理を追加
2. 例外画面JSPを作成

![step07_image]({{ site.baseurl }}/images/texts/text_step07_01.png "Step07 Image")

### 1-2.今回やること および 学習ポイント

1. Spring定義ファイルに例外処理を追加
    - やること
        - Spring 設定ファイルに例外と例外ページのマッピング情報を追加
    - 学習ポイント
        - Spring 設定ファイルでの例外ハンドリング設定方法の把握
2. 例外画面JSPを作成
    - やること
        - 例外画面JSPを作成する
    - 学習ポイント
        - 例外発生時に専用のエラー画面（JSP）に遷移する
        - 例外エラーメッセージの出力方法

<h2 class="handson">2. 技術解説 - 例外処理</h2>

### 2-1. Spring Framework の例外処理

- 例外は全てのレイヤで発生する可能性があるが、それをハンドリングする箇所は、基本的にプレゼンテーション層に実装する
- Spring Framework における例外のハンドリングは次の方法で実装することが基本となる

    1. アプリケーション共通で例外ハンドリングする SimpleMappingHandlerExceptionResolver クラスを利用する
    1. Controller ごとに個別に例外のハンドリングを実装する


### 2-2. アプリケーション共通で例外をハンドリングする

- アプリケーション共通の例外ハンドリングは SimpleMappingHandlerExceptionResolver を使用
- Spring 定義ファイルで SimpleMappingHandlerExceptionResolver オブジェクトを生成、DIコンテナに登録する
- ハンドリングしたい例外クラスは SimpleMappingHandlerExceptionResolver の exceptionMappings プロパティに複数登録できる

    ```xml
    <!--
      @@@@ 
      @@@@ 例外ハンドラ SimpleMappingHandlerExceptionResolver を Bean 定義
      @@@@
      @@@@ -->
      <bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
          <!--
          @@@@
          @@@@ exceptionMappings プロパティに
          @@@@ ハンドリングしたい例外を複数登録
          @@@@ -->
          <property name="exceptionMappings">
              <props>
                  <!-- @@@@ ハンドリングする例外クラス @@@@ -->
                  <prop key="org.springframework.dao.DataAccessException">error/dberror</prop>
                  <prop key="java.lang.Exception">error/syserror</prop>
              </props>
          </property>
      </bean>
    ```

### 2-3. 例外時のエラーメッセージを画面に表示する

- 例外エラーメッセージ表示例

    ```jsp
    <body>
        <h1>例外発生</h1>
        <dl style="color:red">
                          <!--
                          @@@@ SimpleMappingHandlerExceptionResolver で捕捉
                          @@@@ した例外オブジェクトは、JSP では ${exception} 
                          @@@@ にマッピングされている
                          @@@@ -->
        <dt>例外クラス名</dt><dd><c:out value="${exception['class'].name}"/></dd>
        <dt>メッセージ</dt><dd><c:out value="${exception.message}"/></dd>
        </dl>

        <hr />
        <a href="main">書籍管理メイン画面</a> 
    </body>
    ```

<h2 class="handson">3. ハンズオン実習</h2>

### STEP07 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP07' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - アプリケーション共通の例外ハンドリング実装方法を把握しましょう
    - 発生した例外のエラーメッセージを画面に表示させましょう
