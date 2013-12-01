package ie.deri.nlp.dh.mains.indexer;

import java.io.IOException;

import ie.deri.nlp.dh.processor.index.DocIndexer;

public class IndexerMain {
	
	public static void main(String[] args) {
//		String config = args[0];
		String config = "/Users/nitagg/deri/eclipse/Projects/DH/Mains/load/ie.deri.nlp.dh.properties";
		DocIndexer indexer = new DocIndexer(config);
//		WikiRDFAnchorIndexer indexer = new WikiRDFAnchorIndexer(config);

		System.out.println("indexing start");
		try {
			indexer.index();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("indexing done");
		
	}

}
