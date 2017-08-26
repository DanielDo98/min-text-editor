import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

public class NoteEditorWithFiles extends JFrame {
    
    public static JFileChooser fileChoose = new JFileChooser();
    
    public static JScrollPane editorScrollPane = new JScrollPane();
    public static JTextArea control = new JTextArea();
    
    public static int fontSize = 14;
    public static String fontName = "Monospaced";
    public static Color carotColor = new Color(0, 255, 0);
    public static Color foreground = new Color(0, 255, 0);
    public static Color background = new Color(0, 0, 0);
    /*
    public static Color foreground = new Color(9, 34, 19);
    public static Color background = new Color(255, 244, 211);*/
    
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
        
        getContentPane().setBackground(background);
        getContentPane().setLayout(new GridBagLayout());
        
        GridBagConstraints cont = new GridBagConstraints();
        cont.gridx = 0;
        cont.gridy = 0;
        
        control.setFont(new Font(fontName, Font.PLAIN, fontSize));
        control.setForeground(foreground);
        control.setBackground(background);
        control.setCaretColor(carotColor);
        control.setLineWrap(true);
        control.setWrapStyleWord(true);
        control.setBorder(null);
        
        editorScrollPane.setPreferredSize(new Dimension((int) width, (int) height));
        editorScrollPane.setViewportView(control);
        editorScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        editorScrollPane.setBorder(null);
        editorScrollPane.setFont(new Font(fontName, Font.PLAIN, fontSize));
        getContentPane().add(editorScrollPane, cont);
        
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
            	fontPressed(fontName, fontSize);
            }
        };
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                                    "fontIncrease");
        control.getActionMap().put("fontIncrease",
                                     fontIncrease);
        
        Action fontDecrease = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	fontSize -= fontSize <= 20 ? 1 : 2;
                fontPressed(fontName, fontSize);
            }
        };
        control.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                                    "fontDecrease");
        control.getActionMap().put("fontDecrease",
                                     fontDecrease);
        
        /*
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aboutPressed();
            }
        });*/
        
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
        //setUndecorated(true);
        
        pack();
        setLocationRelativeTo(null);

        setVisible(true);
    }
    
    private void newPressed() {
    	control.setText("");
    }
    
    private void fontPressed(String newFont, int newSize) {
    	control.setFont(new Font(newFont, Font.PLAIN, newSize));
    }
    
    private void aboutPressed() {
        String message = "This is a note editor made by Daniel."
                + "\nLast updated on 8/26/17";
        JOptionPane.showConfirmDialog(null, message, "Information", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
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
