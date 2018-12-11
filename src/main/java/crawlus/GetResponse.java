package crawlus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetResponse {
	public GetResponse(){
		this.context = new StringBuffer("");
		this.headers = new HashMap<String, List<String>>();
	}
	public StringBuffer getContext() {
		return context;
	}
	public void setContext(StringBuffer context) {
		this.context = context;
	}
	public Map<String, List<String>> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}
	private StringBuffer context;
	private Map<String, List<String>> headers;
}
