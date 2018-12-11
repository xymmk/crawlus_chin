package crawlus;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.select.Elements;

public class DoCrawlus {

	public static void main(String[] args) throws URISyntaxException, MalformedURLException {
		Map<String, String> params = new HashMap<String, String>();
		String url = "https://www.connecty.co.jp";
		HttpResponse response = DoHttpRequest.doGetWithHttpClient(url, params);
		if(response.getResponse().getStatusCode() == 200){
			
			RedisClient redis = new RedisClient();
			redis.saveObject("fetched:" + url, response.getContext());
			AnalysisHtml.base = url;
//			System.out.println(redis.getRedisObject("chen"));
//			List<String> urls = AnalysisHtml.paserHtml(response.getContext());
//			System.out.println(urls.get(0));
			AnalysisHtml anaHtml = new AnalysisHtml();
			anaHtml.start();
		}
//		RedisClient redis = new RedisClient();
//		redis.saveObject("haha", "fsdfsadfdas");
//		//redis.getRedisObject("*");
//		redis.getRedis().keys("*").iterator();
//		System.out.println(redis.getRedisObject("*"));
	}

}
