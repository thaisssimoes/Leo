package br.uniriotec.ppgi.leo.controller.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;


public class AppConfigurer {
	private static final AppConfigurer _instance = new AppConfigurer();
	
	@SuppressWarnings("unused")
	private static String PATH_WORD_TOKEN_MODEL 		= "./Config/OpenNLP_models/pt-token.bin";
	@SuppressWarnings("unused")
	private static String PATH_SENTENCE_TOKEN_MODEL 	= "./Config/OpenNLP_models/pt-sent.bin";
	
	private AppConfigurer(){}
	
	public static synchronized AppConfigurer getInstance(){
		return _instance;
	}
	
	
	
	/**
	 * Read a text file content and returns all of it in
	 * a single string.
	 * 
	 * @param filePath - Path to file
	 * @return file content.
	 * @throws IOException
	 */
	public String loadTextFile(String filePath) throws IOException{
		
		String content = "";
		
		try{
			FileInputStream inputStream = new FileInputStream(filePath);
		    try {
		        content = IOUtils.toString(inputStream);
		    } finally {
		        inputStream.close();
		    }
		}catch(IOException ex){
			throw new IOException("Error when reading the domain description file. "+ ex.getMessage());
		}
		
		return content;
	}
	
	
	
	/**
	 * Method to load the OpenNLP word tokenizer
	 * 
	 * @deprecated method deactivated. Tokenizing is now executed
	 * by CoreNLP.
	 * 
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
//	public Tokenizer initializeOpenNLPWordTokenizer() throws InvalidFormatException, IOException {
//		final TokenizerModel tokenModel = new TokenizerModel(new FileInputStream(PATH_WORD_TOKEN_MODEL));
//		return new TokenizerME(tokenModel);
//	}
	
	
	/**
	 * Method to load the OpenNLP Sentence tokenizer
	 * 
	 * @deprecated method deactivated. Tokenizing is now executed
	 * by CoreNLP.
	 * 
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
//	public SentenceDetector initializeOpenNLPSentenceTokenizer() throws InvalidFormatException, FileNotFoundException, IOException{
//		SentenceModel sd_model = new SentenceModel(new FileInputStream(PATH_SENTENCE_TOKEN_MODEL));
//		return new SentenceDetectorME(sd_model);
//	}
	
	
}
