package ie.deri.nlp.dh.processor.index;

import ie.deri.nlp.dh.lucene.Indexer;
import ie.deri.nlp.dh.processor.corpus.BasicFileTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;





public class DocIndexer {

	private static Properties config = new Properties();
	private String docDirToRead;
	private String indexPathToWrite;
	private final double BUFFERRAMSIZE = 2000.0;
	private Indexer indexer;

	public DocIndexer(){
		loadConfig();
		openWriter();
	}

	public DocIndexer(String configPath){
		loadConfig(configPath);
		openWriter();
	}

	private void loadConfig(String configPath){
		try {
			config.load(new FileInputStream(configPath));			
			indexPathToWrite = config.getProperty("indexPath");
			docDirToRead = config.getProperty("docDirToRead");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}			

	private void loadConfig(){
		this.loadConfig("resources/load/com.ibm.bluej.watson.wikiAnchorTitleIndex.properties");
	}
	private void openWriter() {		
		try {

			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			Directory index = getIndex(indexPathToWrite);
			if(IndexReader.indexExists(index)) 
				config.setOpenMode(IndexWriterConfig.OpenMode.APPEND);			
			config.setRAMBufferSizeMB(BUFFERRAMSIZE);
			indexer = new Indexer(config, index);	
		}
		catch (IOException e) {
			e.printStackTrace();
		}			
	}

	private Directory getIndex(String indexPath) {
		Directory index = null;
		try {
			index = new SimpleFSDirectory(new File(indexPath));
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return index;
	}	

	public void index() throws IOException {

		List<File> files = BasicFileTools.listAllFiles(docDirToRead);
		
		LucDocCreator docCreator = new LucDocCreator();

		for(File file: files) {
			String title = file.getName();
			String content = BasicFileTools.cleanText(BasicFileTools.extractText(file));
			
			docCreator.addTitleField(title);
			docCreator.addContentField(content);
			indexer.addDoc(docCreator.getLucDoc());
			docCreator.reset();
		}
		indexer.closeIndexer();		
	}



	public static class LucDocCreator {

		private Document LucDoc = new Document();	

		public enum Fields {
			//WikiTitle, wiki article content that only have entities
			Title, TextContent;	
		}

		public void addTitleField(String title) {
			Field titleField = new Field(Fields.Title.toString(), title, Field.Store.YES, Field.Index.NOT_ANALYZED);
			LucDoc.add(titleField);			
		}

		public void addContentField(String content) {
			Field contentField = new Field(Fields.TextContent.toString(), content, Field.Store.YES, Field.Index.ANALYZED);
			LucDoc.add(contentField);			
		}	

		public Document getLucDoc() {		
			return LucDoc;
		}

		public void reset(){
			LucDoc = new Document();	
		}
	}

}


