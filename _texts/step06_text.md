---
layout: handson
step:   "STEP06"
title:  "日本語文字化け対策と Validation の実装"
date:   2017-04-21
---

<h2 class="handson">1. 日本語文字化け対策とValidationの実装</h2>

### 1-1. 実習テーマ

1. 日本語文字化け対策 および HTML エスケープ処理
2. Validation 実装にむけた環境設定
3. Validation 定義とエラーハンドリング

![step06_image]({{ site.baseurl }}/images/texts/text_step06_01.png "Step06 Image")

### 1-2.今回やること および 学習ポイント

1. 日本語文字化け対策 および HTML エスケープ処理
    - やること
        - サーブレット定義ファイル web.xml に設定を追加
    - 学習ポイント
        - サーブレット定義ファイル web.xml における設定内容を把握する
2. Validation 実装にむけた環境設定
    - やること
        - Validation に必要なライブラリを追加する
        - Spring 定義ファイルにエラーメッセージのための Bean 定義を追加
        - エラーメッセージリソースファイルの作成
    - 学習ポイント
        - Validation 実装に向けた環境構築手順を理解する
3. Validation 定義とエラーハンドリング
    - やること
        - モデルクラスに Validation 用アノテーションを付与する
        - コントロールクラスに Validation 実行とエラーハンドリング処理を実装する
        - 画面 JSP ファイルにエラーメッセージを表示させるタグを追加する
    - 学習ポイント
        - Validation 用アノテーションについて理解
        - Validation の実行とエラーハンドリングの実装方法を把握
        - エラーメッセージを画面JSPに出力する方法を習得する

<h2 class="handson">2. 技術解説 - Validation</h2>

### 2-1. JSR-303 - Bean Validation とは

- JavaBeans オブジェクトのデータ整合性検証ロジック（Validation）を対象とする仕様
- Java仕様を策定しているJCP（Java Community Process）が、当該検証ロジックの共通化を図る事を目的に「[JSR-303 Bean Validation](https://jcp.org/en/jsr/detail?id=303)」仕様を策定した
- 幾つかのプロダクトにより実装されているが、現時点で最も利用されている実装は「Hibernate Validator」であり、単体での利用も可能
- Sprign でも本仕様に準じたバリデーションを実装することが可能
- 本仕様により、データ整合性検証ロジックをアノテーションで実装することが可能となった
- 独自に定義する検証処理（Validator）を自分で作成する事も可能

### 2-2. Validation 適用箇所

- Validation はプレゼンテーション層、ビジネスロジック層に適用される

![ValidationArchtecture]({{ site.baseurl }}/images/texts/tech_step06_01.png "Validation Archtecture")

| レイヤ | コンポーネント | 主な Spring 提供機能 |
|:--|:--|:--|
| プレゼンテーション層 | コントローラ | Spring MVC、DI、AOP |
| ビジネスロジック層 | サービス、ドメイン | Validation、Spring Transaction、DI、AOP |
| データアクセス層 | DAO | Spring JDBC、ORM、DI、AOP |

### 2-3. Validation 用のアノテーション

- Validation を定義するアノテーション付与例

    ```java
    // @@@@
    // @@@@ Validation アノテーションは
    // @@@@ Domain クラスのインスタンス変数に付与する
    // @@@@

    @NotNull
    private int id = 0;

    @NotBlank
    @Size(min=10, max=17)
    private String isbn = "";

    @NotBlank
    private String name = "";

    @NotNull
    private int price = 0;
    ```

### 2-4. Validation を適用する

- Validation を適用する例

    ```java
    RequestMapping(value = "/book/add", method = RequestMethod.POST)
                    // @@@@
                    // @@@@ Controllerメソッドの入力値データ（引数）
                    // @@@@ の前に @valid おまじないを付ける
                    // @@@@ ↓
    public String addBook(@Valid @ModelAttribute("book") Book book,
                          BindingResult result) throws Exception {
                    // @@@@ ↑
                    // @@@@ Controller メソッドの入力値データ（引数）
                    // @@@@ の直後に BindinguResult 型の引数を追加する
                    // @@@@

        if(result.hasErrors()){
            // @@@@ ↑
            // @@@@ BindinguResult オブジェクトをチェックすることで
            // @@@@ Validation エラーがあったかどうかが判定できる
            // @@@@ 本例ではValidationエラー時は書籍登録フォーム画面に戻す
            // @@@@
            return "book-form";
        }

        // Something to do ...
        // ...
    }
    ```

### 2-5. Validation エラーメッセージの表示

- Validation エラーメッセージ表示例

    ```jsp
    <form:form action="addbook" method="POST" modelAttribute="book">
    <table>
     <tr>
      <th>書籍ID</th>
      <td><form:input path="id"/></td>
      <td><form:errors path="id" cssStyle="color:red"/></td>
          <!--
          @@@@ Spring Form Tag ライブラリを使用する場合の
          @@@@ エラーメッセージ表示方法 
          @@@@ <form:errors path=“オブジェクトのプロパティ名”>
          @@@@ -->
     </tr>
     <tr>
      <th>ISBNコード</th>
      <td><form:input path="isbn"/></td>
      <td><form:errors path="isbn" cssStyle="color:red"/></td>
      <!-- @@@@ Spring Form Tag ライブラリエラーメッセージ表示 @@@@ -->

     </tr>
    </table>
    </form:form>
    ```

<h2 class="handson">3. ハンズオン実習</h2>

### STEP06 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP06' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - 日本語文字化け対策 および HTML エスケープ処理の設定方法を把握しましょう
    - Validation 実装にむけた環境設定方法をマスターしましょう
    - アノテーションを使用した Validation 定義方法を覚えましょう
    - Validation エラーハンドリング と エラーメッセージの表示方法を理解しましょう
