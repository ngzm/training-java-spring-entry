package jp.sample.bookmgr.biz.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.sample.bookmgr.biz.domain.Book;

@Repository		// DAOとしてDI可能というアノテーションを宣言
public class ModdBookDaoSpringJdbc implements ModBookDao {

	/**
	 * JDBC制御クラス
	 */
	@Autowired		// インジェクション
	private NamedParameterJdbcTemplate template;
	
	/**
	 * 変更された書籍データでデータベースの情報を更新する
	 * 
	 * @param book 書籍情報
	 * @return int 更新された書籍データ数
	 */ 
	@Override
	public int modifyBook(Book book) throws DataAccessException {
		
		// 書籍更新SQLのプレースホルダーを設定
		MapSqlParameterSource pmap = new MapSqlParameterSource();
		pmap.addValue("ID", book.getId());
		pmap.addValue("ISBN", book.getIsbn());
		pmap.addValue("NAME", book.getName());
		pmap.addValue("PRICE", book.getPrice());

		// 書籍更新SQL文
		String sql = "UPDATE book SET isbn = :ISBN, name = :NAME, price = :PRICE WHERE id = :ID";

		// データベースの該当書籍情報を更新する
		int count = template.update(sql, pmap);
		return count;
	}
}
