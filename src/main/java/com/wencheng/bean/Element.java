package com.wencheng.bean;

import com.wencheng.bean.ChildNode;

public class Element {
	
	private String name;
	private ChildNode[] sons;
	private boolean used = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ChildNode[] getSons() {
		return sons;
	}

	public void setSons(ChildNode[] sons) {
		this.sons = sons;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("name:"+name) ;
		sb.append("  sons:");
		for(int i = 0; i<sons.length; i++){
			sb.append(" ");
			sb.append(sons[i]);
		}
		return sb.toString();
		
	}
}
