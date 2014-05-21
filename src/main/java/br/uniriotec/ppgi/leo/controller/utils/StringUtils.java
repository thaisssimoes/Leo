package br.uniriotec.ppgi.leo.controller.utils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible to provide all necessary resources
 * for String object manipulating.
 * 
 * @author Diego Soares
 *
 */
public class StringUtils{
	
	
	/**
	 * <p>Method responsible to remove ISO-8859 characters.</p>
	 * 
	 * @param  str - Input String
	 * @return Formatted String
	 * @throws Exception
	 */
	public static String removeIso8859Chars(String str) throws Exception
	{
		String newStr = str;
		
		if(str.startsWith("=?ISO-8859-1?Q?"))
		{
			newStr = newStr.replace("=?ISO-8859-1?Q?", "");
			newStr = newStr.replace("?=", "");
			newStr = newStr.replaceAll("_", " ");
			newStr = newStr.replaceAll("=", "%");
			
			newStr = URLDecoder.decode(newStr, "ISO-8859-1");
		}
		
		return newStr;
	}
	
	/**
	 * Method to remove chars and replace them based on the correspondent text
	 * described internally.
	 * 
	 * The method removes :{, }, [, ], (, ), <, >, _, -, *, =, +, |, \, %, &, $, #, @
	 * 
	 * @param text - the text to have especial chars removed
	 * @return the text without special chars.
	 */
	public static String removeSpecialChar(String text){
		
		Map<String, String> especialCharMap = new HashMap<String, String>();
			especialCharMap.put("\\{", "");
			especialCharMap.put("\\}", "");
			especialCharMap.put("\\[", "");
			especialCharMap.put("\\]", "");
			especialCharMap.put("\\(", "");
			especialCharMap.put("\\)", "");
			especialCharMap.put("<", "");
			especialCharMap.put(">", "");
			especialCharMap.put("_", "");
			especialCharMap.put(" - ", " ");
			especialCharMap.put("\\*", "");
			especialCharMap.put("=", "");
			especialCharMap.put("\\+", "");
			especialCharMap.put("\\|", "");
			especialCharMap.put("\\\\", "");
			especialCharMap.put("%", "");
			especialCharMap.put("&", "");
			especialCharMap.put("\\$", "");
			especialCharMap.put("#", "");
			especialCharMap.put("@", "");
		
		for(String symbol : especialCharMap.keySet()){
			text = text.replaceAll(symbol, especialCharMap.get(symbol));
		}
		
		return text;
	}
	
	
	
	
	/**
	 * Method to remove punctuation from the inputed text.
	 * 
	 * @param text - the text to have punctuation removed
	 * @return the text without symbols.
	 */
	public static String removePunctuation(String text){
		
		Map<String, String> especialCharMap = new HashMap<String, String>();
			especialCharMap.put("--+", "");
			especialCharMap.put("\\.", "");
			especialCharMap.put(",", "");
			especialCharMap.put(":", "");
			especialCharMap.put(";", "");
			especialCharMap.put("!", "");
			especialCharMap.put("\\?", "");
			especialCharMap.put("~", "");
			especialCharMap.put("`", "");
			especialCharMap.put("'", "");
			especialCharMap.put("\"", "");
			especialCharMap.put("/", "");
			
		
		for(String symbol : especialCharMap.keySet()){
			text = text.replaceAll(symbol, especialCharMap.get(symbol));
		}
		
		return text;
	}
	
	
	
	
	/**
	 * Simple method to remove line breaks from a text. If the line break
	 * is already preceded or followed by a space character the break is 
	 * simply removed. If no spaces can be found one is inserted just for 
	 * safety.
	 * 
	 * @param text - the text that will have the line breaks removed 
	 * @return a text without line breaks.
	 */
	public static String removeLineBreaks(String text) {
		String result = text;
		
		//replace possible line breaks using "\n" and "\r"
		result = result.replaceAll(" *\\n *", " ");
		result = result.replaceAll(" *\\r *", " ");
		
		//replace possible multi spces for a single space
		result = result.replaceAll(" +", " ");
		
		return result;
	}
	
	
	
	
	
	
	
	
}
