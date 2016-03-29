//trying to find 1st level subtopics of a query (multiple senses of a query) 
//or else go to the Esearch_aspect class to retrieve the aspects
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
import qe.Disambiguation;
import qe.RedirectExpansion;
import queryrank.AspectRank;

/**
 *
 * @author smita
 */
public class ESearch_sense {

    static HashSet<String> senseset = null;
    static HashSet<String> relatedset = null;
    static TreeMap<String, Double> tm = new TreeMap<String, Double>();
    static HashSet titles = new HashSet();
    static String query = "";
    static int colorvalue = 447633;

    public static void main(String q[]) throws IOException {
        //find("virus");
    }
   public static void find(String q, PrintWriter pw, String filepath) throws IOException, Exception {

       
        TreeMap<String, Integer> aspectmap = new TreeMap<String, Integer>();
        //String qr = "\"" + q + "\"";
        HashSet<String> el = new HashSet<String>();
//        ArrayList rd=RedirectExpansion.getRedirectlabels(q);
//        rd.add(q);
        try 
        {
            Disambiguation dis = new Disambiguation();
            el = dis.getSenses(q); 
        } 
        catch (IOException ie) 
        {
            System.out.println(ie.getMessage());
        }
       
        el.add(q);
        //pw.println("number of senses: "+el.size());
        //related query expansion and search
        if (el.size() <=1) 
        {
            //go to esearch_aspect.java
           
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
                        BingSearch2.getResults(str, pw,10);
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
                
            } 
            else 
            {
                //normal keyword search
                BingSearch.getResults(q, pw,25);
            }
        }
        //multi sense weighted query expansion and search
        else 
        {
            

            pw.println("results from multiple senses.<p>");
            tm = AspectRank.getAspectRank(el);
            int count=0;
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
