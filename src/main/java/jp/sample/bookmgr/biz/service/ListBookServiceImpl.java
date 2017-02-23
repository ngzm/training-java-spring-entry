package jp.sample.bookmgr.biz.service;

// import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.sample.bookmgr.biz.dao.ListBookDao;
import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧サービス実装クラス
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Service // サービスクラスとしてDI可能というアノテーションを宣言
public class ListBookServiceImpl implements ListBookService {

	/**
	 *  書籍一覧取得DAO
	 */
	@Autowired		// ListBookDaoオブジェクトをインジェクション
	ListBookDao listBookDao;

	/**
	 * 書籍一覧取得サービス
	 * @return 書籍一覧情報
	 */ 
	@Override
	@Transactional(readOnly=true)
	public List<Book> getBookList() throws Exception {
		// 書籍一覧を取得
		return listBookDao.getBookList();
	}
}
