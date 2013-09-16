package com.mp4box.gui.controller;

public class OSMethods {
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
}
