package bing;
	
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.params.*;
import org.apache.http.conn.*;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.params.*;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.tsccm.*;
import org.apache.http.auth.*;
import org.apache.http.impl.auth.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.w3c.dom.NamedNodeMap;

/**
 * This is the main class that is responsible for working with Bing Search API.
 *
 * @author Rami Ghorab
 */
public class BingSearch
{
    
    private DefaultHttpClient defaultHttpClient;
    private static Hashtable<String, String> languageToMarketTable = new Hashtable<String, String>(4);

    static
    {
        //for now, the market will be set to the main country of the language, although the language is spoken in other countries.
        languageToMarketTable.put("en",MicrosoftMarketConstant.ENGLISH_UNITED_STATES);
        languageToMarketTable.put("fr",MicrosoftMarketConstant.FRENCH_FRANCE);
        languageToMarketTable.put("de",MicrosoftMarketConstant.GERMAN_GERMANY);
        languageToMarketTable.put("es",MicrosoftMarketConstant.SPANISH_SPAIN);
        languageToMarketTable.put("ar",MicrosoftMarketConstant.ARABIC_ARABIA);

    }

    public BingResultList search(String query, String language, int requiredNumberOfResults)
    {

        Vector<? extends BingResult> bingResults = null;
        
        boolean useProxy = true;
        String proxyHostName = "www-proxy.cs.tcd.ie" ;
        int proxyHostPort = 8080;
        
        String myAzureBingKey = "/Mc8NpmFnuTipqCtnVfsTIT5cgImxFwJG56yQMH5ajA" ;
        
        String sourceType = "Web"; //this is like the vertical that we are interested in
                                    // which can be: Web, Image, Video, News, Spell, Phonebook, RelatedSearch see: http://www.bing.com/developers/s/APIBasics.html
                                    //   or Composite. If it is composite then an additional attribute must be supplied
                                    //   which is: Sources=
                                    //   e.g. Sources=%27Web%2bVideo%2bNews%27
		
		int offset = 0;
		

        String targetMarket = languageToMarketTable.get(language);

        if(targetMarket==null || targetMarket.equalsIgnoreCase("")) //in case I had no market specified for the language or an error occurred, I will fallback on the irish english market
        {
            targetMarket = MicrosoftMarketConstant.ENGLISH_IRELAND;
        }

        if(useProxy)
        {
            this.initializeConnection(proxyHostName, proxyHostPort);
        }
        else
        {
           this.initializeConnection();
        }

        HttpResponse myResponse = this.submitQuery(query, requiredNumberOfResults, offset,
                sourceType, targetMarket, myAzureBingKey);


        bingResults = this.processXMLResponse(myResponse, sourceType);

        this.shutDownConnection();

        BingResultList bingResultList = new BingResultList(sourceType, bingResults);
        
        return bingResultList;
    }


