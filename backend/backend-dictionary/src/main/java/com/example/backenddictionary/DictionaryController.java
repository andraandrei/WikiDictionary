package com.example.backenddictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backenddictionary.service.FirebaseService;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


@RestController

public class DictionaryController {

	@Autowired
	FirebaseService firebaseService;

    ArrayList<DictionaryEntity> customDictionary = new ArrayList<DictionaryEntity>();

	

	@GetMapping(value="/getWord")

	public String getInfo(@RequestParam(value = "word") String word) throws IOException, JSONException, InterruptedException, ExecutionException
	{

		
		DictionaryEntity obj = new DictionaryEntity();
		JSONArray jarray = new JSONArray();
		JSONObject jobj = new JSONObject();
		String word1 = "";
		JSONArray meaningList = new JSONArray();
		JSONObject meaning = new JSONObject();
		JSONArray definitionList = new JSONArray();
		JSONObject definition = new JSONObject();
		ArrayList <String> arrayDefinition = new ArrayList<String>();
		
		// for (DictionaryEntity elem : customDictionary)
		// {
		// 	if (elem.getWord().equals(word))
		// 	{
		// 		JSONObject foundElem = new JSONObject(elem);
		// 		return foundElem.toString();
		// 	}
		// }

		/// GET FROM FIREBASE FIRST

		InputStream serviceAccount = new FileInputStream("./serviceAccount.json");
				GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
				FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(credentials)
					.build();
				FirebaseApp.initializeApp(options);

				Firestore db = FirestoreClient.getFirestore();
				// DocumentReference documentReference = db.collection("dictionary").document(word1);
				// ApiFuture<DocumentSnapshot> future = documentReference.get();
				// DocumentSnapshot document = future.get();
				
			// Add document data  with id "alovelace" using a hashmap
				ApiFuture<QuerySnapshot> query = db.collection("dictionary").get();
		

				QuerySnapshot querySnapshot = query.get();
				List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

				for (QueryDocumentSnapshot document : documents) {
					String localWord = document.getString("word");

					if(word.equals(localWord))
					{
						ArrayList<String> localDefinitions =  (ArrayList<String>) document.get("definition");

						obj.setWord(localWord);
						obj.setDefinition(localDefinitions);
						JSONObject j = new JSONObject(obj);
						return j.toString();
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
					JSONObject j = new JSONObject(obj);
					return j.toString();
					
		    	} 

				
		    	catch (JSONException je)
		    	{
		    		je.printStackTrace();
		    	}
		    	catch (Exception e)
		    	{
		    		e.printStackTrace();
		    	}

			// 	InputStream serviceAccount = new FileInputStream("./serviceAccount.json");
			// 	GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			// 	FirebaseOptions options = new FirebaseOptions.Builder()
			// 		.setCredentials(credentials)
			// 		.build();
			// 	FirebaseApp.initializeApp(options);

			// 	Firestore db = FirestoreClient.getFirestore();
			// 	// DocumentReference documentReference = db.collection("dictionary").document(word1);
			// 	// ApiFuture<DocumentSnapshot> future = documentReference.get();
			// 	// DocumentSnapshot document = future.get();
				
			// // Add document data  with id "alovelace" using a hashmap
			// 	ApiFuture<QuerySnapshot> query = db.collection("dictionary").get();
			

			// 	QuerySnapshot querySnapshot = query.get();
			// 	List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			// 	for (QueryDocumentSnapshot document : documents) {
					
					
					
			// 		System.out.println("Word: " + document.getString("word"));
					
			// 		System.out.println("Definitions: " + document.getString("definitions"));

			// 		word1=document.getString("word");
					
			// 		meaningList= jobj.getJSONArray(document.getString("definitions"));
			// 	}
			// 		obj.setWord(word1);
		    // 		obj.setDefinition(arrayDefinition);
			// 		JSONObject j = new JSONObject(obj);
			// 		return j.toString();
					
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
		 return word;
	}
	
	
	@DeleteMapping(value="/deleteWord")
	@ResponseBody()
	public String deleteWord(@RequestBody String jsonBody) throws IOException, JSONException 
	{
		
		DictionaryEntity obj = new DictionaryEntity();
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
	
	
	@CrossOrigin
	@PostMapping(value = "/setWord") // adds word to custom dictionary
	@ResponseBody
	public String postInfor (@RequestBody String jsonBody) throws IOException, JSONException, InterruptedException, ExecutionException 
	{


		// Use a service account
		InputStream serviceAccount = new FileInputStream("./serviceAccount.json");
		GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
		FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(credentials)
			.build();
		FirebaseApp.initializeApp(options);

		Firestore db = FirestoreClient.getFirestore();

		////////////////////////////////////////////

		DictionaryEntity obj = new DictionaryEntity();
		JSONObject jword = new JSONObject(jsonBody);
		
		String wordIf = jword.getString("word"); // quitar espacios en blanco
		String definitionIf = jword.getJSONArray("definitions").getString(0);
		
		
		if( wordIf.isBlank()  ||  definitionIf.isBlank() || Pattern.matches("[a-zA-Z]+", wordIf) == false || Pattern.matches("[a-zA-Z]+", definitionIf) == false)
		{
			System.out.println("WORD ESTA VACIO PAKETE");
			JSONObject errorMessage = new JSONObject();
			errorMessage.append("Error", "Word not added successfully");
			return errorMessage.toString();
		} else 
		
		{
			try {

				ArrayList<String> auxArrList = new ArrayList<String>();
				obj.setWord(wordIf);
				JSONArray arr = jword.getJSONArray("definitions");
				for (int i = 0; i<arr.length();i++) 
					
				{
					auxArrList.add(arr.getString(i));
				}
				
				obj.setDefinition(auxArrList);
				customDictionary.add(obj);	


				DocumentReference docRef1 = db.collection("dictionary").document(wordIf);
				// Add document data  with id "alovelace" using a hashmap
				Map<String, Object> data = new HashMap<>();
				data.put("word", wordIf);
				data.put("definitions", auxArrList.toString());
				//asynchronously write data
				ApiFuture<WriteResult> result = docRef1.set(data);
				// ...
				// result.get() blocks on response
				System.out.println("Update time : " + result.get().getUpdateTime());
				
				firebaseService.saveWordDetails(obj);
				
				JSONObject successMessage = new JSONObject();
				successMessage.append("Success", "Word "+obj.getWord()+" added successfully");
				return successMessage.toString();
			
			}
			
			catch (NumberFormatException ex) {
				System.out.println(ex);
				
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
				JSONObject errorMessage = new JSONObject();
				errorMessage.append("Error", "Word not added successfully");
				return errorMessage.toString();
			}
		}
		// JSONObject errorMessage = new JSONObject();
		// errorMessage.append("Error", "Word not added successfully");
		// return errorMessage.toString();
	    return firebaseService.saveWordDetails(obj);
		 
		
	}
	

	
	@PutMapping(value = "/updateWord")
	@ResponseBody()
	public String updateInfor (@RequestBody String jsonBody) throws IOException, JSONException {
		
		System.out.println(jsonBody);
		DictionaryEntity obj = new DictionaryEntity();
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
