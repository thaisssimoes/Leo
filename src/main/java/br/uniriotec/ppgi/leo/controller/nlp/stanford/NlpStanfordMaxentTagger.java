package br.uniriotec.ppgi.leo.controller.nlp.stanford;

import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class NlpStanfordMaxentTagger {
	
	public static String EN_WSJ_L3W 	= "./Config/taggers/EN_US/wsj-0-18-left3words.tagger";
	public static String EN_WSJ_BID 	= "./Config/taggers/EN_US/wsj-0-18-bidirectional-distsim.tagger";
	
	private static MaxentTagger TAGGER_EN_WSJ_L3W = null;
	private static MaxentTagger TAGGER_EN_WSJ_BID = null;
	
	private NlpStanfordMaxentTagger(){}
	
	
	public static MaxentTagger getENStandartTagger() throws IOException, ClassNotFoundException{
		return getTaggerEnWsjBid();
	}
	
	public static synchronized MaxentTagger getTaggerEnWsjL3w() throws IOException, ClassNotFoundException{
		if(TAGGER_EN_WSJ_L3W == null){
			TAGGER_EN_WSJ_L3W = new MaxentTagger(EN_WSJ_L3W);
		}
		
		return TAGGER_EN_WSJ_L3W;
	}
	
	public static synchronized MaxentTagger getTaggerEnWsjBid() throws IOException, ClassNotFoundException{
		if(TAGGER_EN_WSJ_BID == null){
			TAGGER_EN_WSJ_BID = new MaxentTagger(EN_WSJ_BID);
		}
		
		return TAGGER_EN_WSJ_BID;
	}
	
	
	
}
