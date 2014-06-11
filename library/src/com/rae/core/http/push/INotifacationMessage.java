package com.rae.core.http.push;

/**
 * 推送到通知栏接口
 * 
 * @author ChenRui
 * 
 */
public interface INotifacationMessage
{
	void notifac(Message msg);
	
	void removeAll();// 移除所有的通知
	
	void remove(Message msg); // 移除一个通知
}
