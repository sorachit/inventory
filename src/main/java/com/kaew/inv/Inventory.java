package com.kaew.inv;

import java.io.Serializable;

public class Inventory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2402972037514247919L;
	private String id;
	private String inventoryName;
	private String userName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInventoryName() {
		return inventoryName;
	}
	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
