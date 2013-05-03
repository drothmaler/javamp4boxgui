package com.mp4box.gui.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.mp4box.gui.controller.FileSettings;
import com.mp4box.gui.controller.MP4BoxController;
import com.mp4box.gui.controller.VideoListUiController;
import com.mp4box.gui.model.ConfLanguageKeys;
import com.mp4box.gui.model.ConfSettingsKeys;

/**
 * 
 * @author Rune Andre Liland
 *
 */
public class VideoListUi extends JFrame implements DropTargetListener {

	private static final long serialVersionUID = -8428320171364156148L;
	
	DropTarget dt;
	VideoTableModel model = new VideoTableModel();
	GridBagLayout layoutManager = new GridBagLayout();
	VideoListUiController actionListener = new VideoListUiController(this);
	FileSettings fileSettings = new FileSettings();
	
	JTable videoTable = new JTable();
	JScrollPane videoPane = new JScrollPane();
	
	JTabbedPane optionsPane = new JTabbedPane();
	JPanel outputPanel = new JPanel(new GridBagLayout());
	JPanel automationPanel = new JPanel(new GridBagLayout());
	
	JTextField outputTextField = new JTextField();
	JLabel labelOutputFolder = new JLabel();
	JRadioButton radioOutputFolderDefault = new JRadioButton();
	JRadioButton radioOutputFolderVideoSource = new JRadioButton();
	JLabel labelOutputFile = new JLabel();
	JRadioButton radioOutputFileDefault = new JRadioButton();
	JRadioButton radioOutputFileVideoSource = new JRadioButton();
	
	JCheckBox autoclearCheckBox = new JCheckBox();
	JCheckBox autoJoinCheckBox = new JCheckBox();
	
	JButton joinButton = new JButton();
	JButton aboutButton = new JButton();
	
	HashMap<String, String> settings = null;
	
	public VideoListUi(){
		settings = fileSettings.getSettings();
		
		initComponents();
		addComponents();
		addActionListener();
		init();
	}
	
	private void initComponents(){
		videoTable = new JTable(model);
		videoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		videoTable.setFillsViewportHeight(true);
		
		try{
			Field field = Class.forName("java.awt.Color").getField(settings.get(ConfSettingsKeys.LIST_BACKGROUND_COLOUR));
		    Color backgroundColor = (Color)field.get(null);
			videoTable.setBackground(backgroundColor);
		}catch(Exception e){
			e.printStackTrace();
		}
			
		videoPane.add(videoTable);
		videoPane.setViewportView(videoTable);
		
		autoclearCheckBox.setSelected(Boolean.valueOf(settings.get(ConfSettingsKeys.CHECKBOX_AUTOCLEAR_SELECTED)));
		autoJoinCheckBox.setSelected(Boolean.valueOf(settings.get(ConfSettingsKeys.CHECKBOX_AUTOJOIN_SELECTED)));
		autoJoinCheckBox.setToolTipText("Note! The UI doesn't update the table when auto joining on drop unless autoclear is disabled! Also doesn't overwrite files, will try new names!");
		
		outputTextField.setText(fileSettings.getCurrentOutputPath() + settings.get(ConfSettingsKeys.OUTPUT_FILE));
		labelOutputFolder.setText(settings.get(ConfLanguageKeys.OUTPUT_FOLDER_LABEL));
		radioOutputFolderDefault.setText(settings.get(ConfLanguageKeys.OUTPUT_FOLDER_RADIO_DEFAULT));
		radioOutputFolderVideoSource.setText(settings.get(ConfLanguageKeys.OUTPUT_FOLDER_RADIO_VIDEOSOURCE));
		labelOutputFile.setText(settings.get(ConfLanguageKeys.OUTPUT_FILE_LABEL));
		radioOutputFileDefault.setText(settings.get(ConfLanguageKeys.OUTPUT_FILE_RADIO_DEFAULT));
		radioOutputFileVideoSource.setText(settings.get(ConfLanguageKeys.OUTPUT_FILE_RADIO_VIDEOSOURCE));
		
		autoclearCheckBox.setText(settings.get(ConfLanguageKeys.CHECKBOX_AUTOCLEAR));
		autoJoinCheckBox.setText(settings.get(ConfLanguageKeys.CHECKBOX_AUTOJOIN));
		
		joinButton.setText(settings.get(ConfLanguageKeys.BUTTON_TEXT));
		aboutButton.setText("About");
		
		dt = new DropTarget(videoTable, this);
	}
	
