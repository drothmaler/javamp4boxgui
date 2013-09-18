package com.mp4box.gui.model;

import com.mp4box.gui.controller.OSMethods;

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
	public static String CHAPTER_KEEP_FILE = "ChapterKeepFile";
	
	public static String VIDEO_FILE_NAME = "VideoFileName";
	public static String VIDEO_FILE_TYPE = "VideoFileType";
	
	public static String MP4BOX_CMD_TOO_LONG 		= "MP4BoxCmdTooLong";
	public static String MP4BOX_CMD_SPLITTER_STRING = "MP4BoxCmdSplitterString";
	public static String MP4BOX_CMD_CHAPTER 		= "MP4BoxCmdChapter";
	public static String MP4BOX_CMD_INPUT 			= "MP4BoxCmdInput";
	
	public static String MP4BOX_WIN_PATH 		= "MP4BoxWinPath";
	public static String MP4BOX_WIN_EXECUTABLE 	= "MP4BoxWinExec";
	public static String MP4BOX_WIN_COMMAND 	= "MP4BoxWinCommand";
	
	public static String MP4BOX_LINUX_PATH 		= "MP4BoxLinuxPath";
	public static String MP4BOX_LINUX_EXECUTABLE= "MP4BoxLinuxExec";
	public static String MP4BOX_LINUX_COMMAND 	= "MP4BoxLinuxCommand";
	
	public static String MP4BOX_MAC_PATH 		= "MP4BoxMacPath";
	public static String MP4BOX_MAC_EXECUTABLE 	= "MP4BoxMacExec";
	public static String MP4BOX_MAC_COMMAND 	= "MP4BoxMacCommand";
	
	public static String MP4BOX_OTHER_PATH 		= "MP4BoxOtherPath";
	public static String MP4BOX_OTHER_EXECUTABLE= "MP4BoxOtherExec";
	public static String MP4BOX_OTHER_COMMAND 	= "MP4BoxOtherCommand";
	
	public static String HANDBRAKE_SETTINGS 	= "HandbrakeSettings";
	public static String RADIO_BUTTON_VIDEO_CONVERSION_DEFAULT_SELECTION = "VideoConversionDefaultSelection";
	public static String RADIO_BUTTON_VIDEO_CONVERSION_DESTINATION_FOLDER = "VideoConversionDestinationFolder";
	
	public static String HANDBRAKE_WIN_PATH 		= "HandbrakeWinPath";
	public static String HANDBRAKE_WIN_EXECUTABLE 	= "HandbrakeWinExec";
	public static String HANDBRAKE_WIN_COMMAND		= "HandbrakeWinCmd";
	
	public static String HANDBRAKE_LINUX_PATH 		= "HandbrakeLinuxPath";
	public static String HANDBRAKE_LINUX_EXECUTABLE = "HandbrakeLinuxExec";
	public static String HANDBRAKE_LINUX_COMMAND	= "HandbrakeLinuxCmd";
	
	public static String HANDBRAKE_MAC_PATH 		= "HandbrakeMacPath";
	public static String HANDBRAKE_MAC_EXECUTABLE 	= "HandbrakeMacExec";
	public static String HANDBRAKE_MAC_COMMAND		= "HandbrakeMacCmd";
	
	public static String HANDBRAKE_OTHER_PATH 		= "HandbrakeOtherPath";
	public static String HANDBRAKE_OTHER_EXECUTABLE = "HandbrakeOtherExec";
	public static String HANDBRAKE_OTHER_COMMAND	= "HandbrakeOtherCmd";
	
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
	
	public static String MP4BOX_PATH(){
		if(OSMethods.isWindows()){
			return MP4BOX_WIN_PATH;
		}else if(OSMethods.isLinux()){
			return MP4BOX_LINUX_PATH;
		}else if(OSMethods.isMac()){
			return MP4BOX_MAC_PATH;
		}else{
			return MP4BOX_OTHER_PATH;
		}
	}
	
	public static String MP4BOX_EXECUTABLE(){
		if(OSMethods.isWindows()){
			return MP4BOX_WIN_EXECUTABLE;
		}else if(OSMethods.isLinux()){
			return MP4BOX_LINUX_EXECUTABLE;
		}else if(OSMethods.isMac()){
			return MP4BOX_MAC_EXECUTABLE;
		}else{
			return MP4BOX_OTHER_EXECUTABLE;
		}
	}
	
	public static String MP4BOX_COMMAND(){
		if(OSMethods.isWindows()){
			return MP4BOX_WIN_COMMAND;
		}else if(OSMethods.isLinux()){
			return MP4BOX_LINUX_COMMAND;
		}else if(OSMethods.isMac()){
			return MP4BOX_MAC_COMMAND;
		}else{
			return MP4BOX_OTHER_COMMAND;
		}
	}
	
	public static String HANDBRAKE_PATH(){
		if(OSMethods.isWindows()){
			return HANDBRAKE_WIN_PATH;
		}else if(OSMethods.isLinux()){
			return HANDBRAKE_LINUX_PATH;
		}else if(OSMethods.isMac()){
			return HANDBRAKE_MAC_PATH;
		}else{
			return HANDBRAKE_OTHER_PATH;
		}
	}
	
	public static String HANDBRAKE_EXECUTABLE(){
		if(OSMethods.isWindows()){
			return HANDBRAKE_WIN_EXECUTABLE;
		}else if(OSMethods.isLinux()){
			return HANDBRAKE_LINUX_EXECUTABLE;
		}else if(OSMethods.isMac()){
			return HANDBRAKE_MAC_EXECUTABLE;
		}else{
			return HANDBRAKE_OTHER_EXECUTABLE;
		}
	}
	
	public static String HANDBRAKE_COMMAND(){
		if(OSMethods.isWindows()){
			return HANDBRAKE_WIN_COMMAND;
		}else if(OSMethods.isLinux()){
			return HANDBRAKE_LINUX_COMMAND;
		}else if(OSMethods.isMac()){
			return HANDBRAKE_MAC_COMMAND;
		}else{
			return HANDBRAKE_OTHER_COMMAND;
		}
	}
	
}
