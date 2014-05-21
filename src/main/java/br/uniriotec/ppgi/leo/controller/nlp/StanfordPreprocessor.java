package br.uniriotec.ppgi.leo.controller.nlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.uniriotec.ppgi.leo.model.PreprocessedSentence;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.ValueAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class StanfordPreprocessor {
	
	
	/**
	 * Annotate a full text, tokenizing it by sentences and then by
	 * words finding POS tags and lemmas. The result is a list of
	 * sentences annotated with special Stanford CoreNLP annotations.
	 * 
	 * @param fullText
	 * @return list of annotated sentences (Stanford's CoreMap objects)
	 */
	public static List<CoreMap> annotateText(String fullText){
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        Annotation document = new Annotation(fullText);
        pipeline.annotate(document);

        return document.get(SentencesAnnotation.class);
	}
	
	
	
	
	/**
	 * Process a text through Stanford CoreNLP API returns a list of PreprocessedSentence
	 * objects, a POJO DTO object that can be used by the entire application independently.
	 * 
	 * @param fullText - plain text composed by multiple sentences
	 * @return a list of preprocessed sentences.
	 */
	public static List<PreprocessedSentence> preprocessText(String fullText){
		List<PreprocessedSentence> preprocessedText = new ArrayList<PreprocessedSentence>();
		
		List<CoreMap> annotatedSentences = annotateText(fullText);
		for(CoreMap sent : annotatedSentences){
			List<CoreLabel> tokens = sent.get(TokensAnnotation.class);
			
			String[] words = new String[tokens.size()];
			String[] lemmas = new String[tokens.size()];
			
			for(int i=0; i<tokens.size(); i++){
				words[i] 	= tokens.get(i).get(ValueAnnotation.class);
				lemmas[i] 	= tokens.get(i).get(LemmaAnnotation.class);
			}
			
			//Specifically call a POS-Tagger instead of using CoreNLP default pos-tagger
			String[] posTags = new String[tokens.size()];
			posTags	= extractPosTagsArray(NlpTagger.tagSentence(sent.toString()));
			
			preprocessedText.add(
					new PreprocessedSentence(sent.toString(), words, posTags, lemmas)
				);
		}
		
		return preprocessedText;
	}
	
	
	
	
	/**
	 * Process a text through Stanford CoreNLP API returns an array of sentences.
	 * 
	 * @param fullText - plain text composed by multiple sentences
	 * @return a list of preprocessed sentences.
	 */
	public static String[] tokenizeSentences(String fullText){
		
		Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        Annotation document = new Annotation(fullText);
        pipeline.annotate(document);

        List<CoreMap> annotatedSentences = document.get(SentencesAnnotation.class);
        String[] sentences = new String[annotatedSentences.size()];
        
		for(int i=0; i<annotatedSentences.size(); i++){
			sentences[i] = annotatedSentences.get(i).toString();
		}
		
		return sentences;
	}




	/**
	 * Extract an array of POS-Tags from a tagged sentence that uses the
	 * form "word1_POS word2_POS word3_POS ._."
	 * 
	 * @param string
	 * @return
	 */
	private static String[] extractPosTagsArray(String string) {
		String[] pairs = string.split(" ");
		List<String> posTags = new ArrayList<String>();
		
		for(String pair : pairs){
			posTags.add(pair.split("_")[1]);
		}
		
		return posTags.toArray(new String[posTags.size()]);
	}
	
	
}
