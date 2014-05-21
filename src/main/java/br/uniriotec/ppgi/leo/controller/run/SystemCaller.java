package br.uniriotec.ppgi.leo.controller.run;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class SystemCaller {
	
	private static Logger logger = Logger.getLogger(SystemCaller.class);
	
	
	/**
	 * Executes a OS call to run a program, command or executable script. The call's 
	 * output will be printed in the console when running the application. This method 
	 * should not use UNIX redirections (i.e. ">" and ">>"). To redirect the output
	 * of the system call execution to a file use the method callSystemProccess(String command, String outputFile)
	 * 
	 * @param command - the command to be executed
	 * @throws IOException
	 */
	public static void callSystemProccess(String command) throws IOException{
		callSystemProccess(command, null);
	}
		
	
	
	/**
	 * Executes a OS call to run a program, command or executable script. The call's 
	 * output will be written in a file, respecting the name indicated by the user.
	 * If the file doesn't exist it will be created. The user running the application
	 * must have read and write permission on the directory where the output file
	 * is or will be created.
	 * 
	 * @param command - the command to be executed
	 * @param outputFile - the file where the output will be written.
	 * @throws IOException
	 */
	public static void callSystemProccess(String command, String outputFile) throws IOException{
		
		ArrayList<String> args = new ArrayList<String>(Arrays.asList(command.split(" ")));
		
		if (args.size() <= 0) {
			logger.error("Need command to run");
			System.exit(-1);
		}

		if (args.size() == 0) {
            logger.error("You must supply at least one argument.");
            return;
        }
 
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.redirectErrorStream(true);
 
        Process process = processBuilder.start();
        
        if(outputFile != null){
        	OutputStream fileOutputStream = new FileOutputStream(new File(outputFile));
        	copy(process.getInputStream(), fileOutputStream);
        }else{
        	copy(process.getInputStream(), System.out);
        }
        
        try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        logger.debug("Exit Status : " + process.exitValue());
		
		
	}
	

	
	
	/**
	 * Private method responsible for copying the InputStream resulting of the command 
	 * and redirect it to the OutputStream
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private static void copy(InputStream in, OutputStream out) throws IOException {
		while (true) {
            int c = in.read();
            if (c == -1) {
                break;
            }
            out.write((char) c);
        }
    }
	
}
