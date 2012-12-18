package ohcrypt;
import java.io.*;
import java.util.jar.*;
import java.util.Enumeration;
import java.util.ArrayList;


class zipper
{
   static byte[] buffer = new byte[4096];
   
   // unzip an entire zip archive into the destined directory
   public static ArrayList<File> unzip(File zipfile, File outputfolder)  throws Exception
   {
      JarFile zip = new JarFile(zipfile);
      ArrayList<File> filelist = new ArrayList<File>();
     
      Enumeration<?> entries = zip.entries();
      while(entries.hasMoreElements())
      {
         JarEntry entry = (JarEntry) entries.nextElement();
         File unzipped = new File(outputfolder,entry.getName());
           
         if (entry.isDirectory() && !unzipped.exists()) {
            System.out.println("Creating directory: " + entry.getName());
            unzipped.mkdirs();
            continue;
         }
         else if (!unzipped.getParentFile().exists()) {
            System.out.println("Creating missing parent directory for: " + entry.getName());
            unzipped.getParentFile().mkdirs();
         }

         InputStream in = zip.getInputStream(entry);
         FileOutputStream fos = new FileOutputStream(unzipped);
         filelist.add(unzipped); // add to the list
         System.out.println("Unzipping: " + entry.getName());
         
         int count;
         while((count = in.read(buffer, 0, buffer.length)) != -1)
            fos.write(buffer, 0, count);

         // clean up
         fos.close();
         in.close();
      }
      zip.close();
      return filelist;
   }
   
   // unzip an specific file from a zip archive into the destined directory
   public static boolean unzip(File zipfile, File outputfolder, String filename)  throws Exception
   {
      JarFile zip = new JarFile(zipfile);
      InputStream in = null;
      FileOutputStream fos = null;
     
      Enumeration<?> entries = zip.entries();
      while(entries.hasMoreElements())
      {
         JarEntry entry = (JarEntry) entries.nextElement();
         if(new File(entry.getName()).getName().equals(filename)) {
        	 File unzipped = new File(outputfolder,filename);
	         
	         in = zip.getInputStream(entry);
	         fos = new FileOutputStream(unzipped);
	         System.out.println("Unzipping: " + filename);
	         
	         int count;
	         while((count = in.read(buffer, 0, buffer.length)) != -1)
	            fos.write(buffer, 0, count);
	         
	         // clean up
	         fos.close();
	         in.close();
         }
      }
            
      zip.close();
     
      return true;
   }
   
   // overloaded method
   public static void zip(File[] infiles, JarOutputStream jos) throws Exception
   {
      zip(infiles,"",jos);

      // clean up
      jos.flush();
      jos.close();
   }

   public static void zip(File[] infiles, String basefolder, JarOutputStream jos) throws Exception
   {
	  // set compression level
	  jos.setLevel(settings.getRatio());
	   
      for(int i=0; i<infiles.length; i++)
      {
         if(infiles[i].isDirectory()) {
            // recursive call for directories
            zip(infiles[i].listFiles(), infiles[i].getName() + "/", jos);
            continue;
         }

         String filepath = basefolder + infiles[i].getName();
         JarEntry entry = new JarEntry(filepath);
         jos.putNextEntry(entry);
         System.out.println("Zipping: " + filepath);

         FileInputStream fis = new FileInputStream(infiles[i]); // get stream

         int count;
         while((count = fis.read(buffer, 0, buffer.length)) != -1)
            jos.write(buffer, 0, count);
         fis.close();
      }
   }
}