	private GridBagConstraints getComponentConstraints(int fill, double weightx, double weighty, int gridx, int gridy, int gridwidth){
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = fill;
		c.weightx = weightx;
		c.weighty = weighty;
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		
		return c;
	}
	
	private void addComponents(){
		this.getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(videoPane, getComponentConstraints(GridBagConstraints.BOTH, 2, 3, 0, 0, 2));
		
		//Output settings
		ButtonGroup groupOutputFolder = new ButtonGroup();
		groupOutputFolder.add(radioOutputFolderDefault);
		groupOutputFolder.add(radioOutputFolderVideoSource);
		radioOutputFolderDefault.setSelected(true);
		
		ButtonGroup groupOutputFile = new ButtonGroup();
		groupOutputFile.add(radioOutputFileDefault);
		groupOutputFile.add(radioOutputFileVideoSource);
		radioOutputFileDefault.setSelected(true);
		
		outputPanel.add(outputTextField, getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 0, 0, 3));
		outputPanel.add(labelOutputFolder,getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 1, 1));
		outputPanel.add(radioOutputFolderDefault,getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 1, 1, 1));
		outputPanel.add(radioOutputFolderVideoSource,getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 2, 1, 1));
		outputPanel.add(labelOutputFile,getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 2, 1));
		outputPanel.add(radioOutputFileDefault,getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 1, 2, 1));
		outputPanel.add(radioOutputFileVideoSource,getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 2, 2, 1));
		outputPanel.add(new JPanel(), getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 0, 3, 3)); //Filler panel
		
		//Automation settings
		automationPanel.add(autoclearCheckBox, getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 0, 0, 1));
		automationPanel.add(autoJoinCheckBox, getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 0, 1, 1));
		automationPanel.add(new JPanel(), getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 0, 2, 1)); //Filler panel
		
		//Add Tabbed pane and the tabs
		getContentPane().add(optionsPane, getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 1, 2));
		optionsPane.add(settings.get(ConfLanguageKeys.TAB_NAME_OUTPUT), outputPanel);
		optionsPane.add(settings.get(ConfLanguageKeys.TAB_NAME_AUTOMATION), automationPanel);
		
		getContentPane().add(joinButton, getComponentConstraints(GridBagConstraints.CENTER, 1, 0.5, 0, 2, 1));
		getContentPane().add(aboutButton, getComponentConstraints(GridBagConstraints.CENTER, 0, 0, 1, 2, 1));
	}
	
	private void addActionListener(){
		joinButton.addActionListener(actionListener);
		aboutButton.addActionListener(actionListener);
		
		radioOutputFolderDefault.addActionListener(actionListener);
		radioOutputFolderVideoSource.addActionListener(actionListener);
		radioOutputFileDefault.addActionListener(actionListener);
		radioOutputFileVideoSource.addActionListener(actionListener);
	}
	
	private void init(){
		this.setTitle("Java MP4Box Gui v1.2 SNAPSHOT");
		this.setSize(640,480);
		this.setLocationRelativeTo(null); //Centers the window on the screen
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * Source: Drag&Drop: http://www.java-tips.org/java-se-tips/javax.swing/how-to-implement-drag-drop-functionality-in-your-applic.html
	 */
	public void drop(DropTargetDropEvent dtde) {
		try {
			// Ok, get the dropped object and try to figure out what it is
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				//System.out.println("Possible flavor: " + flavors[i].getMimeType());
				// Check for file lists specifically
				if (flavors[i].isFlavorJavaFileListType()) {
					boolean wasDataEmpty = false;
					if(model.getData()==null || model.getData().length==0){
						wasDataEmpty = true;
					}
					
					// Great!  Accept copy drops...
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					// And add the list of file names to our list
					@SuppressWarnings("rawtypes")
					List list = (List)tr.getTransferData(flavors[i]);
					for (int j = 0; j < list.size(); j++) {
						String filePath = list.get(j).toString();
						addFilePathToModel(filePath, j);
					}

					// If we made it this far, everything worked.
					dtde.dropComplete(true);
					model.fireTableStructureChanged();
					videoTable.setModel(model);
					
					/**
					 * Checks if the data was empty and the radio button for video source is selected.
					 */
					if(wasDataEmpty && radioOutputFolderVideoSource.isSelected()){
						actionListener.actionRadioButtonOutputFolderVideoSource();
					}
					
					/**
					 * Checks if the data was empty and the dario button for video source is selected.
					 */
					if(wasDataEmpty && radioOutputFileVideoSource.isSelected()){
						actionListener.actionRadioButtonOutputFileVideoSource();
					}
					
					//Autojoins if that option is selected
					if(autoJoinCheckBox.isSelected()){
						new MP4BoxController(this);
						
						//Auto clears the table is that option is selected
						if(autoclearCheckBox.isSelected()){
							model.removeAllRows();
						}
					}
					
					return;
				}
				// Ok, is it another Java object?
				else if (flavors[i].isFlavorSerializedObjectType()) {
					dtde.dropComplete(true);
					JOptionPane.showMessageDialog(this, "Due to the nature of this application, a Java Object seems to be a somewhat useless input...Sorry!");
					
					return;
				}
				// How about an input stream?
				else if (flavors[i].isRepresentationClassInputStream()) {
					dtde.dropComplete(true);
					JOptionPane.showMessageDialog(this, "If an input stream would work, I'm not aware of it yet....Sorry!");
					
					return;
				}
			}
			// Hmm, the user must not have dropped a file list
			System.out.println("Drop failed: " + dtde);
			JOptionPane.showMessageDialog(this, "Are you sure you dropped a file list?");
			dtde.rejectDrop();
		} catch (Exception e) {
			e.printStackTrace();
			dtde.rejectDrop();
		}
	}

	public int addFilePathToModel(String filePath, int chapterNumber){
		File file = new File(filePath);
		if(file.isDirectory()){
			for(File childFile : file.listFiles()){
				try {
					chapterNumber = addFilePathToModel(childFile.getCanonicalPath().toString(), chapterNumber);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "Unable to properly process a file in " + filePath + "/n Stack trace: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}else{
			chapterNumber = chapterNumber + 1;
			model.addRow(filePath, 
				Boolean.valueOf(settings.get(ConfSettingsKeys.CHAPTER_ENABLED)), 
				settings.get(ConfLanguageKeys.CHAPTER_NAME) + " " + chapterNumber);
		}
		
		return chapterNumber;
	}
	
	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	public HashMap<String, String> getSettings() {
		return settings;
	}
	
	public VideoTableModel getModel() {
		return model;
	}
	
	public JCheckBox getAutoclearCheckBox() {
		return autoclearCheckBox;
	}
	
	public JCheckBox getAutoJoinCheckBox() {
		return autoJoinCheckBox;
	}
	
	public JButton getJoinButton() {
		return joinButton;
	}
	
	public JTextField getOutputTextField() {
		return outputTextField;
	}
	
	public JRadioButton getRadioOutputFolderDefault() {
		return radioOutputFolderDefault;
	}
	
	public JRadioButton getRadioOutputFolderVideoSource() {
		return radioOutputFolderVideoSource;
	}
	
	public JRadioButton getRadioOutputFileDefault() {
		return radioOutputFileDefault;
	}
	
	public JRadioButton getRadioOutputFileVideoSource() {
		return radioOutputFileVideoSource;
	}
	
	public FileSettings getFileSettings() {
		return fileSettings;
	}
	
	/**
	 * This returns the output path in the JTextField which hopefully is correct and so on!
	 * The filename is removed from the string!
	 * @return
	 */
	public String getOutputPath(){
		return outputTextField.getText().substring(0, outputTextField.getText().lastIndexOf(File.separator) + 1);
	}
	
	/**
	 * Returns the filename in the JTextField!
	 * @return
	 */
	public String getOutputFilename(){
		return outputTextField.getText().substring(outputTextField.getText().lastIndexOf(File.separator), outputTextField.getText().length());
	}
	
}
