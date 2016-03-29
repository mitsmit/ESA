/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
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
public class ESearch1 {

    static HashSet<Document> allresults = null;

    public static void main(String q[]) throws IOException, ParseException {
        //find("plant");
    }

    //public static void find(String q) throws IOException, ParseException {
    public static void find(String q, PrintWriter pw, String path) throws IOException, ParseException {

        String queryString = URLDecoder.decode(q);
        HashSet el = new HashSet();
        //-------------------
        //pull the list of subtopics from word2vec model
        //--------------------

        //pw.println("sending query: "+queryString+"<br>");
        el = expandQuery(queryString, path, pw);
        //pw.println(queryString + "<br> Expanded into :" + el.size()+"<br>");
        pw.print("<div class=\"column1\">");
       //------------------
        //iterate the list of subtopics and query
        //-------------------
        
        allresults = new HashSet();
        Iterator it = el.iterator();
        while (it.hasNext()) 
        {
            queryString = it.next().toString().toLowerCase();
            pw.print("<h3>"+queryString+".........</h3>");
            
//            try
//            {
            search(queryString, pw);
            //break;

//            }
//            catch(Exception e){}
            // pw.println("number of results"+allresults.size());
            //search(queryString);
        }
        pw.print("</div>");
        pw.print("<div class=\"column2\">");
        Iterator it2 = el.iterator();
        while (it2.hasNext()) {
            queryString = it2.next().toString();
            pw.print("<h2><a href=./kw?kw="+URLEncoder.encode(queryString)+">"+queryString+"</a></h2>");
        }
        pw.println("</div>");

        //iterate all results
        HashSet titles = new HashSet();

//        Iterator eit = allresults.iterator();
//        while (eit.hasNext()) {
//            Document d = (Document) eit.next();
//            String title = d.get("title");
//            String abs = d.get("abstract");
//
//            if (title != null) {
//
//                if (titles.contains(title)) {
//                } else {
//                    //System.out.println("   Title: " + d.get("title"));
//                    pw.println("<div><p2>" + title + "</h2><p>" + abs + "</p></div>");
//                    titles.add(title);
//
//                }
//            }
////                    if(abs.length()>5)
////                    {
////                        //in.println("<br>"+abs+"<hr>");
////                    }
//        }
    }

    public static void search(String s, PrintWriter in) throws IOException, ParseException //public static void search(String s) throws IOException, ParseException
    {
        //in.println("<br>Search for: "+s+" found ");
        String field = "abs";
        String index = "/Users/smita/Documents/ES/index/meta/";
        int repeat = 0;
        int hitsPerPage = 20;
        String queries = null;
        boolean raw = false;

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse(s);

        doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null);
        //doPagingSearch(searcher, query, hitsPerPage, raw, queries == null);

        reader.close();

    }

    public static void doPagingSearch(PrintWriter pw, IndexSearcher searcher, Query query,
            int hitsPerPage, boolean raw, boolean b) throws IOException {
//    private static void doPagingSearch(IndexSearcher searcher, Query query, 
//            int hitsPerPage, boolean raw, boolean b) throws IOException {

        TopDocs results = searcher.search(query, 1 * hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = results.totalHits;
        int start = 0;
        int end = Math.min(numTotalHits, hitsPerPage);

        for (int i = start; i < end; i++) {
            Document doc = searcher.doc(hits[i].doc);
            String title = doc.get("title");
            String abs = doc.get("abstract");
            pw.println("<div><h2>" + title + "</h2><p>" + abs + "</p></div>");
            allresults.add(doc);
        }

    }

    public static HashSet expandQuery(String s, String p, PrintWriter pw) {
        
       //pw.println("recieving query to expand :" + s);
        //pw.println("<br>file path :" + p+"<br>");
        HashSet<String> el = new HashSet<String>();
        Scanner sc = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(p);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String[] items = sc.nextLine().split(",");
                System.out.println(items[0]);
                if (items[0].equalsIgnoreCase(s)) {
                    //System.out.println("related words of:"+items[0]+" are "+items[2]);
                    Collections.addAll(el, items);
                    
                    //break;
                }
            }
        } catch (Exception e) {
        }

        return el;
    }

}
