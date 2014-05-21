/**
 * 
 */
package br.uniriotec.ppgi.leo.model;

import java.util.ArrayList;

/**
 * Simple class to comprise a main term that should be further analyzed by the WSD system and
 * is likely to be come either a class or relation in the final model.
 * 
 * @author Felipe Leão
 *
 */
public class MainTerm {
	private ArrayList<ArgumentTerm> mainTerm = new ArrayList<ArgumentTerm>();
	private String simplePosTag;
	
	
	public MainTerm(String simplePotsTag){
		this.simplePosTag = simplePotsTag;
	}
	

	public ArrayList<ArgumentTerm> getMainTerm() {
		return mainTerm;
	}

	public void setMainTerm(ArrayList<ArgumentTerm> mainTerm) {
		this.mainTerm = mainTerm;
	}
	
	public String getSimplePosTag() {
		return simplePosTag;
	}


	public void setSimplePosTag(String simplePosTag) {
		this.simplePosTag = simplePosTag;
	}
	
	public boolean isCompound(){
		if(mainTerm.size() > 1){
			return true;
		}else{
			return false;
		}
	}


	public String toString(){
		if(simplePosTag.equals("verb") || mainTerm.size() == 1){
			return mainTerm.get(0).getLemma();
		}else{
			String result = "";
			for(ArgumentTerm term : mainTerm){
				if(result.equals("")){
					result += term.getWord();
				}else{
					result+="_"+term.getWord();
				}
			}
			return result;
		}
	}
	
	public String lemmaToString(){
		if(simplePosTag.equals("verb") || mainTerm.size() == 1){
			return mainTerm.get(0).getLemma();
		}else{
			String result = "";
			for(ArgumentTerm term : mainTerm){
				if(result.equals("")){
					result += term.getLemma();
				}else{
					result+="_"+term.getLemma();
				}
			}
			return result;
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mainTerm == null) ? 0 : mainTerm.hashCode());
		return result;
	}


	/**
	 * Method to check if two MainTerm objects are equal. The method
	 * wound normally compare all properties from the object, but for
	 * this application will only consider the actual words
	 * 
	 * @param obj
	 * @return
	 */
	public boolean equals(Object o){
		
		if(!(o instanceof MainTerm)){
			return false;
		}else{
			MainTerm obj = (MainTerm) o;
			
			//If sizes are different or simple pos tag is different then they are not equal
			if(mainTerm.size() != obj.getMainTerm().size() || simplePosTag.equalsIgnoreCase(obj.getSimplePosTag()) == false){
				return false;
			}
			
			if(this.toString().equals(obj.toString())){
				return true;
			}else{
				return false;
			}
		}
		
	}
	
	

}
