package jp.sample.bookmgr.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.sample.bookmgr.biz.domain.Book;
import jp.sample.bookmgr.biz.service.AddBookService;
import jp.sample.bookmgr.biz.service.GetBookService;
import jp.sample.bookmgr.biz.service.ListBookService;
import jp.sample.bookmgr.biz.service.ModBookService;

/**
 * 書籍管理処理コントローラクラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Controller // ”コントローラとしてDI可能" というアノテーションを宣言
public class BookController {

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory.getLogger(BookController.class);
	
	/**
	 *  書籍一覧処理を実装したビジネスロジックサービス
	 */
	@Autowired		// ListBookServiceオブジェクトをインジェクション
	ListBookService listBookService;

	@Autowired		// インジェクション
	AddBookService addBookService;
	
	@Autowired
	GetBookService getBookService;
	
	@Autowired
	ModBookService modBookService;

	/**
	 * 書籍一覧画面コントローラ
	 * 
	 * @param model Spring MVC Modelオブジェクト
	 * @return 画面JSP名
	 */ 
	@RequestMapping(value = "/listbook", method = RequestMethod.GET)
	public String listBook(Model model) throws Exception {
		
		logger.debug("listBook() start");
		
		// 書籍一覧取得ロジック処理
		List<Book> books = listBookService.getBookList();

		// 書籍一覧情報をモデルに登録
		model.addAttribute("books", books);

		// 画面表示に listbook.jsp を呼び出す
		return "listbook";
	}
	
	/**
	 * 書籍登録フォーム画面コントローラ
	 * 
	 * @return 画面JSP名
	 */ 
	@RequestMapping(value = "/addbookform", method = RequestMethod.GET)
	public String addBookForm(Model model) throws Exception {
		
		logger.debug("addbookform() start");
		
		// 空の書籍情報をモデルに登録
		Book book = new Book();
		model.addAttribute("book", book);
		
		// 画面表示に addbookform.jsp を呼び出す
		return "addbookform";
	}
	
	/**
	 * 書籍登録処理コントローラ
	 * 	
	 * @param book Spring MVC ModelオブジェクトからバインドされたBookインスタンス
	 * @param result バインディング処理時のバリデーションチェック結果
	 * @return 登録結果表示JSP名
	 */ 
	@RequestMapping(value = "/addbook", method = RequestMethod.POST)
	public String addBook(@Valid @ModelAttribute("book") Book book,
			BindingResult result) throws Exception {
		
		logger.debug("addbook() start");
		logger.debug("book isbn:" + book.getIsbn());
		logger.debug("book name:" + book.getName());
		logger.debug("book price:" + book.getPrice());
		
		// Validation エラーが発生しているときは登録フォームに戻る
		if(result.hasErrors()){
			return "addbookform";
		}
		
		// 書籍情報登録処理
		addBookService.addBook(book);
		
		// 登録結果画面 result.jsp を呼び出す
		return "redirect:result";
	}
	
	/**
	 * 書籍変更フォーム画面コントローラ
	 * 
	 * @return 画面JSP名
	 */ 
	@RequestMapping(value = "/modbookform", method = RequestMethod.GET)
	public String modBookForm(@RequestParam("id") Integer bookId, Model model)
			throws Exception {
		
		logger.debug("modbookform() start");
		
		// パラメータで取得したIDから、該当書籍の情報を取得
		Book book = getBookService.getBook(bookId);
		model.addAttribute("book", book);
		
		// 画面表示に modbookform.jsp を呼び出す
		return "modbookform";
	}
	
	/**
	 * 書籍変更処理コントローラ
	 * 	
	 * @param book Spring MVC ModelオブジェクトからバインドされたBookインスタンス
	 * @param result バインディング処理時のバリデーションチェック結果
	 * @return 登録結果表示JSP名
	 */ 
	@RequestMapping(value = "/modbook", method = RequestMethod.POST)
	public String modBook(@Valid @ModelAttribute("book") Book book,
			BindingResult result) throws Exception {
		
		logger.debug("modbook() start");
		logger.debug("book id:" + book.getId());
		logger.debug("book isbn:" + book.getIsbn());
		logger.debug("book name:" + book.getName());
		logger.debug("book price:" + book.getPrice());
		
		// Validation エラーが発生しているときは登録フォームに戻る
		if(result.hasErrors()){
			return "modbookform";
		}
		
		// 書籍情報登録処理
		modBookService.modifyBook(book);
		
		// 登録結果画面 result.jsp を呼び出す
		return "redirect:result";
	}
	
	/**
	 * 処理結果画面表示コントローラ
	 * 
	 * @return 登録結果表示JSP名
	 */
	@RequestMapping(value = "/result", method = RequestMethod.GET)
	public String result() throws Exception {
		logger.info("result START");
		return "result";
	}
}
