package com.mp4box.gui.model;

import java.io.File;

import com.mp4box.gui.controller.FileSettings;

public class ConfSettingsValues {
	public static String CHECKBOX_AUTOCLEAR_SELECTED = "true";
	public static String CHECKBOX_AUTOJOIN_SELECTED = "false";
	
	public static String CHECKBOX_SEPARATE_VIDEOS = "false";
	
	public static String OUTPUT_FOLDER = "";
	
	public static String CHAPTER_ENABLED 				= "true";
	public static String CHAPTER_FILETYPE 				= ".txt";
	public static String CHAPTER_FILE_DATA_TIME 		= "CHAPTER" + ConfSettingsRegex.CHAPTER_FILE_DATA_TIME_NUMBER + "=" + ConfSettingsRegex.CHAPTER_FILE_DATA_TIME_DURATION + " " + FileSettings.NEW_LINE_TEXT_CONF;
	public static String CHAPTER_FILE_DATA_NAME 		= "CHAPTER" + ConfSettingsRegex.CHAPTER_FILE_DATA_NAME_NUMBER + "NAME=" + ConfSettingsRegex.CHAPTER_FILE_DATA_NAME_NAME + " " + FileSettings.NEW_LINE_TEXT_CONF;
	public static String CHAPTER_FILE_DATA_INITIALTIME 	= "00:00:00.000";
	public static String CHAPTER_KEEP_FILE 				= "false";
	
	public static String VIDEO_FILE_NAME = "output";
	public static String VIDEO_FILE_TYPE = ".mp4";
	
	public static String CMD_SPLITTER_STRING = ";";
	
	public static String MP4BOX_CMD_TOO_LONG 	= "The command line is too long.";
	public static String MP4BOX_CMD_CHAPTER 	= CMD_SPLITTER_STRING + "-chap" + CMD_SPLITTER_STRING + "\"" + ConfSettingsRegex.MP4BOX_CHAPTER_FILE + "\"";
	public static String MP4BOX_CMD_INPUT 		= CMD_SPLITTER_STRING + "-cat" + CMD_SPLITTER_STRING + "\"" + ConfSettingsRegex.MP4BOX_INPUT_FILE + "\"";
	
	public static String MP4BOX_WIN_PATH 		= "%ProgramFiles%" + File.separator  + "GPAC";
	public static String MP4BOX_WIN_EXECUTABLE 	= ConfSettingsRegex.MP4BOX_EXECUTABLE_PATH + File.separator + "mp4box.exe";
	public static String MP4BOX_WIN_COMMAND 	= "cmd" + CMD_SPLITTER_STRING + "/c" + CMD_SPLITTER_STRING + "start" + CMD_SPLITTER_STRING + "\"\"" + CMD_SPLITTER_STRING + "\"" + ConfSettingsRegex.MP4BOX_COMMAND_EXECUTABLE + "\"" + ConfSettingsRegex.MP4BOX_COMMAND_INPUT + ConfSettingsRegex.MP4BOX_COMMAND_CHAPTER + CMD_SPLITTER_STRING + "-new" + CMD_SPLITTER_STRING + "\"" + ConfSettingsRegex.MP4BOX_COMMAND_OUTPUT_FILE + "\"";
	
	public static String MP4BOX_LINUX_PATH 		= "";
	public static String MP4BOX_LINUX_EXECUTABLE= ConfSettingsRegex.MP4BOX_EXECUTABLE_PATH + File.separator + "mp4box";
	public static String MP4BOX_LINUX_COMMAND 	= ConfSettingsRegex.MP4BOX_COMMAND_EXECUTABLE + ConfSettingsRegex.MP4BOX_COMMAND_INPUT + ConfSettingsRegex.MP4BOX_COMMAND_CHAPTER + CMD_SPLITTER_STRING + "-new" + CMD_SPLITTER_STRING + ConfSettingsRegex.MP4BOX_COMMAND_OUTPUT_FILE;
	
	public static String MP4BOX_MAC_PATH 		= "";
	public static String MP4BOX_MAC_EXECUTABLE 	= ConfSettingsRegex.MP4BOX_EXECUTABLE_PATH + File.separator + "mp4box";
	public static String MP4BOX_MAC_COMMAND 	= ConfSettingsRegex.MP4BOX_COMMAND_EXECUTABLE + ConfSettingsRegex.MP4BOX_COMMAND_INPUT + ConfSettingsRegex.MP4BOX_COMMAND_CHAPTER + CMD_SPLITTER_STRING + "-new" + CMD_SPLITTER_STRING + ConfSettingsRegex.MP4BOX_COMMAND_OUTPUT_FILE;
	
