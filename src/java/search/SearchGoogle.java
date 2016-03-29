/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
 
public class SearchGoogle {
 
	public static void main(String[] args) throws IOException {
 
           
            //011534530197018961913:-l1zu2qqdyy
            for (int i = 0; i < 20; i = i + 4) {
		String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start="+i+"&q=";
		String query = "Depression(geology)";
		String charset = "UTF-8";
 
		URL url = new URL(address + URLEncoder.encode(query, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);
		GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
 
//		int total = results.getResponseData().getResults().size();
//		System.out.println("total: "+total);
// 
		// Show title and URL of each results
		for(int k=0; k<=3; k++){
			System.out.println("Title: " + results.getResponseData().getResults().get(k).getTitle());
			//System.out.println("URL: " + results.getResponseData().getResults().get(k).getUrl() + "\n");
 
		}
	}
   }

} 
 
class GoogleResults{
 
    private ResponseData responseData;
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    public String toString() { return "ResponseData[" + responseData + "]"; }
 
    
    
    static class ResponseData {
        private List<Result> results;
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }
        public String toString() { return "Results[" + results + "]"; }
    }
 
    static class Result {
        private String url;
        private String title;
        private String snippets;
        public String getUrl() { return url; }
        public String getDesc() { return snippets; }
        public String getTitle() { return title; }
        public void setUrl(String url) { this.url = url; }
        public void setTitle(String title) { this.title = title; }
        public void setSnippet(String desc) { this.snippets=desc; }
        public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
    }
    
    
    
}