package br.uniriotec.ppgi.leo.controller.run;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import br.uniriotec.ppgi.leo.controller.nlp.CoNLL2008Parser;
import br.uniriotec.ppgi.leo.controller.nlp.StanfordPreprocessor;
import br.uniriotec.ppgi.leo.controller.utils.AppConfigurer;
import br.uniriotec.ppgi.leo.controller.utils.StringUtils;
import br.uniriotec.ppgi.leo.model.PreprocessedSentence;


/**
 * Class responsible of reading a file and preprocessing its texts, generating the 
 * SRL system input file, which respects the CONLL 2008 format.
 * 
 * @author Felipe Leao
 *
 */
public class LeoPreProcessor {
	private static Logger logger = Logger.getLogger(LeoPreProcessor.class);
	private static boolean verbose = false;
	
	
	public LeoPreProcessor(boolean verbose){
		LeoPreProcessor.setVerbose(verbose);
	}
	
	public static boolean isVerbose(){
		return verbose;
	}
	
	private static void setVerbose(boolean verbose){
		LeoPreProcessor.verbose = verbose;
	}
	
	public void process(String filepath, String outputFilePath) throws IOException{
		
		
		/* ************************************************
		 * 			Load Domain Description
		 * ************************************************/
		if(isVerbose()) logger.info("Loading domain description file...");
		
		AppConfigurer cfg = AppConfigurer.getInstance();
		String fileContent = 
			StringUtils.removeSpecialChar(
				StringUtils.removeLineBreaks(
					cfg.loadTextFile(filepath)
				)
			);
		
		
		
		/* ************************************************
		 * 			Pre-process text
		 * ************************************************/
		if(isVerbose()) logger.info("Pre-processing text...");
		List<PreprocessedSentence> processedText = StanfordPreprocessor.preprocessText(fileContent);
		
		
		
		
		/* ************************************************
		 * 			Generate SRL Input File
		 * ************************************************/
		if(isVerbose()) logger.info("Generating Semantic Role Labeler input File...");
		
		File inputFile = CoNLL2008Parser.tempInputFileBuilder(processedText);
		
		String caminhoCoNLLFile = outputFilePath+"/srlInputConll2008.tokens";
		File outputFile = new File(caminhoCoNLLFile);
		FileUtils.copyFile(inputFile, outputFile);
		
		
	}
	

}
