package com;

import java.io.Serializable;

public class Param implements Serializable{
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Param create(String name){
		Param param = new Param();
		param.setName(name);
		return param;
	}
}
