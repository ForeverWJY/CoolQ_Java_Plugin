package com.wjyup.coolq.service.impl;

import com.wjyup.coolq.service.IBaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
@Service
public class BaseService implements IBaseService {
	public static final Logger log = LogManager.getLogger(BaseService.class);
}
