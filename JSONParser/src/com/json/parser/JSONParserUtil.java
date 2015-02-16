package com.json.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSONParserUtil {

	private static final String jsonFilePath = "C:\\Users\\prasannakumarr\\git\\JSONParser\\JSONParser\\json\\sample.json";

	public static void main(String[] args) {
		List<String> inputList = new ArrayList<String>();
		inputList.add("firstActiveTime");
		inputList.add("lastActiveTime");
		inputList.add("peer.ipAddress");
		inputList.add("object.ipAddress");

		Map<String, String> extractedMapSimpleJSONParser = extractFieldsFromJSONSimpleJSONParser(inputList);
		System.out.println("Simple JSON Parser Output:");
		System.out.println(extractedMapSimpleJSONParser);
		Map<String, String> extractedMapJacksonParser = extractFieldsFromJSONJacksonParser(inputList);
		System.out.println("Jackson JSON Parser Output:");
		System.out.println(extractedMapJacksonParser);
	}

	private static Map<String, String> extractFieldsFromJSONSimpleJSONParser(List<String> fields) {
		String[] keyObject = null;
		String jsonKeyName = null;
		String jsonValueName = null;
		JSONObject structure = null;
		String jsonKey = null;
		FileReader reader = null;
		JSONParser jsonParser = null;
		JSONObject jsonObject = null;
		Map<String, String> outputMap = new HashMap<>();
		try {
			reader = new FileReader(jsonFilePath);
			jsonParser = new JSONParser();
			jsonObject = (JSONObject) jsonParser.parse(reader);
			Iterator<String> iterator = fields.listIterator();
			while(iterator.hasNext()) {
				jsonKey = iterator.next();
				if(jsonKey.contains(".")) {
					keyObject = jsonKey.split("\\.");
					jsonKeyName = keyObject[0];
					jsonValueName = keyObject[1];
					structure = (JSONObject) jsonObject.get(jsonKeyName);
					outputMap.put(jsonKey, (String) structure.get(jsonValueName));
				} else {
					outputMap.put(jsonKey, (String) jsonObject.get(jsonKey));
				}
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return outputMap;
	}
	
	private static Map<String, String> extractFieldsFromJSONJacksonParser(List<String> fields) {
		String jsonKey = null;
		String[] keyObject = null;
		String jsonKeyName = null;
		String jsonValueName = null;
		JsonNode nameNode = null;
		Map<String, String> outputMap = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode;
		try {
			rootNode = objectMapper.readTree(new File(jsonFilePath));
			Iterator<String> iterator = fields.listIterator();
			while(iterator.hasNext()) {
				jsonKey = iterator.next();
				if(jsonKey.contains(".")) {
					keyObject = jsonKey.split("\\.");
					jsonKeyName = keyObject[0];
					jsonValueName = keyObject[1];
					nameNode = rootNode.path(jsonKeyName);
					outputMap.put(jsonKey, nameNode.path(jsonValueName).getTextValue());
				} else {
					outputMap.put(jsonKey, rootNode.get(jsonKey).getTextValue());
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputMap;
	}

}
