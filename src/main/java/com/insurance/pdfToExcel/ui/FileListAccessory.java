package com.insurance.pdfToExcel.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class FileListAccessory extends JComponent implements PropertyChangeListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private File[] files;
    private DefaultListModel<File> model;
    private JList<File> list;
    private JButton removeItem;

    public FileListAccessory(JFileChooser chooser) {
        chooser.addPropertyChangeListener(this);

        model = new DefaultListModel<File>();
        list = new JList<File>(model);
        JScrollPane pane = new JScrollPane(list);
        pane.setPreferredSize(new Dimension(400, 250));

        removeItem = createRemoveItemButton();

        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        add(pane);
        
        JButton btnNewButton = new JButton("Добави полици");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		addFilesToList();
        	}
        });
        pane.setRowHeaderView(btnNewButton);
        add(removeItem, BorderLayout.SOUTH);

    }

    public DefaultListModel<File> getModel() {
        return model;
    }

    private void addFilesToList() {
    	if(files != null && files.length > 0) {
    		for(int i=0; i<files.length; i++) {
	    		File file = files[i];
		    	if(file != null && !model.contains(file)) {
		    		model.addElement(file);
		    	}
    		}
    	}
    }

    private void removeFilesFromList() {
        if (list.getSelectedValuesList()!= null && list.getSelectedValuesList().size() > 0) {     	
        	List<File> filesToRemove = list.getSelectedValuesList();
        	for(File file: filesToRemove) {
        		model.removeElement(file);
        	}
        }
    }

    private JButton createRemoveItemButton() {
        JButton button = new JButton("Изтрий");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                removeFilesFromList();
            }
        });
        return button;
    }


    public void propertyChange(PropertyChangeEvent e) {  
        String prop = e.getPropertyName();

        if (JFileChooser.SELECTED_FILES_CHANGED_PROPERTY.equals(prop)) {
            files = (File[]) e.getNewValue(); 
            //addFilesToList();
        }
    }
}