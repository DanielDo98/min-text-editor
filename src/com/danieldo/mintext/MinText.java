package com.danieldo.mintext;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;

public class MinText extends JFrame {
    
	private static final long serialVersionUID = 9073550882218327707L;
	
	public final Theme[] themesUsed = {Theme.BASIC, Theme.MATRIX, Theme.SKY, Theme.HALL};
    public final int macMenu = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    public final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    public final String iconPath = "src/icon.png";
    public final double widthPercentage = .6;
    public final double heightPercentage = .92;
    
    public final String helpQuery = "Press ⌘-e for help";
    public final String helpText = "⌘-n  New File"
    		+ "        ⌘-o  Open File"
    		+ "        ⌘-s  Save File"
    		+ "        ⌘--  Decrease Text Size"
    		+ "        ⌘-=  Increase Text Size"
    		+ "        ⌘-[1, 4]  Themes"
    		+ "        esc  Exit";
    
    public final JLabel helpLabel = new JLabel(helpQuery);
    private static JFileChooser fileChoose = new JFileChooser();
    public final JScrollPane editorScrollPane = new JScrollPane();
    public final JTextArea control = new JTextArea();
    
    private int fontSize = 14;
    private int themeNum = 0;

    public static void main(String[] args) {
        new MinText();
    }
 
    public MinText() {
        setResizable(false);
        setTitle("MinText");
        
        setIcon();
        createGUI();
        setKeyBindings();
        
        gd.setFullScreenWindow(this);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void createGUI() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = widthPercentage * screenSize.getWidth();
        double height = heightPercentage * screenSize.getHeight();
        
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        
        control.setLineWrap(true);
        control.setWrapStyleWord(true);
        control.setBorder(null);
        
        editorScrollPane.setPreferredSize(new Dimension((int) width, (int) height));
        editorScrollPane.setViewportView(control);
        editorScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        editorScrollPane.setBorder(null);
        cont.gridx = cont.gridy = 0;
        getContentPane().add(editorScrollPane, cont);
        
        cont.gridy = 1;
        cont.insets = new Insets(20, 0, 0, 0);
        getContentPane().add(helpLabel, cont);
        
        setTheme(themeNum);
    }
    
    private void setKeyBindings() {
    	
    	class ActionLambda extends AbstractAction
    	{
			private static final long serialVersionUID = -1864426065161242421L;
			private final Runnable action;
    	    public ActionLambda(Runnable action)
    	    {
    	        this.action = action;
    	    }

			@Override
			public void actionPerformed(ActionEvent e) {
				action.run();
			}
    	}

    	Action newFile = new ActionLambda(() -> newFile());
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('N', macMenu), "newFile");
        control.getActionMap().put("newFile", newFile);
        
