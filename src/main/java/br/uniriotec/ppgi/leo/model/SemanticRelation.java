/**
 * 
 */
package br.uniriotec.ppgi.leo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author root
 *
 */
public class SemanticRelation {
	
	private SemanticRelationArgument relator;
	private SemanticRelationArgument arg0;
	private SemanticRelationArgument arg1;
	private SemanticRelationArgument arg2;
	private SemanticRelationArgument arg3;
	private Map<String,SemanticRelationArgument> otherArgs = new HashMap<String,SemanticRelationArgument>();
	
	public SemanticRelation(int relatorPosition, String relator, String posTag, String lemma){
		this.relator = new SemanticRelationArgument(relatorPosition, relator, posTag, lemma);
		this.relator.setFullArg(relator);
		this.relator.getArgumentTerms().add(new ArgumentTerm(relator, lemma, posTag));
	}

	public SemanticRelationArgument getRelator() {
		return relator;
	}

	public void setRelator(SemanticRelationArgument relator) {
		this.relator = relator;
	}

	public SemanticRelationArgument getArg0() {
		return arg0;
	}

	public void setArg0(SemanticRelationArgument arg0) {
		this.arg0 = arg0;
	}

	public SemanticRelationArgument getArg1() {
		return arg1;
	}

	public void setArg1(SemanticRelationArgument arg1) {
		this.arg1 = arg1;
	}

	public SemanticRelationArgument getArg2() {
		return arg2;
	}

	public void setArg2(SemanticRelationArgument arg2) {
		this.arg2 = arg2;
	}

	public SemanticRelationArgument getArg3() {
		return arg3;
	}

	public void setArg3(SemanticRelationArgument arg3) {
		this.arg3 = arg3;
	}

	public Map<String,SemanticRelationArgument> getOtherArgs() {
		return otherArgs;
	}

	public void setOtherArgs(HashMap<String,SemanticRelationArgument> otherArgs) {
		this.otherArgs = otherArgs;
	}
	
	public Map<String,SemanticRelationArgument> getAllArgs(){
		Map<String,SemanticRelationArgument> allArgs = new TreeMap<String,SemanticRelationArgument>();
		allArgs.put("00Base", relator);
		if(arg0!=null){ allArgs.put("A0", arg0); }
		if(arg1!=null){ allArgs.put("A1", arg1); }
		if(arg2!=null){ allArgs.put("A2", arg2); }
		if(arg3!=null){ allArgs.put("A3", arg3); }
		for(String k : otherArgs.keySet()){
			allArgs.put(k, otherArgs.get(k));
		}
		return allArgs;
	}

	
	public String toString(){
		String s = "";
		for(Entry<String, SemanticRelationArgument> e : getAllArgs().entrySet()){
			s += "\n | " +e.getKey()+": ("+e.getValue().getFullArg()+" | "+e.getValue().getArgLine()+"|"+e.getValue().getShortArg()+")";
		}
		s.replaceFirst("\n", "");
		return s;
	}
	
	private List<Integer> getArgsBeginings(){
		List<Integer> beginings = new ArrayList<Integer>();
		for(Entry<String, SemanticRelationArgument> e : getAllArgs().entrySet()){
			beginings.add(e.getValue().getArgLine());
		}
		return beginings;
	}

	
	/**
	 * Fix the argument based on where all other arguments begin.
	 * The call to this method is necessary because originally an
	 * argument may consider lots of other words that are a part of
	 * other arguments. 
	 */
	public void fixFullArgs() {
		
		for(SemanticRelationArgument sr : getAllArgs().values()){
			
			/*
			 * The Argument Begining is always (after composing the ) in the line
			 * where the arg tag has been indicated. The 'lastArgIdx' 
			 * is calculated by adding the line number wher the arg
			 * begins to the number of lines that compose the arg (minus 1 
			 * since the line where the arg begins has its own number already.
			 */
			String[] argsPart = sr.getFullArg().split(" ");
			int lastArgIdx = sr.getArgLine()+argsPart.length-1;
			
			/*
			 * check if there is any argument begining inside the SemanticRelationArgumnt
			 * being iteraded. If there is, then the iterated argument must stop where
			 * the other argument begins.
			 */
			for(int i : getArgsBeginings()){
				if(i>sr.getArgLine() && i<=lastArgIdx){//cut the full Arg where "i" is at
					String newFullArg = "";
					for(int j=0; j<i-sr.getArgLine(); j++){
						newFullArg += argsPart[j]+" ";
					}
					
					sr.setFullArg(newFullArg);
					break;
				}
			}
		}
	}
	
	
}
