/*
 * Collection of tools, getters and setters that are used by the
 * rest of the program, to eliminate duplication and promote simplicity.
 * Also, the main that loads the program begins here
 */

package ohcrypt;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.security.SecureRandom;
import java.util.Random;
import java.util.jar.JarOutputStream;


class ohcrypt
{
	// global variables
	static String magic = "ohcrypt";	// max 8 bytes
	static String version = "0.6.4";	// max 6 bytes
	
   public static boolean delfolder(File path) {
      File[] files = path.listFiles();
      for(int i=0; i<files.length; i++) {
         // recursive call
         if (files[i].isDirectory()) delfolder(files[i]);
         else files[i].delete();
      }
      return path.delete();
   }

   public static boolean pack(File encfile) throws Exception
   {
	    System.out.println("Packing...");
	    File myself = new File(ohcrypt.class.getProtectionDomain().getCodeSource().getLocation().getPath());

		// create temp dir
		File tmpdir = new File(encfile.getParentFile(), "tmp");
		if(!tmpdir.mkdir()) return false;
		
		zipper.unzip(myself, tmpdir); // extract jar
		
		encfile.renameTo(new File(tmpdir, "encfile"));	// move file inside tempdir
		
		// create the jar
		File extractablefile = new File(tmpdir.getParentFile(), encfile.getName() + ".jar");
		JarOutputStream jos = new JarOutputStream(new FileOutputStream(extractablefile));
		
		zipper.zip(tmpdir.listFiles(), jos);
		extractablefile.setExecutable(true);
		  
		// finish up
		jos.flush();
		jos.close();
		delfolder(tmpdir);
		
		return true;
   }

   public static boolean safedelete(File file, int iterations) throws Exception
   {
      byte[] rbytes = new byte[128];
      RandomAccessFile raf = new RandomAccessFile(file, "rws");
      Random r = new SecureRandom();
      for(int i=0; i<iterations; i++)
      {
         raf.seek(0);
         for(long k=0; k<(raf.length() / 128); k++) {
            r.nextBytes(rbytes);
            raf.write(rbytes);
         }
      }
      raf.close();
      return file.delete();
   }
   
   public static byte[] padRight(String s, int n) {
	   return String.format("%1$-" + n + "s", s).getBytes();
   }
   
   // getter for the magic, 8 bytes
   public static byte[] getMagic() {
	   return padRight(magic,8);
   }

   // getter for the version, 6 bytes
   public static byte[] getVersion() {
	   return padRight(version,6);
   }
   
   public static void main(String[] inargs) throws Exception {
	   try { 
	         // check for self-extractable file, this will crash if it doesn't exist
	         new File(ClassLoader.getSystemClassLoader().getResource("encfile").getFile());
	         // load minimal
	         minimal.load();
	      }
	      catch (NullPointerException npe) {
	         // run normally
	         System.err.println("No self-extracting file found");
	         gui.load();
	      }
   }
}