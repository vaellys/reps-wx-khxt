package com.reps.khxt.vo;

import java.io.Serializable;

public class UserVo implements Serializable{
	
	private static final long serialVersionUID = 3866408482163342517L;

	private String id;
	
	private String userIdentity;
	
	private String name;
	
	private String sex;
	
	private String organizeName;
	
	private String parentXpath;
	
	public UserVo(String id, String userIdentity, String name, String sex, String organizeName, String parentXpath) {
		super();
		this.id = id;
		this.userIdentity = userIdentity;
		this.name = name;
		this.sex = sex;
		this.organizeName = organizeName;
		this.parentXpath = parentXpath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getOrganizeName() {
		return organizeName;
	}

	public void setOrganizeName(String organizeName) {
		this.organizeName = organizeName;
	}

	public String getParentXpath() {
		return parentXpath;
	}

	public void setParentXpath(String parentXpath) {
		this.parentXpath = parentXpath;
	}
	
}
