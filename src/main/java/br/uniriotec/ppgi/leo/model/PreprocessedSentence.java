package br.uniriotec.ppgi.leo.model;

public class PreprocessedSentence {
	private String fullSentence;
	private String[] words;
	private String[] posTags;
	private String[] lemmas;
	
	public PreprocessedSentence(){}
	
	public PreprocessedSentence(String fullSentence, String[] words, String[] posTags, String[] lemmas){
		this.fullSentence = fullSentence;
		this.words = words;
		this.posTags = posTags;
		this.lemmas = lemmas;
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
	
	public String listWords(){
		String fullList = "[";
		for(int i=0; i<words.length; i++){
			fullList += "'"+words[i]+"', ";
		}
		fullList = fullList.substring(0, fullList.length()-1)+"]";
		return fullList;
	}
	
	public String listPosTags(){
		String fullList = "[";
		for(int i=0; i<posTags.length; i++){
			fullList += "'"+posTags[i]+"', ";
		}
		fullList = fullList.substring(0, fullList.length()-1)+"]";
		return fullList;
	}
	
	public String listLemmas(){
		String fullList = "[";
		for(int i=0; i<lemmas.length; i++){
			fullList += "'"+lemmas[i]+"', ";
		}
		fullList = fullList.substring(0, fullList.length()-1)+"]";
		return fullList;
	}
	
	
}
