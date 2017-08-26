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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

public class NoteEditorWithFiles extends JFrame {
    
    public static JFileChooser fileChoose = new JFileChooser();
    
    public static JScrollPane editorScrollPane = new JScrollPane();
    public static JTextArea control = new JTextArea();
    
    final static String helpQuery = "Press ⌘-e for help";
    final static String helpText = "⌘-n  New File"
    		+ "        ⌘-o  Open File"
    		+ "        ⌘-s  Save File"
    		+ "        ⌘--  Decrease Text Size"
    		+ "        ⌘-=  Increase Text Size"
    		+ "        ⌘-[1, 4]  Themes"
    		+ "        esc  Exit";
    public static JLabel helpLabel = new JLabel(helpQuery);
    
    
    public static int fontSize = 14;
    public static Theme[] themesUsed = {Theme.BASIC, Theme.MATRIX, Theme.SKY, Theme.HALL};
    public static Theme currentTheme = themesUsed[0];

    public static double widthPercentage = .6;
    public static double heightPercentage = .92;

    public static void main(String[] args) {
        new NoteEditorWithFiles();
    }
 
    public NoteEditorWithFiles() {
        setResizable(false);
        setTitle("MinText");
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = widthPercentage * screenSize.getWidth();
        double height = heightPercentage * screenSize.getHeight();
        
        //setUndecorated(true);
        getContentPane().setLayout(new GridBagLayout());
        setTheme();
        
        GridBagConstraints cont = new GridBagConstraints();
        
        control.setLineWrap(true);
        control.setWrapStyleWord(true);
        control.setBorder(null);
        
        editorScrollPane.setPreferredSize(new Dimension((int) width, (int) height));
        editorScrollPane.setViewportView(control);
        editorScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        editorScrollPane.setBorder(null);
        cont.gridx = 0;
        cont.gridy = 0;
        getContentPane().add(editorScrollPane, cont);
        
        cont.gridx = 0;
        cont.gridy = 1;
        cont.insets = new Insets(20, 0, 0, 0);
        getContentPane().add(helpLabel, cont);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitForm();
            }
        });
        
        Action newFile = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                newPressed();
            }
        };
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                                    "newFile");
        control.getActionMap().put("newFile",
                                     newFile);
        
        Action openFile = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                openButtonPressed();
            }
        };
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                                    "openFile");
        control.getActionMap().put("openFile",
                                     openFile);
        
        Action saveFile = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                saveButtonPressed();
            }
        };
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                                    "saveFile");
        control.getActionMap().put("saveFile",
                                     saveFile);
        
        Action exitFile = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                exitForm();
            }
        };
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                                    "exitFile");
        control.getActionMap().put("exitFile",
                                     exitFile);
        
        Action fontIncrease = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	fontSize += fontSize <= 20 && fontSize >= 10 ? 1 : 2;
            	fontPressed(currentTheme, fontSize);
            }
        };
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                                    "fontIncrease");
        control.getActionMap().put("fontIncrease",
                                     fontIncrease);
        
        Action fontDecrease = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	fontSize -= fontSize <= 20 ? 1 : 2;
                fontPressed(currentTheme, fontSize);
            }
        };
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                                    "fontDecrease");
        control.getActionMap().put("fontDecrease",
                                     fontDecrease);
        
        for (int i = 0; i < themesUsed.length; i++) {
        	final int cur = i;
        	Action setTheme = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                	currentTheme = themesUsed[cur];
                	setTheme();
                }
            };
        	
        	control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(i + 49, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "setTheme" + i);
        	control.getActionMap().put("setTheme" + i, setTheme);
        }
        
        Action help = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	helpLabel.setText(helpText);
            }
        };
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('E', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false), "help");
        control.getActionMap().put("help", help);
        
        Action releaseHelp = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	helpLabel.setText(helpQuery);
            }
        };
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('E', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true), "releaseHelp");
        control.getActionMap().put("releaseHelp", releaseHelp);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
        
        //pack();
        setLocationRelativeTo(null);

        setVisible(true);
    }
    
    private void newPressed() {
    	control.setText("");
    }
    
    private void setTheme() {
    	getContentPane().setBackground(currentTheme.getBackground());
    	control.setFont(new Font(currentTheme.getFontName(), Font.PLAIN, fontSize));
        control.setForeground(currentTheme.getForeground());
        control.setBackground(currentTheme.getBackground());
        control.setCaretColor(currentTheme.getCarotColor());
        
        helpLabel.setFont(new Font(currentTheme.getFontName(), Font.PLAIN, 11));
        helpLabel.setForeground(currentTheme.getForeground());
    }
    
    private void fontPressed(Theme theme, int newSize) {
    	control.setFont(new Font(theme.getFontName(), Font.PLAIN, newSize));
    }
    
    private void exitForm() {
    	System.exit(0);
    }
    
    private void openButtonPressed() {
        fileChoose.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChoose.setDialogTitle("Open file");
        fileChoose.addChoosableFileFilter(new FileNameExtensionFilter("Text files","txt"));
        
        if (fileChoose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Scanner scan = new Scanner(fileChoose.getSelectedFile());
                control.setText("");
                while (scan.hasNext()) {
                    control.append(scan.next() + "\n");
                }
                
                scan.close();
                
            } catch (FileNotFoundException e) {
                JOptionPane.showConfirmDialog(null, "There was an error opening the file",
                        "Error reading", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void saveButtonPressed() {
        fileChoose.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChoose.setDialogTitle("Save file");
        fileChoose.addChoosableFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        
        if (fileChoose.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChoose.getSelectedFile().getName();
            
            if (fileChoose.getSelectedFile().exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Do you want to overwrite this file?",
                        "Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.NO_OPTION) {
                    return;
                }
                
                else if (response == JOptionPane.YES_OPTION){
                    int indexOfDot = fileName.indexOf(".");
                    if (indexOfDot == -1) {
                        fileName += ".txt";
                    }
                    
                    else {
                        fileName = fileName.substring(0, indexOfDot) + ".txt";
                    }
                }
            }
            
            try {
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileChoose.getSelectedFile())));
                for (int i = 0; i < control.getLineCount(); i++) {
                    try {
                        writer.println(control.getText().substring(control.getLineStartOffset(i), control.getLineEndOffset(i)));
                    } catch (BadLocationException e) {
                        JOptionPane.showConfirmDialog(null, "Error reading text");
                    }
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(null, "Error writing to file", "Writing error", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
