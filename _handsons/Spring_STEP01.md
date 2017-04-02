---
layout: handson
title: Spring ENTRY STEP01
---

# Spring Framework 入門
### STEP 01 / 開発環境構築 ＆ Hello World
***

## 1. 開発環境構築に必要なソフトウェア
#### 1-1. ソフトウェア一覧
| # | カテゴリ | 製品名 | バージョン | 備考 |
|:--:|:--|:--|:--|:--|
| 1 | Java | Java SE Development Kit | 1.8.0_102 | JSDK 1.8系の最新版でOK |
| 2 | 開発環境 | Eclipse IDE for Java EE Development | 4.6.1 (Neon) |  Eclipse Java EE 4.6系の最新版でOK |
| 3 | 開発環境 | Spring IDE 3.8.2 RELEASE | 3.8.2 RELEASE | - |
| 4 | 開発環境 | junit | 4.1.2 | - |
| 5 | Spring | Spring Framework | 4.2.8.RELEASE | Spring4.2系であれば恐らく大丈夫 |
| 6 | Web/Appサーバ | Tomcat | 8.5.5 | Tomcat 8.5 系の最新版でOK |
| 7 | DBMS | PostgreSQL | 9.6.0 | PostgreSQL 9.6系の最新版でOK |

**(参考)** 本講習における Spring Framework を始めとした各種ライブラリの構成は「Spring IO 2.0.8-Release」に準拠しています。

#### 1-2. ダウンロード先
個別にダウンロードする必要があるものだけを記載します。

| # | 製品名 | URL |
|:--:|:--|:--|
| 1 | Java SE Development Kit | http://www.oracle.com/technetwork/java/javase/downloads/index.html |
| 2 | Eclipse IDE for Java EE Development | https://www.eclipse.org/downloads/ |
| 3 | Tomcat | http://tomcat.apache.org/download-80.cgi |
| 4 | PostgreSQL | http://www.postgresql.org/download/ |

#### 1-3. その他要件
★★開発環境はインターネットに接続している必須があります。  
開発に必要なパッケージ・ライブラリは、インターネットからダウンロードするためです。

## 2. 開発環境構築
#### 2-1. JDKセットアップ
###### 2-1-1. インストール
```
次サイトからダウンロード ⇒ http://www.oracle.com/technetwork/java/javase/downloads/index.html
JDKを選択し普通にインストール
```

###### 2-1-2. 環境変数の設定後，コマンドプロンプトでJavaが実行できるか確認
```
$ java -version
  java version "1.8.0_102"
  Java(TM) SE Runtime Environment (build 1.8.0_102-b14)
  Java HotSpot(TM) Client VM (build 25.102-b14, mixed mode)
```

#### 2-2. Eclipseセットアップ
###### 2-2-1. Eclipseインストール
```
インストールフォルダを「C:/DEV/eclipse」とする（本当はどこでも良い）

1.Eclipse 4.6.1(Neon)をダウンロード ⇒ https://www.eclipse.org/downloads/
  ダウンロードした「eclipse」フォルダを以下に移動
  C:/DEV/eclipse

2.Eclipseが起動することを確認
  C:/DEV/eclipse/eclipse.exe

3.起動時にworkspaceを指定する画面がでてくるので「C:/DEV/workspace」を指定する（本当はどこでもOK）
  C:/DEV/workspace ----> Eclipseのプロジェクトフォルダ
```

###### 2-2-2. Eclipse環境設定（基本編）
```
a.Default のJRE の設定確認
  [Window]→[Preference]→[Java]→[Installed JREs]

b.Java Compiler の設定確認
  [Window]→[Preference]→[Java]→[Compiler]→[Compiler compliance level] : "1.8"

c.Default の文字コードをUTF-8 に変更
  [Window]→[Preference]→[General]→[Workspace]→[Text file Encoding]→[Other]: "UTF-8"

d.行番号の表示
  [Window]→[Preference]→[general]→[Editors]→[Text Editor]→[Show line numbers]をチェック 

e.ホワイトスペースの表示
  [Window]→[Preference]→[general]→[Editors]→[Text Editor]→[Show whitespace characers]をチェック 

f.AT-NET Proxy の設定（AT-NETの場合のみ）
  [Window]→[Preferences]→[General]→[Network Connections]
  ・ Active Providerを "Manual" に変更
  ・ HTTP、HTTPS の Host、Port、User、Passwordに、AT-ProxyとAT-ID、Password を設定 (SOCKSは設定しない)

g.日本語化（必要に応じて、今回は英語のまま行います）
```

