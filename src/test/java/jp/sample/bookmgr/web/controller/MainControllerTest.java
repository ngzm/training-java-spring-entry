package jp.sample.bookmgr.web.controller;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


/**
 * Mainコントローラクラスのテストケース
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */

// SpringによるJunitテストランナー
@RunWith(SpringJUnit4ClassRunner.class)

// WebApplicationContext をロードした環境下でテストコードを実行できるおまじない
@WebAppConfiguration

// Spring定義ファイルの取り込み
@ContextConfiguration({"classpath:test-context-biz.xml", "classpath:test-context-web.xml"})

public class MainControllerTest {
	/**
	 * WebApplicationContext
	 */
	@Autowired
	private WebApplicationContext wac;

	/**
	 * Spring MVCのテストをするためのMockMvcクラス
	 */
	private MockMvc mockMvc;

	/**
	 * 事前のおまじない
	 * MockMvcクラスインスタンスを取得
	 */
	@Before
	public void setup() {
		// MockMvcクラスインスタンスを取得
		mockMvc = webAppContextSetup(wac).build();
	}

	/**
	 * メイン画面表示処理のテスト:正常系その１
	 * GETリクエストを模倣し返って来たHTTPステータスコードとビューの名前をチェック
	 */
	@Test
	public void testMain_normal_1() throws Exception {
		// メイン画面表示のGETリスクエスト発行
		mockMvc.perform(get("/main"))
			.andExpect(status().isOk())
			.andExpect(view().name("main"));
	}
}