package jp.sample.bookmgr.web.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jp.sample.bookmgr.biz.domain.Book;


/**
 * Bookコントローラクラスのテストケース
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

public class BookControllerTest {
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
	 * 書籍一覧画面表示処理のテスト:正常系その１
	 * GETリクエストを模倣し返って来たHTTPステータスコードとビューの名前をチェック
	 * DBに規定データが登録されていることを前提にDBから取得したデータが正しいかもチェック
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testListBook_normal_1() throws Exception {
		// 書籍一覧表示画面のGETリスクエスト発行
		ResultActions retact = mockMvc.perform(get("/listbook"))
			.andExpect(status().isOk())			// HTTPステータスコード
			.andExpect(view().name("listbook"));		// ビュー名

		// DBから読み込んだ書籍情報リストを取り出す
		MvcResult mvcResult = retact.andReturn();
		ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
		List<Book> booksList = (List<Book>)modelMap.get("books");
		assertThat(booksList, CoreMatchers.is(CoreMatchers.not(CoreMatchers.nullValue())));
		
		// DBに ID=1、ISBN="123456789abcdefgh" NAME="JavaScript" PRICE=1200 
		// のデータが登録されていることが前提のテストケース
		Book book1 = booksList.get(0);
		assertThat(book1.getId(), CoreMatchers.is(1));
		assertThat(book1.getIsbn(), CoreMatchers.is("123456789abcdefgh"));
		assertThat(book1.getName(), CoreMatchers.is("JavaScript"));
		assertThat(book1.getPrice(), CoreMatchers.is(1200));
	}
	
	/**
	 * 書籍登録処理のテスト:正常系その１
	 * 書籍フォームからのPOSTリクエストを模倣し実際にデータベースに登録させる。
	 * また、返って来たHTTPステータスコードとビューの名前をチェックする。
	 * @throws Exception
	 */
	@Test
	public void testAddBook_normal_1() throws Exception {
		
		// テストで登録する書籍情報を設定
		Book book = new Book();
		book.setIsbn("TEST-1234567890");
		book.setName("testAddBookで自動登録しました");
		book.setPrice(8888);

		// 書籍登録POSTリクエスト発行
		mockMvc.perform(post("/addbook")
				.param("isbn", book.getIsbn())
				.param("name", book.getName())
				.param("price", new Integer(book.getPrice()).toString())
				)
			.andExpect(status().isFound())				// HTTPステータスコード
			.andExpect(view().name("redirect:result"))	// ビュー名
			.andExpect(model().hasNoErrors());			// エラーがないこと
	}
	
	/**
	 * 書籍登録処理のテスト:異常系その１
	 * 書籍フォームからのPOSTリクエストを模倣するがバリデーションエラーとなるケース。
	 * 返って来たHTTPステータスコードとビューの名前をチェックする。
	 * @throws Exception
	 */
	@Test
	public void testAddBook_abnormal_1() throws Exception {
		
		// テストで登録する書籍情報を設定
		Book book = new Book();
		book.setIsbn("1234");	// @Size 違反
		book.setName("testAddBookで自動登録しました");
		book.setPrice(8888);

		// 書籍登録POSTリクエスト発行
		mockMvc.perform(post("/addbook")
				.param("isbn", book.getIsbn())
				.param("name", book.getName())
				.param("price", new Integer(book.getPrice()).toString())
				)
			.andExpect(status().isOk())							// HTTPステータスコード
			.andExpect(view().name("addbookform"))					// ビュー名
			.andExpect(model().hasErrors())							// エラーが発生していること
			.andExpect(model().errorCount(1))						// エラーの数
			.andExpect(model().attributeExists("book"))				// modelAtribute名
			.andExpect(model().attributeHasFieldErrors("book", "isbn"))	// エラーフィールド名
			;
	}
}
