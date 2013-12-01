package ie.deri.nlp.mains.Similarity;

import gnu.trove.TIntDoubleHashMap;
import ie.deri.nlp.dh.processor.search.DocSearch;

public class PoemCorpusSimilarity {

	private DocSearch docSearch;

	public PoemCorpusSimilarity(String configFile){
		docSearch = new DocSearch(configFile);
	}

	public double getScore(String poem1, String poem2){
		TIntDoubleHashMap vector1 = docSearch.getDSMVector(poem1);
		TIntDoubleHashMap vector2 = docSearch.getDSMVector(poem2);
		return TroveVectorUtils.cosineProduct(vector1, vector2);
	}

}
