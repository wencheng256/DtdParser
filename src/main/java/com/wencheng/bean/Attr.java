package com.wencheng.bean;

public class Attr {
	
	private String name;
	private boolean required;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
	public String toString(){
		return "name:"+name+" required?:"+required;
	}
}
