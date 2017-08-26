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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

public class NoteEditorWithFiles extends JFrame {
    
    public static JMenuBar bar = new JMenuBar();
    public static JMenu file = new JMenu("File");
    public static JMenuItem newItem = new JMenuItem("New");
    public static JMenuItem openItem = new JMenuItem("Open");
    public static JMenuItem saveItem = new JMenuItem("Save");
    public static JMenuItem exitItem = new JMenuItem("Exit");
    public static JFileChooser fileChoose = new JFileChooser();
    
    public static JMenu format = new JMenu("Format");
    public static JCheckBoxMenuItem bold = new JCheckBoxMenuItem("Bold", false);
    public static JCheckBoxMenuItem italic = new JCheckBoxMenuItem("Italic", false);
    public static JMenu size = new JMenu("Size");
    public static ButtonGroup sizeGroup = new ButtonGroup();
    public static JRadioButtonMenuItem size12 = new JRadioButtonMenuItem();
    public static JRadioButtonMenuItem size18 = new JRadioButtonMenuItem();
    public static JRadioButtonMenuItem size24 = new JRadioButtonMenuItem();
    
    public static JScrollPane editorScrollPane = new JScrollPane();
    public static JTextArea control = new JTextArea();
    
    public static JMenu help = new JMenu();
    public static JMenuItem about = new JMenuItem();
    
    public static Font font = new Font("Monospaced", Font.PLAIN, 14);
    public static Color carotColor = new Color(0, 255, 0);
    public static Color foreground = new Color(0, 255, 0);
    public static Color background = new Color(0, 0, 0);
    /*
    public static Color foreground = new Color(9, 34, 19);
    public static Color background = new Color(255, 244, 211);*/
    
    public static double widthPercentage = .6;
    public static double heightPercentage = .92;

    public static void main(String[] args) {
    	for (String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
        	System.out.println(s);
        }
    	
        new NoteEditorWithFiles();
    }
 
    public NoteEditorWithFiles() {
        setResizable(false);
        setTitle("Note editor");
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = widthPercentage * screenSize.getWidth();
        double height = heightPercentage * screenSize.getHeight();
        
        getContentPane().setBackground(background);
        getContentPane().setLayout(new GridBagLayout());
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitForm();
            }
        });
        
        GridBagConstraints cont = new GridBagConstraints();
        cont.gridx = 0;
        cont.gridy = 0;
        
        control.setFont(font);
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
        editorScrollPane.setFont(font);
        getContentPane().add(editorScrollPane, cont);
        
        //setJMenuBar(bar);
        bar.add(file);
        file.add(newItem);
        newItem.setAccelerator(KeyStroke.getKeyStroke('N', Event.CTRL_MASK));
        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPressed();
            }
        });
        file.add(openItem);
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openButtonPressed();
            }
        });
        file.add(saveItem);
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveButtonPressed();
            }
        });
        file.addSeparator();
        file.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitForm();
            }
        });
        
        class FontListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                fontPressed();
            }
        }
        
        FontListener listener = new FontListener();
        
        bar.add(format);
        format.add(bold);
        bold.setAccelerator(KeyStroke.getKeyStroke('B', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        bold.addActionListener(listener);
        format.add(italic);
        italic.addActionListener(listener);
        italic.setAccelerator(KeyStroke.getKeyStroke('I', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        format.add(size);
                
        size12.setText("Small");
        size12.addActionListener(listener);
        size12.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
        size.add(size12);
        sizeGroup.add(size12);
        
        size18.setText("Medium");
        size18.addActionListener(listener);
        size18.setAccelerator(KeyStroke.getKeyStroke('M', Event.CTRL_MASK));
        size.add(size18);
        sizeGroup.add(size18);
        
        size24.setText("Large");
        size24.addActionListener(listener);
        size24.setAccelerator(KeyStroke.getKeyStroke('L', Event.CTRL_MASK));
        size.add(size24);
        sizeGroup.add(size24);
        
        bar.add(help);
        help.setText("Help");
        help.add(about);
        about.setText("About");
        
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aboutPressed();
            }
        });
        
        try {
            Scanner fileScanner = new Scanner(new File("config.ini"));
            bold.setSelected(Boolean.valueOf(fileScanner.next()));
            italic.setSelected(Boolean.valueOf(fileScanner.next()));
            int size = Integer.valueOf(fileScanner.next());
            switch (size) {
            case 1: size12.doClick();
            break;
            case 2: size18.doClick();
            break;
            case 3: size24.doClick();
            break;
            default: JOptionPane.showConfirmDialog(null, "Error in reading file");
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            bold.setSelected(false);
            italic.setSelected(false);
            size18.setSelected(true);
        }
        
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
        setUndecorated(true);
        
        pack();
        setLocationRelativeTo(null);

        setVisible(true);
    }
    
    private void newPressed() {
        if (JOptionPane.showConfirmDialog(null, "Do you want to start a new note?", 
                "Question", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)
                == JOptionPane.YES_OPTION) {
            control.setText("");
        }
    }
    
    private void fontPressed() {
        int size;
        int fontStyle = Font.PLAIN;
        
        if (size12.isSelected()) {
            size = 12;
        }
        
        else if (size18.isSelected()) {
            size = 18;
        }
        
        else {
            size = 24;
        }
        
        if (bold.isSelected()) {
            fontStyle += Font.BOLD;
        }
        
        if (italic.isSelected()) {
            fontStyle += Font.ITALIC;
        }
        
        control.setFont(font);
        //control.setFont(new Font("Monospaced", fontStyle, size));
    }
    
    private void aboutPressed() {
        String message = "This is a note editor made by Daniel."
                + "\nLast updated on 8/23/17";
        JOptionPane.showConfirmDialog(null, message, "Information", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exitForm() {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("config.ini")));
            writer.println(bold.isSelected());
            writer.println(italic.isSelected());
            
            if (size12.isSelected()) {
                writer.println("1");
            }
            
            else if (size18.isSelected()) {
                writer.println("2");
            }
            
            else if (size24.isSelected()) {
                writer.println("3");
            }
            
            writer.flush();
            writer.close();
            
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "Error writing to config file");
        }
        
        finally {
            System.exit(0);
        }
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
