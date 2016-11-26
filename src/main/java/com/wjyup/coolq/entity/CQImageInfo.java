package com.wjyup.coolq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * CoolQ图片信息
 * 
 * @author WJY
 */
public class CQImageInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String md5;// 图片文件的md5值
	private Integer width;// 图片宽度
	private Integer height;// 图片高度
	private Long size;// 图片文件大小,单位b
	private String url;// 图片的网络下载地址
	private Date addtime;// 图片上传至腾讯服务器的时间

	public CQImageInfo() {
	}

	public CQImageInfo(String md5, Integer width, Integer height, Long size, String url, Date addtime) {
		super();
		this.md5 = md5;
		this.width = width;
		this.height = height;
		this.size = size;
		this.url = url;
		this.addtime = addtime;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

}
