package crawlus;

import java.util.Map;

public class DoFetchUrl {
	public static void doFetchUrl(String url, Map<String, String> params){
		HttpResponse response = DoHttpRequest.doGetWithHttpClient(url, params);
		RedisClient redis = new RedisClient();
		if(response != null){
			if(response.getResponse().getStatusCode() >= 200){
				if(response.getResponse().getStatusCode() == 200){
					redis.saveObject("fetched:" + url, response.getContext());
				}
			}
		}
	}
}
