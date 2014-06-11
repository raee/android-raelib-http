package cn.chenrui.testhtpp;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.rae.core.http.async.AsyncHttpResponseHandler;
import com.rae.core.http.async.FileAsyncHttpResponseHandler;
import com.rae.core.http.async.RequestParams;
import com.rae.core.http.async.ResponseHandlerInterface;
import com.rae.core.http.async.SyncHttpClient;

public class MainActivity extends Activity implements View.OnClickListener
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void testUpload() throws IOException
	{
		SyncHttpClient http = new SyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("file", getAssets().open("test.jpg"));
		ResponseHandlerInterface responseHandlerInterface = new FileAsyncHttpResponseHandler(
				this)
		{

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, File file)
			{
				error("上传失败！");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, File file)
			{
				show("上传成功！");
			}
			
		};
		
		
		http.post("http://192.168.1.2:8081/upload.aspx", params,
				responseHandlerInterface);
	}
	
	
	
	@Override
	public void onClick(View v)
	{
		try
		{
			testUpload();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void show(Object msg)
	{
		msg = msg == null ? "" : msg;
		Log.i("maintest", msg.toString());
	}
	
	public void error(Object msg)
	{
		msg = msg == null ? "" : msg;
		Log.e("maintest", msg.toString());
	}
	
}
