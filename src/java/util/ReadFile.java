/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author smita
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;
import javax.json.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author smita
 */
public class ReadFile {

    public static void main(String[] args) throws FileNotFoundException, IOException, JSONException {

        //String indexPath = "/Users/smita/Documents/ES/index/meta";
        String path = "/Users/smita/Documents/data/stream/";
         String metapath = "/Users/smita/Documents/data/stream/";
        PrintWriter pw=new PrintWriter(new FileWriter("/Users/smita/Documents/data/abstract.csv"),true);
        //directory of data
        File folder=new File("/Users/smita/Documents/data/stream/");
        String[] files=folder.list();
        //pw.println("\tdata");
        for(int i=0;i<5;i++)
        {
            String filename=path+files[i];
            System.out.println(filename);
            if(filename.endsWith(".DS_Store")){}
            else
            {
            //readJsonFile(pw,filename);
            readTextFile(pw,filename);
            break;
            }
            
        
        }
       
        Date end = new Date();
        //System.out.println(end.getTime() - start.getTime() + " total milliseconds");
        pw.close();
    }

    private static void readJsonFile(PrintWriter pw,String filename) throws FileNotFoundException, JSONException, IOException {
        
         FileInputStream inputStream = null;
        Scanner sc = null;
        try {

            int linecount = 0;
            inputStream = new FileInputStream(filename);
            sc = new Scanner(inputStream, "UTF-8");
            //String hash = sc.nextLine();
            while (sc.hasNextLine()) {
                String id = "";
                String text="NA";
//                String date = "";
//                String abs = "NA";
//                String[] authors = null;
                //Document doc = new Document();

                linecount++;
                String line = sc.nextLine();
                System.out.println(line);
                //try{
//                JSONObject obj = new JSONObject(line);
//                id = obj.get("identifier").toString();
//                
//                try
//                {
//                //text = obj.get("fullTextSource").toString();
//                    text = obj.get("bibo:abstract").toString();
//                    text=text.replaceAll("[\\t\\n\\r]"," ");
//                    if(text.length()>100){
//                    pw.println("\t\""+text+"\"");
//                    //pw.print("\n");
//                    
//                    //System.out.println("    \""+text+"\"");
//                    }
//                }
//                catch(Exception e2){}
                
               
                //}
//                JSONArray arr = obj.getJSONArray("bibo:AuthorList");
//                if (arr != null) {
//                    for (int i = 0; i < arr.length(); i++) {
//                        doc.add(new TextField("author", arr.get(i).toString(), Field.Store.YES));
//                        //System.out.println(arr.get(i).toString());
//                    }
                   

                
                
                //catch(Exception e3){}

            }
        
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        

        } 
        
        
        finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    
        
    
    }

    private static void readTextFile(PrintWriter pw, String filename) {
        
    FileInputStream inputStream = null;
        Scanner sc = null;
        try {

            int linecount = 0;
            inputStream = new FileInputStream(filename);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) 
            {
                String line = sc.nextLine();
                String id;
                String user;
                String date;
                String content;
                String retweet;
                String fav;
                String rep;
                String media="No Media";
                String[]ele=line.split("\\|");
                if(ele.length>8||ele.length<8){}
                else{
                id=ele[0];
                user=ele[1];
                date=ele[2];
                content=ele[3];
                media=ele[7];
                }
                
                System.out.println(ele.length);
                
            }
        }
        catch(Exception e3){}
    
    
    }
}
