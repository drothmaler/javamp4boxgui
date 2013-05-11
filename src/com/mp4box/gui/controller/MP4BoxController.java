package com.mp4box.gui.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.mp4box.gui.model.ConfLanguageKeys;
import com.mp4box.gui.model.ConfSettingsKeys;
import com.mp4box.gui.ui.VideoListUi;

public class MP4BoxController {
	
	private static Logger log = Logger.getLogger("Log");
	
	private Object[][] data;
	private HashMap<String, String> settings;
	private VideoListUi ui;
	
	public MP4BoxController(VideoListUi uiInput){
		ui = uiInput;
		data = ui.getVideoTableModel().getData();
		settings = ui.getSettings();
	}
	
	/**
	 * Executes the MP4box join command
	 * @param uiInput
	 */
	public void joinVideos(){
		log.log(Level.INFO, "##### Join start #####");
		
		String mp4boxPath = getMP4BoxFilePath();
		if((new File(mp4boxPath).exists())){
			String input = "";
			for(int i=0;i<data.length; i++){
				input += " -cat \"" + data[i][0] + "\" ";
			}
			
			try {
				String outputFile = getOutputFile();
				
				boolean singleFileSkipChapter = Boolean.valueOf(settings.get(ConfSettingsKeys.SINGLE_FILE_SKIP_CHAPTER));
				String chapterFile = "";
				String chapter = "";
				
				// Skips chapter if there is one file and singleFileSkipChapter is true 
				if(data.length>1 || !singleFileSkipChapter){
					chapterFile = getOutputChapterFile();
					BufferedWriter out = new BufferedWriter(new FileWriter(chapterFile));
					
					//Create chapters
					String duration = "00:00:00.000";
					for (int i = 0; i < data.length; i++) {
						if((Boolean) data[i][1]){
							out.write("CHAPTER" + i + "=" + duration + " \n");
						    out.newLine();
						    
						    out.write("CHAPTER" + i + "NAME=" + data[i][2] + " \n");
						    out.newLine();
						}
					    
						duration = addTime(duration, getVideoDuration(String.valueOf(data[i][0])));
					}
					out.flush();
					out.close();
					
					chapter = " -chap \"" + chapterFile + "\" ";
				}else{
					log.log(Level.INFO, "Skipping adding chapter to file. " + ConfSettingsKeys.SINGLE_FILE_SKIP_CHAPTER + " is enabled in " + FileSettings.FILE_NAME_SETTINGS);
				}
				
				String execCommand = settings.get(ConfSettingsKeys.CMD) + " \"\" \"" + mp4boxPath + "\" " + input + chapter + "-new \"" + outputFile + "\"";
				
				log.log(Level.INFO, "Here is the command and output of the command");
				log.log(Level.INFO, execCommand);
				
				Runtime rt = Runtime.getRuntime();
				Process proc = rt.exec(execCommand);
				
				//Output to terminal
				String s;
				
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
	
				// read the output from the command
				while ((s = stdInput.readLine()) != null) {
					log.log(Level.INFO, s);
				}
				
				while ((s = stdError.readLine()) != null) {
					log.log(Level.WARNING, s);
				}
			} catch (IOException e) {
				String message = "An exception (IO) happened, is the total length of the folder and filename very long? \nYou might wanna try a shorter folder path and/or filename!";
				JOptionPane.showMessageDialog(ui, message + "\n" + e.getMessage());
				log.log(Level.SEVERE, message, e);
			} catch (ParseException e) {
				String message = "An exception happened, the application was unable to parse the command! \nHave you messed up the command in the properties file or maybe have strange letters in the folder/filename its unable to handle?";
				JOptionPane.showMessageDialog(ui, message + "\n" + e.getMessage());
				log.log(Level.SEVERE, message, e);
			}catch(Exception e){
				log.log(Level.SEVERE, "If the error is a NullPointer, then it might be related to a filetype thats not supported!", e);
			}
		}else{
			JOptionPane.showMessageDialog(ui, getMP4BoxMissingMessage(mp4boxPath));
		}
		
		//Auto clears the table is that option is selected
		if(ui.getCheckBoxAutoClear().isSelected()){
			ui.getVideoTableModel().removeAllRows();
		}
		
		log.log(Level.INFO, "##### Join done #####");
	}
	
	public String getVideoDuration(String path) throws IOException{
		ProcessBuilder builder = new ProcessBuilder(getMP4BoxFilePath(), "-info", path);
		builder.redirectErrorStream(true);
		Process proc = builder.start();
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		
		String s;
		String duration = null;
		String keyWord = " Duration ";
		while ((s = stdInput.readLine()) != null) {
			if(s.contains(keyWord)){
				int last = s.indexOf(keyWord) + keyWord.length();
				duration = s.substring(last, s.length());
				
				break;
			}
		}
		
		return duration;
	}
	
