package jp.sample.bookmgr.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 書籍管理メイン画面コントローラクラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Controller
public class MainController {
	
	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	/**
	 * ログインフォーム画面コントローラ
	 *	
	 * @return 画面JSP名
	 */
	@RequestMapping(value = "/loginform", method = RequestMethod.GET)
	public String loginForm() {
		// 画面表示にloginform.jsp を呼び出す
		return "loginform";
	}
	
	/**
	 * 権限エラー画面コントローラ
	 *
	 * @return 画面JSP名
	 */
	@RequestMapping(value = "/autherror", method = RequestMethod.GET)
	public String authError() {
		return "error/autherror";
	}
	
	/**
	 * 書籍管理メイン画面コントローラ
	 * 
	 * @return 画面JSP名
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main() throws Exception {
		
		logger.debug("main() start");
		
		// 画面表示に main.jsp を呼び出す
		return "main";
	}
}
