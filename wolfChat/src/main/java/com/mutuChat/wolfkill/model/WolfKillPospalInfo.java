package com.mutuChat.wolfkill.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "wolf_kill_pospal_info")
public class WolfKillPospalInfo implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4389540043307075755L;
	private long customerUid;
	private String categoryName;
	private String number;
	private String name;
	private Float point;
	private Float discount;
	private Float balance;
	private String phone;
	private String birthday;
	private String qq;
	private String email;
	private String address;
	private String createdDate;
	private Integer onAccount;
	private Integer enable;

	public WolfKillPospalInfo() {
	}

	public WolfKillPospalInfo(String categoryName, String number, String name, Float point, Float discount,
			Float balance, String phone, String birthday, String qq, String email, String address, String createdDate,
			Integer onAccount, Integer enable) {
		this.categoryName = categoryName;
		this.number = number;
		this.name = name;
		this.point = point;
		this.discount = discount;
		this.balance = balance;
		this.phone = phone;
		this.birthday = birthday;
		this.qq = qq;
		this.email = email;
		this.address = address;
		this.createdDate = createdDate;
		this.onAccount = onAccount;
		this.enable = enable;
	}

	@Id
	@Column(name = "CUSTOMER_UID", unique = true, nullable = false)
	public long getCustomerUid() {
		return this.customerUid;
	}

	public void setCustomerUid(long customerUid) {
		this.customerUid = customerUid;
	}

	@Column(name = "CATEGORY_NAME", length = 20)
	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Column(name = "NUMBER")
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "NAME", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "POINT")
	public Float getPoint() {
		return this.point;
	}

	public void setPoint(Float point) {
		this.point = point;
	}

	@Column(name = "DISCOUNT")
	public Float getDiscount() {
		return this.discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	@Column(name = "BALANCE", precision = 10)
	public Float getBalance() {
		return this.balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	@Column(name = "PHONE", length = 15)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "BIRTHDAY", length = 20)
	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Column(name = "QQ", length = 10)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "EMAIL", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "ADDRESS", length = 150)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "CREATED_DATE", length = 100)
	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "ON_ACCOUNT")
	public Integer getOnAccount() {
		return this.onAccount;
	}

	public void setOnAccount(Integer onAccount) {
		this.onAccount = onAccount;
	}

	@Column(name = "ENABLE")
	public Integer getEnable() {
		return this.enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}
}
