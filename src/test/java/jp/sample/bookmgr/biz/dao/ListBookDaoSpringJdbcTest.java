package jp.sample.bookmgr.biz.dao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jp.sample.bookmgr.biz.domain.Book;

/**
 * 書籍一覧データアクセス実装クラステストケース
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */

// SpringによるJunitテストランナー
@RunWith(SpringJUnit4ClassRunner.class)

// Spring定義ファイルの取り込み
@ContextConfiguration("classpath:test-context-biz.xml")

public class ListBookDaoSpringJdbcTest {

	/**
	 * JdbcTemplate オブジェクトのモック
	 */
	@Mock(name="template")
	private JdbcTemplate mockJdbc;
	
	/**
	 * テスト対象となる ListBookDao クラス
	 */
	@InjectMocks
	@Autowired
	private ListBookDao listBookDao;

	/**
	 * モック化のおまじない
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * ListBookDao.getBookList() のテストケース 正常系その１
	 * 取得データが正しく返却されているかを検証
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetBookList_normal_1() {
		List<Book> expected = new ArrayList<Book>();
		Book book1 = new Book();
		book1.setId(1);
		book1.setIsbn("978-4-7741-5380-3");
		book1.setName("Spring3入門");
		book1.setPrice(3900);
		expected.add(book1);
		String sql = "SELECT id, isbn, name, price FROM book ORDER BY id";

		// JdbcTemplate#queryの戻り値をMock動作に差替え
		when(mockJdbc.query(eq(sql), (RowMapper<Book>)anyObject())).thenReturn(expected);
		
		List<Book> actual = listBookDao.getBookList();
		assertThat(actual, CoreMatchers.is(expected));
	}
	
	/**
	 * ListBookDao.getBookList() のテストケース 異常系その１
	 * 例外発生時の挙動確認
	 */
	@SuppressWarnings({ "unchecked", "serial" })
	@Test(expected=DataAccessException.class)
	public void testGetBookList_abnormal_1() {
		// ListBookDao#getList
		when(mockJdbc.query(anyString(), any(RowMapper.class))).thenThrow(new DataAccessException(""){});
		listBookDao.getBookList();
	} 
}
