/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
/**
 *
 * @author smita
 */
public class IndexEx {
    
    public static void main(String[]args)
    {
        String indexPath = "index";
        String docsPath = null;
        boolean create = true;
        final Path docDir = Paths.get(docsPath);
    if (!Files.isReadable(docDir)) 
    {
    System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
    System.exit(1);
    }
    
    Date start = new Date();
    try
    {
        System.out.println("Indexing to directory '" + indexPath + "'...");
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        if (create) {
        // Create a new index in the directory, removing any
        // previously indexed documents:
        iwc.setOpenMode(OpenMode.CREATE);
        } else {
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        }
         // iwc.setRAMBufferSizeMB(256.0);
        IndexWriter writer = new IndexWriter(dir, iwc);
        indexDocs(writer, docDir);
        writer.close();
        
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + " total milliseconds");
    }
    catch (IOException e) {
    System.out.println(" caught a " + e.getClass() +
    "\n with message: " + e.getMessage());
    }
    
    
    }
    
    static void indexDocs(final IndexWriter writer, Path path) throws IOException {
    if (Files.isDirectory(path)) {
    Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    try {
    indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
    } catch (IOException ignore) {
    // don't index files that can't be read.
    }
    return FileVisitResult.CONTINUE;
    }
    });
    } else {
    indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
    }
    }
    
    //index single doc
    
    static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
         try (InputStream stream = Files.newInputStream(file)) {
              Document doc = new Document();
              Field pathField = new StringField("path", file.toString(), Field.Store.YES);
              doc.add(pathField);
              doc.add(new LongField("modified", lastModified, Field.Store.NO));
              doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
              if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                  System.out.println("adding " + file);
                  writer.addDocument(doc);
                  
              }else {
                  System.out.println("updating " + file);
                  writer.updateDocument(new Term("path", file.toString()), doc);
              }
         }
    }
    
}
