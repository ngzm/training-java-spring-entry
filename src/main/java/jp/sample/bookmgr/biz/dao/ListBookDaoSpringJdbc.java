package jp.sample.bookmgr.biz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
// import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧データアクセス実装クラス
 * @author	 長住@NTT-AT
 * @version 1.0
 */
@Repository	// リポジトリ（Dao）クラスとしてDI可能というアノテーションを宣言
public class ListBookDaoSpringJdbc implements ListBookDao {

	/**
	 * JDBC制御クラス
	 */
	@Autowired
	private JdbcTemplate template;

	/**
	 * 書籍データベースから書籍一覧データを取得するメソッド
	 * @return	 書籍一覧情報
	 */ 
	@Override
	public List<Book> getBookList() throws DataAccessException  {
		// DBマッピングインナークラスの定義
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
		String sql = "SELECT id, isbn, name, price FROM book ORDER BY id";
		
		// DBを検索して書籍情報リスト取得
		List<Book> bookList = template.query(sql, rowMapper);
		
		return bookList;
	}
}
