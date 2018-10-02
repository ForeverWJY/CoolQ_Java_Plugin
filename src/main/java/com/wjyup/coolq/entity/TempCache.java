package com.wjyup.coolq.entity;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存实体类
 *
 * @author WJY
 */
public class TempCache implements Serializable {

	private static final long serialVersionUID = 1L;

	//缓存一小时数据
	public static final LoadingCache<String, Object> CACHE = CacheBuilder.newBuilder()
			.maximumSize(1000)
			.expireAfterWrite(60, TimeUnit.MINUTES)
			.build(new CacheLoader<String, Object>() {
				@Override
				public Object load(String s) {
					return "";
				}
			});
}
