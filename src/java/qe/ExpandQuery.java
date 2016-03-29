/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qe;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import net.didion.jwnl.JWNLException;
import org.apache.lucene.queryparser.classic.ParseException;
import search.BingSearch;

/**
 *
 * @author smita
 */
public class ExpandQuery {
    
    static String q="queryterm";
    static ArrayList el=new ArrayList();
    
    public static void expandBySynonym(String q) throws JWNLException{
        el=WordnetExpansion.getSenses(q);
    }
     public static void expandByHyponym(String q){
        WordnetExpansion.getHyponym(q);
    }
      public static void expandByMeronym(String q){
        
    }
       public static void expandByRelated(String q) throws Exception{
        HashSet es=BingSearch.getRelated(q);
        el.add(es);
    }
       public static void expandByDBPedia(String q) throws IOException, ParseException{
        el=RedirectExpansion.getRedirectlabels(q);
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
