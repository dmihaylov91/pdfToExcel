package com.insurance.pdfToExcel.ui;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.insurance.pdfToExcel.data.LogData;
import com.insurance.pdfToExcel.data.LogRow;
import com.insurance.pdfToExcel.service.PdfToExcelService;
import com.insurance.pdfToExcel.service.impl.PdfToExcelServiceImpl;

public class App extends JFrame implements ActionListener, PropertyChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2614951350302134739L;
	private JPanel contentPane;
	
	List<File> pdfFiles = new ArrayList<File>();
	File smsSystemExcelFile;
	
	private JList<File> jList = new JList<File>();
	private DefaultListModel<File> jListModel = new DefaultListModel<File>();

	private JProgressBar progressBar;
	private JButton startButton;
	
	private PdfToExcelTask task;
	private PdfToExcelService pdfToExcelService = new PdfToExcelServiceImpl();
	private JLabel smsFileNameLabel;
	
	private StringBuffer errorLog = new StringBuffer();
	
	private LogData logData = new LogData();
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App() {
		//used for pdfbox
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) 
		{
			//
		}
		
				
		Image image = new ImageIcon(getClass().getResource("/resources/pdfIconX32.png")).getImage();
		this.setIconImage(image);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 752, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNewLabel = new JLabel("Избери полици PDF");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 1;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		JButton btnNewButton = new JButton("...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choosePdfFiles();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridwidth = 3;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 6;
		gbc_btnNewButton.gridy = 1;
		contentPane.add(btnNewButton, gbc_btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 10;
		gbc_scrollPane.gridheight = 5;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 10;
		gbc_scrollPane.gridy = 1;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		scrollPane.setViewportView(jList);
		jList.setModel(jListModel);
		
		JLabel lblChooseSmsSystem = new JLabel("Избери застраховки SMS файл");
		GridBagConstraints gbc_lblChooseSmsSystem = new GridBagConstraints();
		gbc_lblChooseSmsSystem.gridwidth = 3;
		gbc_lblChooseSmsSystem.anchor = GridBagConstraints.WEST;
		gbc_lblChooseSmsSystem.insets = new Insets(0, 0, 5, 5);
		gbc_lblChooseSmsSystem.gridx = 2;
		gbc_lblChooseSmsSystem.gridy = 5;
		contentPane.add(lblChooseSmsSystem, gbc_lblChooseSmsSystem);
		
		JButton btnNewButton_2 = new JButton("...");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseSmsSystemFile();
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.gridwidth = 3;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 6;
		gbc_btnNewButton_2.gridy = 5;
		contentPane.add(btnNewButton_2, gbc_btnNewButton_2);
		
		startButton = new JButton("Старт");
		startButton.setActionCommand("start");
		startButton.addActionListener(this);
		
		JButton btnRemove = new JButton("Изтрий");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeAction();
			}
		});
		
		smsFileNameLabel = new JLabel("");
		smsFileNameLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		smsFileNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.gridwidth = 6;
		gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 6;
		contentPane.add(smsFileNameLabel, gbc_lblNewLabel_1);
		GridBagConstraints removeButton = new GridBagConstraints();
		removeButton.insets = new Insets(0, 0, 5, 5);
		removeButton.gridx = 11;
		removeButton.gridy = 6;
		contentPane.add(btnRemove, removeButton);
		
		JButton btnNewButton_3 = new JButton("Изтрий всички");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeAllAction();
			}
		});
		GridBagConstraints removeAllButton = new GridBagConstraints();
		removeAllButton.insets = new Insets(0, 0, 5, 5);
		removeAllButton.gridx = 12;
		removeAllButton.gridy = 6;
		contentPane.add(btnNewButton_3, removeAllButton);
		
		progressBar = new JProgressBar();
		progressBar.setValue(0);
        progressBar.setStringPainted(true);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridheight = 2;
		gbc_progressBar.gridwidth = 9;
		gbc_progressBar.insets = new Insets(0, 0, 5, 5);
		gbc_progressBar.gridx = 11;
		gbc_progressBar.gridy = 7;
		contentPane.add(progressBar, gbc_progressBar);
		
		JButton btnNewButton_1 = new JButton("Лог");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showProcessLog();
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 17;
		gbc_btnNewButton_1.gridy = 10;
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);
		GridBagConstraints gbcexecuteButton = new GridBagConstraints();
		gbcexecuteButton.anchor = GridBagConstraints.SOUTHEAST;
		gbcexecuteButton.gridwidth = 3;
		gbcexecuteButton.gridx = 19;
		gbcexecuteButton.gridy = 10;
		contentPane.add(startButton, gbcexecuteButton);
	}
	
	private void choosePdfFiles() {
		JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("pdf", "pdf");
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        
        FileListAccessory accessory = new FileListAccessory(chooser);
        chooser.setAccessory(accessory);
        
        if(pdfFiles != null && pdfFiles.size() > 0) {
        	chooser.setCurrentDirectory(pdfFiles.get(pdfFiles.size()-1));
        }
        
        int returnVal = chooser.showOpenDialog(this);       
        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
        	DefaultListModel<File> model = accessory.getModel();
        	
        	if(model != null && model.size() > 0) {
        		for(int i=0; i<model.size(); i++) {
        			File file = model.getElementAt(i);
        			if(pdfFiles == null) {
        				pdfFiles = new ArrayList<File>();
        			}
        			if(!pdfFiles.contains(file)) {
        				pdfFiles.add(file);
        				jListModel.addElement(file);
        			}
        		}
        	}
        	
        }
		
	}
	
	private void chooseSmsSystemFile() {
		JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xlsx, xlsm", "xlsx", "xlsm");
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        
        if(smsSystemExcelFile != null) {
        	chooser.setCurrentDirectory(smsSystemExcelFile);
        }
        
        int returnVal = chooser.showOpenDialog(this);       
        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
        	File file = chooser.getSelectedFile();
        	if(file != null) {
        		smsSystemExcelFile = file;
        		smsFileNameLabel.setText(file.getName());
        	}
        	
        }
		
	}
	
	
	private void removeAction() {
		if (jList.getSelectedValuesList() != null && jList.getSelectedValuesList().size() > 0) {
			List<File> filesToRemove = jList.getSelectedValuesList();
			for (File file : filesToRemove) {
				jListModel.removeElement(file);
				//it should contain this
				if(pdfFiles.contains(file)) {
					pdfFiles.remove(file);
				}
			}
		}
	}
	
	private void removeAllAction(){
		if(jListModel != null && jListModel.size() > 0) {
			jListModel.removeAllElements();
		}
		if(pdfFiles != null && pdfFiles.size() > 0) {
			pdfFiles.clear();
		}
	}
	
	private void showLogAction(String message) {
		if(errorLog !=null && errorLog.length() > 0) {
			JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void showProcessLog() {
		if(logData != null && !logData.getLogRows().isEmpty()) {
			String log = "";
			for(LogRow logRow: logData.getLogRows()) {
				log += logRow.getFileName() + " ; " + logRow.getImportStatus() + " ; " + logRow.getFailReason() + "\n";
			}
			
			JTextArea textArea = new JTextArea(25, 100);
			textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
			textArea.setText(log);
			textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(textArea);
			
			JOptionPane.showMessageDialog(this, scrollPane, "Log", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
	private void executeAction() {

		if (pdfFiles != null && pdfFiles.size() > 0 && smsSystemExcelFile != null) {
			errorLog = new StringBuffer();
			logData.getLogRows().clear();
			progressBar.setValue(0);
			startButton.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			task = new PdfToExcelTask();
			task.addPropertyChangeListener(this);
			task.execute();
		} else {
			JOptionPane.showMessageDialog(this, "Please select all necessary files", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public void propertyChange(PropertyChangeEvent evt) {
		
		if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        } 
		
	}
	

	public void actionPerformed(ActionEvent e) {
		executeAction();	
	}	

	
	
	
	class PdfToExcelTask extends SwingWorker<Void, Void> {
		
		@Override
		protected Void doInBackground() {
			
			int progress = 0;
			//Initialize progress property.
	        setProgress(progress);
	        
	        for(int i = 0; i<pdfFiles.size(); i++) {
	        	File file = pdfFiles.get(i);
	        	
	        	try {
	        		pdfToExcelService.convertPdfToExcel(file, smsSystemExcelFile, logData);
	        	}
	        	catch(Exception ex) {
	        		errorLog.append(ex.getMessage() + "\r\n");
	        		showLogAction("Current file: " + file.getAbsolutePath() + "\r\n" + ex.getMessage());
	        		return null;
	        	}
	        	       
	        	//file processed, update progress as percentage
	        	progress = ((i+1) * 100)/pdfFiles.size();
	        	setProgress(progress);
	        }
	        
			return null;
		}
		
		
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            
            showProcessLog();
        }

	}





}
