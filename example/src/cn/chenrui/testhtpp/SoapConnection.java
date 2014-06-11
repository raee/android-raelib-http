package cn.chenrui.testhtpp;

import java.util.Map;
import java.util.Map.Entry;

import com.rae.core.http.soap.SoapEnvelope;
import com.rae.core.http.soap.serialization.SoapObject;
import com.rae.core.http.soap.serialization.SoapSerializationEnvelope;
import com.rae.core.http.soap.transport.HttpTransportSE;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 调用WebSerivce服务器
 * 
 * @author MrChenrui
 * 
 */
public class SoapConnection
{
	private final String		tag							= "SoapConnection";
	public static final int		SOAP_ERROR_RESPONSE_EMPT	= 0;
	public static final int		SOAP_ERROR_INVOKE			= 1;

	// 版本1.0
	public static final int		VER10						= 100;

	// 版本1.1
	public static final int		VER11						= 110;

	// 版本1.2
	public static final int		VER12						= 120;

	private String				namespace;
	private String				methodName;
	private Map<String, Object>	requestParams;
	private String				url;
	private String				soapAction					= null;
	private int					version						= SoapEnvelope.VER11;

	public void setWebServiceVersion(int version)
	{
		this.version = version;
	}

	/**
	 * 请求WebSerivce
	 * 
	 * @param url
	 *            请求的URL
	 * @param methodName
	 *            需要调用的方法名称
	 */
	public void request(String url, String namespace, String methodName,
			Map<String, Object> params)
	{

		this.url = url;
		this.namespace = namespace;
		this.methodName = methodName;
		this.requestParams = params;
		this.soapAction = namespace + methodName;

		new AsyncInVokeWebService().start();
	}

	class AsyncInVokeWebService extends Thread
	{

		protected static final int	MSG_RESPONSE	= 0;
		protected static final int	MSG_ERROR		= 1;

		protected void call()
		{
			// soap对象
			SoapObject soap = new SoapObject(namespace, methodName);

			Log.i(tag, "------------");
			Log.i(tag, "调用URL：" + url);
			Log.i(tag, "调用方法：" + methodName);
			Log.i(tag, "调用的命名空间：" + namespace);

			// 调用参数赋值
			for (Entry<String, Object> param : requestParams.entrySet())
			{
				String key = param.getKey();
				Object value = param.getValue();
				soap.addProperty(key, value);

				Log.i(tag, "提交参数：【" + key + "】：" + value);
			}

			Log.i(tag, "------------");

			// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					version);

			// 设置是否调用的是dotNet开发的WebService,c#写的应用程序必须加上这句
			envelope.dotNet = true;
			envelope.bodyOut = soap;

			HttpTransportSE request = new HttpTransportSE(url);

			try
			{

				request.call(soapAction, envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;

				if (response == null)
				{
					Message.obtain(handler, MSG_ERROR, "服务端返回内容为空")
							.sendToTarget();
				}
				else
				{
					String json = response.getProperty(0).toString();
					if (json.equals("") || json.equals("null"))
					{
						Message.obtain(handler, MSG_ERROR, "服务端返回内容为空")
								.sendToTarget();
					}
					else
					{
						Log.i(tag, "服务器返回内容：");
						Log.i(tag, json);
						Message.obtain(handler, MSG_RESPONSE, json)
								.sendToTarget();
					}
				}

			}
			catch (Exception e)
			{
				String msg = "远程WebSerivce调用失败," + e.getMessage();
				Log.e(tag, msg);
				e.printStackTrace();
				Message.obtain(handler, MSG_ERROR, msg).sendToTarget();
			}
		}

		private Handler	handler	= new Handler(new Handler.Callback()
								{
									@Override
									public boolean handleMessage(Message msg)
									{
										switch (msg.what)
										{
											case MSG_RESPONSE:
												break;
											case MSG_ERROR:
											default:
												break;
										}
										return false;
									}
								});

		@Override
		public void run()
		{
			try
			{
				call();
			}
			catch (Exception e)
			{
				Message.obtain(handler, MSG_ERROR, "登录失败,请重试！");
			}
		}
	}
}
