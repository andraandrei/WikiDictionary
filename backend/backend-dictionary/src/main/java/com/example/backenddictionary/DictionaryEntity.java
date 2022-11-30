package com.example.backenddictionary;
import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;





 @Entity
 @Table (name="pruebabd")
public class DictionaryEntity implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String word;
	@Column (name="definitions",nullable=false, length = 255)
    private ArrayList<String> definitions = new ArrayList<String>();
	
	
	
	public DictionaryEntity() {
		
	}
	
	
	public void setWord(String word) {
		this.word=word;
	}
	
	public void setDefinition(ArrayList<String> def) {
		this.definitions=def;
	}
	
	
	public String getWord() {
		return word;
	}

	
	public ArrayList<String> getDefinition() {
		return definitions;
	}
}
