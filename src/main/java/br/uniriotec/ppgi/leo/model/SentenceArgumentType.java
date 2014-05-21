/**
 * 
 */
package br.uniriotec.ppgi.leo.model;


/**
 * @author root
 *
 */
public enum SentenceArgumentType {

	AGENT         		("Agent", 0),
	VERB            	("Verb", 1),
	DIRECT_OBJECT   	("Direct Object", 2),
	INDIRECT_OBJECT 	("Indirect Object", 4);
	
	private String text;
	private int value;
	
	private SentenceArgumentType(String text, int value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public int getValue() {
		return value;
	}
	
	public static SentenceArgumentType fromInt(int value){
		switch(value){
			case 0: return AGENT;
			case 1: return VERB;
			case 2: return DIRECT_OBJECT;
			case 4: return INDIRECT_OBJECT;
			default: return null;
		}
	}
		
}
