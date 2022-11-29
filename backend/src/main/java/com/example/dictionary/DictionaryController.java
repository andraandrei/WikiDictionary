package com.example.dictionary;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.json.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DictionaryController {
	
	ArrayList<Dictionary> customDictionary = new ArrayList<Dictionary>();
	
	@GetMapping(value="/getWord")
	public String getInfo(@RequestParam(value = "word") String word) throws IOException, JSONException
	{
		
		Dictionary obj = new Dictionary();
		JSONArray jarray = new JSONArray();
		JSONObject jobj = new JSONObject();
		String word1 = "";
		JSONArray meaningList = new JSONArray();
		JSONObject meaning = new JSONObject();
		JSONArray definitionList = new JSONArray();
		JSONObject definition = new JSONObject();
		ArrayList <String> arrayDefinition = new ArrayList<String>();
		
		for (Dictionary elem : customDictionary)
		{
			if (elem.getWord().equals(word))
			{
				JSONObject foundElem = new JSONObject(elem);
				return foundElem.toString();
			}
		}
		
		String urlToRead = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) 
		{
			
			for (String line; (line = reader.readLine()) != null;) 
			{
		    	try {
		    		
		    		jarray = new JSONArray(line);
		    		jobj = jarray.getJSONObject(0);
		    		word1 = jobj.getString("word");
		    		meaningList  = jobj.getJSONArray("meanings");
		    		
		    		for (int i = 0; i<meaningList.length();i++) 
		    		{
		    			meaning = meaningList.getJSONObject(i);
		    			definitionList = meaning.getJSONArray("definitions");
		    			
		    			for (int j = 0; j<definitionList.length();j++) 
		    			{
		    				definition = definitionList.getJSONObject(j);
		    				definition.getString("definition");
		    				String definitio = definition.getString("definition").replace("\\", "");
		    				arrayDefinition.add(definitio);    				  
		    			}
		    		}
		    		obj.setWord(word1);
		    		obj.setDefinition(arrayDefinition);
		    	}
		    	catch (JSONException je)
		    	{
		    		je.printStackTrace();
		    	}
		    	catch (Exception e)
		    	{
		    		e.printStackTrace();
		    	}
			}
		}
		catch (FileNotFoundException fe)
		{
			String message = "This is non existing word. Try again";
			JSONObject err = new JSONObject();
			err.put("error", message);
			//err.append( message);
			return err.toString();
		}
		JSONObject j = new JSONObject(obj);
		return j.toString();
	}

	
	@DeleteMapping(value="/deleteWord")
	@ResponseBody()
	public String deleteWord(@RequestBody String jsonBody) throws IOException, JSONException 
	{
		
		Dictionary obj = new Dictionary();
		JSONObject jword = new JSONObject(jsonBody);
		
		try {
			
			ArrayList<String> auxArrList = new ArrayList<String>();
			obj.setWord(jword.getString("word"));
			JSONArray arr = jword.getJSONArray("definitions");
			
			for (int i = 0; i<arr.length();i++) 
			{
				auxArrList.add(arr.getString(i));
			}
			obj.setDefinition(auxArrList);
			
			for (int i=0; i<customDictionary.size(); i++)
			{
				if (customDictionary.get(i).getWord().equals(obj.getWord()))
				{
					customDictionary.remove(i);
				}
			}	
			JSONObject successMessage = new JSONObject();
			successMessage.append("Success", "Word "+obj.getWord()+" removed successfully");
			return successMessage.toString();
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			JSONObject errorMessage = new JSONObject();
			errorMessage.append("Error", "Word not added successfully");
			return errorMessage.toString();
		}
	}
	
	
	@PostMapping(value = "/setWord") // adds word to custom dictionary
	@ResponseBody
	public String postInfor (@RequestBody String jsonBody) throws IOException, JSONException 
	{
		
		Dictionary obj = new Dictionary();
		JSONObject jword = new JSONObject(jsonBody);
		
		try {
			
			ArrayList<String> auxArrList = new ArrayList<String>();
			obj.setWord(jword.getString("word"));
			JSONArray arr = jword.getJSONArray("definitions");
	
			for (int i = 0; i<arr.length();i++) 
			{
				auxArrList.add(arr.getString(i));
			}
			
			obj.setDefinition(auxArrList);
			customDictionary.add(obj);	
			JSONObject successMessage = new JSONObject();
			successMessage.append("Success", "Word "+obj.getWord()+" added successfully");
			return successMessage.toString();	
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			JSONObject errorMessage = new JSONObject();
			errorMessage.append("Error", "Word not added successfully");
			return errorMessage.toString();
		}
	      
	}
	
	@PutMapping(value = "/updateWord")
	@ResponseBody()
	public String updateInfor (@RequestBody String jsonBody) throws IOException, JSONException {
		
		//Dictionary object = new Dictionary();
		Dictionary obj = new Dictionary();
		JSONObject jword = new JSONObject(jsonBody);
		
		try {
			
			ArrayList<String> arrayDefinition = new ArrayList<String>();
			obj.setWord(jword.getString("word"));
			JSONArray jDefinition = jword.getJSONArray("definitions");
			
			if (customDictionary.isEmpty()) {

				JSONObject errorMessage = new JSONObject();
				errorMessage.append("Error", "Word "+obj.getWord()+" is not posted in the custom dictionary. ");
				return errorMessage.toString();
			} 
			for (int i=0; i<customDictionary.size(); i++) 
			{
				if (customDictionary.get(i).getWord().equals(obj.getWord()))
				{
					for (int j = 0; j<jDefinition.length();j++) 
					{
						arrayDefinition.add(jDefinition.getString(j));
						customDictionary.add(j, obj);
					}
					obj.setDefinition(arrayDefinition);
					customDictionary.add(obj);	
					JSONObject successMessage = new JSONObject();
					successMessage.append("Success", "Word "+obj.getWord()+" updated successfully " +obj.getDefinition());
					return successMessage.toString();
				} 
	
			}			
		
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			JSONObject errorMessage = new JSONObject();
			errorMessage.append("Error", "Word not updated successfully");
			return errorMessage.toString();
		}
		JSONObject j = new JSONObject(obj);
		return j.toString();

		
	}

}

