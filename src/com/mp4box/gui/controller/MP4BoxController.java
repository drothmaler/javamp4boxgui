package com.mp4box.gui.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.mp4box.gui.model.ConfSettingsKeys;
import com.mp4box.gui.ui.VideoListUi;

public class MP4BoxController {
	
	private Object[][] data;
	private HashMap<String, String> settings;
	private VideoListUi ui;
	
	public MP4BoxController(VideoListUi uiInput){
		ui = uiInput;
		data = ui.getModel().getData();
		settings = ui.getSettings();
		
		String mp4boxPath = getMP4BoxFilePath();
		if((new File(mp4boxPath).exists())){
			String input = "";
			for(int i=0;i<data.length; i++){
				input += " -cat \"" + data[i][0] + "\" ";
			}
			
			try {
				
				String outputFile = getOutputFile();
				String chapterFile = getOutputChapterFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(chapterFile));
				
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
				
				String execCommand = settings.get(ConfSettingsKeys.CMD) + " \"\" \"" + mp4boxPath + "\" " + input + " -chap \"" + chapterFile + "\" -new \"" + outputFile + "\"";
				System.out.println(execCommand);
				
				Runtime rt = Runtime.getRuntime();
				Process proc = rt.exec(execCommand);
				
				//Output to terminal
				String s;
				
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
	
				// read the output from the command
				System.out.println("Here is the output of the command:\n");
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}
				
				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(ui, "An exception (IO) happened, is the total length of the folder and filename very long? \nYou might wanna try a shorter folder path and/or filename! \n" + e.getMessage());
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(ui, "An exception happened, the application was unable to parse the command! \nHave you messed up the command in the properties file or maybe have strange letters in the folder/filename its unable to handle? \n" + e.getMessage());
				e.printStackTrace();
			}
		}else{
			JOptionPane.showMessageDialog(ui, "MP4Box can't be found...and it's sort of important! I was looking for it here: " + mp4boxPath +"\n"
					                        + "You can download MP4Box/GPAC from here: http://gpac.wp.mines-telecom.fr/ \n"
					                        + "I advice you to use a fresh build atm (April 2013) so the videos are joined correctly (Nightly DEV build of v0.5.1) \n"
					                        + "Also, look at the webpage 'http://sourceforge.net/p/javamp4boxgui/' for more info and copy/paste possibilities ;-)");
		}
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
		if(milliseconds>999){
			int milliseconds_left = milliseconds % 1000; //milliseconds left when the rest are seconds 
			seconds = (milliseconds - milliseconds_left) / 1000; //seconds we can get out of the milliseconds
			
			milliseconds = milliseconds_left; // Setting the milliseconds left to the main variable for it
		}
		
		/**
		 * ...and as above, but now for seconds and minutes
		 */
		seconds += time1_seconds + time2_seconds; //Total
		if(seconds>59){
			int seconds_left = seconds % 60;
			minutes = (seconds - seconds_left) % 60;
			
			seconds = seconds_left;
		}
		
		/**
		 * ...and as above, but now for minutes and seconds
		 */
		minutes += time1_minutes + time2_minutes; //Total
		if(minutes>59){
			int minutes_left = seconds % 60;
			hours = (minutes - minutes_left) % 60;
			
			minutes = minutes_left;
		}
		
		/**
		 * Here we can just add the rest together. There are no days etc in this time measurement
		 */
		hours += time1_hours + time2_hours;
		
		//And the time string is created and returned!
		return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
	}
	
	private String getOutputFile(){
		return findValidOutputFile(ui.getOutputPath() + ui.getOutputFilename(), settings.get(ConfSettingsKeys.AUTO_VIDEO_NAME), settings.get(ConfSettingsKeys.AUTO_VIDEO_FILETYPE)); 
	}
	
	private String getOutputChapterFile(){
		return findValidOutputFile(ui.getOutputPath() + settings.get(ConfSettingsKeys.CHAPTER_FILENAME), settings.get(ConfSettingsKeys.AUTO_CHAPTER_NAME), settings.get(ConfSettingsKeys.AUTO_CHAPTER_FILETYPE));
	}
	
	private String findValidOutputFile(String outputFile, String name, String filetype){
		if(ui.getAutoJoinCheckBox().isSelected()){
			File file = new File(outputFile);
			if(file.exists()){
				long i = 1;
				while(true){
					File newFile = new File(ui.getOutputPath() + name + i + filetype);
					if(newFile.exists()){
						i++;
					}else{
						outputFile = newFile.toString().replace("%20", " ");
						break;
					}
				}
			}
		}
		
		return outputFile;
	}
	
	private String getMP4BoxFilePath(){
		String mp4boxPath = FileSettings.getApplicationPath();
		if(!settings.get(ConfSettingsKeys.MP4BOX_PATH).isEmpty()){
			mp4boxPath = settings.get(ConfSettingsKeys.MP4BOX_PATH);
		}
		
		return mp4boxPath + settings.get(ConfSettingsKeys.MP4BOX_EXECUTABLE);
	}
	
}
