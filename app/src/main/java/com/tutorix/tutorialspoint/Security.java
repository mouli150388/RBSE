package com.tutorix.tutorialspoint;

import android.net.Uri;

import org.apache.commons.codec.binary.Base64;

import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Security {
    public static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {

        }
        return new String(Objects.requireNonNull(Base64.encodeBase64(crypted)));
    }

    public static byte[] encryptString(String input, String key) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {

        }
        return (crypted);
    }

    public static String getDecryptKey(String data,String key)
    {

        byte[]decode= Base64.decodeBase64(Uri.decode(data).getBytes());
        //byte[]decode= data.getBytes();

        try {
            SecretKeySpec decodedKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, decodedKeySpec);
            return new String(cipher.doFinal(decode));

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    public static String getDecryptKey2(String data,String key)
    {

        byte[]decode= Base64.decodeBase64((data).getBytes());
        //byte[]decode= data.getBytes();

        try {
            SecretKeySpec decodedKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, decodedKeySpec);
            return new String(cipher.doFinal(decode));

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    public static String getDecryptKeyNotes(byte[] data,String key)
    {


        try {
            SecretKeySpec decodedKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, decodedKeySpec);
            return new String(cipher.doFinal(data));

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }


    public static String decryptnew(String input, String key) {
        byte[] crypted = null;
        // String  ss=new String(Objects.requireNonNull(Base64.decodeBase64(input.getBytes())));

        try {




            //final IvParameterSpec ivSpecForData = new IvParameterSpec(ivBytes);

            SecretKeySpec decodedKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, decodedKeySpec);


            String decryptString = new String(cipher.doFinal(input.getBytes()), "UTF-8");
            return decryptString;
            // I have the error here //



        } catch (Exception e) {
            e.printStackTrace();
        }




        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {

        }
        return crypted.toString();
    }

    public static String decrypt(String input, String key) {
        byte[] crypted = null;





        input=new String(Objects.requireNonNull(Base64.decodeBase64(input.getBytes())));
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skey);



            byte[] plainText = new byte[cipher.getOutputSize(input.length())];


            int ptLength = cipher.update(input.getBytes(), 0, input.length(), plainText, 0);



            ptLength += cipher.doFinal(plainText, ptLength);




            String s=new String(plainText);

            return s;
            //Log.v("Key data","key Data "+s);

            // SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            // Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");


            //return new String(crypted, "UTF-8");
        } catch (Exception e) {


        }
        return "";
    }




    public static String encryptFileKey(String original_key, String secret_key) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(secret_key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(original_key.getBytes());
        } catch (Exception e) {

        }
        return new String(Objects.requireNonNull(Base64.encodeBase64(crypted)));
    }

   /* public static String readKey()
    {
        String myData="";
        try {



            File f= new File(AppConfig.getSdCardPath("1"));


            File file=new File(f,"/1/1/file.key");
            FileInputStream fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in, "ISO-8859-15"));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myData;
    }*/
    /*public static void  createFile()
    {
        File f= new File(AppConfig.getSdCardPath("1"));
        File file=new File(f,"/1/1");
        if(file.isDirectory())
        {
            File ff=new File(file,"des_file.key");
            FileOutputStream stream = null;
            try {
                // file.createNewFile();
                stream = new FileOutputStream(ff);
                stream.write(decryptkey().getBytes());
                stream.close();
            }  catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }*/


    public static String encryptkey()
    {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec("1234567891234567".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal("žºÔT»œ¾ä©&¥ŸW\"Å&".getBytes());
        } catch (Exception e) {

        }
        return new String(Objects.requireNonNull(android.util.Base64.encodeToString(crypted,android.util.Base64.DEFAULT)));
    }
    public static String decryptkey()
    {
        String s;

        s=getString();

        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec("1234567891234567".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            crypted = cipher.doFinal(s.getBytes());
        } catch (Exception e) {

        }


        return new String(Objects.requireNonNull(Base64.encodeBase64(crypted)));
    }
    public static String getString()
    {
        return android.util.Base64.decode(encryptkey(),android.util.Base64.DEFAULT).toString();

    }
}
