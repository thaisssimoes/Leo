package br.uniriotec.ppgi.leo.controller.nlp.stanford;

import java.io.IOException;
import java.security.InvalidParameterException;

import org.apache.log4j.Logger;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Class responsible for implementing methods related to Stanford POS-Tagger
 * Each method exposed intends to execute the Tagger with specific settings.
 * 
 * @author Felipe Leao
 *
 */
public class NlpStanfordTagger 
{	
	private static Logger logger = Logger.getLogger(NlpStanfordTagger.class);


	/**
	 * Specifc method to tag a text using the leftWords architecture.
	 * 
	 * @param sentencesToBeTagged
	 * @return
	 * @throws InvalidParameterException
	 */
	public String[] tagLeftWords(String[] sentencesToBeTagged) throws InvalidParameterException{
		logger.debug("Executing : tagLeftWords");
		
		String[] taggedSentences = new String[sentencesToBeTagged.length];
		
		try{
			MaxentTagger tagger = NlpStanfordMaxentTagger.getTaggerEnWsjL3w();
			taggedSentences = posTagSentences(sentencesToBeTagged, tagger);
			
		}catch (IOException e){
			logger.fatal("I/O Failure when attempting to open the Model.");
			
		}catch (ClassNotFoundException e) {
			logger.fatal("ClassNotFoundException when attempting to load the Model.");
		}
		
		return taggedSentences;
	}
	
	
	
	public String[] posTagSentences(String[] sentencesToBeTagged, MaxentTagger tagger){
		logger.debug("Executing : tagLeftWords");
		
		String[] taggedSentences = new String[sentencesToBeTagged.length];
		
		for(int i=0; i<sentencesToBeTagged.length; i++){
			taggedSentences[i] = tagger.tagString(sentencesToBeTagged[i]);
		}
			
		return taggedSentences;
	}
	
	
	
	/**
	 * Specifc method to tag a text using a bidirectional architecture 
	 * 
	 * @param sentencesToBeTagged
	 * @return
	 * @throws InvalidParameterException
	 */
	public String[] tagBidirection(String[] sentencesToBeTagged) throws InvalidParameterException
	{
		logger.debug("Executing : tagBidirection");
		
		String[] taggedSentences = new String[sentencesToBeTagged.length];
		
		try{
			MaxentTagger tagger = NlpStanfordMaxentTagger.getTaggerEnWsjBid();
			taggedSentences = posTagSentences(sentencesToBeTagged, tagger);
			
		}catch (IOException e){
			logger.fatal("I/O Failure when attempting to open the Model.");
			
		}catch (ClassNotFoundException e) {
			logger.fatal("ClassNotFoundException when attempting to load the Model.");
		}
		
		return taggedSentences;
	}
	
	/**
	 * <p>Specifc method to tag a text using a the standart 
	 * tagger returned by the architecure.</p>
	 * 
	 * @param  textToBeTagged - the text to be tagged.
	 * @return Tagged Text
	 * @throws InvalidParameterException - if the language informed is not compatible
	 */
	public String[] tagStandartTagger(String[] sentencesToBeTagged) throws InvalidParameterException
	{
		logger.debug("Executing : tagStandartTagger");
		
		String[] taggedSentences = new String[sentencesToBeTagged.length];
		
		try{
			MaxentTagger tagger = NlpStanfordMaxentTagger.getENStandartTagger();
			taggedSentences = posTagSentences(sentencesToBeTagged, tagger);
			
		}catch (IOException e){
			logger.fatal("I/O Failure when attempting to open the Model.");
			
		}catch (ClassNotFoundException e) {
			logger.fatal("ClassNotFoundException when attempting to load the Model.");
		}
		
		return taggedSentences;
	}

	

}