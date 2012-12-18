package ohcrypt;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/** minimal version, portable mode */
public class minimal
{
    static JFrame mainframe;
	static JFrame keepalive;
    static JPanel mainpanel;
    static JLabel pwdlabel;
	static JLabel noticelabel;
    static JPasswordField pwdfield;
    static JButton decryptbutton;

    public static void load() throws Exception
    {
        // Look and Feel
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch(Exception e) { e.printStackTrace(); }
        
        File minimal = new File(ohcrypt.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        final File folder = new File(".");
        
        // create temporary directory
        File tmpdir = new File("tmp");
        tmpdir.mkdirs();

        // unzip encfile
        zipper.unzip(minimal, tmpdir);

        // locate encrypted file
        final File encfile = new File(tmpdir, "encfile");
    
        // Look and Feel
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch(Exception e) { e.printStackTrace(); }
      
        // main frame
        mainframe = new JFrame("ohcrypt");
        mainframe.setSize(350,120);
        mainframe.setResizable(false);
        mainframe.setLocationRelativeTo(null);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // load icon
        final ImageIcon icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/icon.png"));
        mainframe.setIconImage(icon.getImage());

        // main panel
        mainpanel = new JPanel();
        mainframe.getContentPane().add(mainpanel, BorderLayout.CENTER);

        // password label
        pwdlabel = new JLabel("Password:");
        mainpanel.add(pwdlabel);

        // password field
        pwdfield = new JPasswordField(15);
        pwdfield.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                decryptbutton.doClick();
            }
        });

        mainpanel.add(pwdfield);

        // decrypt button
        decryptbutton = new JButton("Decrypt");
        decryptbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // parse information
                final char[] pwd = pwdfield.getPassword();
                pwdfield.setText(null);
                // show progress window and call decryption method
                //progressdialog.setVisible(true);
                Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() { public void run() {
                    try 
                    {
                        File zipfile = new File(folder, "temp.zip");
                        crypto.decrypt(pwd, encfile, zipfile);
                        // create a list of the unencrypted files
                        ArrayList<File> filelist = zipper.unzip(zipfile, folder);
                        // cleanup
                        zipfile.delete();
                        ohcrypt.delfolder(encfile.getParentFile());
                        // delete decrypted files if program is closed
                        for (File decrypted : filelist) decrypted.deleteOnExit();
                        //progressdialog.dispose();
                        JOptionPane.showMessageDialog(mainframe, "Operation completed", "Operation Completed", JOptionPane.INFORMATION_MESSAGE);
                        keepalive.setVisible(true);
                        mainframe.dispose();
                    }
                    catch (Exception e) 
                    {
                         //progressdialog.dispose();
                         e.printStackTrace();
                         JOptionPane.showMessageDialog(mainframe, "Operation failed" + "\n" + e, "Operation failed", JOptionPane.ERROR_MESSAGE);
                    }
                }});
            }
        });
        mainpanel.add(decryptbutton);

        // keep alive frame
        keepalive = new JFrame("ohcrypt");
        keepalive.setSize(300,150);
        keepalive.setIconImage(icon.getImage());
        keepalive.setLocationRelativeTo(mainframe);
        keepalive.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // keep alive label
        String noticest = "<html>Keep this window open to access your decrypted files. " +
                          "Once you close this window, your files will be automatically deleted.";

        noticelabel = new JLabel(noticest);
        keepalive.getContentPane().add(noticelabel, BorderLayout.CENTER); 
        
        mainframe.setVisible(true);
    }
}
