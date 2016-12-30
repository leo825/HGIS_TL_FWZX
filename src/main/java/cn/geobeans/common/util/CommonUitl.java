package cn.geobeans.common.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class CommonUitl {
    public static Object checkSqlField(Object field) {
        if (field == null) return "'NULL'";
        if (field instanceof String) {
            return "'" + field + "'";
        } else if (field instanceof Date) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "'" + sdf2.format((Date) field) + "'";
        } else if (field instanceof Integer) {
            return field == null ? "" : "" + field + "";
        }
        return field;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof String) {
            if ("NULL".equals(obj.toString().trim().toUpperCase()) || "".equals(obj.toString().trim())) {
                return true;
            } else {
                return false;
            }
        } else if (obj instanceof Collection) {
            return ((Collection<?>) obj).size() == 0;
        } else if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else {
            return false;
        }
    }

    /**
     * 验证字符串是否超出length长度
     *
     * @param resource 字符串
     * @param length   最大长度
     * @return boolean
     */
    public static boolean outLength(String resource, int length) {
        try {
            return resource.getBytes("UTF-8").length <= length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Date string2date(String str, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            if (isEmpty(pattern)) return sdf.parse(str);
            sdf.applyPattern(pattern);
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 默认格式yyyy-MM-dd-HH-mm-ss
     *
     * @param str
     * @return
     */
    public static Date string2date(String str) {
        return string2date(str, null);
    }

    /**
     * 一个字符串转换为long
     *
     * @param str
     * @param pattern
     * @return
     */
    public static Long string2Long(String str, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            if (isEmpty(pattern)) return sdf.parse(str).getTime();
            sdf.applyPattern(pattern);
            return sdf.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long string2Long(String str) {
        return string2Long(str, null);
    }

    /**
     * 调整时间，
     *
     * @param d      要调整的时间
     * @param isAdd  是添加还是减少
     * @param second 以秒为单位
     * @return
     */
    public static Date dateAdjust(Date d, boolean isAdd, int second) {
        long time = d.getTime();
        if (isAdd) time += second2mil(second);
        else time -= second2mil(second);
        Date t = new Date(time);
        return t;
    }

    /**
     * 日期添加，按天添加
     */
    public static Date plusByDay(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTime();
    }

    /**
     * 为一个时间增加xx秒
     *
     * @param d
     * @param second
     * @return
     */
    public static Date datePlus(Date d, int second) {
        return dateAdjust(d, true, second);
    }

    public static Date datePlusByMinute(Date d, int minute) {
        return datePlus(d, minute * 60);
    }

    public static Date datePlusByHour(Date d, int hour) {
        return datePlusByMinute(d, hour * 60);
    }

    public static Date datePlusByDay(Date d, int day) {
        return datePlusByHour(d, day * 24);
    }

    public static Date dateMinus(Date d, int second) {
        return dateAdjust(d, false, second);
    }

    public static long datePlus2long(Date d, int second) {
        return datePlus(d, second).getTime();
    }

    public static long dateMinus2long(Date d, int second) {
        return dateMinus(d, second).getTime();
    }

    private static long second2mil(int second) {
        return second * 1000;
    }

    /**
     * @param timestamp1
     * @param timestamp2
     * @return
     * @see #getTimeInterval(java.sql.Timestamp, java.sql.Timestamp, boolean)
     */
    public static String getTimeInterval(Timestamp timestamp1,
                                         Timestamp timestamp2) {
        return getTimeInterval(timestamp1, timestamp2, false);
    }

    /**
     * 取时间差
     *
     * @param timestamp1
     * @param timestamp2
     * @param showSecond 是否显示秒
     * @return
     */
    public static String getTimeInterval(Timestamp timestamp1,
                                         Timestamp timestamp2, boolean showSecond) {
        if (timestamp1 == null || timestamp2 == null)
            return "";
        StringBuffer buf = new StringBuffer();
        long time1 = timestamp1.getTime();
        long time2 = timestamp2.getTime();
        long time = Math.abs(time1 - time2);
        // day
        long day = time / (24 * 60 * 60 * 1000L);
        // hour
        time = time - day * 24 * 60 * 60 * 1000L;
        long hour = time / (60 * 60 * 1000L);
        // minute
        time = time - hour * 60 * 60 * 1000L;
        long minute = time / (60 * 1000L);
        // second
        time = time - minute * 60 * 1000L;
        long second = time / 1000L;

        if (day != 0) {
            buf.append(day + "天");
        }
        if (hour != 0) {
            buf.append(hour + "时");
        }
        if (minute != 0) {
            buf.append(minute + "分");
        }

        if (showSecond || buf.length() == 0) {
            if (second != 0) {
                buf.append(second + "秒");
            }
        }

        return buf.toString();
    }

    public static boolean isNumeric(String str) {
        if (str.matches("\\d+(\\.?\\d+)?")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成月份的列表，如201309、201310、201311
     */
    public static List<String> genMonthList(int monthLen) {
        List<String> result = new ArrayList<String>();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        String temp = "";

        while (result.size() < monthLen) {
            if (month < 10) {
                temp = year + "0" + month;
            } else {
                temp = year + "" + month;
            }
            result.add(temp);

            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
        }

        return result;
    }

    /**
     * 获取年份，从今年到往后len年
     */
    public static List<Integer> getYears(int len) {
        List<Integer> result = new ArrayList<Integer>();
        Calendar cal = null;
        for (int i = 0; i < len; i++) {
            cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, i);
            result.add(cal.get(Calendar.YEAR));
        }
        return result;
    }


    public static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}
