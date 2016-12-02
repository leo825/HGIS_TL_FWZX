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

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            logger.error("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteOneFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteOneFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.info("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                logger.error("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            logger.error("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            logger.error("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteOneFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            logger.error("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            logger.error("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }
}
