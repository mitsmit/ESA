/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author smita
 */
public class ManualExpansion {
    
    public static HashSet expandQuery(String s) throws FileNotFoundException
    {
        String filename="./subtopics.txt";
        HashSet <String>el=new HashSet<String>();
        Scanner sc = null;
        FileInputStream inputStream = null;
        try 
        {
            inputStream = new FileInputStream(filename);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String[]items=sc.nextLine().split(",");
                if(items[0].equalsIgnoreCase(s))
                {
                    Collections.addAll(el,items);
                    System.out.println(el.toArray());
                    break;
                }
            }
        }
        
        catch(Exception e){}
       
        return el;
    }
    
    
}
