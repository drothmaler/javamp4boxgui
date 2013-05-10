package com.mp4box.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

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
		if((e.getSource() instanceof JButton && ((JButton) e.getSource()).equals(ui.getButtonJoin()))){
			MP4BoxController controller = new MP4BoxController(ui);
			controller.joinVideos();
		}else if(e.getSource() instanceof JRadioButton){
			JRadioButton radioButton = (JRadioButton) e.getSource();
			if(radioButton.equals(ui.getRadioButtonOutputFolderDefault())){
				//We select the default folder (settings or self determined)
				String folderDefault = ui.getFileSettings().getCurrentOutputPath();
				
				//We select the filename already defined in the output textbox
				String filename = splitOutputFilePath(ui.getTextFieldOutput().getText())[1];
				
				//Now we combine the new folder with the existing filename
				ui.getTextFieldOutput().setText(folderDefault + filename);
			}else if(radioButton.equals(ui.getRadioButtonOutputFolderVideoSource())){
				actionRadioButtonOutputFolderVideoSource();
			}else if(radioButton.equals(ui.getRadioButtonOutputFileDefault())){
				//We select the default folder (settings or self determined)
				String fileDefault = ui.getSettings().get(ConfSettingsKeys.VIDEO_FILE_NAME) + ui.getSettings().get(ConfSettingsKeys.VIDEO_FILE_TYPE);
				
				//We select the filename already defined in the output textbox
				String foldername = splitOutputFilePath(ui.getTextFieldOutput().getText())[0];
				
				//Now we combine the new folder with the existing filename
				ui.getTextFieldOutput().setText(foldername + fileDefault);
			}else if(radioButton.equals(ui.getRadioButtonOutputFileVideoSource())){
				actionRadioButtonOutputFileVideoSource();
			}else if(radioButton.equals(ui.getRadioButtonOutputFileVideoSourceFolder())){
				actionRadioButtonOutputFileVideoSourceFolder();
			}
		}else if(e.getSource() instanceof JButton && ((JButton) e.getSource()).equals(ui.getButtonAbout())){
			JOptionPane.showMessageDialog(ui, "Created by Rune André Liland, and tested on the following version of MP4Box: 'GPAC.Framework.Setup-0.5.1-DEV-rev4452'!");
		}else if((e.getSource() instanceof JCheckBox && ((JCheckBox) e.getSource()).equals(ui.getCheckBoxSeparateVideos()))){
			if(ui.getCheckBoxSeparateVideos().isSelected()){
				/**
				 * When the separate videos based on folders option is enabled, other options are set as a result.
				 */
				
				//Enable these two options that are required
				ui.getCheckBoxAutoJoin().setSelected(true);
				ui.getCheckBoxAutoClear().setSelected(true);
				
				//Disable them to make sure they are kept as required
				ui.getCheckBoxAutoJoin().setEnabled(false);
				ui.getCheckBoxAutoClear().setEnabled(false);
			}else if(!ui.getCheckBoxSeparateVideos().isSelected()){
				//Reset options to default
				ui.getCheckBoxAutoJoin().setSelected(Boolean.valueOf(ui.getSettings().get(ConfSettingsKeys.CHECKBOX_AUTOJOIN_SELECTED)));
				ui.getCheckBoxAutoClear().setSelected(Boolean.valueOf(ui.getSettings().get(ConfSettingsKeys.CHECKBOX_AUTOCLEAR_SELECTED)));
				
				//Enable them so the user can do whatever
				ui.getCheckBoxAutoJoin().setEnabled(true);
				ui.getCheckBoxAutoClear().setEnabled(true);
			}
		}
	}

	public void actionRadioButtonOutputFolderVideoSource(){
		Object[][] data = ui.getVideoTableModel().getData();
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
		String filename = splitOutputFilePath(ui.getTextFieldOutput().getText())[1];
		
		//Now we combine the new folder with the existing filename
		ui.getTextFieldOutput().setText(folderSourceVideo + filename);
	}
	
	public void actionRadioButtonOutputFileVideoSource(){
		Object[][] data = ui.getVideoTableModel().getData();
		String firstVideo = "";
		
		//Quick null check
		if(data==null || data.length==0){
			return;
		}else{
			firstVideo = (String) data[0][0];
		}
		
		//We select the folder where the source video resides
		String fileSourceVideo = splitOutputFilePath(firstVideo)[1];
		
		//We select the foldername already defined in the output textbox
		String foldername = splitOutputFilePath(ui.getTextFieldOutput().getText())[0];
		
		//Now we combine the new folder with the existing filename
		ui.getTextFieldOutput().setText(foldername + fileSourceVideo);
	}
	
	public void actionRadioButtonOutputFileVideoSourceFolder(){
		Object[][] data = ui.getVideoTableModel().getData();
		String firstVideo = "";
		
		//Quick null check
		if(data==null || data.length==0){
			return;
		}else{
			firstVideo = (String) data[0][0];
		}
		
		//We select the parent folder of the first video
		File firstVideoFile = new File(firstVideo);
		String fileSourceVideoParentFolder = firstVideoFile.getParentFile().getName().toString() + ui.getSettings().get(ConfSettingsKeys.VIDEO_FILE_TYPE);
		
		//We select the foldername already defined in the output textbox
		String foldername = splitOutputFilePath(ui.getTextFieldOutput().getText())[0];
		
		//Now we combine the new folder with the existing filename
		ui.getTextFieldOutput().setText(foldername + fileSourceVideoParentFolder);
	}
	
	private String[] splitOutputFilePath(String path){
		int folderFileSplitIndex = path.lastIndexOf(File.separator) + 1;
		String folder = path.substring(0, folderFileSplitIndex);
		String file = path.substring(folderFileSplitIndex, path.length());
		
		String[] returnString = {folder, file};
		return returnString;
	}
	
}
