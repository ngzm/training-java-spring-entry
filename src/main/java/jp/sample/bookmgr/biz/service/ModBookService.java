package jp.sample.bookmgr.biz.service;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍変更サービスインターフェース
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
public interface ModBookService {
	/**
	 * 書籍登録サービスを実行する
	 * 
	 * @param Book 書籍情報
	 */ 
	public void modifyBook(Book book) throws Exception ;
}
