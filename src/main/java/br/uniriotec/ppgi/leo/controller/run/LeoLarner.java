package br.uniriotec.ppgi.leo.controller.run;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import br.uniriotec.ppgi.leo.controller.nlp.CoNLL2008Parser;
import br.uniriotec.ppgi.leo.controller.nlp.srl.SRLPostprocessor;
import br.uniriotec.ppgi.leo.controller.nlp.wsd.SenseRelateParser;
import br.uniriotec.ppgi.leo.controller.utils.AppConfigurer;
import br.uniriotec.ppgi.leo.controller.utils.StringUtils;
import br.uniriotec.ppgi.leo.model.KernelSentence;
import br.uniriotec.ppgi.leo.model.MainTerm;
import br.uniriotec.ppgi.leo.model.ProcessedSentence;


/**
 * Class responsible of reading a file and preprocessing its texts, generating the 
 * SRL system input file, which respects the CONLL 2008 format.
 * 
 * @author Felipe Leao
 *
 */
public class LeoLarner {
	private static Logger logger = Logger.getLogger(LeoLarner.class);
	private static boolean verbose;
	
	
	public LeoLarner(boolean verbose){
		LeoLarner.setVerbose(verbose);
	}
	
	private static void setVerbose(boolean verbose) {
		LeoLarner.verbose = verbose;
	}
	
	public static boolean isVerbose(){
		return verbose;
	}
	
	public void process(String inputSentencesFile, String semRoleLabeledInput, String outputFilePath) throws IOException{
		
		
		/* ************************************************
		 * 			  Load Domain Description
		 * ************************************************/
		if(isVerbose()) logger.info("Loading domain description file...");
		
		AppConfigurer cfg = AppConfigurer.getInstance();
		String fileContent = 
			StringUtils.removeSpecialChar(
				StringUtils.removeLineBreaks(
					cfg.loadTextFile(inputSentencesFile)
				)
			);

		
		/* ************************************************
		 * 			 	Parse SRL Output
		 * ************************************************/
		if(isVerbose()) logger.info("Parsing SRL output...");
		
		//Parse SRL Output
		List<ProcessedSentence> srlProcessedSentences = CoNLL2008Parser.parseSrlProcessedFile(fileContent, semRoleLabeledInput);
		
		
		/* ************************************************
		 * 			 Postprocess SRL Output
		 * ************************************************/
		if(isVerbose()) logger.info("Processing labeled sentences...");
		
		//Identifying Kernel Sentences;
		if(isVerbose()) logger.info("-- Identifying kernel sentences");
		SRLPostprocessor.extractKernelSentences(srlProcessedSentences);
		//gather all kernel sentences from all SRLProcessedSentences
		List<KernelSentence> kernelSentencesList = new ArrayList<KernelSentence>();
		for(ProcessedSentence sent : srlProcessedSentences){
			kernelSentencesList.addAll(sent.getKernelSentences());
		}
		
		
		//Determining important domain terms
		if(isVerbose()) logger.info("-- Extracting important domain terms");
		Set<MainTerm> mainTermsList = SRLPostprocessor.extractMainTerms(kernelSentencesList);
		
		
		
		logger.debug("MainTerms found:");
		String maintermsfound = "  ";
		//just log (debug) the found main terms
		for(MainTerm mainTerm : mainTermsList){
			maintermsfound += mainTerm + " ("+mainTerm.getSimplePosTag()+"), ";
		}
		logger.debug(maintermsfound.substring(0, maintermsfound.length()-2));
		
		
		/* ************************************************
		 * 				Disambiguate Senses
		 * ************************************************/
		if(isVerbose()) logger.info("Disambiguating Senses...");
		
		if(isVerbose()) logger.info("-- Composing WSD input");
		//begin by creating a file to be used as WNTAGGED input by SenseRelate AllWords
		//Compound nouns are already informed
		File wsdInputFile = SenseRelateParser.tempInputFileBuilder(srlProcessedSentences, mainTermsList);
		
		String caminhoCoNLLFile = outputFilePath+"/wsdWntaggedInput.wsd";
		File outputFile = new File(caminhoCoNLLFile);
		FileUtils.copyFile(wsdInputFile, outputFile);
		
		
//		//Run WSD system and disambiguate senses against Wordnet
//		//If it hangs the application, call the system from outside due to memory RAM limitations of the JVM
//		if(isVerbose()) logger.info("-- Running WSD system");
//		SystemCaller.callSystemProccess(
//				outputFilePath+"/wsd/WordNet-SenseRelate-AllWords-0.19/utils/wsd.pl "	//Script to run WSD
//				+ "--context "+outputFilePath+"/wsdWntaggedInput.wsd  "					//Input file
//				+ "--format wntagged "													//Format of input file
//				+ "--window 5 ",														//Number of words to look around
//				outputFilePath+"/wsdWntaggedOutput.wsdout"								//output file
//				
//			);
//		
//		//Remove first lines of the outputted file to remove the parameters passed to the WSD system
//		LeoFileUtils.deleteLines(outputFilePath+"/wsdWntaggedOutput.wsdout", 1, 18);
		logger.error("DESCOMENTAR A DESAMBIGUACAO!!!");
		
		
		
		
		/* ************************************************
		 * 				Locate Supersenses
		 * ************************************************/
		
		//Parse WSD system output
		if(isVerbose()) logger.info("-- Parsing WSD system's output");
		SenseRelateParser.parseWsdProcessedFile(srlProcessedSentences,mainTermsList,outputFilePath+"/wsdWntaggedOutput.wsdout");
		
//		for(ProcessedSentence set : srlProcessedSentences){
//			for(WordnetSynset wnSyn : set.getWordnetSynsets()){
//				System.out.println(wnSyn);
//			}
//		}
		
		
		/* ************************************************
		 * 				Identify Semantic Types
		 * ************************************************/
		
		//TODO ajustar os ProccessedSentence para informar o Tipo Semântico de cada supersense
		//TODO Para isso implementar uma classe que aplique as heurísticas para substantivos.
		
		
		
	}

	
	

}
