package jp.sample.bookmgr.biz.dao;

import org.springframework.dao.DataAccessException;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍登録データアクセスクラスインターフェース
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
public interface AddBookDao {
	/**
	 * 指定された書籍データをデータベースに登録して永続化するメソッド
	 * 	
	 * @param Book 書籍情報
	 * @return int 登録された書籍データ数
	 */ 
	public int addBook(Book book) throws DataAccessException;
}
