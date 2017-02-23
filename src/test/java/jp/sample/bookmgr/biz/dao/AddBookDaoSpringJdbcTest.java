package jp.sample.bookmgr.biz.dao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jp.sample.bookmgr.biz.domain.Book;


/**
 * 書籍登録データアクセス実装クラステストケース
 * 		
 * @author 長住@NTT-AT
 * @version 1.0
 */

// SpringによるJunitテストランナー
@RunWith(SpringJUnit4ClassRunner.class)

//Spring定義ファイルの取り込み
@ContextConfiguration("classpath:test-context-biz.xml")

public class AddBookDaoSpringJdbcTest {
	/**
	 * NamedParameterJdbcTemplate オブジェクトのモック
	 */
	@Mock(name="template")
	private NamedParameterJdbcTemplate mockJdbc;
	
	/**
	 * テスト対象となる AddBookDao クラス
	 */
	@InjectMocks
	@Autowired
	private AddBookDao addBookDao;
	
	/**
	 * モック化のおまじない
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * addBookDao.addBook() のテストケース 正常系その１
	 * NamedParameterJdbcTemplate の update メソッドリターン値による挙動確認
	 */
	@Test
	public void testAddBook_normal_1() {
		Book book1 = new Book();
		book1.setIsbn("978-4-7741-5380-3");
		book1.setName("Spring3入門");
		book1.setPrice(3900);

		MapSqlParameterSource pmap = new MapSqlParameterSource();
		pmap.addValue("ISBN", book1.getIsbn());
		pmap.addValue("NAME", book1.getName());
		pmap.addValue("PRICE", book1.getPrice());
		
		String sql = "INSERT INTO book (isbn, name, price) VALUES (:ISBN, :NAME, :PRICE)";

		when(mockJdbc.update(eq(sql), refEq(pmap))).thenReturn(1);
		assertThat(addBookDao.addBook(book1), CoreMatchers.is(1));
	}

	/**
	 * addBookDao.addBook() のテストケース 正常系その２
	 * パラメータが正しく NamedParameterJdbcTemplate の update メソッドにわたっているか？
	 */
	@Test
	public void testAddBook_normal_2() {
		Book book1 = new Book();
		book1.setId(1);
		book1.setIsbn("978-4-7741-5380-3");
		book1.setName("Spring3入門");
		book1.setPrice(3900);

		MapSqlParameterSource pmap = new MapSqlParameterSource();
		pmap.addValue("ISBN", book1.getIsbn());
		pmap.addValue("NAME", book1.getName());
		pmap.addValue("PRICE", book1.getPrice());

		String sql = "INSERT INTO book (isbn, name, price) VALUES (:ISBN, :NAME, :PRICE)";
		
		addBookDao.addBook(book1);
		verify(mockJdbc, times(1)).update(eq(sql), refEq(pmap));
	}
	
	/**
	 * addBookDao.addBook() のテストケース 正常系その３
	 * パラメータが正しく NamedParameterJdbcTemplate の update メソッドにわたっているか？
	 */
	@Test
	public void testAddBook_normal_3() {
		Book book1 = new Book();
		book1.setIsbn("978-4-7741-5380-3");
		book1.setName("Spring3入門");
		book1.setPrice(3900);

		MapSqlParameterSource pmap = new MapSqlParameterSource();
		pmap.addValue("ISBN", "978-4-7741-5380-4");
		pmap.addValue("NAME", "Spring4入門");
		pmap.addValue("PRICE", 4900);
		
		String sql = "INSERT INTO book (isbn, name, price) VALUES (:ISBN, :NAME, :PRICE)";
		
		addBookDao.addBook(book1);
		verify(mockJdbc, never()).update(eq(sql), refEq(pmap));
	}

	/**
	 * addBookDao.addBook() のテストケース 異常系その１
	 * 例外発生時の挙動確認
	 */
	@SuppressWarnings("serial")
	@Test(expected=DataAccessException.class)
	public void testAddBook_abnormal_1() {
		Book book1 = new Book();
		book1.setId(1);
		book1.setIsbn("978-4-7741-5380-3");
		book1.setName("Spring3入門");
		book1.setPrice(3900);

		// AddBookDao#addBook
		when(mockJdbc.update(anyString(), (MapSqlParameterSource)anyObject())).thenThrow(new DataAccessException(""){});
		addBookDao.addBook(book1);
	}
}
