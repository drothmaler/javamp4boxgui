package com.mp4box.gui.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mp4box.gui.model.ConfLanguageKeys;
import com.mp4box.gui.model.ConfLanguageValues;
import com.mp4box.gui.model.ConfSettingsKeys;
import com.mp4box.gui.model.ConfSettingsValues;

public class FileSettings {
	
	private static Logger log = Logger.getLogger("Log");
	
	public static String newline = System.getProperty("line.separator");
	public static String CONFIG_ASSIGN_SYMBOLE = "=";
	public static String NEW_LINE_CONF = "newLine";
	public static String NEW_LINE_CODE = "<br>";
	public static String HTML_TAG = "<html>";
	public static String FILE_NAME_SETTINGS = "settings.conf";
	public static String FILE_NAME_LANGUAGE = "language.conf";
	
	private HashMap<String, String> settings = new HashMap<String, String>();
	
	private HashMap<String, String> settingsHashMap = new HashMap<String, String>();
	private HashMap<String, String> languageHashMap = new HashMap<String, String>();
	
	private File settingsFile = new File(".");;
	private File languageFile = new File(".");;
	
	public FileSettings() {
		//Let's load both config files
		settingsFile = loadFile(FILE_NAME_SETTINGS, settingsFile, settingsHashMap, new ConfSettingsKeys(), new ConfSettingsValues());
		languageFile = loadFile(FILE_NAME_LANGUAGE, languageFile, languageHashMap, new ConfLanguageKeys(), new ConfLanguageValues());
		
		//And lets combine them into one HashMap
		settings.putAll(settingsHashMap);
		settings.putAll(languageHashMap);
	}
	
	/**
	 * This method loads the config file specified by the parameters provided.
	 * It will check if the file exists, and create it if it doesn't exist yet.
	 * It will populate the variables with the data read from conf files, or provide defaults otherwise
	 * @param fileName
	 * @param confFile
	 * @param confHashMap
	 * @param keyObject
	 * @param valueObject
	 * @return
	 */
	private File loadFile(String fileName, File confFile, HashMap<String, String> confHashMap, Object keyObject, Object valueObject){
		String file = "";
		try {
			file = confFile.getCanonicalPath() + File.separator + fileName;
			confFile = new File(file);
			if(!confFile.exists()){
				//Tries to create the missing conf file
				try {
					setupNewConfFile(confFile, confHashMap, keyObject, valueObject);
				} catch (IOException e) {
					log.log(Level.SEVERE, "Tried to create missing conf file " + confFile.getCanonicalPath(), e);
				}
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, "Unable to create missing conf file!", e);
		}
		
		//Only reads in the properties file if it exists
		if(confFile.exists()){
			readConfiguration(confFile, confHashMap);
		}else{
			log.log(Level.SEVERE, "The conf file " + file + "is unreadable, and should have been created by this point!");
		}
		
		return confFile;
	}
	
	/**
	 * Creates config file with default values.
	 * The type of config file is determined by the paramaters provided.
	 * @param confFile
	 * @param confHashMap
	 * @param keyObject
	 * @param valueObject
	 * @throws IOException
	 */
	private void setupNewConfFile(File confFile, HashMap<String, String> confHashMap, Object keyObject, Object valueObject) throws IOException{
		confFile.createNewFile();
		
		HashMap<String, String> keyHashMap = getKeyAndValue(keyObject);
		HashMap<String, String> valueHashMap = getKeyAndValue(valueObject);
		
		for (String key : keyHashMap.keySet()) {
			confHashMap.put(keyHashMap.get(key), valueHashMap.get(key));
		}
		
		saveConfiguration(confFile, confHashMap);
	}
	
	/**
	 * Extracts the variables name and value (key & value) from the class provided.
	 * It's made mainly for the classes that holds the default values in static Strings.
	 * @param klass
	 * @return
	 */
	public HashMap<String, String> getKeyAndValue(Object klass){
		Field[] fields = klass.getClass().getDeclaredFields();
		HashMap<String, String> keyValueHashMap = new HashMap<String, String>();
		
		for(Field field : fields){
			try {
				Object valueObject = field.get(klass);
				
				if(valueObject instanceof String){
					keyValueHashMap.put(field.getName(), (String) valueObject);
				}
			} catch (IllegalArgumentException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			} catch (IllegalAccessException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		
		return keyValueHashMap;
	}
	
	private void readConfiguration(File confFile, HashMap<String, String> confHashMap){
		try {
			Scanner scanner = new Scanner(new FileInputStream(confFile));
			
			try{
				while(scanner.hasNext()){
					String line = scanner.nextLine();
					if(line.equals("") || line.startsWith("##")){ //Skip empty lines
						//Do nothing
					}else{
						//A name value pair to add to the current configuration.
						char[] chars = line.toCharArray();
						
						/**
						 * This loop is here to find the first occurrence of the '='.
						 * It is used to divide the name and the value pairs.
						 */
						int equalsAt = -1;
						for(int i=1;i<chars.length;i++){
							String theChar = String.valueOf(chars[i]);
							if(theChar.equals(CONFIG_ASSIGN_SYMBOLE)){
								equalsAt = i;
								break;
							}
						}
						
						if(equalsAt>0){
							String name = line.substring(0, equalsAt);
							String value = (line.substring(equalsAt+1)).replaceAll(NEW_LINE_CONF, NEW_LINE_CODE);
							confHashMap.put(name, value);
						}
					}
					
					
				}
			}finally{
				scanner.close();
			}
			
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public boolean saveConfiguration(File confFile, HashMap<String, String> confHashMap){
		try {
			String confString = "";
			
			//Adds an explanation to the settings file
			if(confFile.getName().equals(FILE_NAME_SETTINGS)){
				confString += "## Note that some of the values are empty." + newline;
				confString += "## That is because they use the path of the application, but this can be overriden by specifying a different path!" + newline;
				confString += "## It applies to the following: MP4BoxPath, OutputPath" + newline;
			}else if(confFile.getName().equals(FILE_NAME_LANGUAGE)){
				confString += "## To add new line in text, use the text " + NEW_LINE_CONF + newline;
			}
			
			confString += getConfHashMapAsString(confHashMap);
			
			OutputStream os = new FileOutputStream(confFile);
			os.write(confString.getBytes());
			os.close();
			
			return true;
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			return false;
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
		
	}
	
	private String getConfHashMapAsString(HashMap<String, String> confHashMap){
		String returnString = "";
		
		//Let's get the keys as an array and sort it so that the properties file will become more systematic
		String[] keyArray = Arrays.copyOf(confHashMap.keySet().toArray(),confHashMap.keySet().size(), String[].class);
		Arrays.sort(keyArray);
		
		for (String key : keyArray) {
			String value = confHashMap.get(key);
			returnString += key + CONFIG_ASSIGN_SYMBOLE + value + newline;
		}
		
		return returnString;
	}
	
	public String getCurrentOutputPath(){
		String currentPath = getApplicationPath();
		
		//Checks that another path isn't defined in the conf file
		String settingsPath = settingsHashMap.get(ConfSettingsKeys.OUTPUT_FOLDER);
		if(!settingsPath.isEmpty()){
			currentPath = settingsPath + File.separator;
		}
		
		return currentPath;
	}
	
	public static String getApplicationPath(){
		return (new File("")).getAbsolutePath().replace("%20", " ") + File.separator;
	}
	
	public HashMap<String, String> getSettings() {
		return settings;
	}
}
