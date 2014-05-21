/**
 * 
 */
package br.uniriotec.ppgi.leo.controller.nlp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import br.uniriotec.ppgi.leo.controller.utils.AppConfigurer;
import br.uniriotec.ppgi.leo.model.ArgumentTerm;
import br.uniriotec.ppgi.leo.model.PreprocessedSentence;
import br.uniriotec.ppgi.leo.model.ProcessedSentence;
import br.uniriotec.ppgi.leo.model.SemanticRelation;
import br.uniriotec.ppgi.leo.model.SemanticRelationArgument;

/**<p>
 * Class responsible for parsing data to and from CoNLL2008 task format.
 * It outputs processed data to CoNLL 2008 format and reads a SRL output
 * described in CoNLL2008 extracting data such as agents and objects of a verb.
 * </p>
 * 
 * @author felipeleao88
 *
 */
public class CoNLL2008Parser {
	
	private static Logger logger = Logger.getLogger(CoNLL2008Parser.class);
	
	/**
	 * <p>Creates a temporary file formatted according to the CoNLL-2008 format.
	 * The file is a tabulated file in which each line has a word 
	 * that composes the sentence and sentences are separated by 
	 * two line breaks. The lines regarding a sentence are enumerated 
	 * and each line also carries informations such as lemma and pos-tag.
	 * </p>
	 * 
	 * @param preprocessedSentences - list of sentences with lemma and POS tags
	 * @return a fille in CoNLL-2008 format
	 * @throws IOException 
	 */
	public static File tempInputFileBuilder(List<PreprocessedSentence> preprocessedSentences) throws IOException{
		String tempFileExtension = UUID.randomUUID().toString();
		
		try{
			File conll2008_file = File.createTempFile("srl_input_"+tempFileExtension, ".tmp"); 
			conll2008_file.deleteOnExit();
			
			FileWriter fw = new FileWriter(conll2008_file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(PreprocessedSentence ps : preprocessedSentences){
				String[] words = ps.getWords();
				String[] posTags = ps.getPosTags();
				String[] lemmas = ps.getLemmas();
				
				for(int i=0; i<ps.getWords().length; i++){
					//First columm set
					bw.write(i+1+"\t");
					bw.write(words[i]+"\t");
					bw.write(selecionarTermo(words[i], lemmas[i], posTags[i])+"\t");
					bw.write("_\t");
					bw.write(posTags[i]+"\t");
					
					//Second columm set
					bw.write(words[i]+"\t");
					bw.write(lemmas[i]+"\t");
					bw.write(posTags[i]+"\t");
					bw.write("0\t");
					bw.write("_\t");
					bw.write("\n");
				}
				bw.write("\n");
			}
			
			bw.close();
			return conll2008_file;
			
		}catch(IOException ex){
			logger.error("ERROR when build SRL input file. "+ex.getMessage());
			throw new IOException("ERROR when trying to create " +
					"temp file to use as input on SRL system. "+ex.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**<p>
	 * Parses the the inputed values and generates a list of ProcessedSentence objects. As firt param the
	 * method expects a String with the full text being processed by the application, with all the original 
	 * sentences. This is supposed to be the same file as the one inputed to be preprocessed by the application.
	 * The second parameter is the canonical full path to the file outputed by the SRL system. This file must
	 * respect the CoNLL 2008 format. 
	 * </p>
	 * 
	 * @param sentencesFileContent
	 * @param processedCoNLL2008FilePath
	 * @return
	 * @throws IOException Error accessing the processed CoNLL 2008 file
	 */
	public static List<ProcessedSentence> parseSrlProcessedFile(String sentencesFileContent, String processedCoNLL2008FilePath) throws IOException{
		List<ProcessedSentence> processedSentences = new ArrayList<ProcessedSentence>();
		
		logger.debug("Loading SRL processed File");
		AppConfigurer cfg = AppConfigurer.getInstance();
		String fileContent = cfg.loadTextFile(processedCoNLL2008FilePath);
		
		//Split the text in the file to separate each processed sentence
		String[] splittedSentences = fileContent.split("\n\n");
		
		//For earch processed sentence build a ProcessedSentence object
		logger.debug("Building ProcessedSentence objects based on SRL output");
		for(String s : splittedSentences){
			String[] lines = s.split("\n|\r");
			ProcessedSentence sentence = new ProcessedSentence();
				sentence.setFullSentence("");
				sentence.setWords(new String[lines.length]);
				sentence.setLemmas(new String[lines.length]);
				sentence.setPosTags(new String[lines.length]);
			
			//Retrieve Words, Lemmas, POS tags and relator verbs
			for(int i=0; i<lines.length; i++){
				String[] values = lines[i].split("\\t", -1); 
				sentence.getWords()[i] = values[1];
				sentence.getLemmas()[i] = values[2];
				sentence.getPosTags()[i] = values[4];
				
				//Check if the line correspond to a predicate (IsPred = Y)
				if(values[10].trim().equals("_") == false){
					//instantiate a relation informing the word and the line (id) in the SRL outputed sentence
					logger.debug("New Semantic Relation found ("+values[10].trim()+")");
					SemanticRelation rel = new SemanticRelation(i+1, values[10].trim(), values[4].trim(), values[2].trim()); 
					sentence.getSemanticRelations().add(rel);
				}
				
			}
			
			//For each relator found, get arguments of that relators
			for(int j=0; j<sentence.getSemanticRelations().size(); j++){
				for(int i=0; i<lines.length; i++){
					String[] values = lines[i].split("\\t", -1); 
					String argValue = values[11+j].trim();
					if(argValue.equals("_")==false){ //checks if the cell has a value (A0. AM-MOD, A1, AM-TMP, etc.)
						SemanticRelationArgument arg = new SemanticRelationArgument(Integer.parseInt(values[0]), values[1], values[4], values[2]);
						//determine the complete argument based on the dependencies of the arg begining
						preDetermineFullArg(arg, lines);
						if(argValue.equals("A0")) {
							sentence.getSemanticRelations().get(j).setArg0(arg);
						}else if(argValue.equals("A1")) {
							sentence.getSemanticRelations().get(j).setArg1(arg);
						}else if(argValue.equals("A2")) {
							sentence.getSemanticRelations().get(j).setArg2(arg);
						}else if(argValue.equals("A3")) {
							sentence.getSemanticRelations().get(j).setArg3(arg);
						}else{
							sentence.getSemanticRelations().get(j).getOtherArgs().put(argValue, arg);
						}
					}
					sentence.getSemanticRelations().get(j).fixFullArgs();
				}
				
			}
			
			processedSentences.add(sentence);
		}
		
		
		//Add full sentence to each ProcessedSentence Object
		String[] fullSentences = StanfordPreprocessor.tokenizeSentences(sentencesFileContent);
		for(int i=0; i<fullSentences.length; i++){
			processedSentences.get(i).setFullSentence(fullSentences[i]);
		}
		
		
		//DEBUG 
		for(ProcessedSentence sent : processedSentences){
			logger.debug("-------------------------------------------------------------------------------------------");
			logger.debug(sent.getFullSentence());
			for(SemanticRelation rel : sent.getSemanticRelations()){
				logger.debug(rel);
			}
		}
		logger.debug("-------------------------------------------------------------------------------------------");
		
		
		return processedSentences;
	}
	
	
	
	
	
	







	/**
	 * Method to determine a comple argument based on the dependency declared
	 * by an argument. The method calls a recursive method that searches in each 
	 * line of the processed sentence for the dependency declared and dependencies
	 * of each depency
	 * 
	 * @param arg - The original argument
	 * @param sentenceLines - all lines of the SRL file that compse the processed sentence
	 */
	private static void preDetermineFullArg(SemanticRelationArgument arg, String[] sentenceLines) { 
		TreeMap<Integer,ArgumentTerm> parts = new TreeMap<Integer,ArgumentTerm>();
		parts.put(arg.getArgLine(), new ArgumentTerm(arg.getShortArg(), arg.getShortArgLemma(), arg.getShortArgPosTag()));
		
		//call to recursive method
		parts.putAll(retrieveDependentArgs(parts,sentenceLines));
		
		//build a string based on all dependencies found and populate the array of ArgumentTerms
		//The TreeMap guarantees that the words are read in order, since 
		//the nodes are ordered by the line ID
		String fullArg = "";
		int firstWordLine = arg.getArgLine();
		for(Entry<Integer, ArgumentTerm> part : parts.entrySet()){

			//Add ArgumentTerm to array of ArgumentTerm objects
			arg.getArgumentTerms().add(part.getValue());

			fullArg += part.getValue().getWord()+" ";
			//check if the argument actually begins prior the argument lead
			if(part.getKey() < firstWordLine){
				firstWordLine = part.getKey();
			}
		}
		
		arg.setArgLine(firstWordLine);
		arg.setFullArg(fullArg);
	}


	
	
	


	/**
	 * based on each part already identified of an argument, the method 
	 * searches all lines for dependencies of each part. When there are no
	 * more dependencies left it returns the TreeMap of parts. 
	 * 
	 * @param parts
	 * @param sentenceLines
	 * @return
	 */
	private static TreeMap<Integer, ArgumentTerm> retrieveDependentArgs(TreeMap<Integer, ArgumentTerm> parts, String[] sentenceLines) {
		TreeMap<Integer, ArgumentTerm> dependentLines = new TreeMap<Integer, ArgumentTerm>();

		for(int key : parts.keySet()){
			for(int i=0; i<sentenceLines.length; i++){
				String[] values = sentenceLines[i].split("\\t", -1); 
				int depId = Integer.parseInt(values[8]);
				if(depId == key){
					dependentLines.put(Integer.parseInt(values[0]), new ArgumentTerm(values[1], values[2], values[4]));
				}
			}
		}
		if(dependentLines.size()>0){
			dependentLines.putAll(retrieveDependentArgs(dependentLines,sentenceLines));
		}
		return dependentLines;
	}















	/**<p>
	 * Verifiy if the word has a set lemma and if its POS-Tag is a Proper Noun.
	 * If the lemma is set, return it. If the lemma is not set and the POS tag is
	 * a proper noun (NNP or NNPS - from Treebank tagset) return the original word.
	 * If lemma is not set and POS tag is not a proper noun, return de lowercase version
	 * of the original word.
	 * </p>
	 * 
	 * @param word
	 * @param lemma
	 * @param posTag
	 * @return
	 */
	private static String selecionarTermo(String word, String lemma, String posTag) {
		if(lemma.equals("_") == false){
			return lemma;
		}else if(posTag.matches("[NNP|NNPS]")){
			return word;
		}else{
			return word.toLowerCase();
		}
	}
	
	
}
