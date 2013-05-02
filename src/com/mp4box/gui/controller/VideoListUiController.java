package com.mp4box.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import com.mp4box.gui.model.ConfLanguageKeys;
import com.mp4box.gui.model.ConfSettingsKeys;
import com.mp4box.gui.ui.VideoListUi;

public class VideoListUiController implements ActionListener {
	
	private VideoListUi ui;
	
	public VideoListUiController(VideoListUi ui) {
		this.ui = ui;
	}
	
	@SuppressWarnings("null")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton && ((JButton) e.getSource()).getText().equals(ui.getSettings().get(ConfLanguageKeys.BUTTON_TEXT))){
			new MP4BoxController(ui);
			
			if(ui.getAutoclearCheckBox().isSelected()){
				ui.getModel().removeAllRows();
			}
		}else if(e.getSource() instanceof JRadioButton){
			JRadioButton radioButton = (JRadioButton) e.getSource();
			if(radioButton.equals(ui.getRadioOutputFolderDefault())){
				//We select the default folder (settings or self determined)
				String folderDefault = ui.getFileSettings().getCurrentOutputPath();
				
				//We select the filename already defined in the output textbox
				String filename = splitOutputFilePath(ui.getOutputTextField().getText())[1];
				
				//Now we combine the new folder with the existing filename
				ui.getOutputTextField().setText(folderDefault + filename);
			}else if(radioButton.equals(ui.getRadioOutputFolderVideoSource())){
				actionRadioButtonOutputFolderVideoSource();
			}else if(radioButton.equals(ui.getRadioOutputFileDefault())){
				//We select the default folder (settings or self determined)
				String fileDefault = ui.getSettings().get(ConfSettingsKeys.OUTPUT_FILE);
				
				//We select the filename already defined in the output textbox
				String foldername = splitOutputFilePath(ui.getOutputTextField().getText())[0];
				
				//Now we combine the new folder with the existing filename
				ui.getOutputTextField().setText(foldername + fileDefault);
			}else if(radioButton.equals(ui.getRadioOutputFileVideoSource())){
				actionRadioButtonOutputFileVideoSource();
			}
		}else if(e.getSource() instanceof JButton){
			JOptionPane.showMessageDialog(ui, "Created by Rune André Liland, and tested on the following version of MP4Box: 'GPAC.Framework.Setup-0.5.1-DEV-rev4452'!");
		}
	}

	public void actionRadioButtonOutputFolderVideoSource(){
		Object[][] data = ui.getModel().getData();
		String firstVideo = "";
		
		//Quick null check
		if(data==null || data.length==0){
			return;
		}else{
			firstVideo = (String) data[0][0];
		}
		
		//We select the folder where the source video resides
		String folderSourceVideo = splitOutputFilePath(firstVideo)[0];
		
		//We select the filename already defined in the output textbox
		String filename = splitOutputFilePath(ui.getOutputTextField().getText())[1];
		
		//Now we combine the new folder with the existing filename
		ui.getOutputTextField().setText(folderSourceVideo + filename);
	}
	
	public void actionRadioButtonOutputFileVideoSource(){
		Object[][] data = ui.getModel().getData();
		String firstVideo = "";
		
		//Quick null check
		if(data==null || data.length==0){
			return;
		}else{
			firstVideo = (String) data[0][0];
		}
		
		//We select the folder where the source video resides
		String fileSourceVideo = splitOutputFilePath(firstVideo)[1];
		
		//We select the filename already defined in the output textbox
		String foldername = splitOutputFilePath(ui.getOutputTextField().getText())[0];
		
		//Now we combine the new folder with the existing filename
		ui.getOutputTextField().setText(foldername + fileSourceVideo);
	}
	
	private String[] splitOutputFilePath(String path){
		int folderFileSplitIndex = path.lastIndexOf(File.separator) + 1;
		String folder = path.substring(0, folderFileSplitIndex);
		String file = path.substring(folderFileSplitIndex, path.length());
		
		String[] returnString = {folder, file};
		return returnString;
	}
	
}
