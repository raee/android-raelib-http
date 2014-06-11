package com.rae.core.http.push;

/**
 * 消息处理接口
 * 
 * @author ChenRui
 * 
 */
public interface IHanderMessage
{
	/**
	 * 处理消息
	 * 
	 * @param message
	 *            消息对象
	 */
	void handler(Message message);
}
