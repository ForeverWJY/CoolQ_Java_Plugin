package com.wjyup.coolq.util.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.wjyup.coolq.util.service.IBaseService;
@Service
public class BaseService implements IBaseService {
	public Logger log = Logger.getLogger(getClass());
}
