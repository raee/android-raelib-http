package cn.chenrui.testhtpp;

import java.nio.charset.Charset;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.rae.core.http.async.AsyncAuthenticationHttpClient;
import com.rae.core.http.async.AsyncHttpResponseHandler;

/**
 * 基于Windows身份验证测试
 * 
 * @author ChenRui
 * 
 */
public class AuthHttpDownloadTestActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url = "http://nyyxlm.smu.edu.cn/caseimages/diagol/ct/2012/7/17/381_329/dicom/info.xml";
		String username = "pacs";
		String password = "pacs2002";
		AsyncAuthenticationHttpClient client = new AsyncAuthenticationHttpClient();
		client.get(url, username, password, new AsyncHttpResponseHandler() {

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				super.onProgress(bytesWritten, totalSize);
				Log.v("rae", bytesWritten + "/" + totalSize);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				Log.i("rae",
						"成功！"
								+ new String(responseBody, Charset
										.forName("gbk")));

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable throwable) {
				Log.e("rae", "失败：" + statusCode);
				if (throwable != null) throwable.printStackTrace();

			}
		});
	}
}
