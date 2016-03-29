/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;
import javax.json.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author smita
 */
public class IndexCoreMeta {

    public static void main(String[] args) throws FileNotFoundException, IOException, JSONException {

        String indexPath = "/Users/smita/Documents/ES/index/meta";
        String path = "/Users/smita/Documents/data/core/meta/";
        String docsPath = null;
        boolean create = true;

        Date start = new Date();
        System.out.println("Indexing to directory '" + indexPath + "'...");
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        if (create) {
        // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }
        // iwc.setRAMBufferSizeMB(256.0);
        IndexWriter writer = new IndexWriter(dir, iwc);
        
        //directory of data
        File folder=new File("/Users/smita/Documents/data/core/meta/");
        String[] files=folder.list();
        
        for(int i=0;i<870;i++)
        {
            String filename=path+files[i];
            System.out.println(filename);
            if(filename.endsWith(".DS_Store")){}
            else
            {
            readFile(writer,filename);
            }
            
        
        }
       writer.close();

        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + " total milliseconds");

    }

    private static void readFile(IndexWriter writer,String filename) throws FileNotFoundException, JSONException, IOException {
        
         FileInputStream inputStream = null;
        Scanner sc = null;
        try {

            int linecount = 0;
            inputStream = new FileInputStream(filename);
            sc = new Scanner(inputStream, "UTF-8");
            //String hash = sc.nextLine();
            while (sc.hasNextLine()) {
                String id = "";
                String title = "NA";
                String date = "";
                String abs = "NA";
                String[] authors = null;
                Document doc = new Document();

                linecount++;
                String line = sc.nextLine();
                try{
                JSONObject obj = new JSONObject(line);
                //System.out.println(obj.length());
//                
                id = obj.get("identifier").toString();
                doc.add(new TextField("id", id, Field.Store.YES));
                //String type=obj.get("dc:type").toString();
                //document.addField("type", type);
                try
                {
                title = obj.get("bibo:shortTitle").toString();
                doc.add(new TextField("title", title, Field.Store.YES));
//                date = obj.get("dc:date").toString();
//                doc.add(new TextField("date", date, Field.Store.YES));
                }
                catch(Exception e2){}
                
                try 
                {

                    abs = obj.get("bibo:abstract").toString();
                    doc.add(new TextField("abstract", abs, Field.Store.YES));
                    
                    //System.out.println(linecount + "," + abs);

                } catch (Exception e) {
                }
//                JSONArray arr = obj.getJSONArray("bibo:AuthorList");
//                if (arr != null) {
//                    for (int i = 0; i < arr.length(); i++) {
//                        doc.add(new TextField("author", arr.get(i).toString(), Field.Store.YES));
//                        //System.out.println(arr.get(i).toString());
//                    }
                    if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                        //System.out.println("adding " + linecount);
                        writer.addDocument(doc);

                    } else {
                        //System.out.println("updating ");
                        //writer.updateDocument(new Term("path", file.toString()), doc);
                    }

                }
                
                catch(Exception e3){}

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
    
        writer.commit();
    
    }
}
