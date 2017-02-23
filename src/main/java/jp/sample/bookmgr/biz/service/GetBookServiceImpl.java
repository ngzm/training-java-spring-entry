package jp.sample.bookmgr.biz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.sample.bookmgr.biz.dao.GetBookDao;
import jp.sample.bookmgr.biz.domain.Book;

@Service // サービスクラスとしてDI可能というアノテーションを宣言
public class GetBookServiceImpl implements GetBookService {
	@Autowired
	GetBookDao getBookDao;

	@Override
	public Book getBook(int bookId) throws Exception {
		return getBookDao.getBook(bookId);
	}
}
