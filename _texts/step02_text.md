---
layout: handson
step:   "STEP02"
title:  "Spring MVC による画面遷移"
date:   2017-04-16
---

<h2 class="handson">1. Spring MVC による画面遷移</h2>

### 1-1. 実習テーマ

1. メイン画面作成と遷移
2. 書籍一覧画面作成と遷移
3. 書籍登録フォーム画面作成と遷移
4. CSSファイル作成とスタイル適用

![step02_image]({{ site.baseurl }}/images/texts/text_step02_01.png "Step02 Image")

### 1-2.今回やること および 学習ポイント

1. メイン画面作成と遷移
2. 書籍一覧画面作成と遷移
3. 書籍登録フォーム画面作成と遷移
    - やること
        - 各画面コントローラ作成
        - 各画面JSP作成
    - 学習ポイント
        - Spring MVC を使用した画面遷移メカニズムの理解
        - アノテーションを使用したコーディング方法の把握
        - 特に @RequestMapping アノテーションの使い方
4. CSSファイル作成とスタイル適用
    - やること
        - スタイルシート作成
        - Spring定義ファイルの編集
    - 学習ポイント
        - Spring 定義ファイルのリソースマッピング

<h2 class="handson">2. 技術解説 - Spring MVC</h2>
### 2-1. Spring MVC とは

- プレゼンテーション層を実装するフレームワークの一つ
- 同様のフレームワークには、Spring Web Flow や Struts2 などがある
- MVC2 パターン Model - View - Controller に基づいており、各クラスは疎結合である

    ```
    Struts や Struts2 は過去に脆弱性を攻撃される問題が相次いでおりおすすめできません
    ```

### 2-2. Spring MVC 適用箇所

- Spring MVC はプレゼンテーション層に適用される

![MVCArchtecture]({{ site.baseurl }}/images/texts/tech_step02_03.png "Spring MVC Archtecture")

| レイヤ | コンポーネント | 主な Spring 提供機能 |
|:--|:--|:--|
| **プレゼンテーション層** | **コントローラ** | **Spring MVC** |
| ビジネスロジック層 | サービス、ドメイン | バリデーション、Springトランザクション |
| データアクセス層 | DAO | Spring JDBC、ORMインテグレーション |

### 2-3. Model - View - Controller

![AboutMVC]({{ site.baseurl }}/images/texts/tech_step02_01.png "About Spring MVC")

- Model とは、 View と Controller 間のデータ受け渡しを行うオブジェクト

    Model に格納されたデータは、ドメインオブジェクトにマッピングすることで、プレゼンテーション層からビジネスロジック層さらにはデータアクセス層まで、データの受け渡しを行うことができる

- Veiw は、クライアントに対して処理結果を画面等の形にする

    これまでは JSP を用いることが多かったが、XML や JSON などの形式でデータのやりとりを行うケースも増加中

- Controller はクライアントからのリクエストに応じた振る舞いを制御する

    Model を受け取りドメインオブジェクトにマッピングして、ビジネスロジック層やデータアクセス層との連携を行う、また、処理結果を適切なViewに渡す

### 2-4. Spring MVC 処理の流れ

- Spring MVC のおおまかな処理の流れ

![MVCFlow]({{ site.baseurl }}/images/texts/tech_step02_02.png "Spring MVC Flow")

### 2-5. Spring MVC による画面遷移

- 画面遷移するだけなら View と Controller を作成して対応付けを行うだけで良い

![画面遷移]({{ site.baseurl }}/images/texts/tech_step02_04.png "画面遷移")

### 3-6. URI と コントローラ の対応付け

- URI と コントローラ（メソッド）を対応付けするためには @RequestMapping を使用する
- URI の構造

![url_rqmapping]({{ site.baseurl }}/images/texts/tech_step02_05.png "url Requestmapping")

- URL と @RequestMapping の対応付けの例

  ```
  http://[path-to-host]/bookmgr/ 
    @RequestMapping(value = "/", method = RequestMethod.GET)

  http://[path-to-host]/bookmgr/main
    @RequestMapping(value = "/main", method = RequestMethod.GET)

  http://[path-to-host]/bookmgr/aaa/bbb
    @RequestMapping(value = "/aaa/bbb", method = RequestMethod.GET)
  ```

- @RequestMapping はさらに下記の情報によってコントローラと対応付けさせることが可能
    - リクエストパラメータ
    - リクエストヘッダ
    - メディアタイプ

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


<h4 class="handson">次のステップ</h4>

{% for txt in site.texts %}
  {% if txt.step == 'STEP03' %}
    {% capture turl %}{{ site.baseurl }}{{ txt.url }}{% endcapture %}
    {% capture tnam %}{{ txt.step }} - {{ txt.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

次のステップは「 [{{ tnam }}]({{ turl }}) 」です。こちらはとても大事な仕様です。
