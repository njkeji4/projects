package com.shicha.yzmgt.domain;

public class SearchAdv {
	
	Long betrween1;
	Long betrween2;
	
	Integer page = 0;
	Integer size = 10;
	String sort="createTime";
	String order = "desc";//"asc"
	
	Integer requested;
	
	Integer advType;

	public Long getBetrween1() {
		return betrween1;
	}

	public void setBetrween1(Long betrween1) {
		this.betrween1 = betrween1;
	}

	public Long getBetrween2() {
		return betrween2;
	}

	public void setBetrween2(Long betrween2) {
		this.betrween2 = betrween2;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getRequested() {
		return requested;
	}

	public void setRequested(Integer requested) {
		this.requested = requested;
	}

	public Integer getAdvType() {
		return advType;
	}

	public void setAdvType(Integer advType) {
		this.advType = advType;
	}

}
