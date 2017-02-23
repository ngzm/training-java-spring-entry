package jp.sample.bookmgr.biz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.sample.bookmgr.biz.dao.ModBookDao;
import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍更新サービス実装クラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Service	// サービスクラスとしてDI可能というアノテーションを宣言
public class ModBookServiceImpl implements ModBookService {
	/**
	 * データベース書籍更新を行うDAOクラス
	 */
	@Autowired		// インジェクション
	private ModBookDao modBookDao;
	
	/**
	 * 書籍更新サービスを実行する
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
	public void modifyBook(Book book) throws Exception {
		// 書籍更新を行うDAOクラスを使用して書籍情報を永続化する
		modBookDao.modifyBook(book);
		
		// トランザクションのテストのため5秒スリープする
		Thread.sleep(5000);
	}
}
