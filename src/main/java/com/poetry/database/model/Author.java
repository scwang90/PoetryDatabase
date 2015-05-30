package com.poetry.database.model;

import com.poetry.database.annotations.dbmodel.Column;
import com.poetry.database.annotations.dbmodel.Table;

@Table("t_author")
public class Author {

	@Column("d_num")
	private int id;

	@Column("d_author")
	private String author;

	@Column("d_intro")
	private String intro;

	@Column("d_intro2")
	private String intro2;

	@Column("d_dynasty")
	private String dynasty;
	
	public Author() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getIntro2() {
		return intro2;
	}

	public void setIntro2(String intro2) {
		this.intro2 = intro2;
	}

	public String getDynasty() {
		return dynasty;
	}

	public void setDynasty(String dynasty) {
		this.dynasty = dynasty;
	}
	
}
