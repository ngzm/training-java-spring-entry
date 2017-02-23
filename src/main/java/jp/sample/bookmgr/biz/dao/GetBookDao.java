package jp.sample.bookmgr.biz.dao;

import org.springframework.dao.DataAccessException;

import jp.sample.bookmgr.biz.domain.Book;

public interface GetBookDao {
	public Book getBook(int bookId) throws DataAccessException;
}
