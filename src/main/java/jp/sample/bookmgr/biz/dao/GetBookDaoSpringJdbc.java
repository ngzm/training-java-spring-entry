package jp.sample.bookmgr.biz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.sample.bookmgr.biz.domain.Book;

@Repository
public class GetBookDaoSpringJdbc implements GetBookDao {
	
	@Autowired
	private JdbcTemplate template;

	@Override
	public Book getBook(int bookId) throws DataAccessException {
		
		class BookRowMapper implements RowMapper<Book> {
			@Override
			public Book mapRow(ResultSet result, int row) throws SQLException {
				Book book = new Book();
				book.setId(result.getInt("ID"));
				book.setIsbn(result.getString("ISBN"));
				book.setName(result.getString("NAME"));
				book.setPrice(result.getInt("PRICE"));
				return book;
			}
		}
		// DBマッピングクラスオブジェクト取得
		RowMapper<Book> rowMapper = new BookRowMapper();

		// 書籍情報検索SQL
		String sql = "SELECT id, isbn, name, price FROM book WHERE id = ?";

		// DBを検索して書籍情報リスト取得
		Book book = template.queryForObject(sql, rowMapper, bookId);
		return book;
	}
}
