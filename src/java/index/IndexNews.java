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
public class IndexNews {
    public static void main(String[]args) throws FileNotFoundException, IOException
    {
        //index
        String indexPath = "/Users/smita/Documents/ES/index/news/";
        String docsPath = null;
        boolean create = true;
        
        String path="/Users/smita/Documents/data/newsSpace.txt";
        
        Date start = new Date();
//        System.out.println("Indexing to directory '" + indexPath + "'...");
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        if (create) {
//         Create a new index in the directory, removing any
//         previously indexed documents:
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }
         // iwc.setRAMBufferSizeMB(256.0);
        IndexWriter writer = new IndexWriter(dir, iwc);
                
        FileInputStream inputStream = null;
        Scanner sc = null;
        try 
        {
            int linecount=0;
            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");
            //String hash = sc.nextLine();
            while (sc.hasNextLine()) 
            {
                String item="";
                linecount++;
                String line = sc.nextLine();
                System.out.println("-   "+line);
//                if(line.endsWith("\n"))
//                {
//                    item=item+" "+line;
//                    line=sc.nextLine();
//                    //contineous
//                }
//                else
//                {
//                    item=line;
//                }
                String agency=line.split("\t")[0];
//                String title=line.split("\\t")[2];
//                String abs=line.split("\\t")[5];
//                String date=line.split("\\t")[7];
                //System.out.println("-   "+agency);
                //System.out.println(redirectLbl);
//                try
//                {
//                senseLbl=senseLbl.substring(29,senseLbl.length()-1);
//                System.out.print("sense: "+senseLbl +": ");
////                lbl=lbl.replaceAll("_", " ");
////                String cat=line.split(" ")[2];
//                  //System.out.println(idxLbl);
//                idxLbl=idxLbl.substring(29,idxLbl.length()-1);
//                idxLbl=idxLbl.replaceAll("_"," ");
//                cat=cat.replaceAll("_", " ");
                //System.out.println(idxLbl);
                
                //index line as a doc
//                Document doc = new Document();
//                doc.add(new TextField("idxlbl",idxLbl,Field.Store.YES));
//                doc.add(new TextField("senses",senseLbl,Field.Store.YES));
//                if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) 
//                {
//                  System.out.println("adding " +linecount );
//                  writer.addDocument(doc);
//                  
//                }
//                else 
//                {
//                  System.out.println("updating " );
//                  //writer.updateDocument(new Term("path", file.toString()), doc);
//                }
//            }
//                
//                catch(Exception e2){}
                
                
                
                
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
        //writer.close();

                Date end = new Date();
                System.out.println(end.getTime() - start.getTime() + " total milliseconds");
      

    }
}
