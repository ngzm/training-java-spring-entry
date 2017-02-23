package jp.sample.bookmgr.biz.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.sample.bookmgr.biz.domain.Book;

@Repository		// DAOとしてDI可能というアノテーションを宣言
public class AddBookDaoSpringJdbc implements AddBookDao {

	/**
	 * JDBC制御クラス
	 */
	@Autowired		// インジェクション
	private NamedParameterJdbcTemplate template;
	
	/**
	 * 指定された書籍データをデータベースに登録して永続化するメソッド
	 * 
	 * @param book 書籍情報
	 * @return int 登録された書籍データ数
	 */ 
	@Override
	public int addBook(Book book) throws DataAccessException {
		
		// 書籍登録SQLのプレースホルダーを設定
		MapSqlParameterSource pmap = new MapSqlParameterSource();
		pmap.addValue("ISBN", book.getIsbn());
		pmap.addValue("NAME", book.getName());
		pmap.addValue("PRICE", book.getPrice());

		// 書籍登録SQL文
		String sql = "INSERT INTO book (isbn, name, price) VALUES (:ISBN, :NAME, :PRICE)";

		// データベースに書籍を登録する
		int count = template.update(sql, pmap);
		return count;
	}
}
