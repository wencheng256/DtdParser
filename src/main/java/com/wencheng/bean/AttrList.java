package com.wencheng.bean;

import com.wencheng.bean.Attr;

import java.util.List;

public class AttrList {
	
	private String parent;
	private List<Attr> list;


	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public List<Attr> getList() {
		return list;
	}

	public void setList(List<Attr> list) {
		this.list = list;
	}
}
