/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import org.apache.lucene.queryparser.classic.ParseException;
import qe.CategoryExpansion;

/**
 *
 * @author smita
 */
public class ResultCluster {
    
    public static void main(String[]args) throws IOException, ParseException
    {
        HashMap<String,ArrayList>categoryMap=new HashMap<String,ArrayList>();
        ArrayList cl=new ArrayList();
        
        HashSet resourceSet=new HashSet();
        Iterator it=resourceSet.iterator();
        while(it.hasNext())
        {
            String r=it.next().toString();
            
            categoryMap.put(r, cl);
        }
    }
    public static TreeMap getcategories(HashSet r) throws IOException, ParseException
    {
        TreeMap <String,Integer>cm=new TreeMap();
        ArrayList cl=new ArrayList();
        Iterator it=r.iterator();
        while(it.hasNext())
        {
            String temp=it.next().toString().replaceAll(" ","_");
            cl=CategoryExpansion.getCategories(temp);
            Iterator ct=cl.iterator();
            while(ct.hasNext())
            {
                String cat=ct.next().toString();
                if(cm.containsKey(cat))
                {
                    int f=cm.get(cat).intValue()+1;
                    cm.put(cat, f);
                }
                else
                {
                    cm.put(cat, 1);
                }
            }
            
        }
        return cm;
    }
    
    public static void computeSimilarity(HashSet a,HashSet b)
    {
        
    }
    
}
