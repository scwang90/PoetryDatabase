package com.poetry.database.model;

import com.poetry.database.annotations.dbmodel.Column;
import com.poetry.database.annotations.dbmodel.Id;
import com.poetry.database.annotations.dbmodel.Table;

@Table("t_poetry")
public class Poetry {

	@Id("d_num")
	private int id;

	@Column("d_title")
	private String title;

	@Column("d_author")
	private String author;

	@Column("d_poetry")
	private String poetry;

	@Column("d_intro")
	private String intro;

	
	public Poetry() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPoetry() {
		return poetry;
	}

	public void setPoetry(String poetry) {
		this.poetry = poetry;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

}
