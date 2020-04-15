package com.cqi.hr.entity;

import java.util.Collections;
import java.util.List;

public class PagingList<T> {
	@SuppressWarnings("rawtypes")
	public static final PagingList EMPTY_LIST = new PagingList<Object>();
	public static <T> PagingList<T> createOnePagePagingList(List<T> datas) {
		PagingList<T> p = new PagingList<T>();
		p.setCurrentPage(1);
		p.setDatas(datas);
		p.setPageSize(datas.size());
		p.setTotalPages(1);
		p.setTotalRecords(datas.size());
		return p;
	}
	
	private int pageSize     = 0;
	private int currentPage  = 0;
	private int totalPages   = 0;
	private int totalRecords = 0;
	private List<T> datas;
	
	public int getPageSize() {
		return pageSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	@SuppressWarnings("unchecked")
	public List<T> getDatas() {
		if(datas==null) return Collections.EMPTY_LIST;
		return datas;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}	
}
