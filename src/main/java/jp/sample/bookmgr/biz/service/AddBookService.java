package jp.sample.bookmgr.biz.service;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍登録サービスインターフェース
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
public interface AddBookService {
	/**
	 * 書籍登録サービスを実行する
	 * 
	 * @param Book 書籍情報
	 */ 
	public void addBook(Book book) throws Exception ;
}
