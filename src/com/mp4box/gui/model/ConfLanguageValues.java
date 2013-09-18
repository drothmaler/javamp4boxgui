package com.mp4box.gui.model;

import com.mp4box.gui.controller.FileSettings;

public class ConfLanguageValues {
	
	public static String TAB_NAME_OUTPUT = "Output";
	public static String TAB_NAME_AUTOMATION = "Automation";
	public static String TAB_NAME_FOLDER_RECURSION = "Folder Recursion";
	public static String TAB_NAME_VIDEO_CONVERSION = "Video Conversion";
	public static String TAB_NAME_INFORMATION = "Information";
	
	public static String CHECKBOX_AUTOCLEAR_TEXT = "Auto clear table after join?";
	public static String CHECKBOX_AUTOJOIN_TEXT = "Auto join when added?";
	
	public static String LABEL_SEPARATE_VIDEOS = "When adding folder tree, combine videos in each folder seperatly! " + FileSettings.NEW_LINE_HTML_CONF + "Will enable auto join and is intended for use with many folders in a batch mode. " + FileSettings.NEW_LINE_HTML_CONF + "Will not display the folder groups in the list and then let you join them.";
	public static String CHECKBOX_SEPARATE_VIDEOS_TEXT = "Separate videos for each folder?";
	
	public static String CHAPTER_NAME_DEFAULT = "Chapter ";
	public static String BUTTON_JOIN_VIDEOS_TEXT = "Join videos";
	
	public static String LABEL_OUTPUT_FOLDER = "Folder:";
	public static String RADIO_BUTTON_OUTPUT_FOLDER_TEXT_DEFAULT = "Default";
	public static String RADIO_BUTTON_OUTPUT_FOLDER_TEXT_VIDEOSOURCE = "Video source";
	
	public static String LABEL_OUTPUT_FILE = "File:";
	public static String RADIO_BUTTON_OUTPUT_FILE_TEXT_DEFAULT = "Default";
	public static String RADIO_BUTTON_OUTPUT_FILE_TEXT_VIDEOSOURCE = "Video source";
	public static String RADIO_BUTTON_OUTPUT_FILE_TEXT_VIDEOSOURCEFOLDER = "Video Source Folder";
	
	public static String LABEL_CHAPTER_NAME = "Chapter name:";
	public static String RADIO_BUTTON_CHAPTER_NAME_DEFAULT = "Default";
	public static String RADIO_BUTTON_CHAPTER_NAME_VIDEOSOURCE = "Video source";
	
	public static String LABEL_VIDEO_CONVERSION = "Output location:";
	public static String RADIO_BUTTON_VIDEO_CONVERSION_DEFAULT = "Default";
	public static String RADIO_BUTTON_VIDEO_CONVERSION_VIDEOSOURCEFOLDER = "Video source folder";
	public static String LABEL_HANDBRAKE_SETTINGS = "Handbrake settings:";
	public static String LABEL_HANDBRAKE_ENABLED = "Handbrake enabled:";
	
	public static String EDITOR_PANE_INFORMATION = FileSettings.HTML_TAG 
											 	 + "<b>Installation (Wiki):</b> <a href='http://sourceforge.net/p/javamp4boxgui/wiki/Installation/'>http://sourceforge.net/p/javamp4boxgui/wiki/Installation/</a>" + FileSettings.NEW_LINE_HTML_CONF
											 	 + "<b>MP4Box Nightly Builds:</b> <a href='http://gpac.wp.mines-telecom.fr/downloads/gpac-nightly-builds/'>http://gpac.wp.mines-telecom.fr/downloads/gpac-nightly-builds/</a>" + FileSettings.NEW_LINE_HTML_CONF
											 	 + "<b>Handbrake:</b> <a href='http://handbrake.fr/downloads.php'>http://handbrake.fr/downloads.php</a>" + FileSettings.NEW_LINE_HTML_CONF
											 	 + "Download the proper version for your OS, and follow the Wiki instructions!" + FileSettings.NEW_LINE_HTML_CONF
											 	 + "<i>Please note that using other OS's than Windows requires changes to settings.conf (see Wiki).</i>" + FileSettings.NEW_LINE_HTML_CONF;
	
}
