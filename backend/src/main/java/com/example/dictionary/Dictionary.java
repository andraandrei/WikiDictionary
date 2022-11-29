package com.example.dictionary;

import java.util.ArrayList;

import org.json.JSONArray;

public class Dictionary {//class where setter & getter are definded for the id and the content
	;
	private ArrayList<String> definition = new ArrayList<String>();
	private String word;
	
	public Dictionary() {
		
	}
	
	public Dictionary (String word, ArrayList<String> definition){//Constructor de la clase con los parametros id y contentido

		this.definition=definition;
		this.word=word;

		
	}
	
	
	public void setWord(String word) {
		this.word=word;
	}
	
	public void setDefinition(ArrayList<String> def) {
		this.definition=def;
	}
	
	
	public String getWord() {
		return word;
	}

	
	public ArrayList<String> getDefinition() {
		return definition;
	}
	
	
	
}
