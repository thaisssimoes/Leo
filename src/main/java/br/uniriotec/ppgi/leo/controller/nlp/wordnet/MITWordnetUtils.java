package br.uniriotec.ppgi.leo.controller.nlp.wordnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;

import br.uniriotec.ppgi.leo.model.WordnetSynset;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ILexFile;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

public class MITWordnetUtils{
	
	private static Logger logger = Logger.getLogger(MITWordnetUtils.class);
	
	private static IDictionary dict = null;
	
	
	/**
	 * Retrieves the first possible stem of a word.
	 * 
	 * @param word
	 * @param pos
	 * @return
	 * @throws IOException
	 */
	public static String stemWord(String word, POS pos) throws IOException{
		WordnetStemmer stemmer = new WordnetStemmer(getDictionary());
		return stemmer.findStems(word, pos).get(0);
	}
	
	
	
	
	
	/**
	 * Retrieves a WordnetSynset object, which contains wordnet data about a synset, when provided
	 * with a word, its POS tag and the intended sense (1, 2, 3 etc.). When using the method it must
	 * be taken into account that even if the intended sense is the first one and the user informs the
	 * value "1" for senseInnerID, the method will search for the first synset in the array of senses, 
	 * which will be zero (0).
	 * 
	 * The object returned also contains information about the supersense selected.
	 * 
	 * @param word - the word searched
	 * @param pos - if it is a Verb, Adjective, Noun or Adverb
	 * @param senseInnerID - which of the synsets to be returned
	 * @return
	 * @throws IOException
	 */
	public WordnetSynset getWordnetWord(String word, POS pos, int senseInnerID) throws IOException {
		IDictionary dict = getDictionary();

		IIndexWord idxWord = dict.getIndexWord(word, pos); 
		IWordID wordID = idxWord.getWordIDs().get(senseInnerID-1); //subtracts "1" because the array starts with "0"
		IWord wnWord = dict.getWord(wordID);
		
		WordnetSynset wnSynset = new WordnetSynset();
		wnSynset.setId(wordID.toString());
		wnSynset.setLemma(wnWord.getLemma());
		wnSynset.setGloss(wnWord.getSynset().getGloss());
		wnSynset.setPosTag(pos);
		wnSynset.setSupersense(getSupersense(word, pos, senseInnerID));
		
		return wnSynset;
		
	}
	
	
	public static void getLemma(String word, POS pos) throws IOException{
		IDictionary dict = getDictionary();
		IIndexWord idxWord = dict.getIndexWord(word, pos); 
		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord wnWord = dict.getWord(wordID);
		System.out.println(wnWord.getLemma());
	}
	
	
	
	/**
	 * Retrieves the supersense of a synset. The synset itself is identified using
	 * the parameters given and then the LexicalFile corresponding to the supersense
	 * is returned.
	 * 
	 * OBS: The senseID must be the one seen by a common userwhen accessing the wordnet 
	 * through a browser, starting at "1". The method will adjust the given ID to a valid
	 * index of the wordnet.
	 * 
	 * @param word
	 * @param pos
	 * @param senseId
	 * @return the corresponding supersense
	 * @throws IOException
	 */
	public ILexFile getSupersense(String word, POS pos, int senseId) throws IOException{
		IDictionary dict = getDictionary();
		IIndexWord idxWord = dict.getIndexWord (word, pos);
        IWordID wordID = idxWord.getWordIDs().get(senseId-1);
        IWord wordObject = dict.getWord (wordID);   
        ISynset synset = wordObject.getSynset();
        return synset.getLexicalFile();
	}
	
	
	
	
	
	
	
	public static IWord getSenseById(String word, POS pos, String senseGeneralID) throws IOException{
		IDictionary dict = getDictionary();
		IIndexWord idxWord = dict.getIndexWord(word, pos); 
		IWord wnWord = null;
		for(IWordID wordID : idxWord.getWordIDs()){
			String id = wordID.toString().substring(4,12);
			if(id.equals(senseGeneralID)){
				wnWord = dict.getWord(wordID);
				break;
			}
		}
		return wnWord;
	}
	
	
	/**
	 * Instantiate a dictionary object used by the MIT JWI api to 
	 * query the Wordnet. The dictionary returned is a singleton.
	 * 
	 * @return the wordnet dictionary
	 * @throws IOException
	 */
	private static synchronized IDictionary getDictionary() throws IOException{
		if(dict == null){
			logger.debug("Instantiating new Wordnet Dictionary");
			// construct the URL to the Wordnet dictionary directory
			String wnhome = System.getenv("WNHOME");
			
			//Verify if the WNHOME environment variable is set and print warn inf not
			if(wnhome.trim().equals("")){
				logger.error("The WNHOME variable is not set. Set it to indicate your Wordnet home directory.");
			}
			
			String path = wnhome + File.separator + "dict"; 
			URL url = new URL("file", null, path);
			// construct the dictionary object and open it
			dict = new Dictionary(url); 
			dict.open();
		}
		
		return dict;
	}

}
