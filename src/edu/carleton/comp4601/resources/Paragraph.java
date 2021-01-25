package edu.carleton.comp4601.resources;

import java.util.ArrayList;

public class Paragraph{
    int number;
    ArrayList<Sentence> sentences;
    ArrayList<Sentence> sentencesWithoutStopWords;

    Paragraph(int number){
        this.number = number;
        sentences = new ArrayList<Sentence>();
        sentencesWithoutStopWords = new ArrayList<Sentence>();
    }
}