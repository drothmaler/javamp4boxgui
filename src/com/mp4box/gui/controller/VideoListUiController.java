package com.mp4box.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.mp4box.gui.ui.VideoListUi;

public class VideoListUiController implements ActionListener {
	
	private VideoListUi ui;
	
	public VideoListUiController(VideoListUi ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton && ((JButton) e.getSource()).getText().equals(ui.getSettings().get(ParameterStrings.BUTTON_TEXT))){
			new MP4BoxController(ui);
			
			if(ui.getAutoclearCheckBox().isSelected()){
				ui.getModel().removeAllRows();
			}
		}else if(e.getSource() instanceof JButton){
			JOptionPane.showMessageDialog(ui, "Created by Rune André Liland, and tested on the following version of MP4Box: 'GPAC.Framework.Setup-0.5.1-DEV-rev4452'!");
		}
	}

}
