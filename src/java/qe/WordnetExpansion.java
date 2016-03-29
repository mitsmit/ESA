/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qe;

import java.util.ArrayList;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import search.WordnetHelper;
import util.Stopword;

/**
 *
 * @author smita
 */
public class WordnetExpansion {
    public static void main(String[]args) throws JWNLException{
        String w="spider";
        getSenses(w);
    }
    
    public static ArrayList getSenses(String w) throws JWNLException
    {
        ArrayList ss=new ArrayList();
        WordnetHelper.initialize("/Users/smita/Documents/jwnl14-rc2/config/file_properties.xml");
       
        //findPartsOfSpeech(t);
        IndexWord word = WordnetHelper.getWord(POS.NOUN, w);
        Synset[] senses = word.getSenses();
        // Display all definitions
        for (int i = 0; i < senses.length; i++) 
        {
            
            System.out.println("Word senses: "+word + ": " + senses[i].getGloss());
        } 
        
        return ss;
    }
    public static ArrayList getWordnetSenses(String w) throws JWNLException {
        ArrayList ss = new ArrayList();
        WordnetHelper.initialize("/Users/smita/Documents/jwnl14-rc2/config/file_properties.xml");

        //findPartsOfSpeech(t);
        IndexWord word = WordnetHelper.getWord(POS.NOUN, w);
        Synset[] senses = word.getSenses();
        // Display all definitions
        for (int i = 0; i < senses.length; i++) {

            String gloss = senses[i].getGloss();
            if (gloss.indexOf(";") > -1) {
                //gloss=gloss.replaceAll(";","|");
                int ind = gloss.indexOf(";");
                gloss = gloss.substring(0, ind);
                String clean = Stopword.checkString(gloss);
                clean = clean.trim();
                clean = clean.replaceAll(" ", " AND ");
                ss.add(clean);
                System.out.println(clean);
            } else {
                ss.add(gloss);
            }
            //out.println("    senses: " + gloss);
        }

        return ss;
    }
   
     
}