###### 2-2-3.	Eclispe環境設定 CSS、HTML、JSPファイル文字コード設定変更
```
a.CSS ファイルの Default の文字コードが UTF-8 であることを確認、もし違っていれば UTF-8 に変更
  [Window]→[Preference]→[Web]→[CSS Files]→[Creating files]→[Encoding]: "ISO 10646/Unicoede(UTF-8)"

b.HTML ファイルの Default の文字コードが UTF-8 であることを確認、もし違っていれば UTF-8 に変更
  [Window]→[Preference]→[Web]→[HTML Files]→[Creating files]→[Encoding]: "ISO 10646/Unicoede(UTF-8)"

c.JSP ファイルの Default の文字コードをUTF-8 に変更
  [Window]→[Preference]→[Web]→[SP Files]→[Creating files]→[Encoding]: "ISO 10646/Unicoede(UTF-8)"
```

###### 2-2-4. JSPファイルのテンプレートを HTML5 にする
```
1.[Window]→[Preference]→[Web]→[JSP Files]→[Editor]→[Templates]→[New JSP File(html)]をダブルクリック

2.Pattern を以下のように編集（◆◆◆の行が変更対象です）

---
<%@ page language="java" contentType="text/html; charset=${encoding}" pageEncoding="${encoding}"%> ◆◆◆
<!DOCTYPE html> ◆◆◆
<html>
<head>
<meta charset="${encoding}"> ◆◆◆
<title>Insert title here</title>
</head>
<body>
${cursor}
</body>
</html>
```

###### 2-2-5. Maven Proxy設定（AT-NETのような Proxy 配下のネットワークでは本手順が必要）
```
1.[Window]→[Preference]→[Maven]→[User Settings]
   "[UserDirectory]/.m2/settings.xml"を設定

2.エクスプローラなどから "[UserDirectory]/.m2/settings.xml" ファイルを作成

3."[UserDirectory]/.m2/settings.xml" に以下の記述を追加
  ---
  <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
    http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <proxies>  
      <proxy>
        <active>true</active>
        <protocol>http</protocol>
        <host>atproxy.bb.ntt-at.co.jp</host>
        <port>3128</port>
        <username>[XXXX 自分のユーザ XXXX]</username>
        <password>[XXXX 自分のパスワード XXXX]</password>
        <nonProxyHosts></nonProxyHosts>
      </proxy>
    </proxies>
  </settings>
  ---
```
**(参考)** settings.xml：https://maven.apache.org/settings.html

#### 2-3. Spring IDE（Eclipseプラグイン）インストール
```
1.[Help]→[Eclipse MarketPlace]画面を表示

2.検索画面で[Spring]を検索して、以下のツールをインストール
  -----------------
  Spring IDE 3.8.2.RELEASE
  -----------------

プラグインはデフォルト選択状態のままでインストールする。インストール後Eclipseが再起動する。
```

#### 2-4. Tomcat Local Server セットアップ 
###### 2-4-1. Tomcatインストール 
```
インストールフォルダを「C:/DEV/tomcat」とする（本当はどこでも良い）

1.ダウンロード ⇒ http://tomcat.apache.org/download-80.cgi
  Windowsなら、32-bit Windows zip または 64-bit Windows zip を選択。

2.ダウンロードして以下に解凍
  C:/DEV/tomcat/
```

