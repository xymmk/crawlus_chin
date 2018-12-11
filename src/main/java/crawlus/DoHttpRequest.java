package crawlus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class DoHttpRequest {
	public static GetResponse doGet(String url, Map<String, String> params){
			Map<String, List<String>> headers = new HashMap<String ,List<String>>();
			StringBuffer result = new StringBuffer();
	        BufferedReader in = null;
	        GetResponse response = new GetResponse();
	        try {
	            
	            if(params.size() > 0){
	            	url = url + "?";
	            	int i = 0;
	            	int j = params.size();
	            	for (Map.Entry<String, String> entry : params.entrySet()) {
	            		if(i == 0){
	            			url = url + entry.getKey() + "=" + entry.getValue();
	            			i = i + 1;
	            			continue;
	            		}
	            		if(j <= 0){
	            			break;
	            		}else{
	            			url = "&" + entry.getKey() + "=" + entry.getValue();
	            			i = i + 1;
	            		}
	            		j = j - 1;
	            	}
	            }
	            URL realUrl = new URL(url);   
	            URLConnection connection = realUrl.openConnection();
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            connection.connect();
	            headers = connection.getHeaderFields();
	            
	            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result.append(line);
	            }
	        } catch (Exception e) {
	            System.out.println(e);
	            e.printStackTrace();
	        }
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        response.setHeaders(headers);
	        response.setContext(result);
	        return response;
		
	}
	
	public static GetResponse doPost(String url, Map<String, String> params){
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        Map<String, List<String>> headers = new HashMap<String ,List<String>>();
        try {
            if(params.size() > 0){
            	url = url + "?";
            	int i = 0;
            	int j = params.size();
            	for (Map.Entry<String, String> entry : params.entrySet()) {
            		if(i == 0){
            			url = url + entry.getKey() + "=" + entry.getValue();
            			i = i + 1;
            			continue;
            		}
            		if(j <= 0){
            			break;
            		}else{
            			url = "&" + entry.getKey() + "=" + entry.getValue();
            			i = i + 1;
            		}
            		j = j - 1;
            	}
            }
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            headers = conn.getHeaderFields();
            out = new PrintWriter(conn.getOutputStream());
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
            	result.append(line);
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        GetResponse response = new GetResponse();
        response.setHeaders(headers);
        response.setContext(result);
        return response;
	}

	public static HttpResponse doGetWithHttpClient(String url, Map<String, String> params){
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 CloseableHttpResponse res = null;
		 HttpResponse response = new HttpResponse();
		 try{
			 URIBuilder builder = new URIBuilder(url);
			 if(params != null) {
				 for(String key : params.keySet()){
					 builder.addParameter(key, params.get(key));
				 }
			 }
			 URI uri = builder.build();
			 HttpGet httpGet = new HttpGet(uri);
			 //httpGet.setHeader(header);
			 res = httpclient.execute(httpGet);
			 response.setContext( EntityUtils.toString(res.getEntity(), "UTF-8"));
			 response.setHeaders(res.getAllHeaders());
			 response.setResponse(res.getStatusLine());
		 }catch (Exception e){
			 return null;
		 }finally{
			 try{
				 if(res != null){
					 res.close();
				 }
				 httpclient.close();
			 }catch (IOException e) {
				 e.printStackTrace();
				 return null;
			 }
		 }
		return response;
	}

	public static HttpResponse doPostWithHttpClient(String url, Map<String, String> params){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse res = null;
		HttpResponse response = new HttpResponse();
        try {
            HttpPost httpPost = new HttpPost(url);
            if (params != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    paramList.add(new BasicNameValuePair(key, params.get(key)));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            res = httpClient.execute(httpPost);
            response.setContext(EntityUtils.toString(res.getEntity(), "utf-8"));
            response.setHeaders(res.getAllHeaders());
            response.setResponse(res.getStatusLine());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
		
	}
}
