/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qe;

/**
 *
 * @author smita
 */


import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
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
import search.WordnetHelper;

/**
 *
 * @author smita
 */
public class Disambiguation {
    public static void main(String[]args) throws IOException, ParseException, JWNLException
    {
        Disambiguation d=new Disambiguation();
        d.getSenses("leukemia");
        //ArrayList ss=getWordnetSenses("“satellite");
    }
    
    public  static HashSet getSenses(String entity) throws IOException, ParseException
    {
        entity=entity.toLowerCase();
        HashSet rd=new HashSet();
        
        String index = "/Users/smita/Documents/ES/index/disambiguation/";
        String field = "idxlbl";
        String queries = null;
        int repeat = 0;
        boolean raw = false;
        int hitsPerPage = 20;
        
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();
        
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse(entity);
        TopDocs results = searcher.search(query, 1 * hitsPerPage);
        
        if(results.totalHits>0)
        {
            ScoreDoc[] hits = results.scoreDocs;
            int numTotalHits = results.totalHits;
            hits = searcher.search(query, numTotalHits).scoreDocs;
            //System.out.println("nu of senses: " + numTotalHits);
            for (int i = 0; i <numTotalHits; i++) 
            {
            Document doc = searcher.doc(hits[i].doc);
            String idx = doc.get("idxlbl");
            idx=idx.trim();
            if(idx.equalsIgnoreCase(entity)||idx.equalsIgnoreCase(entity+" (disambiguation)"))
            {
            String articles = doc.get("senses");
            //System.out.println("    searched label "+idx);
            System.out.println("    Sense "+articles);
            rd.add(articles);
            }
            else
            {
                 System.out.println("    searched label "+idx);
            }
//            articles=articles.replaceAll("_"," ");
//            articles=articles.replaceAll("\\)","");
//            if(articles.indexOf("(")>-1)
//            {
//                String []words=articles.split("\\(");
//                String ft=words[0].trim();
//                String lt=words[1].trim();
//                lt=lt.replaceAll(" ","_");
//                articles="\""+ft+"\" +"+lt;
//                System.out.println("    Sense "+articles);
//            }
            //articles=articles.replaceAll("_"," ");
            
           
//            articles=articles.replaceAll("\\("," +");
//            articles=articles.replaceAll("\\)","");
//            articles=articles.replaceAll(","," AND ");


           
            
            }
        }
        else
        {
            //System.out.println("No different sense found for query : "+entity);
            //try from wordnet
            //ArrayList ss=getWordnetSenses(entity);
            
        }
    //ArrayList ss=getWordnetSenses(entity);
    //rd.addAll(ss);
    System.out.println("Total number of senses: "+rd.size());
    return rd;
    }
    
    
    public static ArrayList getWordnetSenses(String w) throws JWNLException
    {
        ArrayList ss=new ArrayList();
        WordnetHelper.initialize("/Users/smita/Documents/jwnl14-rc2/config/file_properties.xml");
       
        //findPartsOfSpeech(t);
        IndexWord word = WordnetHelper.getWord(POS.NOUN, w);
        Synset[] senses = word.getSenses();
        // Display all definitions
        
        for (int i = 0; i < senses.length; i++) 
        {
            String gloss=senses[i].getGloss();
            ss.add(gloss);
            System.out.println("    senses: " + gloss);
        } 
        
        return ss;
    }
    
    public static boolean hexChecker(char c) {
        String string = "0123456789abcdefABCDEF";
        return string.indexOf(c) > -1;
    }
}
