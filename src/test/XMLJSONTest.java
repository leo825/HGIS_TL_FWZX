/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2016/9/27
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */

import cn.geobeans.fwzx.util.XmlJsonUtil;
import org.junit.Test;

/**
 * Created by Administrator on 2016/9/27.
 */
public class XMLJSONTest {

    @Test
    public void json2xmlTest(){
        String xml = "<?xml version=\"1.0\" ?> \n" +
                "<GetFeature> \n" +
                "<Query typeName=\"cities\">\n" +
                "<Filter> \n" +
                "<Or> \n" +
                "\t<And> \n" +
                "\t\t<PropertyIsEqualTo> \n" +
                "\t\t\t<PropertyName>COUNTRY</PropertyName> \n" +
                "\t\t\t<Literal>Russia</Literal> \n" +
                "\t\t</PropertyIsEqualTo> \n" +
                "\t\t<PropertyIsGreaterThanOrEqualTo> \n" +
                "\t\t\t<PropertyName>POPULAT</PropertyName> \n" +
                "\t\t\t<Literal>1000000</Literal> \n" +
                "\t\t</PropertyIsGreaterThanOrEqualTo> \n" +
                "\t</And> \n" +
                "\t<And> \n" +
                "\t\t<PropertyIsEqualTo> \n" +
                "\t\t\t<PropertyName>COUNTRY</PropertyName> \n" +
                "\t\t\t<Literal>China</Literal> \n" +
                "\t\t</PropertyIsEqualTo> \n" +
                "\t   <PropertyIsGreaterThanOrEqualTo> \n" +
                "\t\t\t<PropertyName>POPULAT</PropertyName> \n" +
                "\t\t\t<Literal>1000000</Literal> \n" +
                "\t   </PropertyIsGreaterThanOrEqualTo>\n" +
                "\t</And>\n" +
                "</Or> \n" +
                "</Filter> \n" +
                "</Query> \n" +
                "</GetFeature> ";

        System.out.println("json=" + XmlJsonUtil.xml2Json(xml));

        String json = "{\"GetFeature\":{\"Query\":[{\"Filter\":[{\"Or\":[{\"And\":[{\"PropertyIsGreaterThanOrEqualTo\":[{\"PropertyName\":[\"POPULAT\"],\"Literal\":[\"1000000\"]}],\"PropertyIsEqualTo\":[{\"PropertyName\":[\"COUNTRY\"],\"Literal\":[\"Russia\"]}]},{\"PropertyIsGreaterThanOrEqualTo\":[{\"PropertyName\":[\"POPULAT\"],\"Literal\":[\"1000000\"]}],\"PropertyIsEqualTo\":[{\"PropertyName\":[\"COUNTRY\"],\"Literal\":[\"China\"]}]}]}]}]}]}}";
        System.out.println("xml=" + XmlJsonUtil.json2Xml(json));
    }
}
