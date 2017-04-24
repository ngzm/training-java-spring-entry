---
layout: handson
step:   "STEP11"
title:  "ユーザ認証とトランザクション管理"
date:   2017-04-24
---

<h2 class="handson">1. ユーザ認証とトランザクション管理</h2>

### 1-1. 実習テーマ

1. Spring Security を用いてユーザ認証を実装する
2. Spring Transaction Manager を利用してトランザクションを実現する

![step11_image]({{ site.baseurl }}/images/texts/text_step11_01.png "Step11 Image")

### 1-2.今回やること および 学習ポイント

1. Spring Security を用いてユーザ認証を実装する
    - やること
        - Spring Security を利用するための環境設定を行う
        - ログインフォーム画面、認証エラー画面 Controller を作成
        - ログインフォーム画面、認証エラー画面 JSP を作成
    - 学習ポイント
        - Spring Security を利用した認証の仕組みを理解する
        - Spring Security を利用した認証処理の実装手順を把握する
2. Spring Transaction Manager を利用してトランザクションを実現する
    - やること
        - Spring Transaction Manager を使用するための環境設定を行う
        - Transaction Manager アノテーションを用いてトランザクションを実装する
    - 学習ポイント
        - Sprint Transaction Manager を利用したトランザクション実現方法を把握する

<h2 class="handson">2. 技術解説 - Spring Security によるユーザ認証</h2>

### under constructions

<h2 class="handson">3. 技術解説 - Spring transaction Manager によるトランザクション</h2>

### under constructions

<h2 class="handson">4. ハンズオン実習</h2>

### STEP11 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP11' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - Spring Security を用いたユーザ認証の実現方法を学習しましょう
    - Spring Transaction Manager を利用してトランザクションを実現しましょう
