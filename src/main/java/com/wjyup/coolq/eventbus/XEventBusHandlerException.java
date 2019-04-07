package com.wjyup.coolq.eventbus;

/**
 * 事件处理函数抛出的异常
 */
public class XEventBusHandlerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public XEventBusHandlerException(Throwable cause) {
		super(cause);
	}
}
