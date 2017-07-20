package com.mutuChat.wolfkill.model;
// Generated 2017-7-14 21:18:53 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WolfKillChatUserInfo generated by hbm2java
 */
@Entity
@Table(name = "wolf_kill_chat_user_info")
public class WolfKillChatUserInfo implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3191487298392858158L;
	private int id;
	private Integer playerId;
	private String playerPhone;
	private String openId;
	private String openImg;

	public WolfKillChatUserInfo() {
	}

	public WolfKillChatUserInfo(int id) {
		this.id = id;
	}

	public WolfKillChatUserInfo(int id, Integer playerId, String playerPhone, String openId,
			String openImg) {
		this.id = id;
		this.playerId = playerId;
		this.playerPhone = playerPhone;
		this.openId = openId;
		this.openImg = openImg;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "PLAYER_ID")
	public Integer getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	@Column(name = "PLAYER_PHONE")
	public String getPlayerPhone() {
		return this.playerPhone;
	}

	public void setPlayerPhone(String playerPhone) {
		this.playerPhone = playerPhone;
	}

	@Column(name = "OPEN_ID", length = 50)
	public String getOpenId() {
		return this.openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Column(name = "OPEN_IMG", length = 200)
	public String getOpenImg() {
		return this.openImg;
	}

	public void setOpenImg(String openImg) {
		this.openImg = openImg;
	}

}
