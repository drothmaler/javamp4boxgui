package com.mp4box.gui.model;



public class ConfSettingsKeys {
	public static String CHECKBOX_AUTOCLEAR_SELECTED = "CheckBoxAutoClear";
	public static String CHECKBOX_AUTOJOIN_SELECTED = "CheckBoxAutoJoin";
	
	public static String CHECKBOX_SEPARATE_VIDEOS = "CheckBoxSeperateVideos";
	
	public static String OUTPUT_FOLDER = "OutputFolder";
	
	public static String CHAPTER_ENABLED = "ChapterEnabled";
	public static String CHAPTER_FILETYPE = "ChapterFiletype";
	public static String CHAPTER_FILE_DATA_TIME = "ChapterFileDataTime";
	public static String CHAPTER_FILE_DATA_NAME = "ChapterFileDataName";
	public static String CHAPTER_FILE_DATA_INITIALTIME = "ChapterFileDataInitialtime";
	
	public static String VIDEO_FILE_NAME = "VideoFileName";
	public static String VIDEO_FILE_TYPE = "VideoFileType";
	
	public static String MP4BOX_WIN_PATH = "MP4BoxWinPath";
	public static String MP4BOX_WIN_EXECUTABLE = "MP4BoxWinExec";
	public static String MP4BOX_WIN_CHAPTER = "MP4BoxWinChapter";
	public static String MP4BOX_WIN_INPUT = "MP4BoxWinInput";
	public static String MP4BOX_WIN_COMMAND = "MP4BoxWinCommand";
	
	public static String LIST_BACKGROUND_COLOUR = "ListBackground";
	
	public static String RADIO_BUTTON_OUTPUT_FOLDER_DEFAULT_SELECTION = "OutputFolderDefaultSelection";
	public static String RADIO_BUTTON_OUTPUT_FILE_DEFAULT_SELECTION = "OutputFileDefaultSelection";
	
	public static String SINGLE_FILE_KEEP_NAME = "SingleFileKeepName";
	public static String SINGLE_FILE_SKIP_CHAPTER = "SingleFileSkipChapter";
	
	public static String LOG_NAME = "LogName";
	public static String LOG_SIZE = "LogSize";
	public static String LOG_NUMBER_OF_FILES = "LogNumberOfFiles";
	public static String LOG_WRITE_TO_FILE = "LogWriteToFile";
	
	/**
	 * This code is for retrieving the correct settings based on operating system used!
	 */
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public static boolean isWindows(){
		return (OS.indexOf("win")>=0);
	}
	
	public static String MP4BOX_PATH(){
		if(isWindows()){
			return MP4BOX_WIN_PATH;
		}
		
		return "";
	}
	
	public static String MP4BOX_EXECUTABLE(){
		if(isWindows()){
			return MP4BOX_WIN_EXECUTABLE;
		}
		
		return "";
	}
	
	public static String MP4BOX_CHAPTER(){
		if(isWindows()){
			return MP4BOX_WIN_CHAPTER;
		}
		
		return "";
	}
	
	public static String MP4BOX_INPUT(){
		if(isWindows()){
			return MP4BOX_WIN_INPUT;
		}
		
		return "";
	}
	
	public static String MP4BOX_COMMAND(){
		if(isWindows()){
			return MP4BOX_WIN_COMMAND;
		}
		
		return "";
	}
}
