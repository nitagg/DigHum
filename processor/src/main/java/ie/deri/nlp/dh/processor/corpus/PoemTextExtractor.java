package ie.deri.nlp.dh.processor.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PoemTextExtractor {

	private static final String delimiter = "##"; 
	
	public static List<String> extractPoemsParagraphs(String fileName){
		ArrayList<String> paragraphs = new ArrayList<String>();

		BufferedReader reader = BasicFileTools.getBufferedReaderFile(fileName);

		int count = 1;
		String line = null;
		String paragraph = "";
		try {
			while((line = reader.readLine()) != null){
				if(line.length()>10){
					paragraph = paragraph + delimiter + BasicFileTools.cleanText(line);
				}
				else if(line.isEmpty()){
					if(!paragraph.isEmpty()){
						paragraphs.add(fileName+count+ "\t" +paragraph);
						paragraph = "";
						count++;
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return paragraphs;
	}

	public static List<String> extractPoemsParagraphs(File directory, String targetFile){
		ArrayList<String> paragraphs = new ArrayList<String>();

		List<File> files = Arrays.asList(directory.listFiles());

		for(File file : files){
			List<String> poemsParagraphs = extractPoemsParagraphs(file.getAbsolutePath());
			paragraphs.addAll(poemsParagraphs);
		}

		StringBuffer buffer = new StringBuffer();
		for(String para: paragraphs)
			buffer.append(para+"\n");

		BasicFileTools.writeFile(targetFile, buffer.toString());

		return paragraphs;
	}

	public static void main(String[] args) {
		List<String> paragraphs = PoemTextExtractor.extractPoemsParagraphs(new File("/Users/nitagg/Work/Digital Humanities/Experiment/romantic poems esa by line/poems/TM/"),
				"/Users/nitagg/Work/Digital Humanities/Experiment/romantic poems esa by line/poems/AllPara.TM.txt");

		for(String para:paragraphs)
			System.out.println(para);
	}
}
