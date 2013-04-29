package com.mp4box.gui.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Scanner;

public class FileSettings {
	
	public static String newline = System.getProperty("line.separator");
	public static String CONFIG_ASSIGN_SYMBOLE = "=";
	
	private HashMap<String, String> settings = null;
	private File settingsFile = null;;
	
	public FileSettings() {
		settingsFile = new File(".");
		try {
			settingsFile = new File(settingsFile.getCanonicalPath() + "/settings.conf");
			if(!settingsFile.exists()){
				//Tries to create the missing settings file
				try {
					setupNewSettings();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			
			//Tries to create the missing settings file
			try {
				setupNewSettings();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		//Only reads in the properties file if it exists
		if(settingsFile.exists()){
			readSettings(settingsFile);
		}
	}
	
	public FileSettings(File settingsFile) {
		readSettings(settingsFile);
		this.settingsFile = settingsFile;
	}
	
	private void setupNewSettings() throws IOException{
		settingsFile.createNewFile();
		
		settings = new HashMap<String, String>();
		settings.put(ParameterStrings.AUTO_CHAPTER_FILETYPE, ParameterDefaultValues.AUTO_CHAPTER_FILETYPE);
		settings.put(ParameterStrings.AUTO_CHAPTER_NAME, ParameterDefaultValues.AUTO_CHAPTER_NAME);
		settings.put(ParameterStrings.AUTO_VIDEO_FILETYPE, ParameterDefaultValues.AUTO_VIDEO_FILETYPE);
		settings.put(ParameterStrings.AUTO_VIDEO_NAME, ParameterDefaultValues.AUTO_VIDEO_NAME);
		settings.put(ParameterStrings.BUTTON_TEXT, ParameterDefaultValues.BUTTON_TEXT);
		settings.put(ParameterStrings.CHAPTER_ENABLED, ParameterDefaultValues.CHAPTER_ENABLED);
		settings.put(ParameterStrings.CHAPTER_NAME, ParameterDefaultValues.CHAPTER_NAME);
		settings.put(ParameterStrings.CHAPTER_FILENAME, ParameterDefaultValues.CHAPTER_FILENAME);
		settings.put(ParameterStrings.CHECKBOX_AUTOCLEAR, ParameterDefaultValues.CHECKBOX_AUTOCLEAR);
		settings.put(ParameterStrings.CHECKBOX_AUTOCLEAR_SELECTED, ParameterDefaultValues.CHECKBOX_AUTOCLEAR_SELECTED);
		settings.put(ParameterStrings.CHECKBOX_AUTOJOIN, ParameterDefaultValues.CHECKBOX_AUTOJOIN);
		settings.put(ParameterStrings.CHECKBOX_AUTOJOIN_SELECTED, ParameterDefaultValues.CHECKBOX_AUTOJOIN_SELECTED);
		settings.put(ParameterStrings.CMD, ParameterDefaultValues.CMD);
		settings.put(ParameterStrings.LIST_BACKGROUND_COLOUR, ParameterDefaultValues.LIST_BACKGROUND_COLOUR);
		settings.put(ParameterStrings.MP4BOX_EXECUTABLE, ParameterDefaultValues.MP4BOX_EXECUTABLE);
		settings.put(ParameterStrings.MP4BOX_PATH, ParameterDefaultValues.MP4BOX_PATH);
		settings.put(ParameterStrings.OUTPUT_FILE, ParameterDefaultValues.OUTPUT_FILE);
		settings.put(ParameterStrings.OUTPUT_PATH, ParameterDefaultValues.OUTPUT_PATH);
		
		saveSettings(settings);
	}
	
	private void readSettings(File file){
		try {
			Scanner scanner = new Scanner(new FileInputStream(file));
			
			HashMap<String, String> settingsRead = new HashMap<String, String>();
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
							String value = line.substring(equalsAt+1);
							settingsRead.put(name, value);
						}
					}
					
					
				}
			}finally{
				scanner.close();
			}
			
			this.settings = settingsRead;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean saveSettings(HashMap<String, String> settings){
		this.settings = settings;
		
		try {
			OutputStream os = new FileOutputStream(settingsFile);
			os.write(getSettingsAsString().getBytes());
			os.close();
			
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	private String getSettingsAsString(){
		String returnString = "";
		
		returnString += "## Note that some of the values are empty." + newline;
		returnString += "## That is because they use the path of the application, but this can be overriden by specifying a different path!" + newline;
		returnString += "## It applies to the following: MP4BoxPath, OutputPath" + newline;
		
		for (String key : settings.keySet()) {
			String value = settings.get(key);
			returnString += key + CONFIG_ASSIGN_SYMBOLE + value + newline;
		}
		
		return returnString;
	}
	
	public String getCurrentOutputPath(){
		String currentPath = getApplicationPath();
		
		String settingsPath = settings.get(ParameterStrings.OUTPUT_PATH);
		if(!settingsPath.isEmpty()){
			currentPath = settingsPath + File.separator;
		}
		
		return currentPath;
	}
	
	public static String getApplicationPath(){
		//return (new File(ClassLoader.getSystemResource("").getPath())).getAbsolutePath().replace("%20", " ") + File.separator;
		return (new File("")).getAbsolutePath().replace("%20", " ") + File.separator;
	}
	
	public HashMap<String, String> getSettings() {
		return settings;
	}
	
	public void setSettings(HashMap<String, String> settings) {
		this.settings = settings;
	}
}
