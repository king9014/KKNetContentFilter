package cn.dreamfield.model;

import java.util.Date;

public class NetInfo {
	private Long infoId;
	private String infoName;
	private String infoWebsite;
	private String infoCategory;
	private String infoHtmlUrl;
	private String infoOriginUrl;
	private String infoImgUrl;
	private Date infoDate;
	private Date infoDateOpt;
	private String infoIntro;
	private Long infoCid;
	private String infoStatus;
	public Long getInfoCid() {
		return infoCid;
	}
	public void setInfoCid(Long infoCid) {
		this.infoCid = infoCid;
	}
	public String getInfoStatus() {
		return infoStatus;
	}
	public void setInfoStatus(String infoStatus) {
		this.infoStatus = infoStatus;
	}
	public Long getInfoId() {
		return infoId;
	}
	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}
	public String getInfoName() {
		return infoName;
	}
	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}
	public String getInfoWebsite() {
		return infoWebsite;
	}
	public void setInfoWebsite(String infoWebsite) {
		this.infoWebsite = infoWebsite;
	}
	public String getInfoCategory() {
		return infoCategory;
	}
	public void setInfoCategory(String infoCategory) {
		this.infoCategory = infoCategory;
	}
	public String getInfoHtmlUrl() {
		return infoHtmlUrl;
	}
	public void setInfoHtmlUrl(String infoHtmlUrl) {
		this.infoHtmlUrl = infoHtmlUrl;
	}
	public String getInfoOriginUrl() {
		return infoOriginUrl;
	}
	public void setInfoOriginUrl(String infoOriginUrl) {
		this.infoOriginUrl = infoOriginUrl;
	}
	public String getInfoImgUrl() {
		return infoImgUrl;
	}
	public void setInfoImgUrl(String infoImgUrl) {
		this.infoImgUrl = infoImgUrl;
	}
	public Date getInfoDate() {
		return infoDate;
	}
	public void setInfoDate(Date infoDate) {
		this.infoDate = infoDate;
	}
	public String getInfoIntro() {
		return infoIntro;
	}
	public void setInfoIntro(String infoIntro) {
		this.infoIntro = infoIntro;
	}
	public Date getInfoDateOpt() {
		return infoDateOpt;
	}
	public void setInfoDateOpt(Date infoDateOpt) {
		this.infoDateOpt = infoDateOpt;
	}
	
}
