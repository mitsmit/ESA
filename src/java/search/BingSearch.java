package search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashSet;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;
import static search.ESearch_sense.colorvalue;

public class BingSearch {

    static int colorvalue = 447633;

    public static void main(final String[] args) throws Exception {
        final String accountKey = "/Mc8NpmFnuTipqCtnVfsTIT5cgImxFwJG56yQMH5ajA";

        String query = "Latent semantic indexing";
        query = query.replaceAll(" ", "%20");
        final String accountKeyEnc = Base64.getEncoder().encodeToString((accountKey + ":" + accountKey).getBytes());
        URL url;

        String bingUrlPattern = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/RelatedSearch?Query=%27"
                + query + "%27&$top=25&$format=JSON";
        url = new URL(bingUrlPattern);

        final URLConnection connection = url.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            final StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            final JSONObject json = new JSONObject(response.toString());
            final JSONObject d = json.getJSONObject("d");
            final JSONArray results = d.getJSONArray("results");
            final int resultsLength = results.length();
            System.out.println(resultsLength);
            for (int i = 0; i < resultsLength; i++) {
                final JSONObject aResult = results.getJSONObject(i);
                //System.out.println(aResult.get("Url"));
                System.out.println(aResult.get("Title"));
                //System.out.println(aResult.get("Description"));
            }
        }

    }//function for related queries

    public static HashSet getRelated(String q) throws Exception {
        final String accountKey = "/Mc8NpmFnuTipqCtnVfsTIT5cgImxFwJG56yQMH5ajA";

        String query = q;
        HashSet qset = new HashSet();
        query = query.replaceAll(" ", "%20");
        final String accountKeyEnc = Base64.getEncoder().encodeToString((accountKey + ":" + accountKey).getBytes());
        URL url;

        String bingUrlPattern = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/RelatedSearch?Query=%27"
                + query + "%27&$top=25&$format=JSON";
        url = new URL(bingUrlPattern);

        final URLConnection connection = url.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            final StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            final JSONObject json = new JSONObject(response.toString());
            final JSONObject d = json.getJSONObject("d");
            final JSONArray results = d.getJSONArray("results");
            final int resultsLength = results.length();
            System.out.println(resultsLength);
            for (int i = 0; i < resultsLength; i++) {
                final JSONObject aResult = results.getJSONObject(i);
                //System.out.println(aResult.get("Url"));
                String title = aResult.get("Title").toString();
                qset.add(title);

                //System.out.println(aResult.get("Title"));
                //System.out.println(aResult.get("Description"));
            }
        }

        return qset;
    }

    //function for normal search
    public static void getResults(String q, PrintWriter pw, int nu) throws Exception {
        HashSet similar=new HashSet();
        final String accountKey = "/Mc8NpmFnuTipqCtnVfsTIT5cgImxFwJG56yQMH5ajA";
        int top = nu;
        String query = q;
        query = query.replaceAll(" ", "%20");
        final String accountKeyEnc = Base64.getEncoder().encodeToString((accountKey + ":" + accountKey).getBytes());
        URL url;

        String bingUrlPattern = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/Web?Query=%27"
                + query + "%27&$top=" + top + "&$format=JSON";
        url = new URL(bingUrlPattern);

        final URLConnection connection = url.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            final StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            final JSONObject json = new JSONObject(response.toString());
            final JSONObject d = json.getJSONObject("d");
            final JSONArray results = d.getJSONArray("results");
            final int resultsLength = results.length();
            colorvalue = colorvalue + 123456;
            pw.println("<body><table>");
            for (int i = 0; i < resultsLength; i++) 
            {
                final JSONObject aResult = results.getJSONObject(i);
                String title = aResult.get("Title").toString();
                String path = aResult.get("Url").toString();
                String desc = aResult.get("Description").toString();
                //pw.println("<a href="+path+">"+title+"</a><br>");
                pw.println("<tr>");
                pw.println("<td><h2>" + title + "</h2>");
                pw.println("<div class=\"desc\">"+desc+"</div>");
                pw.println("</td>");
                //printRelevanceBox(pw);
                pw.println("</tr>");
               

            }
            pw.println("</table></body>");
        }

    }

    public static void printRelevanceBox(PrintWriter pw)
    {
        pw.println("<td>");
        pw.print("<input type=\"radio\" name=\"rel\" value=\"0\">Irrelevant");
        pw.print("<input type=\"radio\" name=\"rel\" value=\"1\">Somewhat Relevant");
        pw.println("<input type=\"radio\" name=\"rel\" value=\"2\">Highly Relevant");
        pw.println("</td>");
    }
    public static void getSmallResults(String q, PrintWriter pw,int nu) throws Exception {
        final String accountKey = "/Mc8NpmFnuTipqCtnVfsTIT5cgImxFwJG56yQMH5ajA";

        String query = q;
        query = query.replaceAll(" ", "%20");
        final String accountKeyEnc = Base64.getEncoder().encodeToString((accountKey + ":" + accountKey).getBytes());
        URL url;

        String bingUrlPattern = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/Web?Query=%27"
                + query + "%27&$top=5&$format=JSON";
        url = new URL(bingUrlPattern);

        final URLConnection connection = url.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            final StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            final JSONObject json = new JSONObject(response.toString());
            final JSONObject d = json.getJSONObject("d");
            final JSONArray results = d.getJSONArray("results");
            final int resultsLength = results.length();
            colorvalue = colorvalue + 123456;
            
            for (int i = 0; i < resultsLength; i++) {
                final JSONObject aResult = results.getJSONObject(i);
                String title = aResult.get("Title").toString();
                String path = aResult.get("Url").toString();
                String desc = aResult.get("Description").toString();
                //pw.println("<a href="+path+">"+title+"</a><br>");
                
                pw.println("<a href=\"+path+\">\"+<font color=#" + colorvalue + ">" + title + "</a></font></li>");
                //pw.println(desc);
                

            }
            
        }

    }

    public static void getBing() throws Exception {

        HttpClient httpclient = new DefaultHttpClient();

        try {
            HttpGet httpget = new HttpGet("https://api.datamarket.azure.com/Data.ashx/Bing/Search/Web?Query=%27Datamarket%27&$top=10&$format=Json");
            httpget.setHeader("Authorization", "Mc8NpmFnuTipqCtnVfsTIT5cgImxFwJG56yQMH5ajA");

            System.out.println("executing request " + httpget.getURI());

            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }

}
