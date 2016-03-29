/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package index;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author smita
 */
public class Indexcategory {
    public static void main(String[]args) throws IOException
    {
        String indexPath = "/Users/smita/Documents/ES/index/abstract/";
        String docsPath = null;
        boolean create = true;
        
        String path="/Users/smita/Documents/data/dbpedia/short_abstracts_en.nq";
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
        IndexWriter writer = new IndexWriter(dir, iwc);
                
        FileInputStream inputStream = null;
        Scanner sc = null;
        try 
        {
           
            int linecount=0;
            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");
            String ignore=sc.nextLine();
            while (sc.hasNextLine()) 
            {
                linecount++;
                String line = sc.nextLine();
                 //System.out.println(line);
                try
                {
                String article=line.split("> ")[0];
                String category=line.split("> ")[2];
                //System.out.println(article+" ++ "+category);
                
                //index row
                
                    article=article.substring(29,article.length()-1);
                    //category=category.substring(38,category.length()-1);
                    //System.out.println(article+"    "+category);
                    
                    Document doc = new Document();
                doc.add(new TextField("article",article,Field.Store.YES));
                doc.add(new TextField("category",category,Field.Store.YES));
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
                catch(Exception e){}
                
            }
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
    }
    
}
