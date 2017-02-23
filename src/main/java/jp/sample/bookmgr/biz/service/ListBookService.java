package jp.sample.bookmgr.biz.service;

import java.util.List;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧サービスクラスインターフェース
 * @author 長住@NTT-AT
 * @version 1.0
 */
public interface ListBookService {
	/**
	 * 書籍一覧取得サービス
	 * @return 書籍一覧情報
	 */ 
	public List<Book> getBookList() throws Exception;
}
