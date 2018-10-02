package com.wjyup.coolq.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * CoolQ图片信息
 * 
 * @author WJY
 */
@Getter
@Setter
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
}
