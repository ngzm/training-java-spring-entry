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
