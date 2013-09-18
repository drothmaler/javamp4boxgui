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
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
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
import javax.swing.UIManager;

import com.mp4box.gui.controller.FileSettings;
import com.mp4box.gui.controller.MP4BoxController;
import com.mp4box.gui.controller.OSMethods;
import com.mp4box.gui.controller.VideoListUiController;
import com.mp4box.gui.model.ConfLanguageKeys;
import com.mp4box.gui.model.ConfSettingsKeys;
import com.mp4box.gui.model.comparators.NaturalOrderComparator;

/**
 * 
 * @author Rune Andre Liland
 *
 */
public class VideoListUi extends JFrame implements DropTargetListener {

	private static final long serialVersionUID = -8428320171364156148L;
	
	private static Logger log = Logger.getLogger("Log");
	
	DropTarget dropTarget;
	VideoTableModel videoTableModel = new VideoTableModel();
	GridBagLayout layoutManager = new GridBagLayout();
	VideoListUiController actionListener = new VideoListUiController(this);
	FileSettings fileSettings = new FileSettings();
	
	JTable tableVideo = new JTable();
	JScrollPane scrollPaneVideo = new JScrollPane();
	
	JTabbedPane tabbedPaneOptions = new JTabbedPane();
	JPanel panelOutput = new JPanel(new GridBagLayout());
	JPanel panelAutomation = new JPanel(new GridBagLayout());
	JPanel panelFolderRecursion = new JPanel(new GridBagLayout());
	JPanel panelVideoConversion = new JPanel(new GridBagLayout());
	JPanel panelInformation = new JPanel(new GridBagLayout());
	
	JTextField textFieldOutput = new JTextField();
	
	JPanel panelOutputFolder = new JPanel(new GridBagLayout());
	JLabel labeloutputFolder = new JLabel();
	JRadioButton radioButtonOutputFolderDefault = new JRadioButton();
	JRadioButton radioButtonOutputFolderVideoSource = new JRadioButton();
	
	JPanel panelOutputFile = new JPanel(new GridBagLayout());
	JLabel labelOutputFile = new JLabel();
	JRadioButton radioButtonOutputFileDefault = new JRadioButton();
	JRadioButton radioButtonOutputFileVideoSource = new JRadioButton();
	JRadioButton radioButtonOutputFileVideoSourceFolder = new JRadioButton();
	
	JPanel panelChapterName = new JPanel(new GridBagLayout());
	JLabel labelChapterName = new JLabel();
	ButtonGroup groupChapterName = new ButtonGroup();
	JRadioButton radioButtonChapterNameDefault = new JRadioButton();
	JRadioButton radioButtonChapterNameVideoSource = new JRadioButton();
	
	JCheckBox checkBoxAutoclear = new JCheckBox();
	JCheckBox checkBoxAutoJoin = new JCheckBox();
	
	JLabel labelSeparateVideos = new JLabel();
	JCheckBox checkBoxSeparateVideos = new JCheckBox();
	
	JPanel panelVideoConversionOutput = new JPanel(new GridBagLayout());
	JLabel labelVideoConversion = new JLabel();
	ButtonGroup groupVideoConversion = new ButtonGroup();
	JRadioButton radioButtonVideoConversionOutputFolderDefault = new JRadioButton();
	JRadioButton radioButtonVideoConversionOutputFolderVideoSource = new JRadioButton();
	JTextField textFieldVideoConversionOutput = new JTextField();
	JLabel labelVideoConversionHandbrakeSettings = new JLabel();
	JTextField textFieldVideoConversionHandbrakeSettings = new JTextField();
	JLabel labelVideoConversionEnabled = new JLabel();
	JCheckBox checkBoxVideoConversionEnabled = new JCheckBox();
	
	JEditorPane editorPaneInformation = new JEditorPane();
	
	JPanel panelButton = new JPanel(new GridBagLayout());
	JButton buttonJoin = new JButton();
	JButton buttonAbout = new JButton();
	
	HashMap<String, String> settings = null;
	
	FileFilter filterDirectory = new FileFilter() {
		@Override
		public boolean accept(File file) {
			return file.isDirectory();
		}
	};
	
	FileFilter filterFile = new FileFilter() {
		@Override
		public boolean accept(File file) {
			return file.isFile();
		}
	};
	
