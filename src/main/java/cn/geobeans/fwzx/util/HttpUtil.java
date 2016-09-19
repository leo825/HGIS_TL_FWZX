package cn.geobeans.fwzx.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;


/**
 * @author Leo
 * @version 创建时间:2016-4-21上午10:30:50
 * @parameter E-mail:15895982509@163.com
 */
public class HttpUtil {

    private static Logger logger = Logger.getLogger(HttpUtil.class);

    /**
     * 获取request请求的ip地址
     *
     * @param request 请求为HttpServletRequest类型
     * @return String 类型的ip地址
     */
    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }


    /**
     * 通过本地路径获取Document对象
     *
     * @param url String类型本地地址
     * @return Document类型的数据对象
     */
    public static Document getDocByCLWFSearch(String url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(url);
        return doc;
    }

    /**
     * 通过远程路径发送http请求来获取Document对象
     *
     * @param url String类型的远程地址
     * @return Document类型的数据对象
     */
    public static Document getDocByCLWFRoadSearch(String url) throws DocumentException, IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestProperty("accept", "text/xml");
        SAXReader reader = new SAXReader();
        Document doc = reader.read(conn.getInputStream());
        return doc;
    }

    /**
     * 通过get方法来发送http请求来获取String类型数据,并且对请求的数据进行编码
     *
     * @param url      String类型的远程请求地址
     * @param encoding String类型的数据编码方式
     * @return String类型字符串
     */
    public static String getHTMLByGet(String url, String encoding) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoOutput(true);
        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }

    /**
     * 通过get方法来发送http请求来获取Document对象
     *
     * @param url String类型远程请求的地址
     * @return Document类型对象
     */
    public static Document getDocByGet(String url) throws DocumentException, IOException {
        URL u = new URL(url);
        SAXReader reader = new SAXReader();
        return reader.read(u.openStream());
    }

    /**
     * 通过post方法来发送http请求来获取Document对象
     *
     * @param url    String类型远程请求地址
     * @param params Map<String, String>类型的请求参数
     * @return Document类型对象
     */
    public static Document getDocByPost(String url, Map<String, String> params) throws DocumentException, IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        StringBuffer paramStr = new StringBuffer();
        String sep = "";
        for (Iterator<String> param = params.keySet().iterator(); param.hasNext(); ) {
            String key = param.next();
            String val = URLEncoder.encode(params.get(key), "UTF-8");
            if (key.equals("xml") || key.equals("XML")) {
                val = val.replaceAll("\\+", "%20")
                        .replaceAll("\\%21", "!")
                        .replaceAll("\\%27", "'")
                        .replaceAll("\\%28", "(")
                        .replaceAll("\\%29", ")")
                        .replaceAll("\\%7E", "~");
            }
            paramStr.append(sep).append(key).append("=").append(val);
            sep = "&";
        }
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(paramStr.toString());
        out.close();

        SAXReader reader = new SAXReader();
        return reader.read(conn.getInputStream());
    }

    /**
     * 通过get方法来发送http请求来获取Document对象,请求数据无编码
     *
     * @param url    String类型远程请求地址
     * @param params Map<String, String>类型的请求参数
     * @return Document类型对象
     */
    public static Document getDocByPostNoEncoding(String url, Map<String, String> params) throws DocumentException, IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        StringBuffer paramStr = new StringBuffer();
        String sep = "";
        for (Iterator<String> param = params.keySet().iterator(); param.hasNext(); ) {
            String key = param.next();
            String val = params.get(key);
            paramStr.append(sep).append(key).append("=").append(val);
            sep = "&";
        }
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(paramStr.toString());
        out.close();
        SAXReader reader = new SAXReader();
        return reader.read(conn.getInputStream());
    }

    /**
     * 通过post方法来发送http请求来获取Document对象,请求数据UTF-8编码
     *
     * @param url    String类型远程请求地址
     * @param params Map<String, String>类型的请求参数
     * @return Document类型对象
     */
    public static Document getDocByPostUTF8(String url, Map<String, String> params) throws DocumentException, IOException {
        StringBuffer paramStr = new StringBuffer();
        String sep = "";
        for (Iterator<String> param = params.keySet().iterator(); param.hasNext(); ) {
            String key = param.next();
            String val = params.get(key);
            paramStr.append(sep).append(key).append("=").append(val);
            sep = "&";
        }
        URL u = new URL(url + "?" + paramStr.toString());
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        Document dods = DocumentHelper.parseText(data.toString());
        return dods;
    }

    /**
     * 通过post方法来发送http请求来获取Document对象,请求数据进行编码
     *
     * @param url    String类型远程请求地址
     * @param encode String类型对请求数据进行编码
     * @return Document类型对象
     */
    public static Document getDocByPost(String url, String encode) throws DocumentException, IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), encode));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        Document dods = DocumentHelper.parseText(data.toString());
        return dods;
    }

    /**
     * 通过get方法来发送http请求来获取Document对象,请求数据进行编码
     *
     * @param url    String类型远程请求地址
     * @param encode String类型对请求数据进行编码
     * @return Document类型对象
     */
    public static String getStringByGet(String url, String encode) throws DocumentException, IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), encode));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }

    /**
     * 通过post方法来发送http请求来获取json字符串对象
     *
     * @param url    String类型远程请求地址
     * @param params String类型的请求参数
     * @return Json字符串类型
     */
    public static String getJsonDocByPost(String url, Map<String, String> params) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        StringBuffer paramStr = new StringBuffer();
        String sep = "";
        for (Iterator<String> param = params.keySet().iterator(); param.hasNext(); ) {
            String key = URLEncoder.encode(param.next(), "UTF-8");
            String val = params.get(key);
            paramStr.append(sep).append(key).append("=").append(val);
            sep = "&";
        }
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(paramStr.toString());
        out.close();

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }

    /**
     * 通过post方法来发送http请求来获取json字符串对象，并且进行GBK编码
     *
     * @param url    String类型远程请求地址
     * @param params String类型的请求参数
     * @return Json字符串类型
     */
    public static String getJsonDocByPostGBK(String url, Map<String, String> params) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        StringBuffer paramStr = new StringBuffer();
        String sep = "";
        for (Iterator<String> param = params.keySet().iterator(); param.hasNext(); ) {
            String key = param.next();
            String val = URLEncoder.encode(params.get(key), "GBK");
            if (key.equals("xml") || key.equals("XML")) {
                val = val.replaceAll("\\+", "%20")
                        .replaceAll("\\%21", "!")
                        .replaceAll("\\%27", "'")
                        .replaceAll("\\%28", "(")
                        .replaceAll("\\%29", ")")
                        .replaceAll("\\%7E", "~");
            }
            paramStr.append(sep).append(key).append("=").append(val);
            sep = "&";
        }
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(paramStr.toString());
        out.close();

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }

    /**
     * 获取端口的状态
     *
     * @param ip      String类型的ip地址
     * @param port    String类型的端口号
     * @param timeout int类型的连接时间
     * @return boolean类型true代表端口通的, false代表端口不通
     */
    public static boolean isReachable(String ip, String port, int timeout) {
        boolean reachable = false;
        // 如果端口为空，使用 isReachable 检测，非空使用 socket 检测
        if (port == null) {
            try {
                InetAddress address = InetAddress.getByName(ip);
                reachable = address.isReachable(timeout);
            } catch (Exception e) {
                logger.error(e.getMessage());
                reachable = false;
            }
        } else {
            Socket socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(ip, Integer.parseInt(port)), timeout);
                reachable = true;
            } catch (Exception e) {
                logger.error(e);
                reachable = false;
            } finally {
                try {
                    if (socket != null) socket.close();
                } catch (Exception e) {
                }
            }
        }
        return reachable;
    }

    /**
     * 获取服务的连接状态信息
     *
     * @param url     String类型的一个页面地址
     * @param timeout int类型的连接时间
     * @return boolean类型true代表端口通的, false代表端口不通
     */
    public static boolean isReacheable(String url, int timeout) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            con.setConnectTimeout(timeout);
            if (con.getResponseCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    /**
     * 通过post方法发送http请求,并且请求结果采用UTF-8编码
     *
     * @param url    String类型远程请求地址
     * @param params String类型的请求参数
     * @return Json类型的字符串
     */
    public static String getJsonDocByPostUTF8(String url, String params) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(params);
        out.close();

        StringBuffer data = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = rd.readLine()) != null) {
            data.append(line);
        }
        rd.close();
        return data.toString();
    }

}
