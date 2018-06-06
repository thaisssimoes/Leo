package br.uniriotec.ppgi.leo.view;

import java.io.PrintStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import br.uniriotec.ppgi.leo.controller.run.LeoLarner;
import br.uniriotec.ppgi.leo.controller.run.LeoPreProcessor;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

public class CommandLineRunner {
	
	private static Logger logger = Logger.getLogger(CommandLineRunner.class);
	private static boolean verbose = false;
	
	public static void main(String[] args){
		try{
			/* ************************************************
			 * 	Parse Parameters from command line with JSAP
			 * ************************************************/
			JSAPResult parameters = parseParameters(args);
			String inputSentences = parameters.getString("input_sentences");
			String outputFilePath = parameters.getString("output_file_path");
			verbose = parameters.getBoolean("verbose");
			
			//Redirect ordinary System.out and System.err messages
			redirectStdOutErrTolog4j();
			
			logger.info("=== LEO (Learning Well-Founded Ontologies through NLP) ===");
			logger.info("## STARTING LEO ##");
			
			/* ************************************************
			 * 				Start application
			 * ************************************************/
			if(parameters.getBoolean("preprocess")){
				logger.info("** Running on Preprocessing mode **");
				LeoPreProcessor processor = new LeoPreProcessor(verbose);
				processor.process(inputSentences, outputFilePath);
			}else{
				String labeledSentences = parameters.getString("labeled_sentences");
				LeoLarner learner = new LeoLarner(verbose);
				learner.process(inputSentences, labeledSentences, outputFilePath);
			}
			
			
			/* ************************************************
			 * 				Finishing application
			 * ************************************************/
			logger.info("## FINISHING LEO ##");
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("rawtypes")
	private static JSAPResult parseParameters(String[] args) throws Exception{
		
		JSAP jsap = new JSAP();
		
		
		/* ****************************************
		 * 		Create Parametized options
		 * ****************************************/
		
		//Switch to alternate between Preprocessing mode and Learning mode
		Switch preprocess = new Switch("preprocess").setDefault("-p")
		    .setShortFlag('p') 
		    .setLongFlag("preprocess");
			preprocess.setHelp("Runs the application in Preprocess Mode, which use parameters to generate a SRL Input File (CoNLL 2008 format).");
    	
			
			
		//General Parameters
    	FlaggedOption optFilename = new FlaggedOption("input_sentences")
	        .setRequired(true) 
	        .setShortFlag('i') 
	        .setLongFlag("input_sentences");
    		//optFilename.addDefault("/home/thais/Documentos/TCC/Leo/target/alice.txt");//THAIS
	    	optFilename.setHelp("The path to the input TXT file with domain description.");
	    	
	    FlaggedOption outputFile = new FlaggedOption("output_file_path")
	        .setRequired(true) 
	        .setShortFlag('o') 
	        .setLongFlag("output_path");
	    	//outputFile.addDefault("/home/thais/Documentos/TCC/Leo/target/");//THAIS
    		outputFile.setHelp("The path LTH_SRL root where models and descriptors folders are.");
	    	
    		
    		
		//Learning parameters
		FlaggedOption labeledSentences = new FlaggedOption("labeled_sentences")
	        .setRequired(false) 
	        .setShortFlag('l') 
	        .setLongFlag("labeled_sentences");
			labeledSentences.setHelp("The path to the semantic role labeled sentences (CoNLL 2008 format). OBS: Should only be passed when the application is executed on Default Mode (without \"-p\").");
    		

			
    	//General Swithces
        Switch swVerbose = new Switch("verbose")
			.setShortFlag('v')
			.setLongFlag("verbose");
			swVerbose.setHelp("Outputs verbose info.");
        
        Switch swHelp = new Switch("help")
			.setShortFlag('h')
			.setLongFlag("help");
			swHelp.setHelp("Shows this help info.");
		
			
			
			
		/* ****************************************
		 * 			Register Parameters
		 * ****************************************/	
		
		try {
			jsap.registerParameter(preprocess);
			jsap.registerParameter(optFilename);
			jsap.registerParameter(outputFile);
			jsap.registerParameter(labeledSentences);
			jsap.registerParameter(swVerbose);
			jsap.registerParameter(swHelp);
		} catch (Exception e) {
			throw new Exception("FATAL Exception when registering JSAP Parameters. "
					+ e.getMessage());
		}
		

		
		/* ****************************************
		 * 			Parser arguments
		 * ****************************************/
		
        JSAPResult config = jsap.parse(args);  
        
        //Check if its a call for Help
        if(config.getBoolean("help")){
        	System.err.println("This HELP further explains parameters that should be passed to the application.");
        	System.err.println("Flags indicated with \"()\" are required while flags indicated with \"[]\" are optional.\n");
        	System.err.println(jsap.getHelp());
			System.exit(1);
			
		//Check if its an usage mistake	
        }else if(!config.success()) {
        	for (Iterator errs = config.getErrorMessageIterator(); errs.hasNext();) {
                System.err.println("Error: " + errs.next());
            }    
        	System.err.println("Usage: java -jar leo.jar "+jsap.getUsage());
        	System.exit(1);
        
        //Otherwise, return the parsed parameters 
        }else{
        	if(config.getBoolean("preprocess")==false && config.getString("labeled_sentences")==null){
        		System.err.println("ERROR: When running the application in Default mode it is necessary to inform \"labeled_sentences\" parameter.");
        		System.exit(1);
        	}else{
        		return config; 
        	}
        }
        
        return null;
        
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This method, when invoked, overrides regular System.out and
	 * System.err, sending it's output to the application logger or
	 * simply omitting it when verbose option is not set.
	 */
	private static void redirectStdOutErrTolog4j() {
		System.setOut(new PrintStream(System.out) {
	        @Override
	        public void print(final String s) {
	            if(verbose){
	            	logger.info(s);
	            }
	        }
	    });
		System.setOut(new PrintStream(System.out) {
	        @Override
	        public void println(final String s) {
	            if(verbose){
	            	logger.info(s);
	            }
	        }
	    });
	    System.setErr(new PrintStream(System.err) {
	        @Override
	        public void print(final String s) {
	            if(verbose){
	            	logger.error(s);
	            }
	        }
	    });
	    System.setErr(new PrintStream(System.err) {
	        @Override
	        public void println(final String s) {
	            if(verbose){
	            	logger.error(s);
	            }
	        }
	    });
	}
	
	
	
	
	
	
	
	

}
