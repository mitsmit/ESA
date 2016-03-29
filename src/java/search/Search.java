/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import clustering.ResultCluster;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author smita
 */
public class Search {
    
    
    
    public static void main(String q[]) throws IOException, ParseException
    {
        //find("depression");
    }

    public static void find(String q, PrintWriter pw) throws IOException, ParseException {
        
        //public static void find(String q) throws IOException, ParseException {
        String index = "/Users/smita/Documents/ES/index/abstract/";
        String field = "abs";
        int hitsPerPage = 50;
//        if(q.indexOf(" ")>-1){
//            q=q.replaceAll(" ", " AND ");
//        }
        
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse(q);
//        System.out.println("Searching for: " + query.toString(field));
        doPagingSearch(q,pw, searcher, query, hitsPerPage);
        reader.close();

    }

    //private static void doPagingSearch(IndexSearcher searcher, Query query, int hitsPerPage, boolean raw, boolean b) throws IOException {
        
    private static void doPagingSearch(String q,PrintWriter in, IndexSearcher searcher, Query query, int hitsPerPage) throws IOException, ParseException {
        TopDocs results = searcher.search(query, 1 * hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = results.totalHits;
        //System.out.println(numTotalHits + " total matching documents");
        in.write("<p>"+numTotalHits + " total matching documents<div class=result>");
        
        int start = 0;
        int end = Math.min(numTotalHits, hitsPerPage);
        HashSet titles=new HashSet();
        in.println("<ol type=\"1\">");
               
                for (int i = start; i < end; i++)
                {
                    Document doc = searcher.doc(hits[i].doc);
                    String title = doc.get("title");
                    String abs = doc.get("abs");
                    float score=hits[i].score;
                    if(titles.contains(title)){}
                        else
                        {
                           titles.add(title);
                           in.println("<h2>"+title+"</h2>");
                           in.println(abs);
                        }
                         //System.out.println(i+" : " + doc.get("title"));
                         
                        }
        in.println("</ol></div><div class=column2>");
                //write categories
            TreeMap<String,Integer>rm=new TreeMap<String,Integer>(); 
            rm=ResultCluster.getcategories(titles);
            TreeMap<String,Integer> sortedMap=new TreeMap();
            sortedMap=sortByValue(rm);
          for (String key : sortedMap.keySet()) 
            {
                String nextquery=q+" AND "+key.replaceAll("_"," ");
                int v=sortedMap.get(key).intValue();
                if(v>0)
                    
                in.print("<h2><a href=./kw?kw=" + URLEncoder.encode(nextquery) + ">" + key.replaceAll("_"," ") + "</a>:"+v+" </h2>");  
            }
        in.println("</div>");
    }
    
    
    
    public static int findaspect(String q) throws IOException, ParseException {
        String index = "/Users/smita/Documents/ES/index/abstract/";
        String field = "abs";
        String queries = null;
        int repeat = 0;
        boolean raw = false;
        String queryString = q;
        int hitsPerPage = 20;

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();

        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse(queryString);
        int results=PagingSearch(searcher, query, hitsPerPage, raw, queries == null && queryString == null);
        reader.close();

        return results;
        
    }
    
    private static int PagingSearch(IndexSearcher searcher, Query query, int hitsPerPage, boolean raw, boolean b) throws IOException {
        //private static void doPagingSearch(PrintWriter in, IndexSearcher searcher, Query query, int hitsPerPage, boolean raw, boolean b) throws IOException {
        TopDocs results = searcher.search(query, 1 * hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;
        int resultnu=0;
        int numTotalHits = results.totalHits;
        int end = Math.min(numTotalHits, hitsPerPage);
        //System.out.println(numTotalHits + " total matching documents");
//        for (int i = 0; i < end; i++) 
//            {
//                Document doc = searcher.doc(hits[i].doc);
//                float score=hits[i].score;
//                if(score>1.0){resultnu++;}
//            }
        return numTotalHits;
        }
    
    public static TreeMap sortByValue(Map unsortedMap) {
		TreeMap sortedMap = new TreeMap(new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}
    
    

}

class ValueComparator implements Comparator {
 
	Map map;
 
	public ValueComparator(Map map) {
		this.map = map;
	}
 
	public int compare(Object keyA, Object keyB) {
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		return valueB.compareTo(valueA);
	}
}