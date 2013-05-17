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
	
	public static String MP4BOX_CMD_SPLITTER_STRING = "MP4BoxCmdSplitterString";
	
	public static String MP4BOX_WIN_PATH 		= "MP4BoxWinPath";
	public static String MP4BOX_WIN_EXECUTABLE 	= "MP4BoxWinExec";
	public static String MP4BOX_WIN_CHAPTER 	= "MP4BoxWinChapter";
	public static String MP4BOX_WIN_INPUT 		= "MP4BoxWinInput";
	public static String MP4BOX_WIN_COMMAND 	= "MP4BoxWinCommand";
	
	public static String MP4BOX_LINUX_PATH 		= "MP4BoxLinuxPath";
	public static String MP4BOX_LINUX_EXECUTABLE= "MP4BoxLinuxExec";
	public static String MP4BOX_LINUX_CHAPTER 	= "MP4BoxLinuxChapter";
	public static String MP4BOX_LINUX_INPUT 	= "MP4BoxLinuxInput";
	public static String MP4BOX_LINUX_COMMAND 	= "MP4BoxLinuxCommand";
	
	public static String MP4BOX_MAC_PATH 		= "MP4BoxMacPath";
	public static String MP4BOX_MAC_EXECUTABLE 	= "MP4BoxMacExec";
	public static String MP4BOX_MAC_CHAPTER 	= "MP4BoxMacChapter";
	public static String MP4BOX_MAC_INPUT 		= "MP4BoxMacInput";
	public static String MP4BOX_MAC_COMMAND 	= "MP4BoxMacCommand";
	
	public static String MP4BOX_OTHER_PATH 		= "MP4BoxOtherPath";
	public static String MP4BOX_OTHER_EXECUTABLE= "MP4BoxOtherExec";
	public static String MP4BOX_OTHER_CHAPTER 	= "MP4BoxOtherChapter";
	public static String MP4BOX_OTHER_INPUT 	= "MP4BoxOtherInput";
	public static String MP4BOX_OTHER_COMMAND 	= "MP4BoxOtherCommand";
	
	public static String LIST_BACKGROUND_COLOUR = "ListBackground";
	
	public static String RADIO_BUTTON_OUTPUT_FOLDER_DEFAULT_SELECTION = "OutputFolderDefaultSelection";
	public static String RADIO_BUTTON_OUTPUT_FILE_DEFAULT_SELECTION = "OutputFileDefaultSelection";
	
	public static String SINGLE_FILE_KEEP_NAME = "SingleFileKeepName";
	public static String SINGLE_FILE_SKIP_CHAPTER = "SingleFileSkipChapter";
	
	public static String LOG_NAME = "LogName";
	public static String LOG_SIZE = "LogSize";
	public static String LOG_NUMBER_OF_FILES = "LogNumberOfFiles";
	public static String LOG_WRITE_TO_FILE = "LogWriteToFile";
	
	public static String DIVIDE_AND_CONQUERE_SUB_LIST_SIZES = "DivideAndConquereSubListSizes";
	
	/**
	 * This code is for retrieving the correct settings based on operating system used!
	 */
	private static String OS = System.getProperty("os.name");
	
	public static boolean isWindows(){
		return (getOSLowerCase().indexOf("win")>=0);
	}
	
	public static boolean isLinux(){
		return (getOSLowerCase().indexOf("linux")>=0);
	}
	
	public static boolean isMac(){
		return (getOSLowerCase().indexOf("mac")>=0);
	}
	
	public static String getOSLowerCase() {
		return OS.toLowerCase();
	}
	
	public static String getOS() {
		return OS;
	}
	
	public static String MP4BOX_PATH(){
		if(isWindows()){
			return MP4BOX_WIN_PATH;
		}else if(isLinux()){
			return MP4BOX_LINUX_PATH;
		}else if(isMac()){
			return MP4BOX_MAC_PATH;
		}else{
			return MP4BOX_OTHER_PATH;
		}
	}
	
	public static String MP4BOX_EXECUTABLE(){
		if(isWindows()){
			return MP4BOX_WIN_EXECUTABLE;
		}else if(isLinux()){
			return MP4BOX_LINUX_EXECUTABLE;
		}else if(isMac()){
			return MP4BOX_MAC_EXECUTABLE;
		}else{
			return MP4BOX_OTHER_EXECUTABLE;
		}
	}
	
	public static String MP4BOX_CHAPTER(){
		if(isWindows()){
			return MP4BOX_WIN_CHAPTER;
		}else if(isLinux()){
			return MP4BOX_LINUX_CHAPTER;
		}else if(isMac()){
			return MP4BOX_MAC_CHAPTER;
		}else{
			return MP4BOX_OTHER_CHAPTER;
		}
	}
	
	public static String MP4BOX_INPUT(){
		if(isWindows()){
			return MP4BOX_WIN_INPUT;
		}else if(isLinux()){
			return MP4BOX_LINUX_INPUT;
		}else if(isMac()){
			return MP4BOX_MAC_INPUT;
		}else{
			return MP4BOX_OTHER_INPUT;
		}
	}
	
	public static String MP4BOX_COMMAND(){
		if(isWindows()){
			return MP4BOX_WIN_COMMAND;
		}else if(isLinux()){
			return MP4BOX_LINUX_COMMAND;
		}else if(isMac()){
			return MP4BOX_MAC_COMMAND;
		}else{
			return MP4BOX_OTHER_COMMAND;
		}
	}
}