   public void initializeConnection()
    {
        SchemeRegistry supportedSchemes = new SchemeRegistry();
        // Register the "http" and "https" protocol schemes, they are
        // required by the default operator to look up socket factories.
        supportedSchemes.register(new Scheme("http",PlainSocketFactory.getSocketFactory(), 80));
        supportedSchemes.register(new Scheme("https",SSLSocketFactory.getSocketFactory(), 443));
        // prepare parameters
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, true);

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, supportedSchemes);

        defaultHttpClient = new DefaultHttpClient(ccm, params);
    }


    public void initializeConnection(String proxyHostName, int proxyHostPort)
    {
        SchemeRegistry supportedSchemes = new SchemeRegistry();
        // Register the "http" and "https" protocol schemes, they are
        // required by the default operator to look up socket factories.
        supportedSchemes.register(new Scheme("http",PlainSocketFactory.getSocketFactory(), 80));
        supportedSchemes.register(new Scheme("https",SSLSocketFactory.getSocketFactory(), 443));
        // prepare parameters
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, true);

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, supportedSchemes);

        defaultHttpClient = new DefaultHttpClient(ccm, params);

        HttpHost proxyHost = new HttpHost(proxyHostName, proxyHostPort);

        defaultHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);

    }

    public void shutDownConnection()
    {
        defaultHttpClient.getConnectionManager().shutdown();
    }


    public HttpResponse submitQuery(String query, int requiredNumberOfResults, int offset,
            String sourceType, String targetMarket, String azureKey)
    {
        try
		{
            query = URLEncoder.encode(query,"UTF-8");
            query = query.replace("+", "%20");
		}
		catch(UnsupportedEncodingException ex)
		{
	            ex.printStackTrace();
		}

        String bingSearchBaseURL = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/"+sourceType+"?";

        String requestURL = bingSearchBaseURL +
                "Query="+"%27"+query+"%27" +
                "&$top="+requiredNumberOfResults +  //max 50 according to the API
                "&$skip="+offset +  //offset
                "&$format="+ "Atom" + //Atom indicates atom+xml response is required. I haven't implemented the Bing response processor for JSON yet. When I do so, I should have this come in as a parameter in the mthod to incicate whether I want to work with xml or json
                "&Market="+"%27"+targetMarket+"%27" ;
                //Note: The Azure key is not entered as part of the URL.
                //See below the use of some methods to perform authentication using Basic Authentication Scheme.

        HttpGet getRequest = new HttpGet(requestURL);

        HttpResponse myResponse=null;

        try
		{
            UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(azureKey, azureKey); //The Azure service does not require a username, so we just use the key as both username and password.

            getRequest.addHeader(new BasicScheme().authenticate(usernamePasswordCredentials, getRequest));

            myResponse = defaultHttpClient.execute(getRequest);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return myResponse;
    }


    public Vector<? extends BingResult> processXMLResponse(HttpResponse httpResponse, String sourceType)
    {

        Vector<? extends BingResult> bingResults=null;
        
        try
        {
            HttpEntity responseEntity = httpResponse.getEntity();

            InputStream inputStream = responseEntity.getContent();

            Document myDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();

            if(sourceType.equalsIgnoreCase("Web"))
            {
				bingResults = this.processWebResultsXML(myDocument, xPath);
            }

            inputStream.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return bingResults;

    }


	public Vector<? extends BingResult> processWebResultsXML(Document document, XPath path) throws XPathExpressionException
	{

        // the strange thing is that the tags do not work when I type
        // the actual names, like: m:properties, d:Title, etc.
        // It works like this: properties, Title, etc.

        
        
        NodeList nodes = (NodeList)path.evaluate("feed/entry", document, XPathConstants.NODESET);

        int nodeCount = nodes.getLength();

        Vector<BingWebResult> bingResults = new Vector<BingWebResult>(nodeCount,10);

        BingWebResult tempResult=null;

        //iterate over search Result nodes
        for (int i = 0; i < nodeCount; i++)
        {
            //Get each xpath expression as a string
            String type = (String)path.evaluate("title", nodes.item(i), XPathConstants.STRING);
            String lastModifiedDateTime = (String)path.evaluate("updated", nodes.item(i), XPathConstants.STRING);

            Node contentNode = (Node)path.evaluate("content", nodes.item(i), XPathConstants.NODE);

            Node mPropertiesNode = (Node)path.evaluate("properties", contentNode, XPathConstants.NODE);

            String id = (String)path.evaluate("ID", mPropertiesNode, XPathConstants.STRING);
            String title = (String)path.evaluate("Title", mPropertiesNode, XPathConstants.STRING);
            String description = (String)path.evaluate("Description", mPropertiesNode, XPathConstants.STRING);
            String URL = (String)path.evaluate("Url", mPropertiesNode, XPathConstants.STRING);
            String displayURL = (String)path.evaluate("DisplayUrl", mPropertiesNode, XPathConstants.STRING);

            tempResult = new BingWebResult(id, title, description, URL, displayURL, lastModifiedDateTime);
            
            bingResults.add(tempResult);
        }   
        
        return bingResults; 	
    }

}
