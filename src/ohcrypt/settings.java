package ohcrypt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

// TODO: use setters instead of one big savesettings method
public class settings
{
   static File settingsfile = new File("ohc_settings.ini");
   static int SAFEDELETE_ITERATIONS = 20, COMPRESSION_RATIO = 0;

static boolean savesettings(boolean delsource, boolean safedelete, boolean selfextract, int ratio)
   {
      try
      {
         FileOutputStream fos = new FileOutputStream(settingsfile);
         System.out.println("Settings changed");

         String stt = delsource ? "TRUE" : "FALSE";
         fos.write( ("delsource=" + stt + "\n").getBytes() );

         stt = safedelete ? "TRUE" : "FALSE";
         fos.write( ("safedelete=" + stt + "\n").getBytes() );

         stt = selfextract ? "TRUE" : "FALSE";
         fos.write( ("selfextract=" + stt + "\n").getBytes() );
         
         fos.write( ("ratio=" + ratio + "\n").getBytes() );
         
         fos.flush();
         fos.close();
         return true;
      }
      catch(Exception e) {
    	  e.printStackTrace();
    	  return false;
      }
	}

/**
 * @return the dELSOURCE
 */
public static boolean isDELSOURCE() {
	try
    {
       BufferedReader br = new BufferedReader(new FileReader(settingsfile));
       String line;
       while ( (line = br.readLine()) != null ) {
    	   if(line.equals("delsource=TRUE")) {
    		   br.close();
    		   return true;
    	   }
       }
       br.close();
       return false;
    }
	catch(Exception e) { 
		e.printStackTrace();
		return false;
	}
}

/**
 * @return the sAFEDELETE
 */
public static boolean isSAFEDELETE() {
	try
    {
       BufferedReader br = new BufferedReader(new FileReader(settingsfile));
       String line;
       while ( (line = br.readLine()) != null ) {
    	   if(line.equals("safedelete=TRUE")) {
    		   br.close();
    		   return true;
    	   }
       }
       br.close();
       return false;
    }
	catch(Exception e) { 
		e.printStackTrace();
		return false;
	}
}

/**
 * @return the pORTABLE
 */
public static boolean isPORTABLE() {
	try
    {
       BufferedReader br = new BufferedReader(new FileReader(settingsfile));
       String line;
       while ( (line = br.readLine()) != null ) {
    	   if(line.equals("selfextract=TRUE")) {
    		   br.close();
    		   return true;
    	   }
       }
       br.close();
       return false;
    }
	catch(Exception e) { 
		e.printStackTrace();
		return false;
	}
}

public static int getRatio() {
	try {
		BufferedReader br = new BufferedReader(new FileReader(settingsfile));
	       String line;
	       while ( (line = br.readLine()) != null ) {
	    	   if(line.startsWith("ratio=")) {
	    		   br.close();
	    		   return Integer.parseInt( line.substring(6) );
	    	   }
	       }
	       br.close();
	       return 0;
	    }
		catch(Exception e) { 
			e.printStackTrace();
			return 0;
		}
	}
}