	public VideoListUi(){
		settings = fileSettings.getSettings();
		
		initComponents();
		addComponents();
		addActionListeners();
		init();
		saveLogFile();
		
		log.log(Level.INFO, "The operating system detected is: " + OSMethods.getOS());
	}
	
	private void initComponents(){
		tableVideo = new JTable(videoTableModel);
		tableVideo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableVideo.setFillsViewportHeight(true);
		
		try{
			Field field = Class.forName("java.awt.Color").getField(settings.get(ConfSettingsKeys.LIST_BACKGROUND_COLOUR));
		    Color backgroundColor = (Color)field.get(null);
			tableVideo.setBackground(backgroundColor);
		}catch(Exception e){
			log.log(Level.INFO, e.getMessage(), e.getStackTrace());
		}
			
		scrollPaneVideo.add(tableVideo);
		scrollPaneVideo.setViewportView(tableVideo);
		
		checkBoxAutoclear.setSelected(Boolean.valueOf(settings.get(ConfSettingsKeys.CHECKBOX_AUTOCLEAR_SELECTED)));
		checkBoxAutoJoin.setSelected(Boolean.valueOf(settings.get(ConfSettingsKeys.CHECKBOX_AUTOJOIN_SELECTED)));
		checkBoxAutoJoin.setToolTipText("Note! The UI doesn't update the table when auto joining on drop unless autoclear is disabled! Also doesn't overwrite files, will try new names!");
		
		textFieldOutput.setText(fileSettings.getCurrentOutputPath() + settings.get(ConfSettingsKeys.VIDEO_FILE_NAME) + settings.get(ConfSettingsKeys.VIDEO_FILE_TYPE));
		labeloutputFolder.setText(settings.get(ConfLanguageKeys.LABEL_OUTPUT_FOLDER));
		radioButtonOutputFolderDefault.setText(settings.get(ConfLanguageKeys.RADIO_BUTTON_OUTPUT_FOLDER_TEXT_DEFAULT));
		radioButtonOutputFolderVideoSource.setText(settings.get(ConfLanguageKeys.RADIO_BUTTON_OUTPUT_FOLDER_TEXT_VIDEOSOURCE));
		labelOutputFile.setText(settings.get(ConfLanguageKeys.LABEL_OUTPUT_FILE));
		radioButtonOutputFileDefault.setText(settings.get(ConfLanguageKeys.RADIO_BUTTON_OUTPUT_FILE_TEXT_DEFAULT));
		radioButtonOutputFileVideoSource.setText(settings.get(ConfLanguageKeys.RADIO_BUTTON_OUTPUT_FILE_TEXT_VIDEOSOURCE));
		radioButtonOutputFileVideoSourceFolder.setText(settings.get(ConfLanguageKeys.RADIO_BUTTON_OUTPUT_FILE_TEXT_VIDEOSOURCEFOLDER));
		labelChapterName.setText(settings.get(ConfLanguageKeys.LABEL_CHAPTER_NAME));
		radioButtonChapterNameDefault.setText(settings.get(ConfLanguageKeys.RADIO_BUTTON_CHAPTER_NAME_DEFAULT));
		radioButtonChapterNameVideoSource.setText(settings.get(ConfLanguageKeys.RADIO_BUTTON_CHAPTER_NAME_VIDEOSOURCE));
		
		labelVideoConversion.setText(settings.get(ConfLanguageKeys.LABEL_VIDEO_TRANSCODING));
		radioButtonVideoConversionOutputFolderDefault.setText(settings.get(ConfLanguageKeys.RADIO_BUTTON_VIDEO_TRANSCODING_DEFAULT));
		radioButtonVideoConversionOutputFolderVideoSource.setText(settings.get(ConfLanguageKeys.RADIO_BUTTON_VIDEO_TRANSCODING_VIDEOSOURCEFOLDER));
		setTextFieldVideoConversionOutputDefaultValue();
		labelVideoConversionHandbrakeSettings.setText(settings.get(ConfLanguageKeys.LABEL_HANDBRAKE_SETTINGS));
		textFieldVideoConversionHandbrakeSettings.setText(settings.get(ConfSettingsKeys.HANDBRAKE_SETTINGS));
		labelVideoConversionEnabled.setText(settings.get(ConfLanguageKeys.LABEL_HANDBRAKE_ENABLED));
		checkBoxVideoConversionEnabled.setSelected(Boolean.valueOf(settings.get(ConfSettingsKeys.HANDBRAKE_ENABLED)));
		setVideoConversionState(checkBoxVideoConversionEnabled.isSelected());
		
		checkBoxAutoclear.setText(settings.get(ConfLanguageKeys.CHECKBOX_AUTOCLEAR_TEXT));
		checkBoxAutoJoin.setText(settings.get(ConfLanguageKeys.CHECKBOX_AUTOJOIN_TEXT));
		
		labelSeparateVideos.setText(FileSettings.HTML_TAG + settings.get(ConfLanguageKeys.LABEL_SEPARATE_VIDEOS));
		checkBoxSeparateVideos.setText(settings.get(ConfLanguageKeys.CHECKBOX_SEPARATE_VIDEOS_TEXT));
		
		editorPaneInformation.setContentType("text/html");
		editorPaneInformation.setEditable(false);
		editorPaneInformation.setBackground(null);
		editorPaneInformation.setBorder(null);
		editorPaneInformation.setText(settings.get(ConfLanguageKeys.EDITOR_PANE_INFORMATION));
		
		buttonJoin.setText(settings.get(ConfLanguageKeys.BUTTON_JOIN_VIDEOS_TEXT));
		buttonAbout.setText("About");
		
		dropTarget = new DropTarget(tableVideo, this);
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
		getContentPane().add(scrollPaneVideo, getComponentConstraints(GridBagConstraints.BOTH, 2, 3, 0, 0, 1));
		
		//Output settings
		ButtonGroup groupOutputFolder = new ButtonGroup();
		groupOutputFolder.add(radioButtonOutputFolderDefault);
		groupOutputFolder.add(radioButtonOutputFolderVideoSource);
		setRadioButtonDefaultOutputFolder();
		
		ButtonGroup groupOutputFile = new ButtonGroup();
		groupOutputFile.add(radioButtonOutputFileDefault);
		groupOutputFile.add(radioButtonOutputFileVideoSource);
		groupOutputFile.add(radioButtonOutputFileVideoSourceFolder);
		setRadioButtonDefaultOutputFile();
		
		groupChapterName.add(radioButtonChapterNameDefault);
		groupChapterName.add(radioButtonChapterNameVideoSource);
		setRadioButtonDefaultChapterName();
		
		//Output panel
		panelOutput.add(textFieldOutput,	getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 0, 0, 2));
		panelOutput.add(labeloutputFolder,	getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 1, 1));
		panelOutput.add(panelOutputFolder,	getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 1, 1, 1));
		panelOutput.add(labelOutputFile,	getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 2, 1));
		panelOutput.add(panelOutputFile,	getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 1, 2, 1));
		panelOutput.add(labelChapterName,	getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 3, 1));
		panelOutput.add(panelChapterName, 	getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 1, 3, 1));
		panelOutput.add(new JPanel(),		getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 0, 4, 2)); //Filler panel to push components to the top
		
		panelOutputFolder.add(radioButtonOutputFolderDefault,		getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 0, 1));
		panelOutputFolder.add(radioButtonOutputFolderVideoSource,	getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 1, 0, 1));
		
		panelOutputFile.add(radioButtonOutputFileDefault,			getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 0, 1));
		panelOutputFile.add(radioButtonOutputFileVideoSource,		getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 1, 0, 1));
		panelOutputFile.add(radioButtonOutputFileVideoSourceFolder,	getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 2, 0, 1));
		
		panelChapterName.add(radioButtonChapterNameDefault,			getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 0, 1));
		panelChapterName.add(radioButtonChapterNameVideoSource,		getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 1, 0, 1));
		
		//Automation settings
		panelAutomation.add(checkBoxAutoclear, 	getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 0, 0, 1));
		panelAutomation.add(checkBoxAutoJoin, 	getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 0, 1, 1));
		panelAutomation.add(new JPanel(), 		getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 0, 2, 1)); //Filler panel
		
		//Folder recursion and video joining settings
		panelFolderRecursion.add(labelSeparateVideos, 		getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 0, 0, 1));
		panelFolderRecursion.add(checkBoxSeparateVideos, 	getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 0, 1, 1));
		panelFolderRecursion.add(new JPanel(), 				getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 0, 2, 1));
		
		//Video conversion
		groupVideoConversion = new ButtonGroup();
		groupVideoConversion.add(radioButtonVideoConversionOutputFolderDefault);
		groupVideoConversion.add(radioButtonVideoConversionOutputFolderVideoSource);
		setRadioButtonDefaultVideoConversion();
		
		panelVideoConversionOutput.add(radioButtonVideoConversionOutputFolderDefault,		getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 0, 1));
		panelVideoConversionOutput.add(radioButtonVideoConversionOutputFolderVideoSource,	getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 1, 0, 1));
		
		panelVideoConversion.add(labelVideoConversion, 		 				getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 0, 1));
		panelVideoConversion.add(panelVideoConversionOutput, 				getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 1, 0, 1));
		panelVideoConversion.add(textFieldVideoConversionOutput,			getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 1, 1, 1));
		panelVideoConversion.add(labelVideoConversionHandbrakeSettings,		getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 2, 1));
		panelVideoConversion.add(textFieldVideoConversionHandbrakeSettings,	getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 1, 2, 1));
		panelVideoConversion.add(labelVideoConversionEnabled,				getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 3, 1));
		panelVideoConversion.add(checkBoxVideoConversionEnabled,			getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 1, 3, 1));
		panelVideoConversion.add(new JPanel(),								getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 0, 4, 2)); //Filler panel to push components to the top
				
		//Information panel
		panelInformation.add(editorPaneInformation,  getComponentConstraints(GridBagConstraints.BOTH, 1, 0, 0, 0, 1));
		panelInformation.add(new JPanel(),			 getComponentConstraints(GridBagConstraints.BOTH, 1, 1, 0, 1, 1));
		
		//Add Tabbed pane and the tabs
		getContentPane().add(tabbedPaneOptions, getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 1, 1));
		tabbedPaneOptions.add(settings.get(ConfLanguageKeys.TAB_NAME_OUTPUT), 			panelOutput);
		tabbedPaneOptions.add(settings.get(ConfLanguageKeys.TAB_NAME_AUTOMATION), 		panelAutomation);
		tabbedPaneOptions.add(settings.get(ConfLanguageKeys.TAB_NAME_FOLDER_RECURSION), panelFolderRecursion);
		tabbedPaneOptions.add(settings.get(ConfLanguageKeys.TAB_NAME_VIDEO_TRANSCODING), panelVideoConversion);
		tabbedPaneOptions.add(settings.get(ConfLanguageKeys.TAB_NAME_INFORMATION),		panelInformation);
		
		panelButton.add(buttonJoin, getComponentConstraints(GridBagConstraints.CENTER, 1, 0.5, 0, 0, 1));
		panelButton.add(buttonAbout, getComponentConstraints(GridBagConstraints.CENTER, 0, 0, 1, 0, 1));
		getContentPane().add(panelButton, getComponentConstraints(GridBagConstraints.BOTH, 0, 0, 0, 2, 1));
	}
	
	private void addActionListeners(){
		buttonJoin.addActionListener(actionListener);
		buttonAbout.addActionListener(actionListener);
		
		radioButtonOutputFolderDefault.addActionListener(actionListener);
		radioButtonOutputFolderVideoSource.addActionListener(actionListener);
		radioButtonOutputFileDefault.addActionListener(actionListener);
		radioButtonOutputFileVideoSource.addActionListener(actionListener);
		radioButtonOutputFileVideoSourceFolder.addActionListener(actionListener);
		radioButtonChapterNameDefault.addActionListener(actionListener);
		radioButtonChapterNameVideoSource.addActionListener(actionListener);
		
		radioButtonVideoConversionOutputFolderDefault.addActionListener(actionListener);
		radioButtonVideoConversionOutputFolderVideoSource.addActionListener(actionListener);
		checkBoxVideoConversionEnabled.addActionListener(actionListener);
		
		checkBoxSeparateVideos.addActionListener(actionListener);
	}
	
	private void init(){
		//Lets use the system look and feel
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.setTitle("Java MP4Box Gui v1.9-SNAPSHOT");
		this.setSize(640,480);
		this.setLocationRelativeTo(null); //Centers the window on the screen
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void saveLogFile(){
		if(Boolean.valueOf(settings.get(ConfSettingsKeys.LOG_WRITE_TO_FILE))){
			try {
				Handler handler = new FileHandler(settings.get(ConfSettingsKeys.LOG_NAME), Integer.valueOf(settings.get(ConfSettingsKeys.LOG_SIZE)), Integer.valueOf(settings.get(ConfSettingsKeys.LOG_NUMBER_OF_FILES)));
				handler.setFormatter(new SimpleFormatter());
				Logger.getLogger("Log").addHandler(handler);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setRadioButtonDefaultOutputFolder(){
		String selection = settings.get(ConfSettingsKeys.RADIO_BUTTON_OUTPUT_FOLDER_DEFAULT_SELECTION);
		int defaultSelection = Integer.valueOf(selection);
		
		if(defaultSelection==1){
			radioButtonOutputFolderDefault.setSelected(true);
		}else if(defaultSelection==2){
			radioButtonOutputFolderVideoSource.setSelected(true);
		}
	}
	
	private void setRadioButtonDefaultOutputFile(){
		int defaultSelection = Integer.valueOf(settings.get(ConfSettingsKeys.RADIO_BUTTON_OUTPUT_FILE_DEFAULT_SELECTION));
		
		if(defaultSelection==1){
			radioButtonOutputFileDefault.setSelected(true);
		}else if(defaultSelection==2){
			radioButtonOutputFileVideoSource.setSelected(true);
		}else if(defaultSelection==3){
			radioButtonOutputFileVideoSourceFolder.setSelected(true);
		}
	}
	
	private void setRadioButtonDefaultChapterName(){
		int defaultSelection = Integer.valueOf(settings.get(ConfSettingsKeys.RADIO_BUTTON_CHAPTER_NAME_DEFAULT_SELECTION));
		
		if(defaultSelection==1){
			radioButtonChapterNameDefault.setSelected(true);
		}else if(defaultSelection==2){
			radioButtonChapterNameVideoSource.setSelected(true);
		}
	}
	
	private void setRadioButtonDefaultVideoConversion(){
		int defaultSelection = Integer.valueOf(settings.get(ConfSettingsKeys.RADIO_BUTTON_VIDEO_TRANSCODING_DEFAULT_SELECTION));
		
		if(defaultSelection==1){
			radioButtonVideoConversionOutputFolderDefault.setSelected(true);
		}else if(defaultSelection==2){
			radioButtonVideoConversionOutputFolderVideoSource.setSelected(true);
		}
	}
	
	@Override
	/**
	 * Source: Drag&Drop: http://www.java-tips.org/java-se-tips/javax.swing/how-to-implement-drag-drop-functionality-in-your-applic.html
	 */
	public void drop(DropTargetDropEvent dtde) {
		//Quick check to see if it exists, otherwise it will do it for all folders, and that is annoying.
		MP4BoxController controller = new MP4BoxController(this);
		String mp4boxPath = controller.getMP4BoxFilePath();
		if(!(new File(mp4boxPath).exists())){
			JOptionPane.showMessageDialog(this, controller.getMP4BoxMissingMessage(mp4boxPath));
			return;
		}
		
		try {
			// Ok, get the dropped object and try to figure out what it is
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				//System.out.println("Possible flavor: " + flavors[i].getMimeType());
				// Check for file lists specifically
				if (flavors[i].isFlavorJavaFileListType()) {
					boolean wasDataEmpty = false;
					if(videoTableModel.getData()==null || videoTableModel.getData().length==0){
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
					videoTableModel.fireTableStructureChanged();
					tableVideo.setModel(videoTableModel);
					
					if(wasDataEmpty){
						checkAndUpdateFolderFileNames();
					}
					
					//Auto joins if that option is selected, but not if the separate videos option is selected (list should be empty by now then.
					if(checkBoxAutoJoin.isSelected() && !checkBoxSeparateVideos.isSelected()){
						new MP4BoxController(this);
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
			log.log(Level.SEVERE, "File drop failed", e);
			dtde.rejectDrop();
		}
	}
	
	/**
	 * Updates the folder- and filename based on what options are selected
	 */
	private void checkAndUpdateFolderFileNames(){
		/**
		 * Checks if the data was empty and the radio button for video source is selected.
		 */
		if(radioButtonOutputFolderVideoSource.isSelected()){
			actionListener.actionRadioButtonOutputFolderVideoSource();
		}
		
		/**
		 * Checks if the data was empty and the radio button for video source is selected.
		 */
		if(radioButtonOutputFileVideoSource.isSelected()){
			actionListener.actionRadioButtonOutputFileVideoSource();
		}else if(radioButtonOutputFileVideoSourceFolder.isSelected()){
			actionListener.actionRadioButtonOutputFileVideoSourceFolder();
		}
	}

	/**
	 * Adds the files to the model in a recursive manner.
	 * Will call the MP4BoxController manually of the separate videos option is selected.
	 * @param filePath
	 * @param chapterNumber
	 * @return Chapter Number
	 */
	public int addFilePathToModel(String filePath, int chapterNumber){
		File file = new File(filePath);
		
		if(file.isDirectory()){
			if(checkBoxSeparateVideos.isSelected()){
				//Resets the the chap number for each folder, so each video will start with 1. This is due to the recursive use of this method!
				chapterNumber = 0;
				
				/**
				 * First join all videos in the current folder
				 */
				File[] files = file.listFiles(filterFile);
				Arrays.sort(files, new NaturalOrderComparator());
				for(File childFile : files){
					try {
						chapterNumber = addFilePathToModel(childFile.getCanonicalPath().toString(), chapterNumber);
					} catch (IOException e) {
						messageProcessFileException(filePath, e);
					}
				}
				
				//Run join command if there is data
				if(videoTableModel.getData().length>0){
					//Update folder path and file name
					checkAndUpdateFolderFileNames();
					
					//Calls the controller manually so that the process is done per folder, and not on all files.
					MP4BoxController controller = new MP4BoxController(this);
					controller.joinVideos();
				}
				
				/**
				 * Now iterate thru the folders
				 */
				File[] folders = file.listFiles(filterDirectory);
				Arrays.sort(folders, new NaturalOrderComparator());
				for(File childFile : folders){
					try {
						chapterNumber = addFilePathToModel(childFile.getCanonicalPath().toString(), chapterNumber);
					} catch (IOException e) {
						messageProcessFileException(filePath, e);
					}
				}
			}else{
				/**
				 * This is run if the "join per folder" option is unchecked.
				 * All files in the folder(s) will be combined to a single file!
				 */
				File[] files = file.listFiles();
				Arrays.sort(files, new NaturalOrderComparator());
				for(File childFile : files){
					try {
						chapterNumber = addFilePathToModel(childFile.getCanonicalPath().toString(), chapterNumber);
					} catch (IOException e) {
						messageProcessFileException(filePath, e);
					}
				}
			}
		}else{
			/**
			 * Adds the file to the model since it isn't a folder
			 */
			String chaptername = "";
			
			//Names chapters based on what is selected
			if(radioButtonChapterNameDefault.isSelected()){
				chapterNumber = chapterNumber + 1;
				chaptername = settings.get(ConfLanguageKeys.CHAPTER_NAME_DEFAULT) + chapterNumber;
			}else{
				chaptername = FileSettings.splitOutputFilePath(filePath)[1]; //Get filename
				chaptername = chaptername.substring(0, chaptername.lastIndexOf(".")); //Remove filetype
			}
			
			//Add data to the model
			videoTableModel.addRow(filePath, 
				Boolean.valueOf(settings.get(ConfSettingsKeys.CHAPTER_ENABLED)), 
				chaptername);
		}
		
		return chapterNumber;
	}
	
	/**
	 * Sets the value of textFieldVideoConversionOutput to the default value.
	 * If there is a value defined in settings.conf, then that is used,
	 * otherwise the application folder is used.
	 */
	public void setTextFieldVideoConversionOutputDefaultValue(){
		String folderDefault = getFileSettings().getCurrentVideoConversionOutputPath();
		getTextFieldVideoConversionOutput().setText(folderDefault);
	}
	
	public void setVideoConversionState(Boolean enabled){
		if(enabled){
			getRadioButtonVideoConversionOutputFolderDefault().setEnabled(true);
			getRadioButtonVideoConversionOutputFolderVideoSource().setEnabled(true);
			getTextFieldVideoConversionOutput().setEnabled(true);
			getTextFieldVideoConversionHandbrakeSettings().setEnabled(true);
		}else{
			getRadioButtonVideoConversionOutputFolderDefault().setEnabled(false);
			getRadioButtonVideoConversionOutputFolderVideoSource().setEnabled(false);
			getTextFieldVideoConversionOutput().setEnabled(false);
			getTextFieldVideoConversionHandbrakeSettings().setEnabled(false);
		}
	}
	
	public void updateChapterNameNameingscheme(){
		if(radioButtonChapterNameDefault.isSelected()){
			Object[][] data = videoTableModel.getData();
			for(int i=0;i<data.length;i++){
				String chapterNumber = String.valueOf(i+1);
				videoTableModel.setValueAt(settings.get(ConfLanguageKeys.CHAPTER_NAME_DEFAULT) + chapterNumber, i, 2);
			}
		}else{
			Object[][] data = videoTableModel.getData();
			for(int i=0;i<data.length;i++){
				String filename = FileSettings.splitOutputFilePath((String)data[i][0])[1]; //Selects the entire file path and filename, then splits that into two parts.
				filename = filename.substring(0, filename.lastIndexOf(".")); //and then removes the filetype by selecting the substring up until the last "."
				
				videoTableModel.setValueAt(filename, i, 2);
			}
		}
	}
	
	private void messageProcessFileException(String filePath, IOException e){
		JOptionPane.showMessageDialog(this, "Unable to properly process a file in " + filePath + "\nStack trace: " + e.getMessage());
		log.log(Level.SEVERE, "Unable to properly process a file in " + filePath, e);
	}
	
	/**
	 * This returns the output path in the JTextField which hopefully is correct and so on!
	 * The filename is removed from the string!
	 * @return
	 */
	public String getFolderPathOutput(){
		return textFieldOutput.getText().substring(0, textFieldOutput.getText().lastIndexOf(File.separator) + 1);
	}
	
	/**
	 * Returns the filename in the JTextField!
	 * @return
	 */
	public String getFilenameOutput(){
		return getFilenameOutput(textFieldOutput.getText());
	}
	
	public String getFilenameOutput(String file){
		return file.substring(file.lastIndexOf(File.separator) + 1, file.length());
	}
	
	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// Not needed atm
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// Not needed atm
		
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// Not needed atm
	}
	
	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// Not needed atm
	}
	
	public HashMap<String, String> getSettings() {
		return settings;
	}
	
	public VideoTableModel getVideoTableModel() {
		return videoTableModel;
	}
		
	public JTextField getTextFieldOutput() {
		return textFieldOutput;
	}
	
	public JTextField getTextFieldVideoConversionOutput() {
		return textFieldVideoConversionOutput;
	}
	
	public JTextField getTextFieldVideoConversionHandbrakeSettings() {
		return textFieldVideoConversionHandbrakeSettings;
	}
	
	public JRadioButton getRadioButtonOutputFolderDefault() {
		return radioButtonOutputFolderDefault;
	}
	
	public JRadioButton getRadioButtonOutputFolderVideoSource() {
		return radioButtonOutputFolderVideoSource;
	}
	
	public JRadioButton getRadioButtonOutputFileDefault() {
		return radioButtonOutputFileDefault;
	}
	
	public JRadioButton getRadioButtonOutputFileVideoSource() {
		return radioButtonOutputFileVideoSource;
	}
	
	public JRadioButton getRadioButtonOutputFileVideoSourceFolder() {
		return radioButtonOutputFileVideoSourceFolder;
	}
	
	public JRadioButton getRadioButtonVideoConversionOutputFolderDefault() {
		return radioButtonVideoConversionOutputFolderDefault;
	}
	
	public JRadioButton getRadioButtonVideoConversionOutputFolderVideoSource() {
		return radioButtonVideoConversionOutputFolderVideoSource;
	}
	
	public JRadioButton getRadioButtonChapterNameDefault() {
		return radioButtonChapterNameDefault;
	}
	
	public JRadioButton getRadioButtonChapterNameVideoSource() {
		return radioButtonChapterNameVideoSource;
	}
	
	public JButton getButtonJoin(){
		return buttonJoin;
	}
	
	public JButton getButtonAbout(){
		return buttonAbout;
	}
	
	public JCheckBox getCheckBoxVideoConversionEnabled() {
		return checkBoxVideoConversionEnabled;
	}
	
	public JCheckBox getCheckBoxSeparateVideos(){
		return checkBoxSeparateVideos;
	}

	public JCheckBox getCheckBoxAutoJoin(){
		return checkBoxAutoJoin;
	}
	
	public JCheckBox getCheckBoxAutoClear(){
		return checkBoxAutoclear;
	}
	
	public FileSettings getFileSettings() {
		return fileSettings;
	}
	
	public ActionListener getActionListener(){
		return actionListener;
	}
	
}