###### 2-4-2. Eclipse Tomcat Local Server 設定
```
1.[Window]→[Preference]→[Server]→[Runtime Environments]→[Add]  
  →[New Server Runtime Environment]画面で "Apache Tomcat v8.5" を選択
  [Next]ボタン押下

2.[Create a new local server]にチェック（このチェックを忘れないように）
  [Next]ボタン押下

3.[Tomcat Server]画面で以下のパラメータを投入する．
  ---------------
  Name:      "Apache Tomcat v8.5"
  directory: "$HOME/tomcat/apache-tomcat-8.5.5"
  JRE:       "workbench default JRE"
  ---------------
  [Finish]ボタン押下

4.Serversビューの表示
  [Window]→[Show View]→[Other]→[Server]→[Servers]
  下部に Servers View が表示されるので、"Tomcat v8.5 Server at localhost" があることを確認
```

## 3. Springを使用したプロジェクト作成
#### 3-1. Mavenプロジェクト作成
```
1.Eclipseの[Package Explorer]タブでプロジェクト作成を選択
  [File]→[New]→[Project]→[Maven]→[Maven Project]→[Next]

2.[New Maven project]-[Selectproject name and location]画面
  ---------------
  [Create a simple project]を選択
  [Use default Workstpace location]を選択
  ---------------
  →[Next]

3.[New Maven project]-[Selectproject name and location]画面
  ---------------
  Group Id:    "jp.sample" # すべて小文字 
  Artifact Id: "bookmgr"   # すべて小文字 
  Version:     "0.0.1-SNAPSHOT"
  Packaging:   "war"
  Name:        "bookmgr"
  # 他は空白のままでOK
  ---------------
  →[Finish]

4.プロジェクトが作成されプロジェクトがビルドされるのを待つ
```

★ここでプロジェクトのビルドが開始されます。ビルドの進捗は右下に地味に表示されています。   
★ビルドに数分がかかる場合がありますが、終わるまで待っているほうが良いでしょう。  
★なお現段階では、プロジェクトにエラーが表示されていますが、そのままで結構です。本エラーはこの後解消します。

#### 3-2. プロジェクトファセット変更
```
1.bookmgr プロジェクトを選択して、マウス右ボタンで[Properties]を選択

2.[Properties]ダイアログの左メニューから[Project Facets]を選択

3.[Project Facets]画面
  ---------------
  Java のバージョンを "1.5" から "1.8" に変更
  Dynamic Web Module のバージョンを "2.5" から "3.0" に変更
  ---------------
  →[Apply] or [Ok] で設定を保存
```

#### 3-3. Maven設定ファイル pom.xml に依存ライブラリ情報を追加
###### 3-3-1. /pom.xml
以下のとおりに Springframework など依存するライブラリ定義を追加。

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
    http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>jp.sample</groupId>
    <artifactId>bookmgr</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>bookmgr</name>

◆
◆ ---- ↓ここから依存ライブラリ情報を追記 ----
◆

    <properties>
        <org.springframework-version>4.2.8.RELEASE</org.springframework-version>
        <org.slf4j-version>1.7.21</org.slf4j-version>
    </properties>

    <dependencies>

      <!-- Servlet -->
      <dependency>
          <groupId>javax.servlet</groupId>
          <artifactId>javax.servlet-api</artifactId>
          <version>3.1.0</version>
          <scope>provided</scope>
      </dependency>
      <dependency>
          <groupId>javax.servlet.jsp</groupId>
          <artifactId>javax.servlet.jsp-api</artifactId>
          <version>2.2.1</version>
          <scope>provided</scope>
      </dependency>
      <dependency>
          <groupId>javax.servlet</groupId>
          <artifactId>jstl</artifactId>
          <version>1.2</version>
      </dependency>

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

      <!-- Logging -->
      <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-api</artifactId>
          <version>${org.slf4j-version}</version>
      </dependency>
      <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>jcl-over-slf4j</artifactId>
          <version>${org.slf4j-version}</version>
          <scope>runtime</scope>
      </dependency>
      <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-log4j12</artifactId>
          <scope>runtime</scope>
          <version>${org.slf4j-version}</version>
      </dependency>
      <dependency>
          <groupId>log4j</groupId>
          <artifactId>log4j</artifactId>
          <scope>runtime</scope>
          <version>1.2.17</version>
      </dependency>

      <!-- Test -->
      <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <scope>test</scope>
          <version>4.12</version>
      </dependency>

    </dependencies>
    <build>
        <plugins>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
