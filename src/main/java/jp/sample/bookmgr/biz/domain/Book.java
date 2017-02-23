package jp.sample.bookmgr.biz.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 書籍クラス
 * @author 長住@NTT-AT
 * @version 1.0
 */
public class Book {

	/**
	 * 書籍ID
	 */
	private int id = 0;
	
	/**
	 * 書籍ISBN
	 */
	@NotBlank
	@Size(min=10, max=16)
	private String isbn = "";
	
	/**
	 * 書籍名
	 */
	@NotBlank
	private String name = "";
	
	/**
	 * 書籍価格
	 */
	@NotNull
	private int price = 0;
	
	/**
	 * 書籍IDゲッタ・セッタ
	 */
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	/**
	 *  ISBNゲッタ・セッタ
	 */
	public String getIsbn() { return isbn; }
	public void setIsbn(String isbn) { this.isbn = isbn; }

	/**
	 *  書籍名ゲッタ・セッタ
	 */
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	/**
	 *  価格ゲッタ・セッタ
	 */
	public int getPrice() { return price; }
	public void setPrice(int price) { this.price = price; }
}