        Action openFile = new ActionLambda(() -> openFile());
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('O', macMenu), "openFile");
        control.getActionMap().put("openFile", openFile);
        
        Action saveFile = new ActionLambda(() -> saveFile());
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('S', macMenu), "saveFile");
        control.getActionMap().put("saveFile", saveFile);
        
        Action exitFile = new ActionLambda(() -> exitForm());
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitFile");
        control.getActionMap().put("exitFile", exitFile);
        
        Action fontIncrease = new ActionLambda(() -> changeFont(1));
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, macMenu), "fontIncrease");
        control.getActionMap().put("fontIncrease", fontIncrease);
        
        Action fontDecrease = new ActionLambda(() -> changeFont(-1));
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, macMenu), "fontDecrease");
        control.getActionMap().put("fontDecrease", fontDecrease);
        
        Action help = new ActionLambda(() -> helpLabel.setText(helpText));
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('E', macMenu, false), "help");
        control.getActionMap().put("help", help);
        
        Action releaseHelp = new ActionLambda(() -> helpLabel.setText(helpQuery));
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('E', macMenu, true), "releaseHelp");
        control.getActionMap().put("releaseHelp", releaseHelp);
        
        UndoManager manager = new UndoManager();
        control.getDocument().addUndoableEditListener(manager);
        Action undo = new ActionLambda(() -> undo(manager));
      	control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('Z', macMenu), "undo");
        control.getActionMap().put("undo", undo);
        
        Action redo = new ActionLambda(() -> redo(manager));
      	control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('Y', macMenu), "redo");
        control.getActionMap().put("redo", redo);
        
        for (int i = 0; i < themesUsed.length; i++) {
        	final int cur = i;
        	Action setTheme = new ActionLambda(() -> setTheme(cur));
        	control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(i + 49, macMenu), "setTheme" + i);
        	control.getActionMap().put("setTheme" + i, setTheme);
        }
    }
    
    private void setIcon() {
    	try {
    		setIconImage(ImageIO.read(MinText.class.getClassLoader().getResourceAsStream("icon.png")));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    private void exitForm() {
    	System.exit(0);
    }
    
    private void setTheme(int i) {
    	Theme currentTheme = themesUsed[i];
    	getContentPane().setBackground(currentTheme.getBackground());
    	control.setFont(new Font(currentTheme.getFontName(), Font.PLAIN, fontSize));
        control.setForeground(currentTheme.getForeground());
        control.setBackground(currentTheme.getBackground());
        control.setCaretColor(currentTheme.getCarotColor());
        
        helpLabel.setFont(new Font(currentTheme.getFontName(), Font.PLAIN, 11));
        helpLabel.setForeground(currentTheme.getForeground());
    }
    
    //Change is either positive or negative (1 or -1)
    private void changeFont(int change) {
    	fontSize += change * (fontSize <= 20 && fontSize >= 10 ? 1 : 2);
    	control.setFont(new Font(themesUsed[themeNum].getFontName(), Font.PLAIN, fontSize));
    }
    
    private void undo(UndoManager manager) {
    	if (manager.canUndo()) manager.undo();
    }
    
    private void redo(UndoManager manager) {
    	if (manager.canRedo()) manager.redo();
    }
    
    private void newFile() {
    	control.setText("");
    }
    
    private void openFile() {
        fileChoose.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChoose.setDialogTitle("Open file");
        
        gd.setFullScreenWindow(null);
        if (fileChoose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Scanner scan = new Scanner(fileChoose.getSelectedFile());
                control.setText("");
                while (scan.hasNext()) {
                    control.append(scan.next() + "\n");
                }
                
                scan.close();
            } catch (FileNotFoundException e) {
                JOptionPane.showConfirmDialog(this, "There was an error opening the file",
                        "Error reading", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }
        gd.setFullScreenWindow(this);
    }
    
    private void saveFile() {
        fileChoose.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChoose.setDialogTitle("Save file");
        
        gd.setFullScreenWindow(null);
        if (fileChoose.showSaveDialog(getContentPane()) == JFileChooser.APPROVE_OPTION) {
        	File selectedFile = fileChoose.getSelectedFile();
            String filePath = fileChoose.getSelectedFile().getAbsolutePath();
            
            if (fileChoose.getSelectedFile().exists()) {
                int response = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to overwrite this file?",
                        "Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.NO_OPTION) {
                    return;
                }
                
                else if (response == JOptionPane.YES_OPTION){
                    int indexOfDot = filePath.indexOf(".");
                    if (indexOfDot == -1) {
                        selectedFile = new File(selectedFile + ".txt");
                    } else {
                    	selectedFile = new File(filePath.substring(0, indexOfDot) + ".txt");
                    }
                }
            } else {
            	selectedFile = new File(selectedFile + ".txt");
            }
            
            try {
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(selectedFile)));
                for (int i = 0; i < control.getLineCount(); i++) {
                    try {
                        writer.println(control.getText().substring(control.getLineStartOffset(i), control.getLineEndOffset(i)));
                    } catch (BadLocationException e) {
                        JOptionPane.showConfirmDialog(this, "Error reading text");
                    }
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(this, "Error writing to file", "Writing error", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        gd.setFullScreenWindow(this);
    }
}
