package com.example.backenddictionary;



import java.util.ArrayList;

public class DictionaryEntity {
  private String word;
  
  private ArrayList<String> definitions = new ArrayList<>();
  
  public void setWord(String word) {
    this.word = word;
  }
  
  public void setDefinition(ArrayList<String> def) {
    this.definitions = def;
  }
  
  public String getWord() {
    return this.word;
  }
  
  public ArrayList<String> getDefinition() {
    return this.definitions;
  }
}
