package ohcrypt;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

class crypto
{
   // header variables declaration
   static byte[] OHCRYPT_MAGIC = new byte[8];
   static byte[] OHCRYPT_VERSION = new byte[6];
   static byte[] OHCRYPT_ALGORITHM = new byte[8];
   static byte[] SALT = new byte[20];

   public static void encrypt(char[] pwd, String algorithm, File sourcefile, File outputfile) throws Exception
   {
      // parse info
      OHCRYPT_MAGIC = ohcrypt.getMagic();
      OHCRYPT_VERSION = ohcrypt.getVersion();
      OHCRYPT_ALGORITHM = ohcrypt.padRight(algorithm, 8);

      // generate salt
      Random r = new SecureRandom();
      r.nextBytes(SALT);

      // hash the password
      SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      PBEKeySpec keyspec = new PBEKeySpec(pwd, SALT, 128, 128);
      SecretKey hashedpwd = keyfactory.generateSecret(keyspec);

      // generate secret key
      SecretKey skey = new SecretKeySpec(hashedpwd.getEncoded(), algorithm);

      // start the cipher
      Cipher encrypter = Cipher.getInstance(algorithm);
      encrypter.init(Cipher.ENCRYPT_MODE, skey);

      // write header
      FileOutputStream fos = new FileOutputStream(outputfile);
      fos.write(OHCRYPT_MAGIC); // magic
      fos.write(OHCRYPT_VERSION); // version
      fos.write(OHCRYPT_ALGORITHM);
      fos.write(SALT); // salt

      // set up the input streams
      FileInputStream fis = new FileInputStream(sourcefile);
      CipherInputStream cis = new CipherInputStream(new BufferedInputStream(fis), encrypter);

      // read the source file
      int count;
      byte[] filebytes = new byte[4096];
      while((count = cis.read(filebytes, 0, filebytes.length)) != -1)
         fos.write(filebytes, 0, count);

      // finish up
      cis.close();
      fis.close();
      fos.close();
  }

  public static void decrypt(char[] pwd, File sourcefile, File outputfile) throws Exception
  {
      FileInputStream fis = new FileInputStream(sourcefile);

      // read header
      fis.read(OHCRYPT_MAGIC);
      fis.read(OHCRYPT_VERSION);
      fis.read(OHCRYPT_ALGORITHM);
      fis.read(SALT);

      // parse info
      String algorithm = (new String(OHCRYPT_ALGORITHM)).trim();

      // hash the password
      SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      PBEKeySpec keyspec = new PBEKeySpec(pwd, SALT, 128, 128);
      SecretKey hashedpwd = keyfactory.generateSecret(keyspec);

      // generate secret key
      SecretKey skey = new SecretKeySpec(hashedpwd.getEncoded(), algorithm);

      // start the cipher
      Cipher decrypter = Cipher.getInstance(algorithm);
      decrypter.init(Cipher.DECRYPT_MODE, skey);

      // set up the output streams
      FileOutputStream fos = new FileOutputStream(outputfile);
      CipherOutputStream cos = new CipherOutputStream(new BufferedOutputStream(fos), decrypter);

      // read the source file and decrypt it
      int count;
      byte[] filebytes = new byte[4096];
      while((count = fis.read(filebytes)) != -1) 
         cos.write(filebytes, 0, count);

      // finish up
      fis.close();
      cos.flush();
      cos.close();
  }
}
