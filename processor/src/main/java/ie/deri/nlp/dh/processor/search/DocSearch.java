package ie.deri.nlp.dh.processor.search;


import gnu.trove.TIntDoubleHashMap;
import ie.deri.nlp.dh.lucene.Searcher;
import ie.deri.nlp.dh.processor.index.DocIndexer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;


/**
 * @author naggarw
 *
 */

public class DocSearch {

	//search field (Value)
	private static final String CONTENT_FIELD = DocIndexer.LucDocCreator.Fields.TextContent.toString();
	//CUI field (Key)
//	private static final String TITLE_FIELD = DocIndexer.LucDocCreator.Fields.Title.toString();

	private Properties config = null;

	private Searcher searcher;
	private int docHits = 1000;

	public DocSearch(String configFile){
		loadConfig(configFile);
		initialize();
	}

	private void initialize(){
		String indexPath = this.config.getProperty("indexPath");
		boolean RAM = Boolean.parseBoolean(config.getProperty("RAM")); 
		System.out.println("RAM: "+RAM);
		this.searcher = new Searcher(indexPath, RAM);
		docHits = new Integer(this.config.getProperty("docHits"));		
	}

	public void loadConfig(String configFilePath) {
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

	
	public TIntDoubleHashMap getDSMVector(String query){
		TIntDoubleHashMap results = new TIntDoubleHashMap();
		TopScoreDocCollector docCollector = this.searcher.search(query, docHits, CONTENT_FIELD, new StandardAnalyzer(Version.LUCENE_35));
//		TopScoreDocCollector docCollector = this.searcher.search(query, docHits, CONTENT_FIELD, new WhitespaceAnalyzer(Version.LUCENE_35));
		org.apache.lucene.search.ScoreDoc[] scoreDocs = docCollector.topDocs().scoreDocs;

		for(int docNo = 0; docNo < scoreDocs.length; ++docNo) {
			int docID = scoreDocs[docNo].doc;
			double score = scoreDocs[docNo].score;
			results.put(docID, score);
//			Document document = searcher.getDocumentWithDocID(docID);
//			System.out.println(docNo+": "+document.get(TITLE_FIELD));
		}
		return results;
	}

	
//	public WikiArticle getWikiArticle(String wikiTitle){
//		wikiTitle = wikiTitle.replace(" ", "_");
//		TopScoreDocCollector docCollector = this.searcher.termQuerySearch(wikiTitle, WIKI_TITLE_FIELD, 1);
//		org.apache.lucene.search.ScoreDoc[] scoreDocs = docCollector.topDocs().scoreDocs;
//
//		if(0 < scoreDocs.length) {
//			int docID = scoreDocs[0].doc;
//			org.apache.lucene.document.Document document = searcher.getDocumentWithDocID(docID);
//			return new WikiArticle(document.get(WIKI_CONTENT_FIELD), document.get(WIKI_TITLE_FIELD));
//		}
//		return null;		
//	}

	
	public static void main(String[] args) {
//		String configFile  = args[0];
		String configFile = "/Users/nitagg/deri/eclipse/Projects/DH/Mains/load/ie.deri.nlp.dh.properties";
		DocSearch docSearch = new DocSearch(configFile);

		TIntDoubleHashMap vector = docSearch.getDSMVector("love");
		System.out.println(vector.size());

		//		System.out.println(wikiEntitySearch.searcher.getTotalDocs());
		//		System.out.println(wikiEntitySearch.searcher.getTotalUniqueTerms());
		//				List<WikiResult> results = wikiEntitySearch.search(args[1].toLowerCase());				
		//

		//		WikiArticle wikiArticle = wikiEntitySearch.getWikiArticle("Montreux_Convention_Regarding_the_Regime_of_the_Straits");
		//		System.out.println(wikiArticle.getTitle() + "\ncontent:"+ wikiArticle.getEntitiesContent()+":");


	}
}