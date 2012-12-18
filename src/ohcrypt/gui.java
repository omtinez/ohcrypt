package ohcrypt;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Executor;
import java.util.jar.JarOutputStream;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class gui
{
   // GUI components
   static JFrame mainframe;
   static JDialog progressdialog;
   static JTabbedPane tabbedpane;
   static JPanel mainpanel, algorithmspanel, filespanel, settingspanel, progresspanel, aboutpanel;
   static JRadioButton[] rbutton = new JRadioButton[4];
   static ButtonGroup bgroup;
   static JComboBox<?> box;
   static JButton openbutton, removebutton, encryptbutton, decryptbutton, savebutton;
   static JCheckBox delsourcebutton, safedeletebutton, portablebutton;
   static JList<String> list;
   static DefaultListModel<String> listmodel;
   static JLabel pwdlabel, compressionlabel;
   static JSlider compressionslider;
   static JSeparator separator;
   static JPasswordField pwdfield;
   static JProgressBar progressbar;

   // program variables
   static String algorithm;

   public static void load() throws Exception
   {
      // Look and Feel
      try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
      catch(Exception e) { e.printStackTrace(); }

      // main frame
      mainframe = new JFrame("ohcrypt");
      mainframe.setSize(400,350);
      mainframe.setResizable(false);
      mainframe.setLocationRelativeTo(null);
      mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // load icons
      final ImageIcon icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/icon.png"));
      mainframe.setIconImage(icon.getImage());

      // containers
      tabbedpane = new JTabbedPane();
      mainframe.add(tabbedpane);
      
      // main panel
      mainpanel = new JPanel();
      tabbedpane.add("Main", mainpanel);

      // layouts
      mainpanel.setLayout(new GridBagLayout());
      final GridBagConstraints c = new GridBagConstraints();
      c.anchor = GridBagConstraints.LINE_START;
      c.insets = new Insets(5,0,5,0);

      // settings checkbox
      delsourcebutton = new JCheckBox("Delete source file(s)", settings.isDELSOURCE());
      delsourcebutton.setToolTipText("Source file(s) will be deleted after the program is closed");
      safedeletebutton = new JCheckBox("Secure deletion of temp files", settings.isSAFEDELETE());
      safedeletebutton.setToolTipText("Temporary files will be overwritten several times with random data before they are deleted");
      portablebutton = new JCheckBox("Generate portable encrypted files", settings.isPORTABLE());
      portablebutton.setToolTipText("Encrypted files are embedded into stand-alone ohcrypt minimal version ready to be decrypted (experimental)");
      
      // settings compression slider
      compressionslider = new JSlider(0,9,0);
      compressionslider.setToolTipText("Ratio that will be used to compress the encrypted file using zip (experimental)");
      
      // action listeners
      delsourcebutton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
             // save the settings
             settings.savesettings(
             		delsourcebutton.isSelected(),
             		safedeletebutton.isSelected(),
             		portablebutton.isSelected(),
             		compressionslider.getValue());
       }});
      safedeletebutton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
             // save the settings
             settings.savesettings(
             		delsourcebutton.isSelected(),
             		safedeletebutton.isSelected(),
             		portablebutton.isSelected(),
             		compressionslider.getValue());
       }});
      portablebutton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
             // save the settings
             settings.savesettings(
             		delsourcebutton.isSelected(),
             		safedeletebutton.isSelected(),
             		portablebutton.isSelected(),
             		compressionslider.getValue());
       }});
      compressionslider.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent ce) {
			// save the settings
			settings.savesettings(
             		delsourcebutton.isSelected(),
             		safedeletebutton.isSelected(),
             		portablebutton.isSelected(),
             		compressionslider.getValue());
      }});
      

      //settings panel
      settingspanel = new JPanel(new GridLayout(0,1));
      settingspanel.add(delsourcebutton);
      settingspanel.add(safedeletebutton);
      settingspanel.add(portablebutton);
      //settingspanel.add(savebutton);
      settingspanel.add(new JLabel("Compression ratio:"));
      settingspanel.add(compressionslider);
      tabbedpane.add("Settings", settingspanel);

      // about item
      final String aboutst = 
         
         "<html><b>OhCrypt Encryption Tool v. " + "0.6.3" + "</b>" +
         "<p>" +	"<p>" +
         "Developed by: omtinez" +
         "<p>" +	"<p>" +
         "omartinez1@malone.edu" +
         "<p>" +	"<p>" +
         "Licensed Under GPL" +
         "<p>" + "<p>" +
         "<a href=sourceforge.net/p/ohcrypt>sourceforge.net/p/ohcrypt</a></html>";
      
      // about panel
      aboutpanel = new JPanel();
      aboutpanel.add(new JLabel(aboutst));
      tabbedpane.add("About", aboutpanel);

      // openbutton
      openbutton = new JButton("Open");
      openbutton.setEnabled(true);
      openbutton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            JFileChooser filechooser = new JFileChooser();
            filechooser.setDialogTitle("Open File");
            filechooser.setMultiSelectionEnabled(true);
            filechooser.showOpenDialog(mainframe);
            for(int i=0; i<filechooser.getSelectedFiles().length; i++)
            {
               // if element has not been added before
               if(!listmodel.contains(filechooser.getSelectedFiles()[i].toString()))
                  listmodel.addElement(filechooser.getSelectedFiles()[i].toString());
            }
      }});
      c.gridwidth = 1;
      c.fill = GridBagConstraints.NONE;
      mainpanel.add(openbutton, c);

      // files panel
      filespanel = new JPanel(new GridLayout(0,1));
      c.gridx = 1;
      c.gridy = 0;
      c.gridwidth = 3;
      c.gridheight = 3;
      c.ipady = 80;
      c.fill = GridBagConstraints.BOTH;
      // add it within a scroll pane
      mainpanel.add(new JScrollPane(filespanel),c);
      
      // file list
      listmodel = new DefaultListModel<String>();
      list = new JList<String>(listmodel);
      list.setToolTipText("Double click to delete files from list");
      list.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent me) {
            if(me.getClickCount() == 2)
            { // if double click occurs
               int index = list.getSelectedIndex();
               listmodel.remove(index);
            }
         }
         // to avoid compilation errors
         public void mouseExited(MouseEvent me) {}
         public void mouseEntered(MouseEvent me) {}
         public void mouseReleased(MouseEvent me) {}
         public void mousePressed(MouseEvent me) {}
      });
      filespanel.add(list);

      // algorithms panel
      algorithmspanel = new JPanel();
      algorithmspanel.setBorder(BorderFactory.createTitledBorder("Encryption Algorithm"));
      c.anchor = GridBagConstraints.LINE_START;
      c.gridwidth = 4;
      c.gridheight = 1;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.ipady = 0;
      c.gridx = 0;
      c.gridy += 3;
      mainpanel.add(algorithmspanel, c);

      // radio buttons
      rbutton[0] = new JRadioButton("AES",true);
      rbutton[1] = new JRadioButton("Blowfish");
      rbutton[2] = new JRadioButton("Serpent");
      rbutton[2].setEnabled(false); // temp
      rbutton[3] = new JRadioButton("Twofish");
      rbutton[3].setEnabled(false); // temp

      // button group
      bgroup = new ButtonGroup();
      for(int i=0; i<rbutton.length; i++) 
      {
         bgroup.add(rbutton[i]);
         algorithmspanel.add(rbutton[i]);
      }

      // password label
      pwdlabel = new JLabel("Passwd:");
      c.ipady = 0;
      c.gridx = 0;
      c.gridy += 1;
      mainpanel.add(pwdlabel, c);

      // password field
      pwdfield = new JPasswordField();
      c.gridwidth = 3;
      c.gridx = 1;
      mainpanel.add(pwdfield, c);

      // separator
      separator = new JSeparator();
      c.gridx = 0;
      c.gridy += 1;
      c.gridwidth = 4;
      mainpanel.add(separator, c);

      // encryptbutton
      encryptbutton = new JButton("Encrypt");
      encryptbutton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            // parse information
            final char[] pwd = pwdfield.getPassword();
            pwdfield.setText(null);
            // check if all needed information is ready
            if(listmodel.getSize() > 0 && pwd.length != 0) 
            {
               // get algorithm name
               for(int i=0; i<rbutton.length; i++)
                   /** tricornio aqui tambien? */
                  if(rbutton[i].isSelected()) algorithm = rbutton[i].getText();
               // select output file through a save file dialog
               JFileChooser filesaver = new JFileChooser();
               filesaver.setDialogTitle("Select Destination File");
               filesaver.showSaveDialog(mainframe);
               final File outputfile = filesaver.getSelectedFile();
               final File outputfolder = outputfile.getParentFile();
               // get input files
               final File[] sourcefiles = new File[listmodel.getSize()];
               Object[] temp = listmodel.toArray();
               for(int i=0; i<sourcefiles.length; i++) sourcefiles[i] = new File(temp[i].toString());
               // show progress window and call encryption method
               progressdialog.setVisible(true);
               Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();
               executor.execute(new Runnable() { public void run() {
                  try 
                  {
                     File zipfile = new File(outputfolder, "temp.zip");
                     JarOutputStream jos = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(zipfile)));
                     zipper.zip(sourcefiles, jos);
                     crypto.encrypt(pwd, algorithm, zipfile, outputfile);
                     // clean up
                     jos.flush();
                     jos.close();
                     // apply settings
                     if(settings.isPORTABLE()) ohcrypt.pack(outputfile);
                     if(settings.isSAFEDELETE()) ohcrypt.safedelete(zipfile, settings.SAFEDELETE_ITERATIONS);
                     else zipfile.delete();
                     if(settings.isDELSOURCE()) 
                        for(int i=0; i<sourcefiles.length; i++) sourcefiles[i].delete();
                     progressdialog.dispose();
                     JOptionPane.showMessageDialog(mainframe, "Operation completed", "Operation Completed", JOptionPane.INFORMATION_MESSAGE);
                     listmodel.clear();
                  }
                  catch (Exception e) 
                  {
                     e.printStackTrace();
                     progressdialog.dispose();
                     JOptionPane.showMessageDialog(mainframe, "Operation failed" + "\n" + e, "Operation failed", JOptionPane.ERROR_MESSAGE);
                  }
               }});
            }
         }
      });

      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy += 1;
      c.fill = GridBagConstraints.NONE;
      mainpanel.add(encryptbutton, c);

      // decryptbutton
      decryptbutton = new JButton("Decrypt");
      decryptbutton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            // parse information
            final char[] pwd = pwdfield.getPassword();
            pwdfield.setText(null);
            // check if all needed information is ready
            if(listmodel.getSize() > 0 && pwd.length != 0) 
            {
               // select output folder through a open file dialog
               JFileChooser folderchooser = new JFileChooser();
               folderchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
               folderchooser.setDialogTitle("Select Destination Folder");
               folderchooser.showOpenDialog(mainframe);
               final File outputfolder = folderchooser.getSelectedFile();
               final File[] sourcefiles = new File[listmodel.getSize()];
               Object[] temp = listmodel.toArray();
               for(int i=0; i<sourcefiles.length; i++) sourcefiles[i] = new File(temp[i].toString());
               // show progress window and call decryption method
               progressdialog.setVisible(true);
               Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();
               executor.execute(new Runnable() { public void run() {
                  try {
                     File zipfile = new File(outputfolder, "temp.zip");
                     for(int i=0; i<sourcefiles.length; i++) {
                        crypto.decrypt(pwd, sourcefiles[i], zipfile);
                        zipper.unzip(zipfile, outputfolder);
                     }
                     // cleanup
                     if(settings.isSAFEDELETE()) ohcrypt.safedelete(zipfile, settings.SAFEDELETE_ITERATIONS);
                     else zipfile.delete();
                     if(settings.isDELSOURCE()) 
                        for(int i=0; i<sourcefiles.length; i++) sourcefiles[i].delete();
                     progressdialog.dispose();
                     JOptionPane.showMessageDialog(mainframe, "Operation completed", "Operation Completed", JOptionPane.INFORMATION_MESSAGE);
                     listmodel.clear();
                  }
                  catch (Exception e) {
                     progressdialog.dispose();
                     e.printStackTrace();
                     JOptionPane.showMessageDialog(mainframe, "Operation failed" + "\n" + e, "Operation failed", JOptionPane.ERROR_MESSAGE);
                  }
               }});
            }
      }});

      c.gridx = 1;
      mainpanel.add(decryptbutton, c);

      // progress window 
      progressdialog = new JDialog(mainframe, "Working... ");
      progressdialog.setSize(300,80);
      progressdialog.setLocationRelativeTo(mainframe);
      progressdialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

      // progress bar
      progressbar = new JProgressBar(0,100);
      progressbar.setIndeterminate(true);

      // cancel button
      JButton cancelbutton = new JButton("Cancel");
      cancelbutton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            System.exit(0);
      }});

      // progress panel
      progresspanel = new JPanel();
      progresspanel.add(progressbar);
      progresspanel.add(cancelbutton);
      progressdialog.getContentPane().add(progresspanel, BorderLayout.CENTER);

      // display everything
      mainframe.setVisible(true);
      //progressdialog.setVisible(true);
   }
}
