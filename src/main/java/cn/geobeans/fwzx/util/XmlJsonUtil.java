package cn.geobeans.fwzx.util;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * JSON-XML转换工具
 */
public class XmlJsonUtil {
    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param xml xml格式的字符串
     * @return 成功返回json 格式的字符串;失败反回null
     */
    public static String xml2Json(String xml) {
        String jsonStr = null;
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(xml);
            jsonStr = xmlJSONObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * 将json的字符串转换成xml字符串
     *
     * @param json     指的是要转换的json字符串
     * @param encoding 指的是xml的编码格式
     * @param root     指的是指定xml的根部节点名称，不可缺少
     * @return 成功后返回转换后的xml字符串，失败返回null
     */
    public static String json2Xml(String json, String encoding, String root) {
        String xmlStr = null;
        try {
            JSONObject jsonFileObject = new JSONObject(json);
            xmlStr = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n<" + root + ">"
                    + XML.toString(jsonFileObject) + "</" + root + ">";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return xmlStr;
    }
}