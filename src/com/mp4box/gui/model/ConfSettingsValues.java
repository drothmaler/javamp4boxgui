package com.mp4box.gui.model;

import java.io.File;

import com.mp4box.gui.controller.FileSettings;

public class ConfSettingsValues {
	public static String CHECKBOX_AUTOCLEAR_SELECTED = "true";
	public static String CHECKBOX_AUTOJOIN_SELECTED = "false";
	
	public static String CHECKBOX_SEPARATE_VIDEOS = "false";
	
	public static String OUTPUT_FOLDER = "";
	
	public static String CHAPTER_ENABLED = "true";
	public static String CHAPTER_FILETYPE = ".txt";
	public static String CHAPTER_FILE_DATA_TIME = "CHAPTER" + ConfSettingsRegex.CHAPTER_FILE_DATA_TIME_NUMBER + "=" + ConfSettingsRegex.CHAPTER_FILE_DATA_TIME_DURATION + " " + FileSettings.NEW_LINE_TEXT_CONF;
	public static String CHAPTER_FILE_DATA_NAME = "CHAPTER" + ConfSettingsRegex.CHAPTER_FILE_DATA_NAME_NUMBER + "NAME=" + ConfSettingsRegex.CHAPTER_FILE_DATA_NAME_NAME + " " + FileSettings.NEW_LINE_TEXT_CONF;
	public static String CHAPTER_FILE_DATA_INITIALTIME = "00:00:00.000";
	
	public static String VIDEO_FILE_NAME = "output";
	public static String VIDEO_FILE_TYPE = ".mp4";
	
	public static String MP4BOX_WIN_PATH = "";
	public static String MP4BOX_WIN_EXECUTABLE = ConfSettingsRegex.MP4BOX_EXECUTABLE_PATH + "MP4Box" + File.separator + "MP4Box.exe";
	public static String MP4BOX_WIN_CHAPTER = "-chap \"" + ConfSettingsRegex.MP4BOX_CHAPTER_FILE + "\"";
	public static String MP4BOX_WIN_INPUT = " -cat \"" + ConfSettingsRegex.MP4BOX_INPUT_FILE + "\"";
	public static String MP4BOX_WIN_COMMAND = "cmd /c start \"\" \"" + ConfSettingsRegex.MP4BOX_COMMAND_EXECUTABLE + "\"" + ConfSettingsRegex.MP4BOX_COMMAND_INPUT + " " + ConfSettingsRegex.MP4BOX_COMMAND_CHAPTER + " -new \"" + ConfSettingsRegex.MP4BOX_COMMAND_OUTPUT_FILE + "\"";
	
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
