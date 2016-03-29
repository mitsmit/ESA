/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryrank;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.lucene.queryparser.classic.ParseException;


/**
 *
 * @author smita
 */
public class AspectRank {
    public static void main(String[]args) throws IOException, ParseException
    {
        HashSet ql=new HashSet();
        ql.add("depression");
        ql.add("great depression") ;
        ql.add("long depression") ;
        ql.add("anatomical motion action") ;
        ql.add("depression (geology)") ;
        ql.add("depression (mood)");
        ql.add("depression (song)");
        ql.add("no depression (1992)");
       
        getAspectRank(ql); 
    }
    public static TreeMap<String,Double>getAspectRank(HashSet s) throws IOException, ParseException
    {
        System.out.println("sense size:"+s.size());
        TreeMap<String,Double> tm=new TreeMap<String,Double>();
        Iterator it=s.iterator();
        while(it.hasNext())
        {
            String subtopic=it.next().toString();
            double res=0;
            String aspect=subtopic.replaceAll("_"," ");
            aspect=aspect.replaceAll("\\)","");
            if(aspect.indexOf("(")>-1)
            {
                String []words=aspect.split("\\(");
                String ft=words[0].trim();
                String lt=words[1].trim();
                lt=lt.replaceAll(" ","_");
                aspect="\""+ft+"\" AND "+lt;
                System.out.println("    Sense "+aspect);
                res=search.Search.findaspect(aspect);
            }
            else
            {
                res=search.Search.findaspect("\""+aspect+"\"");
                
            }

            if(res>2.0)
            {
                System.out.println("    Query "+aspect+" has "+res+" results");
            tm.put(subtopic,res);
            }
            
            
        }
        TreeMap sortedMap = sortByValue(tm);
	System.out.println(sortedMap.size());
        return sortedMap;
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