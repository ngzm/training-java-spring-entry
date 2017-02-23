package jp.sample.bookmgr.biz.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧データアクセスクラスインターフェース
 * @author	 長住@NTT-AT
 * @version 1.0
 */
public interface ListBookDao {

	/**
	 * 書籍データベースから書籍一覧データを取得するメソッド
	 * @return 書籍一覧情報
	 */ 
	public List<Book> getBookList() throws DataAccessException;
}
