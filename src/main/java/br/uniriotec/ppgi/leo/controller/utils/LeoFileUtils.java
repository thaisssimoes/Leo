package br.uniriotec.ppgi.leo.controller.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class LeoFileUtils {
	
	private static Logger logger = Logger.getLogger(LeoFileUtils.class);
	
	public static boolean deleteLines(String filename, int startline, int numlines) throws IOException{
		
		BufferedReader br = null;
		FileWriter fw = null;
		
		try{
			br = new BufferedReader(new FileReader(filename));
 
			//String buffer to store contents of the file
			StringBuffer sb=new StringBuffer("");
 
			//Keep track of the line number
			int linenumber=1;
			String line;
 
			while((line = br.readLine())!=null){
				//Store each valid line in the string buffer
				if(linenumber<startline||linenumber>=startline+numlines)
					sb.append(line+"\n");
				linenumber++;
			}
			
			if(startline+numlines>linenumber){
				logger.debug("End of file reached.");
			}
			
			
 
			fw = new FileWriter(new File(filename));
			//Write entire string buffer into the file
			fw.write(sb.toString());
			
			return true;
		
		}catch (Exception e){
			logger.error("Error when removing lines from file. ", e);
			return false;
		
		}finally{
			if(br != null){
				br.close();
			}
			
			if(fw != null){
				fw.close();
			}
		}
	}
}
