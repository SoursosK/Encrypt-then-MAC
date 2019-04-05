
package authenticatedencryption;

import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

public class AuthenticatedEncryption {

    public static void main(String[] args) {
        try {
            String plainText = "The Magic Words are Squeamish Ossifrage";
            
            //create keyGenerator AES-CBC
            KeyGenerator keyGenAES = KeyGenerator.getInstance("AES");
            keyGenAES.init(256);                                                    //initialize the generator for key size 256
            
            //create key AES-CBC
            SecretKey key1 = keyGenAES.generateKey();
            String encodedKey1 = Base64.getEncoder().encodeToString(key1.getEncoded());  //encoding + printing of the key in base64 format, testing purposes
            System.out.println("Secret AES Key1: " + encodedKey1);
            
            //Encryption
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");                  //get the type of cipher we will be using / provider-specific values for the mode
            c.init(Cipher.ENCRYPT_MODE, key1);                                      //initialise the cipher for encryption with the given key
            byte[] encValue = c.doFinal(plainText.getBytes());                      //encrypt the plaintext
            String encryptedValue = Base64.getEncoder().encodeToString(encValue);         //encoding + printing of the cipherText in base64 format, testing purposes
            System.out.println("CipherText:" + encryptedValue);
            
            //Decryption                                                                    //decryption + printing of the decrypted cipherText (aka plainText) in base64 format, testing purposes
//            c.init(Cipher.DECRYPT_MODE, key1);
//            byte[] decValue = c.doFinal(encValue);
//            String decryptedVal = new String(decValue);
//            System.out.println("Decrypted PlainText: " + decryptedVal + "\n");
            
            
            //create keyGenerator HmacSHA256
            KeyGenerator keyGenHmacSHA256 = KeyGenerator.getInstance("HmacSHA256");
            
            //create key HmacSha256
            SecretKey key2 = keyGenHmacSHA256.generateKey();
            String encodedKey2 = Base64.getEncoder().encodeToString(key2.getEncoded());  //encoding + printing of the key2 in base64 format, testing purposes
            System.out.println("\nSecret Hmac Key: " + encodedKey2);
            
            //Hashing
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key2);
            byte[] digest = mac.doFinal(encValue);                                  //hash the ciphertext( IV is included in the ciphertext, it's the first block)
            String macVal = Base64.getEncoder().encodeToString(digest);           //encoding + printing the MAC in base64 format, testing purposes
            System.out.println("Mac: " + macVal);
            
            
            //Creating a combined byte array encValue(ciphertext) + digest(mac)
            byte[] finalText = new byte[encValue.length + digest.length];
            
            for (int i = 0; i < finalText.length; ++i)
                finalText[i] = i < encValue.length ? encValue[i] : digest[i - encValue.length];
            
            String encfinalText = Base64.getEncoder().encodeToString(finalText);
            System.out.println("\nCipherText + Mac: " + encfinalText);
        
        } catch (Exception ex) {
            Logger.getLogger(AuthenticatedEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
