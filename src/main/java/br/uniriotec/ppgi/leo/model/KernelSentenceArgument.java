/**
 * 
 */
package br.uniriotec.ppgi.leo.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author felipeleao88
 * 
 * 
 *
 */
public class KernelSentenceArgument {
	
	private ArrayList<ArgumentTerm> value = new ArrayList<ArgumentTerm>();
	private SentenceArgumentType type;
	
	
	public KernelSentenceArgument(String[] words, String[] lemmas, String[] posTags, SentenceArgumentType type){
		this.type = type;
		for(int i=0; i< words.length; i++){
			value.add(new ArgumentTerm(words[i], lemmas[i], posTags[i]));
		}
	}
	
	public KernelSentenceArgument(ArrayList<ArgumentTerm> argumentTermList, SentenceArgumentType type){
		this.type = type;
		this.value = argumentTermList;
	}
	
	public ArrayList<ArgumentTerm> getValue() {
		return value;
	}
	public void setValue(ArrayList<ArgumentTerm> value) {
		this.value = value;
	}
	public SentenceArgumentType getType() {
		return type;
	}
	public void setType(SentenceArgumentType type) {
		this.type = type;
	}
	
	
	
	public String toReadable(){
		if(value.size() > 0){
			String[] noPrevSpaceTags = {")", "*", ",", ".", ":"};
			String[] noAfterSpaceTags = {"("};
			String result = "";
			
			for(ArgumentTerm triple : value){
				// If the "word" being iterated is tagged as a "noPrevSpaceTags" item, then remove spaces in the end of the string
				if(Arrays.asList(noPrevSpaceTags).contains(triple.getPosTag())){
					result = result.trim() + triple.getWord();
				}else{
					result += triple.getWord();
					// If the "word" being iterated is tagged as a "noAfterSpaceTags" item, then does not add a space after
					if(Arrays.asList(noAfterSpaceTags).contains(triple.getPosTag()) == false){
						result += " ";
					}
				}
			}
			
			return result;
		}else{
			return "";
		}
	}
	
}
