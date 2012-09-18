package cn.dreamfield.model;

import java.util.Date;

public class NetArticle {

	private Long id;
	private String name;
	private Long pageTotal;
	private Long pageCorrent;
	private Date date;
	private Date optDate;
	private String htmlUrl;
	private String imgUrl;
	private String imgUrlS;
	private String isExist;
	private String originUrl;
	
	public String getOriginUrl() {
		return originUrl;
	}
	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(Long pageTotal) {
		this.pageTotal = pageTotal;
	}
	public Long getPageCorrent() {
		return pageCorrent;
	}
	public void setPageCorrent(Long pageCorrent) {
		this.pageCorrent = pageCorrent;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getOptDate() {
		return optDate;
	}
	public void setOptDate(Date optDate) {
		this.optDate = optDate;
	}
	public String getHtmlUrl() {
		return htmlUrl;
	}
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgUrlS() {
		return imgUrlS;
	}
	public void setImgUrlS(String imgUrlS) {
		this.imgUrlS = imgUrlS;
	}
	public String getIsExist() {
		return isExist;
	}
	public void setIsExist(String isExist) {
		this.isExist = isExist;
	}
}
