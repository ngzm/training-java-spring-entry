---
layout: handson
step:   "STEP08"
title:  "AOPを使って走行ログを出力させてみる"
date:   2017-04-22
---

<h2 class="handson">1. AOP を使って走行ログを出力させてみる</h2>

### 1-1. 実習テーマ

1. AOP を利用するための環境設定を行う
2. 走行ログを出力する Aspect クラスを作成する

![step08_image]({{ site.baseurl }}/images/texts/text_step08_01.png "Step08 Image")

### 1-2.今回やること および 学習ポイント

1. AOP を利用するための環境設定を行う
    - やること
        - pom.xml に AOP に必要となるライブラリを追加
        - Sprign 設定ファイルに AOP を使用する設定情報を追加
    - 学習ポイント
        - AOP を利用する場合の環境構築手順を覚える
2. 走行ログを出力する Aspect クラスを作成する
    - やること
        - 走行ログを出力する Aspect クラスを実装する
    - 学習ポイント
        - AOP 概要と動作の仕組みを学習する
        - AOP を使用した実装方法を把握する

<h2 class="handson">2. 技術解説 - AOP</h2>

### 2-1. AOP とは

- AOP は Aspect Oriented Programming の略、日本語では「アスペクト指向プログラミング」と呼ばれる
- アプリケーションが本来行うべき機能と、そうではないがシステムとして必要となる機能（例えばロギングやトランザクション、例外処理など）を分離することができる機構

    すなわち、オブジェクトが「本来行うべき機能」とそうでない「横断的感心事」を分離できる

- 「横断的感心事」は、一般的には複数のオブジェクトにおける共通処理であるが、AOP により、これを任意に追加したり取り外ししたりできるようになった

### 2-2. AOP 適用箇所

- AOP は Web アプリケーションに限らず様々な Spring アプリケーションで利用できる

![AOPArchtecture]({{ site.baseurl }}/images/texts/tech_step08_01.png "AOP Archtecture")

| レイヤ | コンポーネント | 主な Spring 提供機能 |
|:--|:--|:--|
| プレゼンテーション層 | コントローラ | Spring MVC、DI、AOP |
| ビジネスロジック層 | サービス、ドメイン | Validation、Spring Transaction、DI、AOP |
| データアクセス層 | DAO | Spring JDBC、ORM、DI、AOP |

### 2-3. AOP の仕組み 

![AOPFlow]({{ site.baseurl }}/images/texts/tech_step08_02.png "AOP Flow")

### 2-4. Advice の種類

| タイプ | 説明 |
|:--|:--|
| Before | Joinpoint の前に Advice を実行する |
| Advice | Joinpoint の後に Advide を実行する |
| AfterReturning | Joinpoint が完全に終了した後に Advice を実行する |
| Around | Joinpoint の前後で Advice を実行する |
| AfterThrowig | Joinpoint で例外が発生した場合に Advice を実行する |

### 2-5. PointCut について

- PointCut とは、下記の条件にてAdvice を実行するかどうかをフィルタリングする機構
    1. JoinPointとなるメソッド修飾子（Public、Private等）
    1. JoinPointとなるメソッドの戻り値型
    1. JoinPointとなるメソッドのパッケージ名
    1. JoinPointとなるメソッドのクラス（インターフェース名）
    1. JoinPointとなるメソッドのメソッド名
    1. JoinPointとなるメソッドの引数の型
    1. JoinPointとなるメソッドがスローする例外
- PointCut はワイルドカードによる指定も可能
- 論理積（AND）や論理和（OR）といった論理演算子も利用可能

### 2-6. アノテーションベースの AOP その1

- Before Advice 実装例

    ```java
    // @@@@
    // @@@@ Aspect アノテーションを付与すること！
    // @@@@ さらに DI コンテナに登録するための Component などのアノテーションも重要
    // @@@@
    @Aspect
    @Component
    public class BookMgrAspect {

      private static final Logger logger = LoggerFactory.getLogger(BookMgrAspect.class);

      // @@@@ 
      // @@@@ Pointcut
      // @@@@ ↓
      @Before("execution(* jp.sample.bookmgr.*..*(..))")

      // @@@@
      // @@@@ Advice
      // @@@@ 以下メソッド全体
      public void beforeMethod(JoinPoint jp) {
                  // @@@@
                  // @@@@ JoinPoint は AOP 対象メソッドの情報を保持するオブジェクト
                  // @@@@ そのクラス名やメソッド名を始めとした情報を格納している
                  // @@@@
        // AOP対象のクラス名を取得
        String className = jp.getTarget().getClass().getName();

        // AOP対象のメソッド名を取得
        String methodName = jp.getSignature().getName();

        // ログ出力
        logger.info("Before: " + className + "#" + methodName + " START!!");
      }
    }
```

### 2-6. アノテーションベースの AOP その2

- AfterReturnig Advice 実装例

    ```java
    // @@@@
    // @@@@ Aspect アノテーションを付与すること！
    // @@@@ さらに DI コンテナに登録するための Component などのアノテーションも重要
    // @@@@
    @Aspect
    @Component
    public class BookMgrAspect {
    
      private static final Logger logger = LoggerFactory.getLogger(BookMgrAspect.class); 
      
      // @@@@ 
      // @@@@ Pointcut
      // @@@@ ↓
      @AfterReturning(value="execution(* jp.sample.bookmgr.*Controller.*(..))",
                                                          returning="returnValue")
      // @@@@
      // @@@@ Advice
      // @@@@ 以下メソッド全体
      public void afterReturningMethod(JoinPoint jp, Object returnValue) {
                                            // @@@@
                                            // @@@@ AfterReturning の Advice では
                                            // @@@@ joinpoint が return した値を
                                            // @@@@ 引数で取得することができる
                                            // @@@@
  
        // AOP対象のクラス名を取得
        String className = jp.getTarget().getClass().getName();
   
        // AOP対象のメソッド名を取得
        String methodName = jp.getSignature().getName();
    
        // リターン値を取得
        String retVal = returnValue.toString();
    
        // ログ出力
        logger.info("AfterReturning: " + className + "#"
                        + methodName + " RETURN(\"" + retVal + "\")");
      }
    }
    ```

### 2-7. Spring 定義ファイルによる AOP

- Spring設定ファイルで AOP を実装する方法もある
    - ただしこのトレーニングではここで紹介する程度

- この方法は DI と同様に AOP を集中的に設計・管理できるメリットが有る
    - このため、比較的大規模なアプリケーションの場合に有効である。

<h2 class="handson">3. ハンズオン実習</h2>

### STEP06 ハンズオン

{% for handson in site.handsons %}
  {% if handson.step == 'STEP08' %}
    {% capture hurl %}{{ site.baseurl }}{{ handson.url }}{% endcapture %}
    {% capture hnam %}{{ handson.step }} - {{ handson.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

- **ハンズオン資料「 [{{ hnam }}]({{ hurl }}) 」に従い実習を開始してください。**

    - AOP を利用するための環境設定方法を習得しましょう
    - Aspect クラスの実装方法をマスターしましょう

<h4 class="handson">次のステップ</h4>

{% for txt in site.texts %}
  {% if txt.step == 'STEP09' %}
    {% capture turl %}{{ site.baseurl }}{{ txt.url }}{% endcapture %}
    {% capture tnam %}{{ txt.step }} - {{ txt.title }}{% endcapture %}
    {% break %}
  {% endif %}
{% endfor %}

次のステップは「 [{{ tnam }}]({{ turl }}) 」です。もう少しで終わりです！
