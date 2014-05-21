package br.uniriotec.ppgi.leo.controller.nlp.wsd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import br.uniriotec.ppgi.leo.controller.nlp.wordnet.MITWordnetUtils;
import br.uniriotec.ppgi.leo.controller.utils.AppConfigurer;
import br.uniriotec.ppgi.leo.model.MainTerm;
import br.uniriotec.ppgi.leo.model.ProcessedSentence;
import br.uniriotec.ppgi.leo.model.WordnetSynset;
import edu.mit.jwi.item.POS;


/**
 * This class is meant to comprise methods related to building and reading files
 * inputed into and outputed by Sense Relate Word Sense Disambiguation systems, 
 * proposed and developed by Ted Pedersen.
 * 
 * @author Felipe Leao
 * @version 1.0
 * @since 31/04/2014
 *
 */
public class SenseRelateParser {
	private static Logger logger = Logger.getLogger(SenseRelateParser.class);
	
	
	
	/**
	 * <p>Creates a temporary file formatted according to the WNTAGGED format,
	 * used by some WSD systems based on the Wordnet.
	 * 
	 * The file is composed by one sentence per line, where each word carries along
	 * the Wordnet POS-tag corresponding to the Treebank tag used when tagging
	 * all words in each sentence.
	 * </p>
	 * 
	 * @param srlProcessedSentences - list of objects that represents a processed sentence
	 * @return a file in WNTAGGED format
	 * @throws IOException 
	 */
	public static File tempInputFileBuilder(List<ProcessedSentence> srlProcessedSentences, Set<MainTerm> mainTermsList) throws IOException{
		String tempFileExtension = UUID.randomUUID().toString();
		Set<String> stopwords = loadStopWords();
		
		//identify which main terms are compound nouns and group them into a List
		ArrayList<MainTerm> compoundNouns = new ArrayList<MainTerm>();
		for(MainTerm mt : mainTermsList){
			if(mt.isCompound()){
				compoundNouns.add(mt);
			}
		}
		
		
		try{
			File wntagged_file = File.createTempFile("wsd_input_"+tempFileExtension, ".tmp"); 
			wntagged_file.deleteOnExit();
			
			FileWriter fw = new FileWriter(wntagged_file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(ProcessedSentence ps : srlProcessedSentences){
				
				String wntaggedSentence = "";
				
				for(int i=0; i<ps.getWords().length; i++){
					
					//If it is punctuation, skip
					if((ps.getLemmas()[i].matches("\\p{Punct}")) == true){
						continue;
					}
					
					//if it is not a stopword add the WNTAG
					if(stopwords.contains(ps.getLemmas()[i]) == false){
						//add the word to the sentence
						wntaggedSentence += ps.getLemmas()[i]+"#";
						if(ps.getPosTags()[i].matches("\\bJJ.*")){ //If is Adjective use postag "a"
							wntaggedSentence += "a";
						}else if(ps.getPosTags()[i].matches("\\bRB.*")){ //If is Adverb use postag "r"
							wntaggedSentence += "r";
						}else if(ps.getPosTags()[i].matches("\\bVB.*")){ //If is Verb use postag "v"
							wntaggedSentence += "v";
						}else if(ps.getPosTags()[i].matches("\\bNN.*")){ //If is Noun use postag "n"
							wntaggedSentence += "n";
						}else{
							wntaggedSentence += "?";
						}
						
						//adds a blank space to separate from the next word
						wntaggedSentence += " ";
						
					}
					
				}
				
				//replace mistaken occurences of compound nouns by a single compound nouns
				//Ex: "committee#n member#n" will be replaced by "committee_member#n"
				
				logger.debug("Raw sentence      : "+wntaggedSentence);
				//check if the sentence uses a compound noun
				String resultingWntaggedSentence = wntaggedSentence;
				for(MainTerm cn : compoundNouns){
					//replaces all "_" by "#n" and adds a final "#n", since there is no "_" in the end
					String compoundNounWithSpaces = cn.lemmaToString().replaceAll("_", "#n ")+"#n";
					resultingWntaggedSentence = resultingWntaggedSentence.replaceAll("(?i)(\\b"+compoundNounWithSpaces+"\\b)", cn.lemmaToString()+"#n");
				}
				logger.debug("Resulting Sentence: "+resultingWntaggedSentence);
				
				//Add sentence to file
				bw.write(resultingWntaggedSentence+"\n");
			}
			
			bw.close();
			return wntagged_file;
			
		}catch(IOException ex){
			logger.error("ERROR when build WSD-WNTAGGED input file. "+ex.getMessage());
			throw new IOException("ERROR when trying to create " +
					"temp file to use as input on the WSD system. "+ex.getMessage());
		}
	}
	
	
	
	
	
	
	/**
	 * Simple method to load a file containing stopwords
	 * 
	 * @return
	 * @throws IOException
	 */
	private static Set<String> loadStopWords() throws IOException{
		Set<String> stopWords = new LinkedHashSet<String>();
        BufferedReader swBuffer = null;
		try {
			swBuffer = new BufferedReader(new FileReader("./Config/stopwords.words"));
			for(String line;(line = swBuffer.readLine()) != null;){
	           stopWords.add(line.trim());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(swBuffer != null){
				swBuffer.close();
			}
		}
        
        return stopWords;
	}
	
	
	
	
	
	
	
	/**
	 * Based on the output of the WSD system the method identifies the supersenses for each 
	 * noun and verb and relates the wordnet data (synset, supersense, etc.) to each argument
	 * of the Kernel Sentences.
	 * 
	 * (TODO Only the first supersense is returned, in the future would be wise to count
	 * on the user opinion for cases where a single synset points to multiple supersenses)
	 * 
	 * @param ProcessedSentences
	 * @param mainTermsList
	 * @throws IOException
	 */
	public static void parseWsdProcessedFile(List<ProcessedSentence> processedSentences, Set<MainTerm> mainTermsList, String wsdOutputFile) throws IOException{
		
		logger.debug("Loading WSD output");
		AppConfigurer cfg = AppConfigurer.getInstance();
		String wsdOutputFileContent = cfg.loadTextFile(wsdOutputFile);
		String[] outputLines = wsdOutputFileContent.split("\n|\r");
		
		for(int i=0; i<outputLines.length; i++){
			ProcessedSentence sent = processedSentences.get(i);
			String[] wordnetWords = outputLines[i].split("[ ]+");
			
			//For each word find the supersense and add the processed word to the sentence
			for(String processedWord : wordnetWords){
				String[] tokens = processedWord.split("#");
				if(tokens.length != 3){ //Checks if it is not a bizarre output like a blank line, or blank spaces
					System.out.println(processedWord);
					continue;
				}
				
				String word = tokens[0];
				POS pos = null;
				int innerSenseID = -1;
				
				//if there is a valid innerSenseId, convert it
				if(tokens[2].matches("[0-9]*")){
					innerSenseID = Integer.parseInt(tokens[2]);
				}else{
					//The word was not found in Wordnet, so skip it.
					continue;
				}
				
				//Define POS
				if(tokens[1].equals("n")){
					pos = POS.NOUN;
				}else if(tokens[1].equals("v")){
					pos = POS.VERB;
				}else if(tokens[1].equals("a")){
					pos = POS.ADJECTIVE;
				}else if(tokens[1].equals("r")){
					pos = POS.ADVERB;
				}
				
				//Find the corresponding Synset and supersense
				MITWordnetUtils wnUtils = new MITWordnetUtils();
				WordnetSynset wnSynset = wnUtils.getWordnetWord(word, pos, innerSenseID);
				
				//Add the WordnetSynset object to the list in the sentence
				sent.getWordnetSynsets().add(wnSynset);
				
			}
		}
		
		
		
	}
	
	
	
	
	
}




