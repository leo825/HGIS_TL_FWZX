import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import cn.geobeans.fwzx.util.HttpUtil;

public class MainTest {
	
	public static Document getDocByPost(String url,String parms) throws IOException, DocumentException{
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		
        PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(parms);
		out.close();
     
        SAXReader reader = new SAXReader();       
		return reader.read(conn.getInputStream());
	}
	
	
	public static String getJsonDocByPost(String url,  String params) throws IOException {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(params);
		out.close();
		
		StringBuffer data = new StringBuffer();
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
		String line;
		while ((line = rd.readLine()) != null) {
			data.append(line);
		}
		rd.close();
		return data.toString();
	}
	
	
	public static Document getDocByGet(String url) throws DocumentException,IOException{
		URL u=new URL(url);
		SAXReader reader = new SAXReader();
		return reader.read(u.openStream());
	}
	
	public static String getXmlString(){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<EASYXML version=\"1.1\" xmlns=\"http://mapservice.easymap.com\">");
		sb.append("<REQUEST>");
		sb.append("<EXECUTE>");
		sb.append("<SELECT alwaysreturnshape=\"false\" featurelimit=\"10\" beginrecord=\"0\" objectname=\"GIS010000000000.CS_BGFD_PT\">");
		sb.append("<COLUMNSCLAUSE>MC</COLUMNSCLAUSE>");
		sb.append("<WHERECLAUSE>1=1</WHERECLAUSE>");
		sb.append("<ORDERBYCLAUSE>CJRQ</ORDERBYCLAUSE>");
		sb.append("</SELECT>");
		sb.append("</EXECUTE>");
		sb.append("</REQUEST>");
		sb.append("</EASYXML>");
		return sb.toString();
		
	}
	
	public static String getHTMLByGet(String url,String encoding) throws IOException{
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setDoOutput(true);
		StringBuffer data = new StringBuffer();
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),encoding));
		String line;
		while ((line = rd.readLine()) != null) {
			data.append(line);
		}
		rd.close();
		return data.toString();
	}
	
	/**
	 * 日志格式
	 * */
	public static String logStringFormate(String operateTime, String result, String ip, String userName, String serverName, String projectName){
		return "["+operateTime+"]"+" "+"["+result+"]"+"ip="+ip+",userName="+userName+",serverName="+serverName+",projectName="+projectName+System.getProperty("line.separator");
	}

	/**
	 *
	 * returns.
	 */
	public static void runServer(String host, int remoteport, int localport)
			throws IOException {
		// Create a ServerSocket to listen for connections with
		ServerSocket ss = new ServerSocket(localport);

		final byte[] request = new byte[10240];
		byte[] reply = new byte[40960];

		while (true) {
			Socket client = null, server = null;
			try {
				// Wait for a connection on the local port
				client = ss.accept();

				final InputStream streamFromClient = client.getInputStream();
				final OutputStream streamToClient = client.getOutputStream();

				// Make a connection to the real server.
				// If we cannot connect to the server, send an error to the
				// client, disconnect, and continue waiting for connections.
				try {
					server = new Socket(host, remoteport);
				} catch (IOException e) {
					PrintWriter out = new PrintWriter(streamToClient);
					out.print("Proxy server cannot connect to " + host + ":"
							+ remoteport + ":\n" + e + "\n");
					out.flush();
					client.close();
					continue;
				}

				// Get server streams.
				final InputStream streamFromServer = server.getInputStream();
				final OutputStream streamToServer = server.getOutputStream();

				Thread t = new Thread() {
					public void run() {
						int bytesRead;
						try {
							while ((bytesRead = streamFromClient.read(request)) != -1) {
								streamToServer.write(request, 0, bytesRead);
								streamToServer.flush();							
							}

						} catch (IOException e) {
						}

						// the client closed the connection to us, so close our
						// connection to the server.
						try {
							streamToServer.close();
						} catch (IOException e) {
						}
					}
				};

				t.start();

				// Read the server's responses
				// and pass them back to the client.
				int bytesRead;
				try {
					while ((bytesRead = streamFromServer.read(reply)) != -1) {
						streamToClient.write(reply, 0, bytesRead);
						
					}
				} catch (IOException e) {
				}
				streamToClient.flush();
				// The server closed its connection to us, so we close our
				// connection to our client.
				streamToClient.close();
			} catch (IOException e) {
				System.err.println(e);
			} finally {
				try {
					if (server != null)
						server.close();
					if (client != null)
						client.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static void redirectTest(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		out.write("this is a test");
		out.flush();
		out.close();
	}
	
	/**
	 * 程序执行main方法
	 * @throws DocumentException 
	 * */
	public static void main(String[] args) throws IOException, DocumentException {
		String responseString;
		String url = "http://localhost:8080/HGIS_TL_FWZX/http/redirect?projectName=FWZX&serverName=get_project_tree";
		
		for(int i = 0; i < 100; i++){
			responseString = HttpUtil.getStringByGet(url, "UTF-8");
			System.out.println(i+" "+responseString);		
		}
	}
}