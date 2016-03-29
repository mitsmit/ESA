/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qe;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author smita
 */
public class CategoryExpansion {
    public static void main(String[]args) throws IOException, ParseException
    {
        getCategories("Promyelocytic_leukemia_protein");
    }
    
    public static ArrayList getCategories(String entity) throws IOException, ParseException
    {
        ArrayList rd=new ArrayList();
        String index = "/Users/smita/Documents/ES/index/category/";
        String field = "article";
        String queries = null;
        int repeat = 0;
        boolean raw = false;
        String queryString = entity;
        int hitsPerPage = 500;
        
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();
        
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse(queryString);
        System.out.println("Searching for: " + query.toString(field));
        
        TopDocs results = searcher.search(query, 5 * hitsPerPage);
    
        ScoreDoc[] hits = results.scoreDocs;
        int numTotalHits = results.totalHits;
        hits = searcher.search(query, numTotalHits).scoreDocs;
        for (int i = 0; i <numTotalHits; i++) {
        Document doc = searcher.doc(hits[i].doc);
        String article = doc.get("article");
        if(article.equalsIgnoreCase(entity)){
        String category = doc.get("category");
        System.out.println(category);
        rd.add(category);
        }
        }
    return rd;
    }
    
}
