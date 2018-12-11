package crawlus;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;

public class HttpResponse {
	
	private Header[] headers;
	private String context;
	private StatusLine response;
	public Header[] getHeaders() {
		return headers;
	}
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public StatusLine getResponse() {
		return response;
	}
	public void setResponse(StatusLine response) {
		this.response = response;
	}
}