◆
◆ ---- ↑ここまで依存ライブラリ情報を追記 ----
◆

</project>
```

★Maven設定ファイル（pom.xml）を更新するとワークスペースのビルドが始まる。ビルドの進捗は右下に地味に表示されているだけなので見落とさないこと。ビルドに数分がかかる場合があるが、終わるまで待っているほうが良い。

#### 3-4. サーブレット設定ファイル web.xml を作成
###### 3-4-1. /src/main/webapp/web.xml 作成
```
1.格納フォルダ (/src/main/webapp/WEB-INF/) の新規作成
  /src/main/webapp で右クリック→[New]→[Folder]

  Folder画面
  ---------------
  Folder Name: に "WEB-INF" と入力して[Finish]
  ---------------

2.web.xmlファイル (/src/main/webapp/WEB-INF/web.xml) の新規作成
  /src/main/webapp/WEB-INF で右クリック→[New]→[File]

  File画面
  ---------------
  File Name: に "web.xml" と入力して[Finish]
  ---------------
```

###### 3-4-2. /src/main/webapp/web.xml コーディング
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
    http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
    version="3.0">

    <!-- The definition of the Root Spring Container shared by all Servlets and Filters -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            /WEB-INF/spring/application-context-biz.xml
        </param-value>
    </context-param>

    <!-- Creates the Spring Container shared by all Servlets and Filters -->
    <listener>
        <listener-class>
            org.springframework.web.context.ContextLoaderListener
        </listener-class>
    </listener>

    <!-- Processes application requests -->
    <servlet>
        <servlet-name>appServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/spring/application-context-web.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>appServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

</web-app>
```

#### 3-5. Spring定義ファイル作成
###### 3-5-1. /src/main/webapp/WEB-INF/spring/application-context-biz.xml 及び application-context-web.xml 作成

```
1.格納フォルダ (/src/main/webapp/WEB-INF/spring) の新規作成
  /src/main/webapp/WEB-INF/ で右クリック→[New]→[Folder]

  Folder画面
  ---------------
  Folder Name: に "spring" と入力して[Finish]
  ---------------

2.application-context-biz.xml ファイル新規作成
  /src/main/webapp/WEB-INF/spring/ で右クリック→[New]→[File]

  File 画面
  ---------------
  File Name: に "application-context-biz.xml" と入力して[Finish]
  ---------------

3.application-context-web.xml ファイル新規作成
  /src/main/webapp/WEB-INF/spring/ で右クリック→[New]→[File]

  File 画面
  ---------------
  File Name: に "application-context-web.xml" と入力して[Finish]
  ---------------
```

###### 3-5-2. /src/main/webapp/WEB-INF/spring/application-context-biz.xml コーディング
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
  
</beans> 
```

###### 3-5-3. /src/main/webapp/WEB-INF/spring/application-context-web.xml コーディング
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:mvc="http://www.springframework.org/schema/mvc"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="        
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- Defines this servlet's request-processing infrastructure -->
    <context:component-scan base-package="jp.sample.bookmgr.web" />
    
    <!-- Enables the Spring MVC @Controller programming model -->
    <mvc:annotation-driven />

    <!-- Resource mappings -->
    <mvc:resources mapping="/resources/**" location="/resources/" />

    <!-- Resolves views selected for rendering by @Controllers
         to .jsp resources in the /WEB-INF/views directory -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/views/" />
        <property name="suffix" value=".jsp" />
    </bean>

</beans> 
```

#### 3-6. Log4j 設定ファイル作成
###### 3-6-1. /src/main/resources/log4j.xml 作成
```
log4j.xml ファイル新規作成
/src/main/resources/ で右クリック→[New]→[File]

File 画面
---------------
File Name: に "log4j.xml" と入力して[Finish]
---------------
```

