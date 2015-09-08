package com.rae.core.http.async;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 基于Windows认证的异步HTTP 下载
 * 
 * @author ChenRui
 * 
 */
public class AsyncAuthenticationHttpClient extends AsyncHttpClient {

	public RequestHandle get(String url, String username, String password,
			ResponseHandlerInterface responseHandler) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());
		client.getCredentialsProvider().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
				new NTCredentials(username, password, "", ""));

		return sendRequest(client, getHttpContext(), new HttpGet(
				getUrlWithQueryString(true, url, null)), null, responseHandler,
				null);

	}

}
