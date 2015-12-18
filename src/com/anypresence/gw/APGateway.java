package com.anypresence.gw;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.NotImplementedException;

import com.anypresence.gw.callbacks.IAPFutureCallback;
import com.anypresence.gw.exceptions.RequestException;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * 
 *
 */
public class APGateway {
	/**
	 * URL to connect to
	 */
	private String url;

	/**
	 * HTTP method to use
	 */
	private HTTPMethod method;

	private IRestClient restClient;

	private IParser jsonParser = new JSONParser();

	/**
	 * Payload body for POST requests
	 */
	private String body;

	private APGateway() {
	}

	public String getUrl() {
		return url;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	public HTTPMethod getMethod() {
		return method;
	}

	public void setMethod(HTTPMethod method) {
		this.method = method;
	}

	/**
	 * Executes the request
	 */
	public void execute() {
		execute(url);
	}

	public void execute(String url) {
		execute(url, null);
	}

	/**
	 * @param <T>
	 * @see APGateway#execute()
	 * @param url
	 *            relative url to connect to
	 */
	public <T> void execute(final String url, IAPFutureCallback<T> callback) {
		if (callback == null) {
			try {
				connect(url, method);
			} catch (RequestException e) {
				e.printStackTrace();
			}
		} else {
			// Handle callback
			ListenableFuture<T> future = AsyncHandler.getService().submit(
					new Callable<T>() {
						@SuppressWarnings("unchecked")
						public T call() throws Exception {
							connect(url, method);

							APObject apObjecct = new APObject();
							return (T) readResponseObject(apObjecct);
						}
					});

			AsyncHandler.handleCallbacks(future, callback);
		}
	}

	private void connect(String url, HTTPMethod method) throws RequestException {
		getRestClient().openConnection(Utilities.updateUrl(this.url, url),
				method);

		switch (method) {
		case POST:
			getRestClient().post(getBody());
		default:
			break;
		}
	}

	/**
	 * @see APGateway#post(String)
	 */
	public void post() {
		execute();
		getRestClient().post(body);
	}

	/**
	 * Sends post request
	 * 
	 * @param url
	 *            relative url to connect to
	 */
	public void post(String url) {
		execute(url);
		getRestClient().post(body);
	}

	/**
	 * @see APGateway#get(String)
	 */
	public void get() {
		execute();
	}

	/**
	 * Sends a get request
	 * 
	 * @param url
	 *            relative url to connect to
	 */
	public void get(String url) {
		execute(url);
	}

	public <T> void get(String url, IAPFutureCallback<T> callback) {
		execute(url, callback);
	}

	public <T extends APObject> T readResponseObject(T obj) {
		String response = readResponse();

		Map<String, String> data = jsonParser.parseData(response);

		if (data != null) {
			for (Entry<String, String> entry : data.entrySet()) {
				obj.set(entry.getKey(), entry.getValue().toString());
			}
		}

		return obj;
	}

	public <T extends List<APObject>> T readResponseObject(T obj) {
		throw new NotImplementedException("Not yet implemented");
	}

	/**
	 * Reads the raw response.
	 * 
	 * @return the response
	 */
	public String readResponse() {
		return getRestClient().readResponse();
	}

	public IRestClient getRestClient() {
		if (restClient == null) {
			restClient = new DefaultRestClient();
		}

		return restClient;
	}

	protected void setRestClient(IRestClient restClient) {
		this.restClient = restClient;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Sets the relative url
	 * 
	 * @param url
	 */
	public void setRelativeUrl(String url) {
		String updatedUrl = this.url + "/" + url;
		URI uri;
		try {
			uri = new URI(updatedUrl);
			uri = uri.normalize();

			this.url = uri.toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Builder for APGateway
	 * 
	 */
	public static class Builder {
		String url;
		HTTPMethod method;

		public Builder() {
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder method(HTTPMethod method) {
			this.method = method;
			return this;
		}

		public APGateway build() {
			APGateway gw = new APGateway();
			gw.setUrl(url);
			gw.setMethod(method);

			return gw;
		}
	}

}
