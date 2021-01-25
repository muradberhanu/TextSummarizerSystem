package edu.carleton.comp4601.resources;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Collections;


public class SummaryTool{
    String in;
    FileOutputStream out;
    ArrayList<Sentence> sentences, sentencesWithoutStopWords, contentSummary;
    ArrayList<Paragraph> paragraphs;
    int noOfSentences, noOfParagraphs;
    int maxSummaryLength = 15;

    double[][] intersectionMatrix;
    LinkedHashMap<Sentence,Double> dictionary;
    
    //Python NLTK stop words
//    String[] stopWords = {"ourselves", "hers", "between", "yourself", "but", "again", "there",
//    		"about", "once", "during", "out", "very", "having", "with", "they", "own", "an", 
//    		"be", "some", "for", "do", "its", "yours", "such", "into", "of", "most", "itself", 
//    		"other", "off", "is", "s", "am", "or", "who", "as", "from", "him", "each", "the", 
//    		"themselves", "until", "below", "are", "we", "these", "your", "his", "through", 
//    		"don", "nor", "me", "were", "her", "more", "himself", "this", "down", "should", 
//    		"our", "their", "while", "above", "both", "up", "to", "ours", "had", "she", "all", 
//    		"no", "when", "at", "any", "before", "them", "same", "and", "been", "have", "in", 
//    		"will", "on", "does", "yourselves", "then", "that", "because", "what", "over", 
//    		"why", "so", "can", "did", "not", "now", "under", "he", "you", "herself", "has", 
//    		"just", "where", "too", "only", "myself", "which", "those", "i", "after", "few", 
//    		"whom", "t", "being", "if", "theirs", "my", "against", "a", "by", "doing", "it", 
//    		"how", "further", "was", "here", "than" };
    
    String[] stopWords = { "a","able","about","after","all","also","am",
          "an","and","any","are","as","at","be","because","been","but","by","can","cannot","could","did",
          "do","does","either","else","ever","every","for","from","get","got","had","has","have","he","her","hers","him","his","how","I",
          "if","in","into","is","it","its","just","let","like","likely","may","me",
          "might","most","must","my","neither","no","nor","not","of","off",
          "often","on","only","or","other","our","own","said","say","says","she",
          "should","so","some","than","that","the","their","them","then","there",
          "these","they","this","they're","to","too","that's","us","was","we","were",
          "what","when","where","which","while","who","whom","why","will","with",
          "would","yet","you","your", "you're" };


    SummaryTool(){
        in = null;
        out = null;
        noOfSentences = 0;
        noOfParagraphs = 0;
    }

    void init(String urlEnding, int maxSummaryLength){
    	this.maxSummaryLength = maxSummaryLength;
        sentences = new ArrayList<Sentence>();
        sentencesWithoutStopWords = new ArrayList<Sentence>();
        paragraphs = new ArrayList<Paragraph>();
        contentSummary = new ArrayList<Sentence>();
        dictionary = new LinkedHashMap<Sentence,Double>();
        noOfSentences = 0;
        noOfParagraphs = 0;
        try {
            String page = "https://en.wikipedia.org/wiki/"+urlEnding;
            //Connecting to the web page
            Connection conn = Jsoup.connect(page).validateTLSCertificates(false);
            //executing the get request
            Document doc = conn.get();
            //Retrieving the contents (body) of the web page
            doc.select("a").unwrap();
            Elements selector = doc.select("sup");
            for (Element element : selector) {
                element.remove();
            }
            Elements e = doc.select("p"); 
            String result = e.toString();
            //System.out.println(result);

            for(Element element: e) {
            	Paragraph paragraph = new Paragraph(noOfParagraphs);
            	String[] sentenceList = element.toString().split("(?<=[a-z])\\.\\s+");
            	for(String sentence: sentenceList) {
            		String newSentence = Jsoup.parse(sentence).text();
            		Sentence sentenceObject = new Sentence(noOfSentences, newSentence, newSentence.length(), noOfParagraphs);
            		sentences.add(sentenceObject);
//            		String sentenceToClean = newSentence;
//            		for(String word: stopWords) {
//            			sentenceToClean = sentenceToClean.replace(word, "");
//            		}
//            		Sentence sentenceWithoutStopWordsObject = new Sentence(noOfSentences, sentenceToClean, sentenceToClean.length(), noOfParagraphs);
//            		sentencesWithoutStopWords.add(sentenceWithoutStopWordsObject);
            		noOfSentences++;
            		paragraph.sentences.add(sentenceObject);
//            		paragraph.sentencesWithoutStopWords.add(sentenceWithoutStopWordsObject);
            	}
            	paragraphs.add(paragraph);
            	noOfParagraphs++;
            }
            in = result;
            //out = new FileOutputStream("output.txt");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    double noOfCommonWords(Sentence str1, Sentence str2){
        double commonCount = 0;

        for(String str1Word : str1.value.split("\\s+")){
            for(String str2Word : str2.value.split("\\s+")){
                if(str1Word.compareToIgnoreCase(str2Word) == 0){
                    commonCount++;
                }
            }
        }

        return commonCount;
    }

    void createIntersectionMatrixAndDictionary(){
        intersectionMatrix = new double[noOfSentences][noOfSentences];
        for(int i=0;i<noOfSentences;i++){
            for(int j=0;j<noOfSentences;j++){

                if(i<=j){
                	Sentence str1 = sentences.get(i);
                    Sentence str2 = sentences.get(j);
//                    Sentence str1 = sentencesWithoutStopWords.get(i);
//                    Sentence str2 = sentencesWithoutStopWords.get(j);
                    intersectionMatrix[i][j] = noOfCommonWords(str1,str2) / ((double)(str1.noOfWords + str2.noOfWords) /2);
                }else{
                    intersectionMatrix[i][j] = intersectionMatrix[j][i];
                }

            }
        }
        
        //create Dictionary
        for(int i=0;i<noOfSentences;i++){
            double score = 0;
            for(int j=0;j<noOfSentences;j++){
                score+=intersectionMatrix[i][j];
            }
            dictionary.put(sentences.get(i), score);
            ((Sentence)sentences.get(i)).score = score;
        }
    }

    void createSummary(){

    	for(Paragraph paragraph: paragraphs) {
    		int primary_set = paragraph.sentences.size()/5;

            //Sort based on score (importance)
            Collections.sort(paragraph.sentences,new SentenceComparator());
            for(int i=0;i<=primary_set;i++){
                contentSummary.add(paragraph.sentences.get(i));
            }
    	}

        //To ensure proper ordering
        Collections.sort(contentSummary, new SentenceComparatorForSummary());
        
        //max sentence length for summary
        if(contentSummary.size()>maxSummaryLength) {
        	for(int i=contentSummary.size(); i>maxSummaryLength; i--) {
        		double minScore = Double.MAX_VALUE;
        		int removeIndex = -1;
        		for(int j=0; j<contentSummary.size(); j++) {
        			Sentence sentence = contentSummary.get(j);
        			if(sentence.score<minScore) {
        				minScore = sentence.score;
        				removeIndex = j;
        			}
        		}
        		contentSummary.remove(removeIndex);
        	}
        }
        

    }

    void printSummary(){
        //System.out.println("no of paragraphs = "+ noOfParagraphs);
        for(Sentence sentence : contentSummary){
            System.out.println(sentence.value);
        }
    }


}