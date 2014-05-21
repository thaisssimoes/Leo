package br.uniriotec.ppgi.leo.controller.nlp;

import java.security.InvalidParameterException;

import br.uniriotec.ppgi.leo.controller.nlp.stanford.NlpStanfordTagger;
import br.uniriotec.ppgi.leo.controller.utils.StringUtils;

/** 
 * This class implements generic natural language processing techniques.
 * 
 * @author Felipe Leao
 *
 */
public class NlpTagger
{	
	
	/**
	 * <p>The method invokes a specific POS-tagging approach based on the source 
	 * of the text. The text to be tagged has some especial characters like {, [, 
	 * (, <, +, &, $, etc. removed, in order to avoid mistakes.</p>
	 * 
	 * @param  textToBeTagged - the text to be processed
	 * @return tagged text
	 * @throws InvalidParameterException
	 */
	public static String[] tagSentences(String[] sentencesToBeTagged) throws InvalidParameterException
	{	
		String[] cleanSentences = new String[sentencesToBeTagged.length];
		//Remove special characters that may confuse the tagger from text
		for(int i=0; i<sentencesToBeTagged.length; i++){
			cleanSentences[i] = StringUtils.removeSpecialChar(sentencesToBeTagged[i]);
		}
		
		NlpStanfordTagger stanfordTagger = new NlpStanfordTagger();
		String[] taggedSentences = stanfordTagger.tagStandartTagger(cleanSentences);
			
		
		return taggedSentences;
	}

	/**
	 * <p>The method tags a single sentence, erturning a string of tokens alog with pos-tags
	 * separated by blank spaces</p>
	 * 
	 * @param sentence - the text to be processed
	 * @return
	 */
	public static String tagSentence(String sentence) {
		NlpStanfordTagger stanfordTagger = new NlpStanfordTagger();
		return stanfordTagger.tagStandartTagger(new String[]{sentence})[0];
	}


	
}