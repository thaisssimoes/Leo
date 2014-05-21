/**
 * 
 */
package br.uniriotec.ppgi.leo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author felipeleao88
 * 
 * Kernel sentences are composed by agent, verb and object (both direct and/or indirect).
 * Other arguments such as adjuncts do not participate.
 *
 */
public class KernelSentence {
	private String fullKernelSentence;
	private KernelSentenceArgument agent;
	private KernelSentenceArgument directObject;
	private KernelSentenceArgument indirectObject;
	private KernelSentenceArgument verb;
	
	public KernelSentence(KernelSentenceArgument agent, KernelSentenceArgument verb, KernelSentenceArgument directObject, KernelSentenceArgument indirectObject){
		this.agent = agent;
		this.verb = verb;
		this.directObject = directObject;
		this.indirectObject = indirectObject;
		
		if(agent != null){
			this.fullKernelSentence = agent.toReadable()+" ";
		}
		
		this.fullKernelSentence = verb.toReadable();
		
		if(directObject != null){
			this.fullKernelSentence = " "+directObject.toReadable();
		}
		if(indirectObject != null){
			this.fullKernelSentence = " "+indirectObject.toReadable();
		}
		
	}
	
	public String getFullKernelSentence() {
		return fullKernelSentence;
	}
	public void setFullKernelSentence(String fullKernelSentence) {
		this.fullKernelSentence = fullKernelSentence;
	}
	public KernelSentenceArgument getAgent() {
		return agent;
	}
	public void setAgent(KernelSentenceArgument agent) {
		this.agent = agent;
	}
	public KernelSentenceArgument getDirectObject() {
		return directObject;
	}
	public void setDirectObject(KernelSentenceArgument directObject) {
		this.directObject = directObject;
	}
	public KernelSentenceArgument getIndirectObject() {
		return indirectObject;
	}
	public void setIndirectObject(KernelSentenceArgument indirectObject) {
		this.indirectObject = indirectObject;
	}
	public KernelSentenceArgument getVerb() {
		return verb;
	}
	public void setVerb(KernelSentenceArgument verb) {
		this.verb = verb;
	}
	
	public List<KernelSentenceArgument> getAllArguments(){
		List<KernelSentenceArgument> allArgs = new ArrayList<KernelSentenceArgument>();
		if(agent != null){
			allArgs.add(agent);
		}
		if(directObject != null){
			allArgs.add(directObject);
		}
		if(indirectObject != null){
			allArgs.add(indirectObject);
		}
		
		allArgs.add(verb);
		
		return allArgs;
	}
	
	public String toString(){
		String result = "";
		
		if(agent != null){
			result += agent.toReadable();
		}
		if(verb != null){
			result += verb.toReadable();
		}
		if(directObject != null){
			result += directObject.toReadable();
		}
		if(indirectObject != null){
			result += indirectObject.toReadable();
		}
		
		return result; 
	}
	
}
