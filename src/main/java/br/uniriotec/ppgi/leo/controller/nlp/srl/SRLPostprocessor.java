/**
 * 
 */
package br.uniriotec.ppgi.leo.controller.nlp.srl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import br.uniriotec.ppgi.leo.model.ArgumentTerm;
import br.uniriotec.ppgi.leo.model.KernelSentence;
import br.uniriotec.ppgi.leo.model.KernelSentenceArgument;
import br.uniriotec.ppgi.leo.model.MainTerm;
import br.uniriotec.ppgi.leo.model.ProcessedSentence;
import br.uniriotec.ppgi.leo.model.SemanticRelation;
import br.uniriotec.ppgi.leo.model.SentenceArgumentType;

/**
 * This class comprises methods related to processing the output
 * of the SRL system. The class works primarily with objects
 * from the system itself.
 * 
 * @author Felipe Leao
 *
 */
public class SRLPostprocessor {
	
	private static Logger logger = Logger.getLogger(SRLPostprocessor.class);
	
	
	
	/**
	 * The method identifies kernel sentences on each SRLProcessedSentence
	 * object. It is possible that a single SRLProcessedSentence generates
	 * more than one Kernel sentence, since each  kernel sentence correspond
	 * to a predicate found in the labeled sentence. The identified Kernel
	 * Sentences are stored inside the SRLProcessedSentence object.
	 * 
	 * OBS: When constructing Kernel sentences only Verb predicates are taken 
	 * into account.
	 * 
	 * @param srlProcessedSentences List of SRLProcessedSentence objects 
	 * 			originated from parsing the SRL system output
	 */
	public static void extractKernelSentences(List<ProcessedSentence> srlProcessedSentences){
		
		logger.debug("Listing post-processed SRL Output");
		for(ProcessedSentence sent : srlProcessedSentences){
			ArrayList<KernelSentence> kernelSentencesList = new ArrayList<KernelSentence>();
			
			logger.debug("Full Sentence: "+sent.getFullSentence());
			for(SemanticRelation rel : sent.getSemanticRelations()){
				
				/*
				 * Build kernel sentences only when the semantic relation detected by 
				 * the SRL system has as Core a verb.
				 * OBS: Take into account Penntreebank verb tags that always begins with VB
				 */
				if(rel.getRelator().getShortArgPosTag().matches("VB.")){
				
					//Build a KernelSentence object for the semantic relation
					KernelSentenceArgument verb = new KernelSentenceArgument(rel.getRelator().getArgumentTerms(), SentenceArgumentType.VERB);
					KernelSentenceArgument agent = null;
					KernelSentenceArgument directObject = null;
					KernelSentenceArgument indirectObject = null;
					
					if(rel.getArg0()!=null){
						agent = new KernelSentenceArgument(rel.getArg0().getArgumentTerms(), SentenceArgumentType.AGENT);
					}
					if(rel.getArg1()!=null){
						directObject =new KernelSentenceArgument(rel.getArg1().getArgumentTerms(), SentenceArgumentType.DIRECT_OBJECT);
					}
					if(rel.getArg2()!=null){
						indirectObject = new KernelSentenceArgument(rel.getArg2().getArgumentTerms(), SentenceArgumentType.INDIRECT_OBJECT);
					}
					
					kernelSentencesList.add(new KernelSentence(agent, verb, directObject, indirectObject));
				}
			}
			//add list of kernel sentences to the SRLProcessedSentence object
			sent.setKernelSentences(kernelSentencesList);
			//DEBUG
			for(KernelSentence kSent : kernelSentencesList){
				logger.debug("Kernel Sentence: "+kSent);
			}
		}
		
	}
	
	
	
	
	
	
	/**
	 * Based on a list of KernelSentences the method looks for important terms that
	 * should be considered when modeling the domain. this terms can be NOUNS (including
	 * COMPOUND NOUNS), which are the entities that will compose the model, OR VERBS, which 
	 * denote the relations between entities. 
	 * 
	 * OBS: When modeling a well-founded model, the nouns detected can also become 
	 * RELATORs, EVENTs, etc. (from OntoUML)
	 * 
	 * @param kernelSentencesList a list of kernel sentences
	 * @return a list of MainTerms
	 */
	public static Set<MainTerm> extractMainTerms(List<KernelSentence> kernelSentencesList){
		Set<MainTerm> mainTermsList = new HashSet<MainTerm>();
		
		//Iterate over kernel sentences
		for(KernelSentence ks : kernelSentencesList){
			
			//iterate over each argument of the kernel sentence
			for(KernelSentenceArgument ksArg : ks.getAllArguments()){

				//IF the argument is the VERB of the sentence, simply adds it
				if(ksArg.getType() == SentenceArgumentType.VERB){
					MainTerm mainTerm = new MainTerm("verb");
					mainTerm.setMainTerm(ksArg.getValue());
					mainTermsList.add(mainTerm);			
					
				//If the argument is an AGENT or OBJECT, return only the nouns (NN's and NP's) or compound nouns, 
				//which are considered the important terms on the argument
				}else{
					mainTermsList.addAll(findNouns(ksArg.getValue()));
				}
			}
			
		}
		
		return mainTermsList;
	}





	/**
	 * Find the main nouns on a Argument, including compound nouns
	 * 
	 * @param kernelSentenceArgument a list of ArgumentTerm objects that 
	 * 			togheter compose a argument for the kernel sentence.
	 * @return list of MainTerm objects. Each of them may correspond to 
	 * 			a compound noun or a common noun (when composed simply by 1 item).
	 */
	private static List<MainTerm> findNouns(ArrayList<ArgumentTerm> kernelSentenceArgument) {
		
		List<MainTerm> result = new ArrayList<MainTerm>();
		boolean foundPosTag = false;
		
		//Look for compound as NOUN + NOUN
		while(kernelSentenceArgument.size() > 0){
			MainTerm mainTerm = new MainTerm("noun");
			
			do{
				if(kernelSentenceArgument.get(0).getPosTag().matches("NN.*")){
					foundPosTag = true;
					mainTerm.getMainTerm().add(kernelSentenceArgument.get(0));
				}else{
					foundPosTag = false;
					if(mainTerm.getMainTerm().size() > 0){
						result.add(mainTerm);
					}
				}
				kernelSentenceArgument.remove(0);
			}while(foundPosTag && kernelSentenceArgument.size() > 0);
			
			//Checks if there were any main term identified bu not added to the list 
			if(foundPosTag){ 
				result.add(mainTerm);
			}
		}
		
		return result;
		
	}
	
	
	
	
	
}
