package cn.geobeans.fwzx.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuxi E-mail:15895982509@163.com
 * @version 创建时间:2016-3-28上午11:26:37
 */
public class StringUtil {

    private static Logger logger = Logger.getLogger(StringUtil.class);
    /**
     * 用来查找特定第几个字符在字符串中的位置
     *
     * @param string    字符串
     * @param i         字符出现的位置
     * @param character 要找的字符
     */
    public static int getCharacterPosition(String string, int i, String character) {
        //这里是获取"/"符号的位置
        // Matcher slashMatcher = Pattern.compile("/").matcher(string);

        boolean isExist = false;
        Matcher slashMatcher = Pattern.compile(character).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if (mIdx == i) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            return slashMatcher.start();
        }
        return -1;
    }

    /**
     * String 为空判断
     */
    public static boolean isNull(String... obj) {
        try {
            for (String s : obj) {
                if (s == null || "".equals(s)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * List 为空判断
     */
    public static boolean isListEmpty(List<?> list) {
        if (list == null || list.isEmpty() || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * post请求下针对中文编码转换
     */
    public static String encodeStr(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 把map对象的key全部转为小写形式
     *
     * @param map
     * @return
     */
    public static Map<String, Object> keyToLower(Map<String, Object> map) {
        Map<String, Object> r = new HashMap<String, Object>();
        if (map == null || map.size() == 0)
            return r;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            r.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return r;
    }

    /**
     * 把list map中map对象的key全部转为小写形式
     *
     * @param listmap
     * @return
     */
    public static List<Map<String, Object>> listKeyToLower(List<Map<String, Object>> listmap) {
        List<Map<String, Object>> r = new ArrayList<Map<String, Object>>();
        if (listmap == null || listmap.size() == 0)
            return r;
        for (Map<String, Object> map : listmap) {
            r.add(keyToLower(map));
        }
        return r;
    }

    /**
     * 如果目录不存在就创建目录
     */
    public static boolean checkDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return true;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录  
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }
}