	public static String MP4BOX_OTHER_PATH 		= "";
	public static String MP4BOX_OTHER_EXECUTABLE= ConfSettingsRegex.MP4BOX_EXECUTABLE_PATH + File.separator + "mp4box";
	public static String MP4BOX_OTHER_COMMAND 	= ConfSettingsRegex.MP4BOX_COMMAND_EXECUTABLE + ConfSettingsRegex.MP4BOX_COMMAND_INPUT + ConfSettingsRegex.MP4BOX_COMMAND_CHAPTER + CMD_SPLITTER_STRING + "-new" + CMD_SPLITTER_STRING + ConfSettingsRegex.MP4BOX_COMMAND_OUTPUT_FILE;
	
	public static String HANDBRAKE_ENABLED			= "false";
	public static String HANDBRAKE_SETTINGS 		= "-i" + CMD_SPLITTER_STRING + ConfSettingsRegex.HANDBRAKE_COMMAND_INPUT + CMD_SPLITTER_STRING + "-o" + CMD_SPLITTER_STRING + ConfSettingsRegex.HANDBRAKE_COMMAND_OUTPUT;
	public static String RADIO_BUTTON_VIDEO_CONVERSION_DEFAULT_SELECTION 	= "1";
	public static String RADIO_BUTTON_VIDEO_CONVERSION_DESTINATION_FOLDER 	= "";
	
	public static String HANDBRAKE_WIN_PATH 		= "%ProgramFiles%" + File.separator  + "Handbrake";
	public static String HANDBRAKE_WIN_EXECUTABLE 	= ConfSettingsRegex.HANDBRAKE_EXECUTABLE_PATH + File.separator + "HandBrakeCLI.exe";
	public static String HANDBRAKE_WIN_COMMAND		= "cmd" + CMD_SPLITTER_STRING + "/c" + CMD_SPLITTER_STRING + "start" + CMD_SPLITTER_STRING + "\"\"" + CMD_SPLITTER_STRING + ConfSettingsRegex.HANDBRAKE_EXECUTABLE + CMD_SPLITTER_STRING + ConfSettingsRegex.HANDBRAKE_SETTINGS;
	
	public static String HANDBRAKE_LINUX_PATH 		= "";
	public static String HANDBRAKE_LINUX_EXECUTABLE = ConfSettingsRegex.HANDBRAKE_EXECUTABLE_PATH + File.separator + "HandBrakeCLI";
	public static String HANDBRAKE_LINUX_COMMAND	= ConfSettingsRegex.HANDBRAKE_EXECUTABLE + CMD_SPLITTER_STRING + ConfSettingsRegex.HANDBRAKE_SETTINGS;
	
	public static String HANDBRAKE_MAC_PATH 		= "";
	public static String HANDBRAKE_MAC_EXECUTABLE 	= ConfSettingsRegex.HANDBRAKE_EXECUTABLE_PATH + File.separator + "HandBrakeCLI";
	public static String HANDBRAKE_MAC_COMMAND		= ConfSettingsRegex.HANDBRAKE_EXECUTABLE + CMD_SPLITTER_STRING + ConfSettingsRegex.HANDBRAKE_SETTINGS;
	
	public static String HANDBRAKE_OTHER_PATH 		= "";
	public static String HANDBRAKE_OTHER_EXECUTABLE = ConfSettingsRegex.HANDBRAKE_EXECUTABLE_PATH + File.separator + "HandBrakeCLI";
	public static String HANDBRAKE_OTHER_COMMAND	= ConfSettingsRegex.HANDBRAKE_EXECUTABLE + CMD_SPLITTER_STRING + ConfSettingsRegex.HANDBRAKE_SETTINGS;
	
	public static String LIST_BACKGROUND_COLOUR = "white";
	
	public static String RADIO_BUTTON_OUTPUT_FOLDER_DEFAULT_SELECTION = "1";
	public static String RADIO_BUTTON_OUTPUT_FILE_DEFAULT_SELECTION = "1";
	
	public static String SINGLE_FILE_KEEP_NAME = "true";
	public static String SINGLE_FILE_SKIP_CHAPTER = "true";
	
	public static String LOG_NAME = "application.log";
	public static String LOG_SIZE = "100000000";
	public static String LOG_NUMBER_OF_FILES = "10";
	public static String LOG_WRITE_TO_FILE = "true";
	
	public static String DIVIDE_AND_CONQUERE_SUB_LIST_SIZES = "2,4,6";
	
}
