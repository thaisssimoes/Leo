package br.uniriotec.ppgi.leo.controller.utils;



/**
 * Class that implement method to tokenize texts, both by words and
 * by sentences. The initial version implements tokenization through
 * the OpenNLP API, but the class can be adapted to use any tokenizer
 * instead, without compromising the application.
 * 
 * The models are loaded using AppConfigurer class.
 * 
 * @deprecated Class is deactivated. Tokenizing is now executed
 * by CoreNLP. Use StanfordPreprocessor class instead.
 * 
 * @author felipeleao88
 *
 */
public class LEOTokenizer {
	
	private static LEOTokenizer _tokenizer = null;
//	private Tokenizer _openNlpWordTokenizer = null;
//	private SentenceDetector _openNlpSentTokenizer = null;
	@SuppressWarnings("unused")
	private AppConfigurer _cfg = null;
	
	private LEOTokenizer(){
		_cfg = AppConfigurer.getInstance();
	}
	
	public static synchronized LEOTokenizer getInstance(){
		if(_tokenizer == null){
			_tokenizer = new LEOTokenizer();
		}
		return _tokenizer;
	}
	
	
	/**
	 * Tokenizes a String through a Word Tokenize technique, 
	 * returning an Array of Strings, where each String is a word.
	 * 
	 * @param text to be tokenized per word
	 * @return tokenized words
	 * @throws IOException
	 */
//	public String[] tokenizeWords(String text) throws IOException{
//		if(_openNlpWordTokenizer == null){
//			try {
//				_openNlpWordTokenizer = _cfg.initializeOpenNLPWordTokenizer();
//			} catch (InvalidFormatException e) {
//				throw new IOException("Invalid format of tokenizer model given to OpenNLP when instantiating Word Tokenizer.");
//			} catch (IOException e) {
//				throw new IOException("IOException when trying to load \"Word Tokens\" model for OpenNLP.");
//			}
//		}
//		return _openNlpWordTokenizer.tokenize(text);
//	}
	
	
	/**
	 * Tokenizes a String through a Sentence Tokenize technique,
	 * returni an Array of Strings, where each String is a sentence.
	 * 
	 * @param text to be tokenized as sentences.
	 * @return tokenized sentences.
	 * @throws IOException
	 */
//	public String[] tokenizeSentences(String text) throws IOException{
//		if(_openNlpSentTokenizer == null){
//			try {
//				_openNlpSentTokenizer = _cfg.initializeOpenNLPSentenceTokenizer();
//			} catch (InvalidFormatException e) {
//				throw new IOException("Invalid format of tokenizer model given to OpenNLP when instantiating Sentence Tokenizer.");
//			} catch (IOException e) {
//				throw new IOException("IOException when trying to load \"Sentence Tokens\" model for OpenNLP.");
//			}
//		}
//		return _openNlpSentTokenizer.sentDetect(text);
//	}

	
	
}