	public String addTime(String time1, String time2) throws ParseException{
		String splitt1 = ":";
		String splitt2  = ".";
		
		time1 = time1.replace(splitt2,splitt1);
		String[] time1splits = time1.split(splitt1);
		int time1_milliseconds 	= Integer.parseInt(time1splits[3]);
		int time1_seconds 		= Integer.parseInt(time1splits[2]);
		int time1_minutes	 	= Integer.parseInt(time1splits[1]);
		int time1_hours 		= Integer.parseInt(time1splits[0]);
		
		time2 = time2.replace(splitt2,splitt1);
		String[] time2splits = time2.split(splitt1);
		int time2_milliseconds 	= Integer.parseInt(time2splits[3]);
		int time2_seconds 		= Integer.parseInt(time2splits[2]);
		int time2_minutes	 	= Integer.parseInt(time2splits[1]);
		int time2_hours 		= Integer.parseInt(time2splits[0]);
		
		int milliseconds = 0;
		int seconds = 0;
		int minutes = 0;
		int hours = 0;
		
		/**
		 * Calculating how many milliseconds we have and how many seconds in the total milliseconds totally
		 */
		milliseconds = time1_milliseconds + time2_milliseconds; //Total
		int milliseconds_left = milliseconds % 1000; //milliseconds left when the rest are seconds 
		seconds = (milliseconds - milliseconds_left) / 1000; //seconds we can get out of the milliseconds
		milliseconds = milliseconds_left; // Setting the milliseconds left to the main variable for it
		
		/**
		 * ...and as above, but now for seconds and minutes
		 */
		seconds += time1_seconds + time2_seconds; //Total
		int seconds_left = seconds % 60;
		minutes = (seconds - seconds_left) / 60;
		seconds = seconds_left;
		
		/**
		 * ...and as above, but now for minutes and seconds
		 */
		minutes += time1_minutes + time2_minutes; //Total
		int minutes_left = minutes % 60;
		hours = (minutes - minutes_left) / 60;
		minutes = minutes_left;
		
		/**
		 * Here we can just add the rest together. There are no days etc in this time measurement
		 */
		hours += time1_hours + time2_hours;
		
		//And the time string is created and returned!
		return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
	}
	
	/**
	 * Gets the output video file destination.
	 * Makes sure it's a filename it can use (file doesn't exist already)
	 * @return
	 */
	private String getOutputFile(){
		String folderpath = ui.getFolderPathOutput();
		String fileType = settings.get(ConfSettingsKeys.VIDEO_FILE_TYPE);
		String filename = ui.getFilenameOutput().replace(fileType, "");
		
		// If there is only one file in the list, then lets just use that files name
		boolean keepName = Boolean.valueOf(settings.get(ConfSettingsKeys.SINGLE_FILE_KEEP_NAME));
		if(data.length==1 && keepName){
			filename = ui.getFilenameOutput((String) data[0][0]).replace(fileType, "");
			
			log.log(Level.INFO, "Using filename as output filename. " + ConfSettingsKeys.SINGLE_FILE_KEEP_NAME + " is enabled in " + FileSettings.FILE_NAME_SETTINGS);
		}
		
		return findValidOutputFile(folderpath, filename, fileType);
	}
	
	/**
	 * Gets the output chapter file destination.
	 * Makes sure it's a filename it can use (file doesn't exist already)
	 * @return
	 */
	private String getOutputChapterFile(){
		String folderpath = ui.getFolderPathOutput();
		String filename = settings.get(ConfSettingsKeys.CHAPTER_FILENAME);
		String filetype = settings.get(ConfSettingsKeys.CHAPTER_FILE_TYPE);
		
		return findValidOutputFile(folderpath, filename, filetype);
	}
	
	/**
	 * If the auto join is selected, then don't overwrite, but find a new filename!
	 * @param folderpath
	 * @param filename
	 * @param filetype
	 * @return
	 */
	private String findValidOutputFile(String folderpath, String filename, String filetype){
		if(ui.getCheckBoxAutoJoin().isSelected()){
			File file = new File(folderpath + filename + filetype);
			if(file.exists()){
				long i = 1;
				while(true){
					File newFile = new File(folderpath + filename + "_" + i + filetype);
					if(newFile.exists()){
						i++;
					}else{
						try {
							log.log(Level.INFO, "File exists, new file created: " + newFile.getCanonicalPath().toString());
						} catch (IOException e) {
							log.log(Level.WARNING, "Exception with the new output file!", e);
						}
						
						file = newFile;
						break;
					}
				}
			}
			
			//Return the new file to use
			return file.toString().replace("%20", " ");
		}
		
		//Return the same file since we only want to overwrite in auto mode!
		return folderpath + filename + filetype;
	}
	
	public String getMP4BoxFilePath(){
		String mp4boxPath = FileSettings.getApplicationPath();
		if(!settings.get(ConfSettingsKeys.MP4BOX_PATH).isEmpty()){
			mp4boxPath = settings.get(ConfSettingsKeys.MP4BOX_PATH);
		}
		
		return mp4boxPath + settings.get(ConfSettingsKeys.MP4BOX_EXECUTABLE);
	}
	
	public String getMP4BoxMissingMessage(String mp4boxPath){
		return "Can't find the MP4Box binary! \nExpected to find it here: " + mp4boxPath +"\n"
            + "If you need further information, look in the '" + settings.get(ConfLanguageKeys.TAB_NAME_INFORMATION) + "' tab!\n"
            + "This app is tested with build: 'Nightly DEV build of v0.5.1' \n";
	}
	
}
