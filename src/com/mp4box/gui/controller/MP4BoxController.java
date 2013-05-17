package com.mp4box.gui.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.mp4box.gui.model.ConfLanguageKeys;
import com.mp4box.gui.model.ConfSettingsKeys;
import com.mp4box.gui.model.ConfSettingsRegex;
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
		
		String mp4boxFilePath = getMP4BoxFilePath();
		if((new File(mp4boxFilePath).exists())){
			String addInputCommand = "";
			String outputFile = getOutputFile();
			String addChapterCommand =  createChapterFile();
				
			if(addChapterCommand!=null){
				try {
					addInputCommand = createInputCommand(0, data.length);
					executeMP4BoxCommand(mp4boxFilePath, addInputCommand, addChapterCommand, outputFile);
				} catch (IOException e) {
					String message = "An exception (IO) happened, is the total length of the folder and filename very long? \nWill try and join the video list in portions! (Divide & Conquere)";
					log.log(Level.SEVERE, message, e);
					
					/**
					 * Since the join failed, and it's likely that it has to do with the command being to big, 
					 * lets try and split the data list up and join them in portions.
					 */
					divideAndConquere(mp4boxFilePath, addChapterCommand, outputFile);
				}catch(Exception e){
					log.log(Level.SEVERE, "If the error is a NullPointer, then it might be related to a filetype thats not supported!", e);
				}
			}
		}else{
			JOptionPane.showMessageDialog(ui, getMP4BoxMissingMessage(mp4boxFilePath));
		}
		
		//Auto clears the table is that option is selected
		if(ui.getCheckBoxAutoClear().isSelected()){
			ui.getVideoTableModel().removeAllRows();
		}
		
		log.log(Level.INFO, "##### Join done #####");
	}
	
	/**
	 * When the number of videos are too great, divide and conquer the workload.
	 * There is an issue where the join String/command is too long, 
	 * this method will try and split up the list of videos to join to avoid this problem!
	 * - First attempt:  Split the list in two
	 * - Second attempt: Split the list in four
	 * - Third attempt:  Add one onto the previously joined expanding the joined video one video at time.
	 * @param mp4boxFilePath
	 * @param outputFile
	 */
	private void divideAndConquere(String mp4boxFilePath, String addChapterCommand, String outputFile){
		log.log(Level.INFO, "===== DIVIDE & CONQUER =====");
		
		String outputPath = outputFile.substring(0, outputFile.lastIndexOf(File.separator) + 1); //The folder to save the output file in
		int attempt = 0; //Attempt three things, divide by 2, 4 or finally join 2 and 2 videos at a time!
		
		String[] tempAttemptSubListSizes = settings.get(ConfSettingsKeys.DIVIDE_AND_CONQUERE_SUB_LIST_SIZES).split(",");
		int[] attemptSubListSizes = new int[tempAttemptSubListSizes.length]; //The size values for splitting the data list
		
		/**
		 * Converts the attempt values from the temp string array to the int array
		 */
		for(int i=0;i<attemptSubListSizes.length; i++){
			attemptSubListSizes[i] = Integer.valueOf(tempAttemptSubListSizes[i]);
		}
		
		/**
		 * Let's try three different "ways" of joining the videos
		 * The "+ 1" represents the final solution which is adding one and one.
		 */
		while(attempt<(attemptSubListSizes.length + 1)){
			log.log(Level.INFO, "===== Attempt " + (attempt + 1 ) + " =====");
			
			toAttemptIf:
			if(attempt<attemptSubListSizes.length){
				int divider = attemptSubListSizes[attempt];
				
				ArrayList<String> listTempOutputFiles = new ArrayList<>(); //Temp files that should be deleted
				int dataLength = data.length;
				int listChunkSmall = dataLength/divider; //The small list size chunk. The last list chunk will have a different size often due to math! ;-)
				
				/**
				 * Let's join chunks of the list together into temporary output video files.
				 * This will process the data contents into video files in stages
				 */
				log.log(Level.INFO, "===== Joining video list in portions =====");
				for(int i=0;i<divider;i++){
					String tempOutputFile = findValidOutputFile(outputPath, "temp_join_" + i, settings.get(ConfSettingsKeys.VIDEO_FILE_TYPE)); //Temp output filename
					int listStart = i*listChunkSmall; //Where to start in the data list
					int listStop = listChunkSmall*(i+1); //Where to stop in the data list
					
					/**
					 * For the last iteration, the stop should be the end of the data list size so that the remaining videos are added.
					 */
					if(i==(divider-1)){
						listStop = dataLength;
					}
					
					listTempOutputFiles.add(tempOutputFile); //Add the temporary output file to a list of files to remove
					try {
						String addInputCommand = createInputCommand(listStart, listStop); //Creates input commands from a subset of the main list of videos to add
						
						executeMP4BoxCommand(mp4boxFilePath, addInputCommand, "", tempOutputFile); //Creates a temp video file of the list subset
					} catch (Exception e){
						log.log(Level.SEVERE, "Tried joining smaller lists of input files, but had an IOexception in attempt " + (attempt + 1), e);
						
						break toAttemptIf;
					}
				}
				
				/**
				 * Create input commands of the temp video files created
				 */
				String addInputCommand = "";
				for (int i = 0; i < listTempOutputFiles.size(); i++) {
					String tempInput = settings.get(ConfSettingsKeys.MP4BOX_INPUT());
					tempInput = tempInput.replace(ConfSettingsRegex.MP4BOX_INPUT_FILE, listTempOutputFiles.get(i));
					
					addInputCommand += tempInput;
				}
				
				/**
				 * Join together the temp video files into the final output video
				 */
				log.log(Level.INFO, "===== Joining temporary videos to final video with chapters =====");
				try {
					executeMP4BoxCommand(mp4boxFilePath, addInputCommand, addChapterCommand, outputFile);
					attempt = attemptSubListSizes.length + 2; //Sets the attempt number high to stop the while loop
				} catch (IOException e) {
					log.log(Level.SEVERE, "Tried attempt " + attempt + " for joining videos after an IOexception was thrown in the original join!", e);
					
					break toAttemptIf;
				}
				
				/**
				 * Deletes the temporary files created
				 */
				for(int i=0;i<listTempOutputFiles.size();i++){
					File fileToDelete = new File(listTempOutputFiles.get(i));
					if(fileToDelete.delete()){
						log.log(Level.INFO, "The temp file " + fileToDelete.getName() + " is deleted!");
					}else{
						log.log(Level.WARNING, "Unable to delete the temp file " + fileToDelete.getName() + "!");
					}
				}
			}else{
				/**
				 * Ok, so the other attempts failed, so lets try adding one and one video onto a temp video file.
				 * It's slow (REALLY SLOW), but unless the video folder and filename is ridiculously long, it should work!
				 */
				log.log(Level.INFO, "===== Joining videos by adding one onto a temp file at a time =====");
				
				String currentTempOutputFile = ""; //The temp video file used between loops
				for(int i=0;i<data.length;i++){
					String tempOutputFile = findValidOutputFile(outputPath, "temp_join_" + i, settings.get(ConfSettingsKeys.VIDEO_FILE_TYPE)); //The next temp video file for this iteration!
					
					//The first video is all the previous videos we have joined so far
					String inputOne = "";
					
					/**
					 * Here the add input command for the inputOne variable is created.
					 * [IF]   Checks if it is the first iteration, since "currentTempOutputFile" will be empty, we will add the first video in the data list
					 * [ELSE] Use the currentTempOutputFile. This will be done everytime except the first iteration.
					 */
					if(i==0){
						inputOne = settings.get(ConfSettingsKeys.MP4BOX_INPUT());
						inputOne = inputOne.replace(ConfSettingsRegex.MP4BOX_INPUT_FILE, (String) data[i][0]);
						
						//Let's iterate the i value so that the second video input is set to the next video below
						i++;
					}else{
						inputOne = settings.get(ConfSettingsKeys.MP4BOX_INPUT());
						inputOne = inputOne.replace(ConfSettingsRegex.MP4BOX_INPUT_FILE, currentTempOutputFile);
					}
					
					//The next video to add to the first one.
					String inputTwo = settings.get(ConfSettingsKeys.MP4BOX_INPUT()); 
					inputTwo = inputTwo.replace(ConfSettingsRegex.MP4BOX_INPUT_FILE, (String) data[i][0]);
					
					/**
					 * [IF]   As long as we have not reached the last video in the data list, we will join video onto an temp output file.
					 * [ELSE] When we reach the last item in the video list, we will join the videos into the final output videos.
					 */
					if(i<(data.length-1)){
						try {
							executeMP4BoxCommand(mp4boxFilePath, inputOne + inputTwo, "", tempOutputFile);
						} catch (IOException e){
							log.log(Level.SEVERE, "Tried joining two input files, but had an IOexception in attempt " + (attempt + 1), e);
							
							break toAttemptIf;
						}
					}else{
						try {
							executeMP4BoxCommand(mp4boxFilePath, inputOne + inputTwo, addChapterCommand, outputFile);
						} catch (IOException e){
							log.log(Level.SEVERE, "Tried joining two input files and output final output file, but had an IOexception in attempt " + (attempt + 1), e);
							
							break toAttemptIf;
						}
					}
					
					/**
					 * Lets delete the temp video file for the previous iteration, so that we don't end up using twice the storage space!
					 */
					File fileToDelete = new File(currentTempOutputFile);
					if(fileToDelete.delete()){
						log.log(Level.INFO, "The temp file " + fileToDelete.getName() + " is deleted!");
					}else{
						log.log(Level.WARNING, "Unable to delete the temp file " + fileToDelete.getName() + "!");
					}
					
					/**
					 * Now lets assign the new temp video file destination to the "currentTempOutputFile" variable so that the next iteration can use it!
					 */
					currentTempOutputFile = tempOutputFile;
				}
			}
			
			attempt++;
		}
		
		log.log(Level.INFO, "===== CONQUERING FINISHED =====");
	}
	
	/**
	 * Creates a chapter file based on model data and returns the new chapter command for MP4Box
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	private String createChapterFile(){
		String addChapterCommand = "";
		String tempChapterFile = "";
		String duration = "";
		
		String tempDurationFile = "";
		
		try{
			// Skips chapter if there is one file and singleFileSkipChapter is true 
			boolean singleFileSkipChapter = Boolean.valueOf(settings.get(ConfSettingsKeys.SINGLE_FILE_SKIP_CHAPTER));
			if(data.length>1 || !singleFileSkipChapter){
				tempChapterFile = getOutputChapterFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(tempChapterFile));
				
				//Create chapters
				duration = settings.get(ConfSettingsKeys.CHAPTER_FILE_DATA_INITIALTIME);
				for (int i = 0; i < data.length; i++) {
					tempDurationFile = "";
					
					if((Boolean) data[i][1]){
						String timeData = settings.get(ConfSettingsKeys.CHAPTER_FILE_DATA_TIME);
						timeData = timeData.replace(ConfSettingsRegex.CHAPTER_FILE_DATA_TIME_NUMBER, String.valueOf(i));
						timeData = timeData.replace(ConfSettingsRegex.CHAPTER_FILE_DATA_TIME_DURATION, duration);
						
						out.write(timeData);
					    out.newLine();
					    
					    String nameData = settings.get(ConfSettingsKeys.CHAPTER_FILE_DATA_NAME);
					    nameData = nameData.replace(ConfSettingsRegex.CHAPTER_FILE_DATA_NAME_NUMBER, String.valueOf(i));
					    nameData = nameData.replace(ConfSettingsRegex.CHAPTER_FILE_DATA_NAME_NAME, (String) data[i][2]);
					    
					    out.write(nameData);
					    out.newLine();
					}
					
					tempDurationFile = String.valueOf(data[i][0]);
					duration = addTime(duration, getVideoDuration(tempDurationFile));
				}
				out.flush();
				out.close();
				
				addChapterCommand = settings.get(ConfSettingsKeys.MP4BOX_CHAPTER());
				addChapterCommand = addChapterCommand.replace(ConfSettingsRegex.MP4BOX_CHAPTER_FILE, tempChapterFile);
			}else{
				log.log(Level.INFO, "Skipping adding chapter to file. " + ConfSettingsKeys.SINGLE_FILE_SKIP_CHAPTER + " is enabled in " + FileSettings.FILE_NAME_SETTINGS);
			}
		}catch(IOException e){
			String message = "An exception (IO) happened while creating the chapter file " + tempChapterFile;
			JOptionPane.showMessageDialog(ui, message + "\n" + e.getMessage());
			log.log(Level.SEVERE, message, e);
			
			addChapterCommand = null;
		}catch(ParseException e){
			String message = "An exception (Parse) happened while adding time together from the file " + tempDurationFile;
			JOptionPane.showMessageDialog(ui, message + "\n" + e.getMessage());
			log.log(Level.SEVERE, message, e);
			
			addChapterCommand = null;
		}
		
		return addChapterCommand;
	}
	
	/**
	 * Uses data table to create input commands
	 * @return
	 */
	private String createInputCommand(int startRow, int stopRow) throws Exception{
		String addInputCommand = "";
		for(int i=startRow; i<stopRow; i++){
			String tempInput = settings.get(ConfSettingsKeys.MP4BOX_INPUT());
			tempInput = tempInput.replace(ConfSettingsRegex.MP4BOX_INPUT_FILE, (String) data[i][0]);
			
			addInputCommand += tempInput;
		}
		return addInputCommand;
	}
	
	/**
	 * Assembles and executes the command to run the mp4box joining
	 * @param mp4boxFilePath
	 * @param addInputCommand
	 * @param addChapterCommand
	 * @param outputFile
	 * @throws IOException
	 */
	private void executeMP4BoxCommand(String mp4boxFilePath, String addInputCommand, String addChapterCommand, String outputFile) throws IOException{
		String execCommand = settings.get(ConfSettingsKeys.MP4BOX_COMMAND());
		execCommand = execCommand.replace(ConfSettingsRegex.MP4BOX_COMMAND_EXECUTABLE, mp4boxFilePath);
		execCommand = execCommand.replace(ConfSettingsRegex.MP4BOX_COMMAND_INPUT, addInputCommand);
		execCommand = execCommand.replace(ConfSettingsRegex.MP4BOX_COMMAND_CHAPTER, addChapterCommand);
		execCommand = execCommand.replace(ConfSettingsRegex.MP4BOX_COMMAND_OUTPUT_FILE, outputFile);
		
		log.log(Level.INFO, "Here is the command and output of the command");
		log.log(Level.INFO, execCommand);
		
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(execCommand.split(settings.get(ConfSettingsKeys.MP4BOX_CMD_SPLITTER_STRING)));
		
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
			
			throw new IOException(s);
		}
	}
	
	/**
	 * Returns the duration for the video based on it's meta data.
	 * @param path
	 * @return
	 * @throws IOException
	 */
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
	
	/**
	 * Add to time strings together.
	 * Will carry the seconds and minutes forward.
	 * @param time1
	 * @param time2
	 * @return
	 * @throws ParseException
	 */
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
		String filename = getOutputFilename();
		String fileType = settings.get(ConfSettingsKeys.VIDEO_FILE_TYPE);
		
		return findValidOutputFile(folderpath, filename, fileType);
	}
	
	/**
	 * Gets the output chapter file destination.
	 * Makes sure it's a filename it can use (file doesn't exist already)
	 * @return
	 */
	private String getOutputChapterFile(){
		String folderpath = ui.getFolderPathOutput();
		String filename = getOutputFilename();
		String filetype = settings.get(ConfSettingsKeys.CHAPTER_FILETYPE);
		
		return findValidOutputFile(folderpath, filename, filetype);
	}
	
	/**
	 * Returns the filename from the output text field.
	 * Unless there is one video to join and the use filename option is enabled!
	 * @return
	 */
	private String getOutputFilename(){
		String filename = ui.getFilenameOutput().replace(settings.get(ConfSettingsKeys.VIDEO_FILE_TYPE), "");
		
		// If there is only one file in the list, then lets just use that files name
		boolean keepName = Boolean.valueOf(settings.get(ConfSettingsKeys.SINGLE_FILE_KEEP_NAME));
		if(data.length==1 && keepName){
			filename = ui.getFilenameOutput((String) data[0][0]).replace(settings.get(ConfSettingsKeys.VIDEO_FILE_TYPE), "");
			
			log.log(Level.INFO, "Using the single video's filename as the output filename (" + filename + "). " + ConfSettingsKeys.SINGLE_FILE_KEEP_NAME + " is enabled in " + FileSettings.FILE_NAME_SETTINGS);
		}
		
		return filename;
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
		if(!settings.get(ConfSettingsKeys.MP4BOX_PATH()).isEmpty()){
			mp4boxPath = settings.get(ConfSettingsKeys.MP4BOX_PATH());
		}
		
		String returnString = settings.get(ConfSettingsKeys.MP4BOX_EXECUTABLE());
		returnString = returnString.replace(ConfSettingsRegex.MP4BOX_EXECUTABLE_PATH, mp4boxPath);
		
		return returnString;
	}
	
	public String getMP4BoxMissingMessage(String mp4boxPath){
		return "Can't find the MP4Box binary! \nExpected to find it here: " + mp4boxPath +"\n"
            + "If you need further information, look in the '" + settings.get(ConfLanguageKeys.TAB_NAME_INFORMATION) + "' tab!\n"
            + "This app is tested with build: 'Nightly DEV build of v0.5.1' \n";
	}
	
}
