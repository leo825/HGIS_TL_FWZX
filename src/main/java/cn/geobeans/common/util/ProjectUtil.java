package cn.geobeans.common.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import org.codehaus.jackson.map.ObjectMapper;


/**
 * 项目工具类
 * Created by ice on 2016/3/15.
 */
public class ProjectUtil {

    static final char[] CHARS = { '0','1','2','3','4','5','6','7','8','9'};

    /**
     * 获取uuid
     *
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 将对象序列化为json
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String convertObjToJson(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * 将json反序列化为对象
     *
     * @param json   要反序列化的json字符串
     * @param _class 目标对象类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T convertJsonToObj(String json, Class<T> _class) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, _class);
    }


    /**
     * 完成根据已有的CODE生成下一个同级CODE
     * @param pre 前一个CODE
     * @param codeLen 一个层级CODE的长度
     * @return
     */
    public static String getNextCode(String pre,int codeLen) throws Exception {
        if(pre == null){
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<codeLen; i++){
                sb.append(CHARS[0]);
            }
            return sb.toString();
        }
        char[] arr = pre.substring(pre.length()-codeLen,pre.length()).toUpperCase().toCharArray();
        arr =  getReplaceChar(arr);
        boolean repeat = true;
        for(char c : arr){
            if(c != CHARS[0]){
                repeat = false;
            }
        }
        if(repeat){
            throw new Exception("The Code Is Used Up.");
        }
        return pre.substring(0,pre.length()-codeLen) + new String(arr);
    }

    /**
     * 获取数组中被替换之后的数组
     * @param arr 要被替换的数组
     * @return
     */
    private static char[] getReplaceChar(char[] arr) {
        for (int i=arr.length-1;i>=0;i--){
            char ic = getNextChar(arr[i]);
            arr[i] = ic;
            if(ic != CHARS[0]){
                break;
            }
        }
        return arr;
    }

    /**
     * 根据已有的CHAR获取队列中的下一个CHAR
     * @param c 上一个CHAR
     * @return 下一个CHAR
     */
    private static char getNextChar(char c) {

        for(int i = 0;i< CHARS.length;i++){
            char ic = CHARS[i];
            if(ic == c){
                if(i == CHARS.length - 1){
                    return CHARS[0];
                }else{
                    return CHARS[i+1];
                }
            }
        }
        return 0;
    }

    /**
     * 判断参数字符串是否为空
     * @param str
     * @return
     */
    public static boolean isNull(String str){
        return str == null || str.trim().equals("");
    }


    /**
     * 获取配置文件内容
     * @param key
     * @return
     */
    public static String getProperty(String key){
        Properties props = new Properties();

        String name = "application.properties";

        InputStream input= ProjectUtil.class.getClassLoader().getResourceAsStream(name);

        try{
            props.load(input);
            String origin = props.getProperty(key);
            return  new String(origin.getBytes("ISO-8859-1"), "UTF-8");
        }catch(Exception e){
            System.err.println("不能读取属性文件. 请确保在CLASSPATH指定的路径中");
        }
        return null;
    }
}
