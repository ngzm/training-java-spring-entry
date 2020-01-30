---
layout: handson
step:   "STEP01"
title:  "開発環境構築＋Hello World"
date:   2017-04-15
---

<h2 class="handson">1. 開発環境構築＋Hello World</h2>

### 1-1. 実習テーマ

1. 開発環境を構築する
2. Hello World 画面を出す

![step01_image]({{ site.baseurl }}/images/texts/text_step01_01.png "Step01 Image")

### 1-2.今回やること および 学習ポイント

1. 開発環境構築 
    - やること
        - Eclipse +  STS + Maven による開発環境を構築する
    - 学習ポイント
        - 開発環境の構成を理解する
        - 開発環境構築手順を習得する
2. Hello World 画面作成
    - やること
        - Spring + Maven プロジェクトを作成
        - Hello World アプリケーションをコーディング
        - Hello World アプリケーションを起動
    - 学習ポイント
        - Spring Framework 概要を理解する
        - Maven の基本を把握する
        - プロジェクト構成を理解する
        - Web アプリケーションのアーキテクチャを理解する
        - Web アプリケーションデ実行環境を把握する

<h2 class="handson">2. ハンズオン実習</h2>
### STEP01 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP01' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - 開発環境を構築しましょう！  
    - Spring Framework を使用して Hello World 画面も出してみましょう！

<h2 class="handson">3. 技術解説 - Spring Framework 概要</h2>

### 3-1. Spring Framework とは

- Java言語を用いて開発するWebアプリケーション向けフレームワークのひとつ
- 現在、最も利用されているフレームワークである
- 主に以下の機能を提供

  ```
  - フレームワーク・コア機能：Spring Framework Core
  - Webアプリケーション開発フレームワーク：Spring MVC
  - データベースアクセス機能：Spring JDBC
  - など...
  ```

- Spring Framework に関する情報は下記のWebサイトを参照のこと
    - [Spring Framework 本家のWebサイト][springfw]
    - [Spring Framework Documentation][springdoc]

[springfw]: https://spring.io/ "spring framework"
[springdoc]: https://spring.io/docs/ "spring docs"

### 3-2. Spring Framework エコシステム

- Spring Framework の主要なエコシステム構成は下図の通りです。

![AboutSpring]({{ site.baseurl }}/images/texts/tech_step01_01.png "About Spring")

### 3-3. Webアプリケーションのアーキテクチャ

- Webアプリケーションの一般的なアーキテクチャ（論理階層）

| レイヤ | 役割 |
|:--|:--|
| プレゼンテーション層 | ユーザインターフェースを提供 |
| ビジネスロジック層 | ビジネスロジック（業務用兼を実装する機能）を提供 | 
| データアクセス層 | データ永続化（一般にデータベースを利用）を提供 | 

レイヤの名称は利用するフレームワークによって異なる場合があるが、基本的な考え方は同様である。

### 3-4. Spring Framework のアーキテクチャ

- Spring Framework のアーキテクチャ（論理階層）

![SpringArchtecture]({{ site.baseurl }}/images/texts/tech_step01_02.png "Spring Archtecture")

| レイヤ | コンポーネント | 主な Spring 提供機能 |
|:--|:--|:--|
| プレゼンテーション層 | コントローラ | Spring MVC |
| ビジネスロジック層 | サービス、ドメイン | バリデーション、Springトランザクション |
| データアクセス層 | DAO | Spring JDBC、ORMインテグレーション |


<h2 class="handson">4. 技術解説 - Maven</h2>
### 4-1. Maven とは

- Java 用のプロジェクト構成管理ツールの一つ
    - 以前 Ant が主流であったが、現在は、Maven を使うケースが増えている
    - 昨今では Gradle という Groovy ベースのツールを利用するユーザも増加中

- Maven の特徴は下記の通り。

  ```
  - プロジェクト構成（依存ライブラリ構成）などを pom.xml という設定ファイルで管理
  - ビルドや動作に必要な依存外部ライブラリ（JAR）を自動的にダウンロードする
  - ビルドやテスト、デプロイを１アクションで行える
  - Jenkinsとの連携も簡単にできる
  ```

- Maven に関する情報は下記のWebサイトを参照のこと
    - [Maven 本家のWebサイト][maven]

[maven]: https://maven.apache.org/

### 4-2. Maven プロジェクト構成

- 基本的な Maven プロジェクト構成は下記の通り

  ```
  - アプリケーション用ファイル
      /src/main/java
      /src/main/resources

  - テスト用ファイル
      /src/test/java
      /src/test/resources
  ```

- 解説
    - アプリケーションファイルは /src/main に配置する
    - テスト用ファイルは /src/test に配置する
    - /src/test は、テスト実行時のみクラスパスに追加される
    - ソースコードは  java に、リソースファイル等は resources に配置する

<h4 class="handson">次のステップ</h4>

{% for txt in site.texts %}
  {% if txt.step == 'STEP02' %}
    {% capture turl %}{{ site.baseurl }}{{ txt.url }}{% endcapture %}
    {% capture tnam %}{{ txt.step }} - {{ txt.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

次のステップは「 [{{ tnam }}]({{ turl }}) 」です。サクサク行きましょう
