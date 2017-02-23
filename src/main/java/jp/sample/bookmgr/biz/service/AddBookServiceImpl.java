package jp.sample.bookmgr.biz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.sample.bookmgr.biz.dao.AddBookDao;
import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍登録サービス実装クラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Service	// サービスクラスとしてDI可能というアノテーションを宣言
public class AddBookServiceImpl implements AddBookService {
	/**
	 * データベースに書籍登録を行うDAOクラス
	 */
	@Autowired		// インジェクション
	private AddBookDao addBookDao;
	
	/**
	 * 書籍登録サービスを実行する
	 * 
	 * @param Book 書籍情報
	 */ 
	@Override
	@Transactional(
			propagation=Propagation.REQUIRED,
			isolation=Isolation.READ_COMMITTED,
			timeout=10,
			readOnly=false,
			rollbackFor=RuntimeException.class)
	public void addBook(Book book) throws Exception {
		// 書籍登録を行うDAOクラスを使用して書籍情報を永続化する
		addBookDao.addBook(book);
	}
}
