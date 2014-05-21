/**
 * 
 */
package br.uniriotec.ppgi.leo.model;


/**
 * @author felipeleao88
 * 
 * Argument term comprises a word, its corresponding lemma and POS tag
 * 
 */
public class ArgumentTerm {
    private String word; 
    private String lemma;
    private String posTag; 

    public ArgumentTerm(String word, String lemma, String posTag) {
        this.word = word;
        this.lemma = lemma;
        this.posTag = posTag;
    }

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getPosTag() {
		return posTag;
	}

	public void setPostag(String posTag) {
		this.posTag = posTag;
	}
    
    public String toString(){
    	return "W:'"+word+"'|L:'"+lemma+"'|POS:'"+posTag+"'";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}
    
    

}