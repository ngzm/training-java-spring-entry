package jp.sample.bookmgr.biz.service;

import jp.sample.bookmgr.biz.domain.Book;

public interface GetBookService {
	/**
	 * 書籍取得サービス
	 * @param 書籍ID
	 * @return 書籍情報
	 */ 
	public Book getBook(int bookId) throws Exception;
}
