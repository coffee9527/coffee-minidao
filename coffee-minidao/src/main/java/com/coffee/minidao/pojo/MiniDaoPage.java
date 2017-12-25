package com.coffee.minidao.pojo;

import java.util.List;

public class MiniDaoPage<T> {
	// 当前页面
	private int page;
	// 每页显示记录数
	private int rows;
	// 总行数
	private int total;
	// 总页数
	private int pages;
	// 结果集
	private List<T> results;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public List<T> getResults() {
		return results;
	}
	public void setResults(List<T> results) {
		this.results = results;
	}
	
}
