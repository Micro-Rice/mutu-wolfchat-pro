package com.mutuChat.wolfkill.vo;

public class PlayerInfoVo {
	private Long num = (long)-1;
	private String name;
	private Long telephone;
	private Integer point = 0;
	private Integer pointMax = 0;
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTelephone() {
		return telephone;
	}
	public void setTelephone(Long telephone) {
		this.telephone = telephone;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public Integer getPointMax() {
		return pointMax;
	}
	public void setPointMax(Integer pointMax) {
		this.pointMax = pointMax;
	}
	
}
