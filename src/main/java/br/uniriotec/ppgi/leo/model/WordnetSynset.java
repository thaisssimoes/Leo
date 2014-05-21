package br.uniriotec.ppgi.leo.model;

import edu.mit.jwi.item.ILexFile;
import edu.mit.jwi.item.POS;

/**
 * Class to comprise all informations about a word processed by the WSD system
 * and further complemented by info such as the related supersense, lemma, etc.
 * 
 * @author felipeleao88
 *
 */
public class WordnetSynset {
	private String wordId;
	private String lemma;
	private String gloss;
	private POS posTag;
	private ILexFile supersense;
	
	public String getWordId() {
		return wordId;
	}
	public void setId(String id) {
		this.wordId = id;
	}
	public String getLemma() {
		return lemma;
	}
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	public String getGloss() {
		return gloss;
	}
	public void setGloss(String gloss) {
		this.gloss = gloss;
	}
	public POS getPosTag() {
		return posTag;
	}
	public void setPosTag(POS posTag) {
		this.posTag = posTag;
	}
	public ILexFile getSupersense() {
		return supersense;
	}
	public void setSupersense(ILexFile supersense) {
		this.supersense = supersense;
	}
	public String toString(){
		return wordId+" \t "+lemma+" \t "+supersense.getName()+" \t "+gloss;
	}
}
