/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getextip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.json.simple.JSONObject;

import javax.crypto.*;



//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 *
 * @author volodymyr
 */
public class GetExtIP {

    private static final String USER_AGENT = "Nervozilla/5.0";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, Exception {
        URL connection = new URL("http://checkip.amazonaws.com/");
        URLConnection con = connection.openConnection();
        String str = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        str = reader.readLine();
        System.out.println(str);
        encodeIP(str);
    }

    public static void encodeIP(String ip)  throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException , IOException, Exception{
      JSONObject obj = new JSONObject();
      obj.put("machine", "ch_inna");
      obj.put("ip", ip);
      
      JSONObject req = new JSONObject();
      req.put("request", "set_ip");
      req.put("text", obj);
      
/*
      obj.put("name", "foo");
      obj.put("num", new Integer(100));
      obj.put("balance", new Double(1000.21));
      obj.put("is_vip", new Boolean(true));
*/
      sendGet(req.toString());
      System.out.print(req);        
      AESCrypt(req.toString());
    }
    
    public static void AESCrypt(String strJSonIP) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {

        String FileName = "encryptedtext.txt";
        String FileName2 = "decryptedtext.txt";

        KeyGenerator KeyGen = KeyGenerator.getInstance("AES");
        KeyGen.init(128);

        SecretKey SecKey = KeyGen.generateKey();

        
        Cipher AesCipher = Cipher.getInstance("AES");


        byte[] byteText = strJSonIP.getBytes();

        AesCipher.init(Cipher.ENCRYPT_MODE, SecKey);
        byte[] byteCipherText = AesCipher.doFinal(byteText);
        Files.write(Paths.get(FileName), byteCipherText);


        byte[] cipherText = Files.readAllBytes(Paths.get(FileName));

        AesCipher.init(Cipher.DECRYPT_MODE, SecKey);
        byte[] bytePlainText = AesCipher.doFinal(cipherText);
        Files.write(Paths.get(FileName2), bytePlainText);
        
        System.out.println("\n" + "SecKey: " + SecKey.toString());
        
    }
    
	// HTTP GET request
	private static void sendGet(String urlParams) throws Exception {

		String url = "http://mcms.s.com/api1?json={\"request\":\"set_ip\",\"text\":{\"machine\":\"ch_inna\",\"ip\":\"178.93.128.151\"}}";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}
    
}
