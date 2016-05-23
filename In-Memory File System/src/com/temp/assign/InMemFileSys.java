/**
 * 
 */
package com.temp.assign;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * @author Pallavi
 *
 */
public class InMemFileSys {

	/**
	 * Create a program that will mimic a file system in a non-persistent way.
	 * Your program should not write anything to the HDDs and when it is
	 * restarted, the "file system" should be empty. Your program should handle
	 * the following file system commands:
	 * 
	 */
	public static void main(String[] args) {
		//make a new dir - dir2
		File dir = makeDir("/Users/Pallavi/Documents/dir2");

		if (dir.exists()) {
			//creates a temp file which is not written to HDD
			File nFile = createFile(dir.getAbsolutePath());
			
			// a file on disk to copy contents of a temp file
			File outputFile = new File("/Users/Pallavi/Documents/dir3/output.txt");
			
			//write data to a file
			String data = "This data is written to the file  - outpu.txt!!!";
			write(nFile.getAbsolutePath(), data);
			
			//display file contents
			displayFileContent(nFile.getAbsolutePath());
			
			//copy file content from temp file to output file
			copyFiles(nFile.getAbsolutePath(), outputFile.getAbsolutePath());
		}
		
		//list files under Documents
		list("/Users/Pallavi/Documents/");
		
		//find files with the name under root directory - /Users/Pallavi
		find("anyFile.txt");
		
		//Search for a file by name - starting folder given
		find("/Users/Pallavi/Documents" , "searchFile.txt");
		
	}

	/**
	 * 1. mkdir /someFolder Create a new folder - Takes a parameter of absolute
	 * folder path
	 * 
	 * @param absoluteFolderPath:
	 *            path in which a new folder is created
	 * @return File: an abstract pathname for the newly created dir
	 */
	public static File makeDir(String absoluteFolderPath) {

		File dir = new File(absoluteFolderPath);

		// attempt to create the directory here
		boolean successful = dir.mkdir();
		// can also use dir.mkdirs() to create parent dirs

		if (successful) {
			// creating the directory succeeded
			System.out.println("directory was created successfully");
			dir.deleteOnExit();
			return dir;

		} else {

			// creating the directory failed
			System.out.println("failed trying to create the directory");
			return null;
		}

	}

	/**
	 * 2 & 3 create /file1 || create /someFolder/file1 Create a new file - Take
	 * a parameter of absolute file path
	 * 
	 * @param absoluteFilePath:
	 *            The directory path in which the file is to be created.
	 * @return File: An abstract pathname for the newly-created empty file
	 */
	public static File createFile(String absoluteFilePath) {
		File f = null;
		File filepath = new File(absoluteFilePath);

		if (!filepath.exists()) {
			filepath = makeDir(absoluteFilePath);
		}

		try {
			// creates temporary file with tmp as prefix and .txt as suffix
			f = File.createTempFile("tmp", ".txt", filepath);

			// prints absolute path
			System.out.println("File path: " + f.getAbsolutePath());

			// deletes file when the virtual machine terminate
			f.deleteOnExit();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;

	}

	/**
	 * 4. write "Some text" /file1 Add content to a file - Take 2 parameters:
	 * Content to append to a file; Absolute path to a file
	 * 
	 * @param absoluteFilePath:
	 *            absolute path of the file to be written
	 * @param data:
	 *            text to be written
	 */
	public static void write(String absoluteFilePath, String data) {

		Path p = Paths.get(absoluteFilePath);

		try {
			Files.write(p, data.getBytes(), StandardOpenOption.APPEND);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Display file contents - Takes absolute path to a file as an input; Prints
	 * out file contents as an output
	 */
	public static void displayFileContent(String absoluteFilePath) {

		Path p = Paths.get(absoluteFilePath);

		try {
			List<String> lines = Files.readAllLines(p, Charset.defaultCharset());

			System.out.println("-------------writing contents of the file --------------");
			for (String line : lines) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Copy files - Takes 2 parameters: Absolute path to a source file; Absolute
	 * path to a destination file (NOTE: If destination file exists, it will be
	 * overwritten)
	 * 
	 * @param filepath1
	 * @param filepath2
	 */
	public static void copyFiles(String filepath1, String filepath2) {

		File ip = new File(filepath1);
		File op = new File(filepath2);

		if (!ip.exists()) {
			createFile(filepath1);
		}

		if (!op.exists()) {
			try {
				op.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileReader fr = null;
		FileWriter fw = null;

		try {
			fr = new FileReader(ip);
			fw = new FileWriter(op, true);

			int c = fr.read();
			while (c != -1) {
				fw.write(c);
				c = fr.read();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fr.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * List folder contents - Takes absolute path to a folder as an input;
	 * Prints out folder contents as an output
	 * 
	 * @param fp: absolute file path afp
	 * 
	 */
	public static void list(String afp) {

		File f = new File(afp);

		if (f.isDirectory()) {
			List<String> ls = new ArrayList<String>(Arrays.asList(f.list()));

			for (String filename : ls) {
				System.out.println(filename);
			}

		}
	}
	
	
	/**
	 * Search for a file by name - Takes name of a file to find; 
	 * Prints out list of absolute paths to files with matching names
	 * 
	 * 
	 */
	
	public static void find(String filename){
		
		File root = new File("/Users/Pallavi");
		
		try{
			boolean recursive = true;
			
			Collection<File> files = FileUtils.listFiles(root, null, recursive);
			
			for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
                File file = iterator.next();
                if (file.getName().equals(filename))
                    System.out.println(file.getAbsolutePath());
            }
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Search for a file by name - Takes 2 parameters: 
	 * Absolute path to a starting folder and file name; 
	 * Outputs list of absolute paths to files with matching names
	 * 
	 */
	public static void find(String absoluteFP , String filename){
		
		File root = new File(absoluteFP);
		
		try{
			boolean recursive = true;
			if (root.exists() && root.isDirectory()) {
				Collection<File> files = FileUtils.listFiles(root, null, recursive);

				for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
					File file = iterator.next();
					if (file.getName().equals(filename))
						System.out.println(file.getAbsolutePath());
				}
			} else
				System.out.println("the directory doesn't exist!!! or not a directory!!");
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
}

