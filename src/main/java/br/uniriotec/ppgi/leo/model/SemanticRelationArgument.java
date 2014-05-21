/**
 * 
 */
package br.uniriotec.ppgi.leo.model;

import java.util.ArrayList;

/**
 * @author root
 *
 */
public class SemanticRelationArgument {
	private int argLine;
	private String shortArg;
	private String shortArgLemma;
	private String shortArgPosTag;
	private String fullArg;
	private ArrayList<ArgumentTerm> argumentTerms;
	
	public SemanticRelationArgument(int argLine, String shortArg, String shortArgPosTag, String shortArgLemma){
		this.argLine = argLine;
		this.shortArg = shortArg;
		this.shortArgPosTag = shortArgPosTag;
		this.shortArgLemma = shortArgLemma;
		this.argumentTerms = new ArrayList<ArgumentTerm>();
	}
	
	public int getArgLine() {
		return argLine;
	}
	public void setArgLine(int argLine) {
		this.argLine = argLine;
	}
	public String getShortArg() {
		return shortArg;
	}
	public void setShortArg(String shortArg) {
		this.shortArg = shortArg;
	}
	public String getFullArg() {
		return fullArg;
	}
	public void setFullArg(String fullArg) {
		this.fullArg = fullArg;
	}
	public String getShortArgPosTag() {
		return shortArgPosTag;
	}
	public void setShortArgPosTag(String shortArgPosTag) {
		this.shortArgPosTag = shortArgPosTag;
	}

	public String getShortArgLemma() {
		return shortArgLemma;
	}

	public void setShortArgLemma(String shortArgLemma) {
		this.shortArgLemma = shortArgLemma;
	}

	public ArrayList<ArgumentTerm> getArgumentTerms() {
		return argumentTerms;
	}

	public void setArgumentTerms(ArrayList<ArgumentTerm> argumentTerms) {
		this.argumentTerms = argumentTerms;
	}

}
