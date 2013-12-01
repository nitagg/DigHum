package ie.deri.nlp.dh.processor.corpus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class CorpusProcessor {


	private static void extrcatDocuments(List<File> files, String dirToWrite) throws IOException{
		int docMaxLines = 100;
		for(File file: files){
			String text = BasicFileTools.extractText(file);
			List<String> lines = Arrays.asList(text.split("\n"));
			int docNo = 1;
			int count = 0;			
			StringBuffer buffer = new StringBuffer();
			for(String line: lines){
				if(count < docMaxLines)
					buffer.append(line +"\n");
				else{
					String filePathToWrite = dirToWrite + File.separator +file.getName().substring(0, file.getName().lastIndexOf("."))+".doc"+docNo+".txt";
					BasicFileTools.writeFile(filePathToWrite, buffer.toString());
					buffer = new StringBuffer();
					docNo++;
					count = 0;
				}
				count++;
			}
			if(buffer.length()>0){
				if(Arrays.asList(buffer.toString().split("\n")).size() > docMaxLines/2){
					String filePathToWrite = dirToWrite + File.separator+file.getName().substring(0, file.getName().lastIndexOf("."))+".doc"+docNo+".txt";
					BasicFileTools.writeFile(filePathToWrite, buffer.toString());
					buffer = new StringBuffer();
					docNo++;
					count = 0;
				}
			}
		}

	}
	
	private static List<File> removeHiddenFiles(List<File> files){
		List<File> filesList = new ArrayList<File>();
		for(File file: files)
			if(!file.getName().startsWith("."))
				filesList.add(file);
		
		return filesList;
	} 

	public static void main(String[] args) {

		String dirPath = "/Users/nitagg/Work/Digital Humanities/Experiment/romantic_poems_PoetryCorpus/VerseOnlyTexts";
		String dirToWrite = "/Users/nitagg/Work/Digital Humanities/Experiment/romantic_poems_PoetryCorpus/PoemCorpus";
		List<File> files = removeHiddenFiles(BasicFileTools.listAllFiles(dirPath));
		try {
			extrcatDocuments(files, dirToWrite);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		int count = 1;
//		for(File file: files){				
//				System.out.println(count++ + ": " +file.getName());
//		}

	}
}
