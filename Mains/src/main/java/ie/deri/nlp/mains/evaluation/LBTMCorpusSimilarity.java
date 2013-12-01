package ie.deri.nlp.mains.evaluation;


import ie.deri.nlp.dh.processor.corpus.BasicFileTools;
import ie.deri.nlp.mains.Similarity.PoemCorpusSimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class LBTMCorpusSimilarity {

	private Properties config = null;
	private String poemsFile1ToRead = null;
	private String poemsFile2ToRead = null;
	private String poemsFileToWrite = null;
	private PoemCorpusSimilarity poemCorpusSimilarity = null;

	public LBTMCorpusSimilarity(String configFile){
		loadConfig(configFile);
		this.poemsFile1ToRead = this.config.getProperty("poemsFile1ToRead");
		this.poemsFile2ToRead = this.config.getProperty("poemsFile2ToRead");

		this.poemsFileToWrite = this.config.getProperty("poemsFileToWrite");
		this.poemCorpusSimilarity = new PoemCorpusSimilarity(configFile);
	}

	private void loadConfig(String configFilePath) {
		if(this.config == null) {
			try {
				this.config =  new Properties();
				this.config.load(new FileInputStream(configFilePath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void process() throws IOException{
		StringBuffer buffer = new StringBuffer();

		BufferedReader reader1 = BasicFileTools.getBufferedReaderFile(poemsFile1ToRead);
		String line1 = null;
		while((line1 = reader1.readLine()) != null){
			List<String> pair1 = Arrays.asList(line1.split("\t"));
			if(pair1.size()<2)
				continue;			
			String poem1 = pair1.get(1);
			File file = new File(pair1.get(0));
			String poem1FileName = file.getName(); 

			//read file to get comparison between every poems.
			BufferedReader reader2 = BasicFileTools.getBufferedReaderFile(poemsFile2ToRead);
			String line2 = null;
			while((line2 = reader2.readLine()) != null){
				List<String> pair2 = Arrays.asList(line2.split("\t"));
				if(pair2.size()<2)
					continue;
				String poem2 = pair2.get(1); 

				//				double score = this.esaSimilarity.getScore(poems1, poems2);
				double score = this.poemCorpusSimilarity.getScore(poem1.replace("##", " "), poem2.replace("##", " "));

				file = new File(pair2.get(0));
				String poem2FileName = file.getName(); 

				buffer.append(poem1FileName+"\t"+poem2FileName+"\t"+score+"\n");
				System.out.println(poem1FileName+"\t"+poem2FileName+"\t"+score);
			}


		}

		BasicFileTools.writeFile(poemsFileToWrite, buffer.toString());
	}

	public static void main(String[] args) {

//		String configFile = args[0];
				String configFile = "/Users/nitagg/deri/eclipse/Projects/DH/Mains/load/ie.deri.nlp.dh.properties";

		LBTMCorpusSimilarity main = new LBTMCorpusSimilarity(configFile);

		try {
			main.process();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
