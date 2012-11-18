package com.megadevs.nostradamus.nostrapushreceiver;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/** Network utils class */
public class Utils {

	public static final int BUFFER_SIZE = 2*1024;

	public static byte[] getBytesFromUrl(String URL) throws IOException{
		URL url = new URL(URL);
		ByteArrayOutputStream bais = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = url.openStream ();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
			int n;

			while ( (n = is.read(byteChunk)) > 0 ) {
				bais.write(byteChunk, 0, n);
			}
		}
		catch (IOException e) {
			System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
			e.printStackTrace ();
			// Perform any other exception handling that's appropriate.
		}
		finally {
			if (is != null) { is.close(); }

		}
		return bais.toByteArray();


	}

	/**<b><br>ATTENZIONE,  PREFERIBILE USARE IL METODO executeHTTPUrlPost</b><br><br>
	 * Read data from  HTTP url
	 * @param myurl 
	 * @return response data
	 * @throws URISyntaxException bad URL
	 * @throws IOException 
	 * @throws FakeConnectivityException 
	 */
	public static HTTPResult executeHTTPUrl(String myurl,String auth, String cookie) throws IOException, URISyntaxException {

		HTTPResult ris = new HTTPResult();

		//Result data
		String data="";

		//Prepare url
		String composeUrl="//"+myurl;
		URLEncoder.encode(composeUrl, "UTF-8");
		URI	tmpUri = new URI("http",composeUrl,null);

		//creo URI per quotare i caratteri speciali
		composeUrl = tmpUri.toString(); //estraggo come stringa
		//System.out.println("compose url: "+composeUrl);
		URL url = new URL(composeUrl); //creo URL da aprire

		//Apro connessione
		URLConnection connection = url.openConnection();
		connection.setRequestProperty( "User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.19 (KHTML, like Gecko) Ubuntu/11.10 Chromium/18.0.1025.168 Chrome/18.0.1025.168 Safari/535.19");
		//connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connection.setRequestProperty("Accept-Language" ,"en-US,en;q=0.8");
		connection.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");	

		if(cookie!=null){
			connection.setRequestProperty("Cookie", cookie);
		}
		
		if(auth!=null){
			String encoding = base64Encode(auth);
			connection.setRequestProperty("Authorization", "Basic "	+ encoding);
		}

		connection.setDoOutput(true);


		/*
		//Leggo l'esito dello script
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
						connection.getInputStream()));


		String decodedString;
		while ((decodedString = in.readLine()) != null) {
			data = data+decodedString+"\n";
			if(!in.ready()){
				break;
			}
		}
		in.close();

		data=data.trim();
		 */
		
		System.out.println(((HttpURLConnection)connection).getResponseCode());
		data=readInputStreamAsString(connection.getInputStream());
		if(data==null)data="";



		ris.setData(data);
		ris.setHeader(connection.getHeaderFields());

		return ris;
	}


	public static String readInputStreamAsString(InputStream is) throws IOException{
		final char[] buffer = new char[BUFFER_SIZE];
		StringBuilder out = new StringBuilder(BUFFER_SIZE);
		Reader in = new InputStreamReader(is, "UTF-8");
		int read;
		do {
			read = in.read(buffer, 0, buffer.length);
			if (read>0) {
				out.append(buffer, 0, read);
			}
		} while (read>=0);
		return out.toString();
	}


	/**
	 * Check if passed data is the result of a fake connection
	 * @param data data to test
	 * @return true=fake
	 */
	private static boolean checkFakeConnection(String data) {
		// TODO fare un metodo per controllare se la risposta arriva da una form di login di un accesspoint o altro...
		return false;


	}

	public static String readFileAsString(String filePath)
			throws java.io.IOException{
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(
				new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead=0;
		while((numRead=reader.read(buf)) != -1){
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	public static Object LOCK = new Object();
	public static boolean checkFinished = false;
	public static boolean checkRis = false;

	public static boolean checkRemoteImageTimed(final String myurl, int msecTimeout){
		//reset checking
		checkFinished = false;
		checkRis = false;

		Thread t = new Thread(){
			public void run(){
				try {
					checkRis = checkRemoteImage(myurl);
					synchronized (LOCK) {
						checkFinished=true;
						LOCK.notifyAll();
					}
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		};
		t.setDaemon(true);
		t.start();

		synchronized (LOCK) {
			while(!checkFinished){
				try {
					LOCK.wait(msecTimeout);
					if(!checkFinished)return false;
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			return checkRis;
		}

	}

	public static boolean checkRemoteImage(String myurl) throws URISyntaxException, IOException{
		if(myurl== null)return false;
		myurl = myurl.replace("http://", "");

		HTTPResult ris = new HTTPResult();

		//Result data
		String data="";

		//Prepare url
		String composeUrl="//"+myurl;
		URLEncoder.encode(composeUrl, "UTF-8");
		URI	tmpUri = new URI("http",composeUrl,null);

		//creo URI per quotare i caratteri speciali
		composeUrl = tmpUri.toString(); //estraggo come stringa
		//System.out.println("compose url: "+composeUrl);
		URL url = new URL(composeUrl); //creo URL da aprire

		//Apro connessione
		URLConnection connection = url.openConnection();
		//connection.setReadTimeout(3000);
		if(connection==null)return false;
		connection.setDoOutput(true);

		ris.setHeader(connection.getHeaderFields());

		//System.out.println(ris.getHeader());
		List<String> typeAsList = ris.getHeader().get("Content-type");
		if(typeAsList==null) typeAsList = ris.getHeader().get("Content-Type");
		if(typeAsList==null) typeAsList = ris.getHeader().get("content-type");
		List<String> rispAsList = ris.getHeader().get(null);

		if(typeAsList!= null && rispAsList!=null){
			String type = typeAsList.toString();
			String risp = rispAsList.toString();

			if(type.toLowerCase().contains("image/jpeg") && risp.toUpperCase().contains("200 OK"))return true;
		}


		return false;
	}



	/** Codifica in base64 per autenticazione HTTP */
	public static String base64Encode(String s) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		Base64OutputStream out = new Base64OutputStream(bOut);
		try {
			out.write(s.getBytes());
			out.flush();
		} catch (IOException exception) {
		}
		return bOut.toString();
	}
	

/*
 * BASE64 encoding encodes 3 bytes into 4 characters.
 * |11111122|22223333|33444444| Each set of 6 bits is encoded according to the
 * toBase64 map. If the number of input bytes is not a multiple of 3, then the
 * last group of 4 characters is padded with one or two = signs. Each output
 * line is at most 76 characters.
 */

}

class Base64OutputStream extends FilterOutputStream {
	public Base64OutputStream(OutputStream out) {
		super(out);
	}

	public void write(int c) throws IOException {
		inbuf[i] = c;
		i++;
		if (i == 3) {
			super.write(toBase64[(inbuf[0] & 0xFC) >> 2]);
			super.write(toBase64[((inbuf[0] & 0x03) << 4)
			                     | ((inbuf[1] & 0xF0) >> 4)]);
			super.write(toBase64[((inbuf[1] & 0x0F) << 2)
			                     | ((inbuf[2] & 0xC0) >> 6)]);
			super.write(toBase64[inbuf[2] & 0x3F]);
			col += 4;
			i = 0;
			if (col >= 76) {
				super.write('\n');
				col = 0;
			}
		}
	}

	public void flush() throws IOException {
		if (i == 1) {
			super.write(toBase64[(inbuf[0] & 0xFC) >> 2]);
			super.write(toBase64[(inbuf[0] & 0x03) << 4]);
			super.write('=');
			super.write('=');
		} else if (i == 2) {
			super.write(toBase64[(inbuf[0] & 0xFC) >> 2]);
			super.write(toBase64[((inbuf[0] & 0x03) << 4)
			                     | ((inbuf[1] & 0xF0) >> 4)]);
			super.write(toBase64[(inbuf[1] & 0x0F) << 2]);
			super.write('=');
		}
	}

	private static char[] toBase64 = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
		'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
		'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
		'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
		'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', '+', '/' };

	private int col = 0;

	private int i = 0;

	private int[] inbuf = new int[3];
}
