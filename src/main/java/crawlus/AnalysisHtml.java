package crawlus;


import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AnalysisHtml extends Thread{
	RedisClient redis = new RedisClient();
	public static String base = null;
	public void run(){
        try {
    		while(true){
    	        Set<String> keys = redis.getRedis().keys("fetched:*"); 
    	        List<String> urls = new ArrayList<String>();
    	        List<String> savedUrl = new ArrayList<String>();
    	        Iterator<String> it = keys.iterator();
    	        while(it.hasNext()){
    	        	String key = it.next();
    	        	savedUrl.add(key);
    	        }
    	        for(int i = 0; i < savedUrl.size(); i++){
    	        	String context = redis.getRedisObject(savedUrl.get(i));
    	        	urls = AnalysisHtml.paserHtml(context);
    	        	for(int j = 0; j < urls.size(); j++){
    	        		Boolean fetchedUrl = redis.getRedis().exists("fetched:" + urls.get(j));
    	        		Boolean fetchUrl = redis.getRedis().exists("fetch:" + urls.get(j));
    	        		if(fetchUrl){
    	        			DoFetchUrl.doFetchUrl(urls.get(j), null);
    	        		}
    	        		if(!fetchedUrl && !fetchUrl){
    	        			DoFetchUrl.doFetchUrl(urls.get(j), null);
    	        		}
    	        	}
    	        }
//    	        while(it.hasNext()){   
//    	            String key = it.next();
//    	            String context = redis.getRedisObject(key);
//    	            urls = AnalysisHtml.paserHtml(context);
//    	            for(int i = 0 ; i < urls.size(); i++){
//    	            	String fetchedUrl = redis.getRedisObject("fetched:" + urls.get(i));
//    	            	String fetchUrl = redis.getRedisObject("fetch" + urls.get(i));
//    	            	if(fetchedUrl == null && fetchUrl == null){
//    	            		int index = 0;
//    	            		while(it.hasNext()){
//    	            			if(!it.next().split("fetched:")[1].equals(urls.get(i))){
//    	            				index = index + 1;
//    	            			}else{
//    	            				break;
//    	            			}
//    	            		}
//    	            		if(index >= keys.size()){
//    	            			redis.saveObject(urls.get(i), "");
//    	            		}
//    	            	}
//    	            }
//    	        }
    			Thread.sleep(100);
    		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	public static List<String> paserHtml(String html) throws MalformedURLException{
		List<String> urls = new ArrayList<String>();
		Document doc = Jsoup.parse(html);
		Elements links=doc.getElementsByTag("a");
		for(int i = 0 ;i < links.size(); i++){
			String link = links.get(i).attr("href");
			if(link.length() > 0){
				if (!link.matches("^(https?|ftp):(\\\\|//).*$")) {
						String first = link.substring(0, 1);
						if(!first.equals("/")){
							link = AnalysisHtml.base + "/" + link;
						}else{
							link = AnalysisHtml.base + link;
						}
						
					}
				urls.add(link);
			}
		}
		return urls;
	}
}