###### 3-6-2. /src/main/resources/log4j.xml コーディング
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE log4j:configuration PUBLIC "-//APACHE//DTD LOG4J 1.2//EN" "log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">

    <!-- Appenders -->
    <appender name="console" class="org.apache.log4j.ConsoleAppender">
        <param name="Target" value="System.out" />
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%-5p: %c - %m%n" />
        </layout>
    </appender>
    
    <!-- Application Loggers -->
    <logger name="jp.sample.bookmgr.biz">
        <level value="debug" />
    </logger>
    <logger name="jp.sample.bookmgr.web">
        <level value="debug" />
    </logger>

    <!-- 3rdparty Loggers -->
    <logger name="org.springframework.core">
        <level value="info" />
    </logger>
    <logger name="org.springframework.beans">
        <level value="info" />
    </logger>
    <logger name="org.springframework.context">
        <level value="info" />
    </logger>
    <logger name="org.springframework.web">
        <level value="info" />
    </logger>

    <!-- Root Logger -->
    <root>
        <priority value="warn" />
        <appender-ref ref="console" />
    </root>
    
</log4j:configuration>
```

#### 3-7. エラーがないことを確認
このタイミングで画面下の [Markers] タブを参照し、エラーが検出されていたら以下の操作を試してみること。

```
Project Explorerの [bookmgr] を右クリック→[Maven]→[Update Project]
```

★★★ 数秒でプロジェクトが更新されエラーが消えるはず。★★★ ***エラーが残る場合は声掛けしてください***

## 4. Hello Spring アプリケーション作成
#### 4-1. Hello Spring 画面コントロールクラス作成
###### 4-1-1. /src/main/java/jp.sample.bookmgr.web.controller.HelloController.java 作成

```
/src/main/java/ で右クリック→[New]→[Class]

Java Class 画面
---------------
Package: "jp.sample.bookmgr.web.controller"
Name: HelloController" と入力して[Finish]
---------------
```

###### 4-1-2. /src/main/java/jp.sample.bookmgr.web.controller.HelloController.java コーディング
```java
package jp.sample.bookmgr.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Hello Spring 画面コントローラクラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Controller
public class HelloController {
    
    /**
     * Hello Spring 画面コントローラ
     * 
     * @return 画面JSP名
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloSpring() throws Exception {
        return "hello";
    }
}
```

#### 4-2. Hello Spring 画面 View 作成
###### 4-2-1. /src/main/webapp/WEB-INF/views/hello.jsp 作成
```
1.Viewファイル格納フォルダ作成
  /src/main/webapp/WEB-INF/ で右クリック→[New]→[Folder]

  Folder画面
  ---------------
  Folder Name: に "views" と入力して[Finish]
  ---------------

 2./src/main/wqbapp/WEB-INF/views/hello.jsp 新規作成
  bookmgrkk/src/main/webapp/WEB-INF/views で右クリック→[New]→[JSP File]

  New JSP File 画面
  ---------------
  File Name: に "hello.jsp" と入力して[Finish]
  ---------------
```

###### 4-2-2. /src/main/webapp/WEB-INF/views/hello.jsp コーディング
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello Spring 画面</title>
</head>
<body>

<h1>Hello Spring</h1>
きちんとみれましたでしょうか？

</body>
</html>
```

#### 4-3. Hello World 画面表示テスト
**ここではEclipseにセットアップした"Tomcat Local Server"上でサンプルプログラムを起動**

```
1.プログラム起動方法
  プロジェクト右クリック→[Run as]→[Run on Server]

  Run On Server 画面
  ---------------
  "Tomcat 8.0 Server at localhost" を選択 → [Next]
  "bookmgr" が [Configerd] に入っていることを確認して [Finish]
  ---------------

（注意)上記画面の[Always use this server when running this project]オプションは入れないほうが吉

2.プログラム起動確認
  Eclipseの内部ブラウザが立上り、以下のURLでHello worldが表示されれば完了
  "http://localhost:8080/bookmgr/"
```

![Hello Spring Image](/images/step1-1.png "Hello Spring Image")
