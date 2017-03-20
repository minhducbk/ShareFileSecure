package Crypto;
import javax.crypto.spec.*;
import javax.swing.JFileChooser;

import Utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.*;
import javax.crypto.*;
  
public class RC2
{
   public static void main(String []args) throws Exception {
	   JFileChooser chooser = new JFileChooser();
		File f;
		chooser.setDialogTitle("Choose a file to encrypt");
		chooser.setVisible(true);
		try {
			f = new File(new File("").getCanonicalPath());
			chooser.setSelectedFile(f);
			chooser.showOpenDialog(null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
	    File curFile = chooser.getSelectedFile();
	    byte[] toEncrypt = FileUtils.readBinary(curFile.getAbsolutePath());
  
      System.out.println("Encrypting...");
      System.out.println("toEncrypt file size: "+ toEncrypt.length);
      Integer length_padded = (int)toEncrypt.length;
	  if (length_padded % 8 != 0)
  		length_padded = ((int)(length_padded / 8) + 1) * 8;
      byte[] encrypted = encrypt(toEncrypt, "password");
      FileUtils.writeBinary(encrypted, curFile.getAbsolutePath()+".encrypted");
      System.out.println("length padded: "+length_padded);
      System.out.println("encrypted file size: "+ encrypted.length);
      System.out.println("Decrypting...");
      byte[] decrypted = decrypt(encrypted, "password");
      FileUtils.writeBinary(decrypted, curFile.getAbsolutePath()+".encrypted.decrypted");
      System.out.println("decrypted file size: "+ decrypted.length);
      System.out.println("192.168.1.103:14964 sent a file Client_ver1.zip (390056 bytes)".split("\\(")[1].split(" ")[0]);
   }
  
   public static byte[] encrypt(byte[] mybytearray, String key) throws Exception {
      // create a binary key from the argument key (seed)
      SecureRandom sr = new SecureRandom(key.getBytes());
      KeyGenerator kg = KeyGenerator.getInstance("RC2");
      kg.init(sr);
      SecretKey sk = kg.generateKey();
  
      // create an instance of cipher
      Cipher cipher = Cipher.getInstance("RC2");
  
      // initialize the cipher with the key
      cipher.init(Cipher.ENCRYPT_MODE, sk);
  
      // enctypt!
      byte[] encrypted = cipher.doFinal(mybytearray);
  
      return encrypted;
   }
  
   public static byte[] decrypt(byte[] toDecrypt, String key) throws Exception {
      // create a binary key from the argument key (seed)
      SecureRandom sr = new SecureRandom(key.getBytes());
      KeyGenerator kg = KeyGenerator.getInstance("RC2");
      kg.init(sr);
      SecretKey sk = kg.generateKey();
  
      // do the decryption with that key
      Cipher cipher = Cipher.getInstance("RC2");
      cipher.init(Cipher.DECRYPT_MODE, sk);
      byte[] decrypted = cipher.doFinal(toDecrypt);
  
      return decrypted;
   }
}