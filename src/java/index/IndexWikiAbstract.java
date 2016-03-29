/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package index;

import static index.IndexEx.indexDocs;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author smita
 */
public class IndexWikiAbstract {
    public static void main(String[]args) throws FileNotFoundException, IOException
    {
        //index
         String indexPath = "/Users/smita/Documents/ES/index/abstract/";
        String docsPath = null;
        boolean create = true;
        
        String path="/Users/smita/Documents/data/dbpedia/long_abstracts_en.nt";
        
        Date start = new Date();
//        System.out.println("Indexing to directory '" + indexPath + "'...");
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
//         // iwc.setRAMBufferSizeMB(256.0);
        IndexWriter writer = new IndexWriter(dir, iwc);
                
        FileInputStream inputStream = null;
        Scanner sc = null;
        try 
        {
            int linecount=0;
            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");
            String hash = sc.nextLine();
            while (sc.hasNextLine()) 
            {
                
                linecount++;
                String line = sc.nextLine();
                
                
                try
                {
                String title=line.split(" ")[0];
                String prop=line.split(" ")[1];
                String abs=line.substring(title.length()+prop.length()+2);
                //System.out.println(abs);
                abs=abs.substring(0,abs.length()-6);
                title=title.replaceAll("_"," ");
                title=title.substring(29,title.length()-1);
                //System.out.println(abs);
                
                //index line as a doc
                Document doc = new Document();
                doc.add(new TextField("title",title,Field.Store.YES));
                doc.add(new TextField("abs",abs,Field.Store.YES));
                if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) 
                {
                  System.out.println("adding " +linecount );
                  writer.addDocument(doc);
                  
                }
                else 
                {
                  System.out.println("updating " );
                  //writer.updateDocument(new Term("path", file.toString()), doc);
                }
            }
                
                catch(Exception e2){}
                
                
                
                
            }
            
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) 
            {
                throw sc.ioException();
            }
        } 
        finally 
        {
            if (inputStream != null) 
            {
                inputStream.close();
            }
            if (sc != null) 
            {
                sc.close();
            }
        }
        writer.close();

                Date end = new Date();
                System.out.println(end.getTime() - start.getTime() + " total milliseconds");
      

    }
}
