package com.photoLister;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
public class my_main {
	
	private static String[] words;
	private static Scanner scanner;
	
	public static void main(String[] args) {
		
		getInput();
		showInput();
		waitPermission();
		
		System.out.println("Starting...");
		
		String[] command = { "cmd", };

		Process p;

		String inputFilePath = "";
		String resultingTxtFile = "";
		String outputFilePath = "";

		String directory = System.getProperty("user.dir") + "\\test files";
		System.out.println(directory);
		String photoDirectory = directory + "\\photos to be searched";

		String tesseract_install_path = "D:\\Tesseract-OCR\\tesseract";

		FileReader fr;
		BufferedReader br;

		File dir = new File(photoDirectory);

		File[] directoryListing = dir.listFiles();
		File[] fetchAroundPhotos = new File[directoryListing.length];
		
		
		for(int count = 0 ; count < directoryListing.length ; count++) {
			fetchAroundPhotos[count] = directoryListing[count];
		}
		
		int count = 0;
		
		int containsCounter = 1;
		
		if (directoryListing != null) {
			for(int theCounter = 0 ; theCounter < directoryListing.length ; theCounter++) {
				
				File child = directoryListing[theCounter];
				
				inputFilePath = child.getAbsolutePath();
				
				if(inputFilePath.contains(".keep"))
					continue;
				
				String type = inputFilePath.substring(inputFilePath.lastIndexOf('.'));
				
				resultingTxtFile = directory + "\\text files\\" + child.getName();
				outputFilePath = directory + "\\photos containing the text\\photo " + containsCounter + type;
				
				try {
					p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println(
							"\"" + tesseract_install_path + "\" \"" + inputFilePath + "\" \"" + resultingTxtFile + "\" -l eng");
					stdin.close();
					p.waitFor();
					System.out.println();
					System.out.println();
					System.out.println();
					
					fr = new FileReader(resultingTxtFile + ".txt");
					br = new BufferedReader(fr);

					String line;
					boolean contains = false;
					boolean myBreak = false;
					while ((line = br.readLine()) != null) {
						for(int count2 = 0 ; count2 < words.length ; count2++) {
							if(line.toLowerCase().contains(words[count2].toLowerCase())) {
								contains = true;
								myBreak = true;
								break;
							}
						}
						if(myBreak)
							break;
					}

					fr.close();
					br.close();

					if (contains) {
						
						int lowerBound = 2;
						int upperBound = 2;
						
						if(count - lowerBound < 0 )
							lowerBound = count;
						
						if(count + upperBound > directoryListing.length - 1)
							upperBound = directoryListing.length - upperBound + 1;
					
						
						InputStream is = null;
						OutputStream os = null;
						OutputStream os2 = null;
						
						is = new FileInputStream(inputFilePath);
						os = new FileOutputStream(outputFilePath);
						os2 = new FileOutputStream(directory + "\\original photos\\photo " + containsCounter + type);
	
						
						byte[] buffer = new byte[1024];
						int length;
						while ((length = is.read(buffer)) > 0) {
							os.write(buffer, 0, length);
							os2.write(buffer, 0, length);
						}
						
						is.close();
						os.close();
						os2.close();
						
						
						
						int firstCounter = 1, secondCounter = 1;
						
						for(int lowerCount = count - lowerBound ; lowerCount < count ; lowerCount++) {
							is = new FileInputStream(fetchAroundPhotos[lowerCount].getAbsolutePath());
							String extraOutput = outputFilePath.substring(0, outputFilePath.lastIndexOf('\\')+1) + "photo " + containsCounter + " - extra fetched from back " + firstCounter + type;
							os = new FileOutputStream(extraOutput);
							buffer = new byte[1024];
							
							while ((length = is.read(buffer)) > 0) {
								os.write(buffer, 0, length);
							}
	
							is.close();
							os.close();
							
							firstCounter++;

						}
						
						for(int upperCount = count + 1 ; upperCount <= count + upperBound && upperCount < fetchAroundPhotos.length ; upperCount++) {
			
							is = new FileInputStream(fetchAroundPhotos[upperCount].getAbsolutePath());
							String extraOutput = outputFilePath.substring(0, outputFilePath.lastIndexOf('\\')+1) + "photo " + containsCounter + " - extra fetched from front " + secondCounter + type;
							os = new FileOutputStream(extraOutput);
							buffer = new byte[1024];
							
							while ((length = is.read(buffer)) > 0) {
								os.write(buffer, 0, length);
							}
	
							is.close();
							os.close();
							
							secondCounter++;

						}
						
						containsCounter++;
						theCounter = theCounter + upperBound;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				count++;
				
			}
		} 
		

	}
	
	private static void showInput() {
		System.out.println("Aranacak kelimeler: ");
		for(int count = 0 ; count < words.length - 1 ; count++) {
			System.out.print(words[count] + ", ");
		}
		System.out.println(words[words.length-1]);
	}

	private static void getInput() {
		scanner = new Scanner(System.in);
		words = null;
		int wordCount = 0;
		System.out.println("Aranacak kelimeleri girin: ");
		String input = scanner.nextLine();
		String temp = input.replace(" ", "");
		wordCount = input.length() - temp.length() + 1;
		words = new String[wordCount];
		int count;
		for(count = 0 ; count < words.length - 1; count++) {
			words[count] = input.substring(0, input.indexOf(' '));
			input = input.substring(input.indexOf(' ') + 1);
		}
		words[count] = input;
	}
	
	private static void waitPermission() {
		System.out.println("Press Enter to start");
		scanner.nextLine();
	}
}