package cn.geobeans.fwzx.util;

import net.sf.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

/**
 * <p>Title: JSON-XML转换工具</p>
 * <p>desc:
 * <p>Copyright: Copyright(c)Gb 2012</p>
 *
 * @author http://www.ij2ee.com
 * @version 1.0
 * @time 上午8:20:40
 */
public class XmlJsonUtil {


    private static Logger logger = Logger.getLogger(XmlJsonUtil.class);

    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param xml xml格式的字符串
     * @return 成功返回json 格式的字符串;失败反回null
     */
    public static String xml2Json(String xml) {
        JSONObject obj = new JSONObject();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(is);
            Element root = doc.getRootElement();
            obj.put(root.getName(), iterateElement(root));
            return obj.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param file java.io.File实例是一个有效的xml文件
     * @return 成功反回json 格式的字符串;失败反回null
     */
    @SuppressWarnings("unchecked")
    public static String xml2Json(File file) {
        JSONObject obj = new JSONObject();
        try {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(file);
            Element root = doc.getRootElement();
            obj.put(root.getName(), iterateElement(root));
            return obj.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 一个迭代方法
     *
     * @param element : org.jdom.Element
     * @return java.util.Map 实例
     */
    @SuppressWarnings("unchecked")
    private static Map iterateElement(Element element) {
        List jiedian = element.getChildren();
        Element et = null;
        Map obj = new HashMap();
        List list = null;
        for (int i = 0; i < jiedian.size(); i++) {
            list = new LinkedList();
            et = (Element) jiedian.get(i);
            if (et.getTextTrim().equals("")) {
                if (et.getChildren().size() == 0)
                    continue;
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(iterateElement(et));
                obj.put(et.getName(), list);
            } else {
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(et.getTextTrim());
                obj.put(et.getName(), list);
            }
        }
        return obj;
    }

    /**
     * 将json转换成xml
     */
    public static String json2Xml(final String json)  {
        try {
            return json2Xml(json, new com.leo.camel.util.JsonXmlReader());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 将xml转换成json
     */
    public static String json2Xml(final String json, final com.leo.camel.util.JsonXmlReader reader) {

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            InputSource source = new InputSource(new StringReader(json));
            Result result = new StreamResult(out);
            transformer.transform(new SAXSource(reader, source), result);
            return new String(out.toByteArray());
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }


    public static void main(String[] agrs){

        String jsonStr = "{\n" +
                "  \"total\":7,\n" +
                "  \"rows\":[{\n" +
                "    \"id\":1,\n" +
                "    \"name\":\"天津会议室中控\",\n" +
                "    \"category\":\"智能中控\",\n" +
                "    \"ip\":\"10.92.173.253\",\n" +
                "    \"boardroom\":\"天津视频会议室\",\n" +
                "    \"status\":\"正常\"\n" +
                "  },{\n" +
                "    \"id\":2,\n" +
                "    \"name\":\"天津海关缉私局1\",\n" +
                "    \"category\":\"终端\",\n" +
                "    \"ip\":\"10.92.173.247\",\n" +
                "    \"boardroom\":\"天津视频会议室\",\n" +
                "    \"status\":\"正常\"\n" +
                "  },{\n" +
                "    \"id\":3,\n" +
                "    \"name\":\"天津海关缉私局2\",\n" +
                "    \"category\":\"终端\",\n" +
                "    \"ip\":\"10.92.173.248\",\n" +
                "    \"boardroom\":\"天津视频会议室\",\n" +
                "    \"status\":\"正常\"\n" +
                "  },{\n" +
                "    \"id\":4,\n" +
                "    \"name\":\"宁波会议室中控\",\n" +
                "    \"category\":\"智能中控\",\n" +
                "    \"ip\":\"10.119.21.184\",\n" +
                "    \"boardroom\":\"宁波视频会议室\",\n" +
                "    \"status\":\"正常\"\n" +
                "  },{\n" +
                "    \"id\":5,\n" +
                "    \"name\":\"宁波海关缉私局\",\n" +
                "    \"category\":\"终端\",\n" +
                "    \"ip\":\"10.119.21.180\",\n" +
                "    \"boardroom\":\"宁波视频会议室\",\n" +
                "    \"status\":\"正常\"\n" +
                "  },{\n" +
                "    \"id\":6,\n" +
                "    \"name\":\"广州备用\",\n" +
                "    \"category\":\"终端\",\n" +
                "    \"ip\":\"10.40.249.212\",\n" +
                "    \"boardroom\":\"广州视频会议室\",\n" +
                "    \"status\":\"正常\"\n" +
                "  },{\n" +
                "    \"id\":7,\n" +
                "    \"name\":\"广州海关缉私局1\",\n" +
                "    \"category\":\"终端\",\n" +
                "    \"ip\":\"10.40.131.13\",\n" +
                "    \"boardroom\":\"广州视频会议室\",\n" +
                "    \"status\":\"正常\"\n" +
                "  }]\n" +
                "}";

       String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><document><total>7</total><rows><rows><id>1</id><name>天津会议室中控</name><category>智能中控</category><ip>10.92.173.253</ip><boardroom>天津视频会议室</boardroom><status>正常</status></rows><rows><id>2</id><name>天津海关缉私局1</name><category>终端</category><ip>10.92.173.247</ip><boardroom>天津视频会议室</boardroom><status>正常</status></rows><rows><id>3</id><name>天津海关缉私局2</name><category>终端</category><ip>10.92.173.248</ip><boardroom>天津视频会议室</boardroom><status>正常</status></rows><rows><id>4</id><name>宁波会议室中控</name><category>智能中控</category><ip>10.119.21.184</ip><boardroom>宁波视频会议室</boardroom><status>正常</status></rows><rows><id>5</id><name>宁波海关缉私局</name><category>终端</category><ip>10.119.21.180</ip><boardroom>宁波视频会议室</boardroom><status>正常</status></rows><rows><id>6</id><name>广州备用</name><category>终端</category><ip>10.40.249.212</ip><boardroom>广州视频会议室</boardroom><status>正常</status></rows><rows><id>7</id><name>广州海关缉私局1</name><category>终端</category><ip>10.40.131.13</ip><boardroom>广州视频会议室</boardroom><status>正常</status></rows></rows></document>\n";



        System.out.println(json2Xml("{\"document\":"+jsonStr+"}"));

        System.out.println(xml2Json(xmlStr));
    }

}