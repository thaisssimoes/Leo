/**
 * 
 */
package br.uniriotec.ppgi.leo.model;

import java.util.ArrayList;

/**
 * Object that represents a processed sentence, comprinsing informations such as 
 * the raw sentence itself, POS-tags, lemmas, Semantic role labels, wordnet synsets,
 * etc.
 * 
 * @author felipeleao88
 *
 */
public class ProcessedSentence {
	private String fullSentence;
	private String[] words;
	private String[] posTags;
	private String[] lemmas;
	private ArrayList<SemanticRelation> semanticRelations = new ArrayList<SemanticRelation>();
	private ArrayList<KernelSentence> kernelSentences = new ArrayList<KernelSentence>();
	private ArrayList<WordnetSynset> wordnetSynsets = new ArrayList<WordnetSynset>();
	
	
	public ProcessedSentence(){}
	
	/**
	 * Builds a Processed object based on a Preprocessed object
	 * 
	 * @param preprocessedSentence
	 */
	public ProcessedSentence(PreprocessedSentence preprocessedSentence){
		this.fullSentence 	= 	preprocessedSentence.getFullSentence();
		this.words 			= 	preprocessedSentence.getWords();
		this.posTags 		= 	preprocessedSentence.getPosTags();
		this.lemmas 		= 	preprocessedSentence.getLemmas();
	}
	
	
	
	public String getFullSentence() {
		return fullSentence;
	}

	public void setFullSentence(String fullSentence) {
		this.fullSentence = fullSentence;
	}

	public String[] getWords() {
		return words;
	}

	public void setWords(String[] words) {
		this.words = words;
	}

	public String[] getPosTags() {
		return posTags;
	}

	public void setPosTags(String[] posTags) {
		this.posTags = posTags;
	}

	public String[] getLemmas() {
		return lemmas;
	}

	public void setLemmas(String[] lemmas) {
		this.lemmas = lemmas;
	}

	public ArrayList<SemanticRelation> getSemanticRelations() {
		return semanticRelations;
	}

	public void setSemanticRelations(ArrayList<SemanticRelation> semanticRoles) {
		this.semanticRelations = semanticRoles;
	}

	public ArrayList<KernelSentence> getKernelSentences() {
		return kernelSentences;
	}

	public void setKernelSentences(ArrayList<KernelSentence> kernelSentences) {
		this.kernelSentences = kernelSentences;
	}

	public ArrayList<WordnetSynset> getWordnetSynsets() {
		return wordnetSynsets;
	}

	public void setWordnetSynsets(ArrayList<WordnetSynset> wordnetSynsets) {
		this.wordnetSynsets = wordnetSynsets;
	}


	
}
