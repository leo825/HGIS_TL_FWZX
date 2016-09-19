package cn.geobeans.fwzx.util;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
 
/**
 * 
* <p>Title: JSON-XML转换工具</p>
* <p>desc:
* <p>Copyright: Copyright(c)Gb 2012</p>
* @author http://www.ij2ee.com
* @time 上午8:20:40
* @version 1.0
* @since
 */
public class XmlJsonUtil {
    public static String xml2Json(String xml){
        return new XMLSerializer().read(xml).toString();
    }
     
    public static String json2Xml(String json){
        JSONObject jobj = JSONObject.fromObject(json);
        String xml =  new XMLSerializer().write(jobj);
        return xml;
    }
     
    public static void main(String[] args) {
    	String STR_JSON = "{\"name\":\"Michael\",\"address\":{\"city\":\"Suzou\",\"street\":\" Changjiang Road \",\"postcode\":100025},\"blog\":\"http://www.ij2ee.com\"}";
        String xml = json2Xml(STR_JSON);
        System.out.println("xml = "+xml);
        String json = xml2Json(xml);
        System.out.println("json="+json);
    }
}