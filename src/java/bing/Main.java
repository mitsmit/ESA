package bing;

import java.util.*;


public class Main
{
	public static void main(String[] args)
	{
		BingSearch bingSearch = new BingSearch();
		
		String query = "hello world";
		String language = "en";
		int requiredNumberOfResults = 5;
		
		
		BingResultList bingResultList = bingSearch.search(query, language, requiredNumberOfResults);
		String type = bingResultList.getType();
    
    	for(int i=0; i<bingResultList.getCount(); i++)
    	{
    		BingResult br = bingResultList.get(i);
    		System.out.println ("Result#"+(i+1));
    		System.out.println("Id: " + br.getId());
    		
    		if(type.equalsIgnoreCase("Web"))
    		{
	    		BingWebResult bwr = (BingWebResult) br;
	    		System.out.println("Title: " + bwr.getTitle());
	    		System.out.println("URL: " + bwr.getURL());	
	    		System.out.println("Summary: " + bwr.getDescription());
	    	}
	    	
	    	System.out.println("\n");	
    	}
    
    
    	
	}	
	
}