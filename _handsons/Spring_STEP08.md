---
layout: handson
step:   "STEP08"
title:  "AOP を使ってみる！"
date:   2017-04-03
---
## 1. AOP が使えるように準備しよう
#### 1-1. Maven に Spring AOP ライブラリを追加
###### 1-1-1. /pom.xml に Spring AOP に必要となるライブラリ定義を追加
```xml
◆
◆ 省略
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
    ◆ -- 47行目付近
    ◆ ---- ↓ Spring AOP に必要なライブラリを追加 ----
    ◆
        <!-- aop -->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.8</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>
    ◆
    ◆ ---- ↑ ここまで、Spring AOP に必要なライブラリを追加 ----
    ◆

        <!-- PostgreSQL + JDBC -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>
◆
◆ 以下省略
◆
```
★Maven設定ファイル（pom.xml）を更新するとワークスペースのビルドが始まります。ビルドに数分がかかる場合がありますが、終わるまで待っているほうが良いでしょう。

#### 1-2. Spring AOP を有効にする設定を追加する
###### 1-2-1. /src/main/webapp/WEB-INF/spring/application-context-biz.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:aop="http://www.springframework.org/schema/aop"                   ◆◆◆ --- この行を追加
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd    ◆◆◆ --- ここから以下の宣言を追加
        http://www.springframework.org/schema/aop                           ◆◆◆ --- この行追加
        http://www.springframework.org/schema/aop/spring-aop.xsd">          ◆◆◆ --- この行追加、閉じタグ
        
    <!-- Defines shared resources visible to all other web components -->
    <context:component-scan base-package="jp.sample.bookmgr.biz" />
    
  ◆
  ◆ ---- ↓ Spring AOP を有効にする宣言追加 ----
  ◆
    <!-- Enables the Spring AOP -->
    <aop:aspectj-autoproxy />
  ◆
  ◆ ---- ↑ Spring AOP を有効にする宣言追加終わり ----
  ◆
    
    <!-- データソース用のリソース -->
    <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="location" value="classpath:/jdbc.properties" />
    </bean>
◆
◆ 以下省略
◆
```

Spring AOPを有効にする設定内容の詳細は、「Spring3入門」の90ページ（第3章）を参照してください。  
Spring AOPに関する基本的な説明は、「Spring3入門」の第3章を参照してください。

## 2. AOP による走行ログ出力処理を実装する
#### 2-1. AOP を用いて走行ログを出力するクラスを作成
###### 2-1-1. /src/main/java/jp.sample.bookmgr.biz.aspect.BookAspect.java を新規作成
```
/src/main/java で右クリック→[New]→[Class]
    
Java Class 画面
---------------
[Package]: "jp.sample.bookmgr.biz.aspect"
[Name]: "BookAspect" と入力して [Finish]
---------------

クラスのひな形が自動作成される
```

###### 2-1-1. /src/main/java/jp.sample.bookmgr.biz.aspect.BookAspect.java を次のようにコーディングする
```java
package jp.sample.bookmgr.biz.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 書籍管理アプリケーションのアスペクト処理実装クラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Aspect
@Component
public class BookAspect {

    /**
     * ロガー
     */
    private static final Logger logger = LoggerFactory.getLogger(BookAspect.class);

    /**
     * jp.sample.bookmgr.biz パッケージの全てのクラス・メソッドの開始前に呼ばれる処理
     * APO対象となるクラスとメソッドの名前を取得し、メソッド開始ログを出力する
     * 
     * @param jp JoinPointオブジェクト
     */ 
    @Before("execution(* jp.sample.bookmgr.biz..*(..))")
    public void beforeMethod(JoinPoint jp) {
        // AOP対象のクラス名を取得
        String className = jp.getTarget().getClass().getName();
        // AOP対象のメソッド名を取得
        String methodName = jp.getSignature().getName();
        // ログ出力
        logger.info("Before: " + className + "#" + methodName + " START!!");
    }

    /**
     * jp.sample.bookmgr.biz パッケージの全てのクラス・メソッドが終了する直前に呼ばれる処理
     * APO対象となるメソッドの名前を取得し、メソッド終了ログを出力する
     * 
     * @param jp JoinPointオブジェクト
     */ 
    @After("execution(* jp.sample.bookmgr.biz..*(..))")
    public void afterMethod(JoinPoint jp) {
        // AOP対象のクラス名を取得
        String className = jp.getTarget().getClass().getName();
        // AOP対象のメソッド名を取得
        String methodName = jp.getSignature().getName();
        // ログ出力
        logger.info("After: " + className + "#" + methodName + " END!!");
    }

    /**
     * jp.sample.bookmgr.biz パッケージの全てのクラス・メソッドが例外をスローした時に呼ばれる処理
     * APO対象となるクラスとメソッドの名前を取得し、例外メッセージを出力する
     * 
     * @param jp JoinPointオブジェクト
     */ 
    @AfterThrowing(value="execution(* jp.sample.bookmgr.biz..*(..))", throwing="ex")
    public void afterThrowingMethod(JoinPoint jp, Exception ex) {
        // AOP対象のクラス名を取得
        String className = jp.getTarget().getClass().getName();
        // AOP対象のメソッド名を取得
        String methodName = jp.getSignature().getName();
        // Exceptionメッセージを取得
        String mes = ex.toString();
        // ログ出力
        logger.info("AfterThrowing: " + className + "#" + methodName + " Throw(\"" + mes + "\")");
    }
}
```

#### 2-2. 画面表示テスト
書籍管理プログラムを起動し、コンソール画面に出力されるログメッセージを確認しましょう。

![running log Image](/images/step8-1.png "running log Image")
