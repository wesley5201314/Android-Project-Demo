package com.example.domain;
/**
 * @author wesley
 * @version 
 * @date 2015年1月30日 下午5:39:15
 * 
 */
public class Video {
	
	private int id;
	private String name;
	private int timelength;
	
	public Video(){
		
	}
	
	
	public Video(int id, String name, int timelength) {
		this.id = id;
		this.name = name;
		this.timelength = timelength;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", name=" + name + ", timelength="
				+ timelength + "]";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTimelength() {
		return timelength;
	}
	public void setTimelength(int timelength) {
		this.timelength = timelength;
	}
}
