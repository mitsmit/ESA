package search;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
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
import qe.Disambiguation;
import qe.RedirectExpansion;
import queryrank.AspectRank;

/**
 *
 * @author smita
 */
public class Esearch_aspect {

    static HashSet<String> senseset = null;
    static HashSet<String> relatedset = null;
    static TreeMap<String, Double> tm = new TreeMap<String, Double>();
    static HashSet titles = new HashSet();
    static String query = "";
    static int colorvalue = 447633;

    public static void main(String q[]) throws IOException, ParseException {
        //find("virus");
    }
   public static void find(String q, PrintWriter pw, String filepath) throws IOException, ParseException, Exception {

       
        TreeMap<String, Integer> aspectmap = new TreeMap<String, Integer>();
        //String qr = "\"" + q + "\"";
        HashSet<String> el = new HashSet<String>();
        ArrayList rd=RedirectExpansion.getRedirectlabels(q);
        rd.add(q);
        try 
        {
            Disambiguation dis = new Disambiguation();
            el = dis.getSenses(q); 
        } 
        catch (IOException ie) 
        {
            System.out.println(ie.getMessage());
        }
        catch (ParseException p) 
        {
            System.out.println("Sorry: need some fixing "+p.getMessage());
        }
        el.add(q);
        //pw.println("number of senses: "+el.size());
        //related query expansion and search
        if (el.size() <= 2) 
        {
            
            
            //el = expandQuery(q, filepath);
            //related queries from yahoo
            el=BingSearch.getRelated(q);
            
            if (el.size() > 1) 
            {
                
                //pw.println("<br>aspect size: " + el.size() + "<br>");
                //pw.println("results from single sense: ");
               
                int count=1;
                Iterator eit = el.iterator();
               
                while (eit.hasNext()) 
                {
                    count++;
                    if(count<7)
                    {
                        pw.println("<div class=\"aspectblock\">");
                        String str=eit.next().toString();
                        pw.println("<h2>" + str + "</h2>");
                        BingSearch.getResults(str, pw,10);
                        //search(str, pw, 5);
                        pw.println("<h2 style='text-align: right;color:#e7642c'><a href=./kw?kw="+URLEncoder.encode(str)+">"
                                + " More from this section..</a></h2>");
                        pw.println("</div>");
                        
                    }
                    else
                    {
                        pw.println(eit.next().toString()+"<br>");
                        //break;
                    }
                       
                }
                
//                pw.print("<div class=\"column2\">");
//                Iterator it2 = el.iterator();
//                while (it2.hasNext()) {
//                    String queryString = it2.next().toString();
//                    pw.print("<h2><a href=./kw?kw=" + URLEncoder.encode(queryString) + ">" + queryString + "</a></h2>");
//                }
//                pw.println("</div>");
            } 
            else 
            {
                //normal keyword search
                
                    //el.addAll(rd);
//                    Iterator elt=rd.iterator();
//                    while(elt.hasNext())
//                    {
//                        pw.print(elt.next().toString()+ " ");
//                        qr=qr+ " "+elt.next().toString();
//                    }
                //Search.find(qr, pw);
                BingSearch.getResults(q, pw,25);
            }
        }
        //multi sense weighted query expansion and search
        else 
        {
            

            pw.println("results from multiple senses.<p>");
            tm = AspectRank.getAspectRank(el);
            int count=1;
            //pw.println("<br>Results for senses in wiki: " + tm.size() + "<br>");
            //pw.print("<div class=\"column1\">");
            for (Iterator it = tm.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, Double> entry = (Map.Entry<String, Double>) it.next();
                String key = entry.getKey();
                count++;
                if(count<7)
                {
                pw.println("<div class=\"aspectblock\">");
                pw.println("<h2>" + key + "........</h2>");
                //int results = search(key, pw, 5);
                BingSearch2.getResults(key, pw,5);
                pw.println("<h2 style='text-align: right;color:#e7642c'><a href=./kw?kw="+URLEncoder.encode(key)+">"
                                + " More from this section..</a></h2>");
                pw.print("</div>");
                //aspectmap.put(key, results);
                }
                    else
                    {
                        pw.println("<h2 style='text-align: left;color:#e7642c'><a href=./kw?kw="+URLEncoder.encode(key)+">"
                                + key+"</a></h2>");
                        //break;
                    }
                       
                //break;
            }
            //pw.println("</div>");
            //begining of the second column
//            pw.print("<div class=\"column2\">");
//            for (String key : aspectmap.keySet()) 
//            {
//                double v=tm.get(key).doubleValue();
//                pw.print("<h2><a href=./kw?kw=" + URLEncoder.encode(key) + ">" + key + "</a>:"+v+" </h2>");  
//            }
//            pw.println("</div>");
        }
    }

    private static int search(String s, PrintWriter in, int resultNu) throws IOException, ParseException 
    {
        String field = "abs";
        String index = "/Users/smita/Documents/ES/index/abstract/";
        int repeat = 0;
        int hitsPerPage = resultNu;

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse(s);
        int results = PagingSearch(in, searcher, query, hitsPerPage);
        reader.close();
        return results;

    }

    private static int PagingSearch(PrintWriter pw, IndexSearcher searcher, Query query,
            int hitsPerPage) throws IOException {

        TopDocs results = searcher.search(query, 1 * hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;
        int numTotalHits = results.totalHits;
        int start = 0;
        int end = Math.min(numTotalHits, hitsPerPage);
        colorvalue = colorvalue + 123456;
        pw.write("<ol>");
        for (int i = start; i < end; i++) 
        {
            Document doc = searcher.doc(hits[i].doc);
            //allresults.add(doc);
            String title = doc.get("title");
            String abs = doc.get("abs");
            //if(abs.length()>100){
            if (titles.contains(title)) {

            } else {
                pw.println("<li><font color=#" + colorvalue + ">" + title + "</font></li>");
                //pw.println("<font color=#"+colorvalue+"><div><h2>"+ title + "</h2><p>" + abs + "</p></div></font>");
            }
            titles.add(title);
                        
        }
        pw.write("</ol>");
        return numTotalHits;
    }

    public static HashSet expandQuery(String s, String p) {

        HashSet<String> el = new HashSet<String>();
        Scanner sc = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(p);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String[] items = sc.nextLine().split(",");
                if (items[0].equalsIgnoreCase(s)) {
                    Collections.addAll(el, items);
                    System.out.println(el.toArray());
                    break;
                }
            }
        } catch (Exception e) {
        }

        return el;
    }

}
