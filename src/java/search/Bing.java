package search;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
public class Bing {
    /**
     * @param args
     */
    public static void main(String[] args) throws JSONException {
        // TODO Auto-generated method stub
        //--------------------------------------Bing search------------------------------
        String searchText = "sushi";
        //searchText = searchText.replaceAll(" ", "%20");
        String accountKey="Mc8NpmFnuTipqCtnVfsTIT5cgImxFwJG56yQMH5ajA";
      
            byte[] accountKeyBytes = Base64.encodeBase64(accountKey.getBytes());
            String accountKeyEnc = new String(accountKeyBytes);

           //https://api.datamarket.azure.com/Bing/Search/v1/Web?Query=%27sushi%27
        URL url;
        try {
            url = new URL(
                    "https://api.datamarket.azure.com/Bing/Search/v1/Web?Query=%27" + searchText + "%27&$format=JSON");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
 
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        System.out.println("Output from Server .... \n");
        //write json to string sb
        while ((output = br.readLine()) != null) {
 
            
            sb.append(output);
 
        }
 
        conn.disconnect();
         //find webtotal among output      
       int find= sb.indexOf("\"WebTotal\":\"");
       int startindex = find + 12;
 
 
       int lastindex = sb.indexOf("\",\"WebOffset\"");
 
        //System.out.println(sb.substring(startindex,lastindex));
       System.out.println(sb.toString());
 
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
 
            e.printStackTrace();
        }
 
       
    }
   
}