package cn.geobeans.common.util;


import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 *
 * @author guo
 */
public class CalendarUtil {

    private static Logger logger = Logger.getLogger(CalendarUtil.class);

    public static Date getTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static Date getDate() {
        try {
            int y, m, d;
            Calendar cal = Calendar.getInstance();
            y = cal.get(Calendar.YEAR);
            m = cal.get(Calendar.MONTH) + 1;
            d = cal.get(Calendar.DATE);
            String dateStr = y + "-" + m + "-" + d;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static String getTimeStr1() {
        int h, mi, s;
        Calendar cal = Calendar.getInstance();
        h = cal.get(Calendar.HOUR_OF_DAY);
        mi = cal.get(Calendar.MINUTE) + 1;
        s = cal.get(Calendar.SECOND);
        String TimeStr1 = h + ":" + mi + ":" + s;
        return TimeStr1;
    }

    public static String getDateStr() {
        int y, m, d;
        Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH) + 1;
        d = cal.get(Calendar.DATE);
        String yStr = y < 10 ? ("0" + y) : String.valueOf(y);
        String mStr = m < 10 ? ("0" + m) : String.valueOf(m);
        String dStr = d < 10 ? ("0" + d) : String.valueOf(d);
        String time = yStr + "-" + mStr + "-" + dStr;
        return time;
    }

    //上月
    public static String getLastMonthTimeStr() {
        int y, m, d;
        Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DATE);
        String time = y + "-" + m + "-" + d;
        return time;
    }

    //上周日期
    public static String getLastWeekTimeStr() {
        int year, day, month;
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        day = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH);
        String date = "";
        if ((day - 7) <= 0) {
            if (month == 0) {
                date = (year - 1) + "-12-" + (31 - (7 - day));
            } else {
                //前一个月如果是30天
                if (month == 11 || month == 9 || month == 6 || month == 4) {
                    date = year + "-" + (month) + "-" + (30 - (7 - day));
                }
                //前一个月是31天
                else if (month == 10 || month == 8 || month == 7 || month == 5 || month == 3 || month == 1) {
                    date = year + "-" + (month) + "-" + (31 - (7 - day));
                }
                //否则就是3月1日
                //判断是否闰年
                else if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    date = year + "-" + (month) + "-" + (29 - (7 - day));
                } else {
                    date = year + "-" + (month) + "-" + (28 - (7 - day));
                }
            }
        }    //否则不是当月前天
        else {
            date = year + "-" + (month + 1) + "-" + (day - 7);
        }
        return date;
    }

    /**
     * 获取毫秒级时间编码
     *
     * @return
     */
    public static String getTimeStr() {
        int y, m, d, h, mi, s, ms;
        Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH) + 1;
        d = cal.get(Calendar.DATE);
        h = cal.get(Calendar.HOUR_OF_DAY);
        mi = cal.get(Calendar.MINUTE);
        s = cal.get(Calendar.SECOND);
        ms = cal.get(Calendar.MILLISECOND);
        String yStr = y < 10 ? ("0" + y) : String.valueOf(y);
        String mStr = m < 10 ? ("0" + m) : String.valueOf(m);
        String dStr = d < 10 ? ("0" + d) : String.valueOf(d);
        String hStr = h < 10 ? ("0" + h) : String.valueOf(h);
        String miStr = mi < 10 ? ("0" + mi) : String.valueOf(mi);
        String sStr = s < 10 ? ("0" + s) : String.valueOf(s);
        String msStr = ms < 10 ? ("0" + ms) : String.valueOf(ms);
        String time = yStr + mStr + dStr + hStr + miStr + sStr + msStr;
        return time;
    }

    public static String getTimeStr(Date time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = "";
        if (time != null) {
            timeStr = format.format(time);
        }
        return timeStr;
    }

    public static Date getDateTime() {
        return getDate(getDataTimeStr());
    }

    public static String getDataTimeStr() {
        int y, m, d, h, mi, s;
        Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH) + 1;
        d = cal.get(Calendar.DATE);
        h = cal.get(Calendar.HOUR_OF_DAY);
        mi = cal.get(Calendar.MINUTE);
        s = cal.get(Calendar.SECOND);
        String yStr = y < 10 ? ("0" + y) : String.valueOf(y);
        String mStr = m < 10 ? ("0" + m) : String.valueOf(m);
        String dStr = d < 10 ? ("0" + d) : String.valueOf(d);
        String hStr = h < 10 ? ("0" + h) : String.valueOf(h);
        String miStr = mi < 10 ? ("0" + mi) : String.valueOf(mi);
        String sStr = s < 10 ? ("0" + s) : String.valueOf(s);
        String time = yStr + "-" + mStr + "-" + dStr + " " + hStr + ":" + miStr + ":" + sStr;
        return time;
    }

    public static String getfileNameTime() {
        int y, m, d, h, mi, s;
        Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH) + 1;
        d = cal.get(Calendar.DATE);
        h = cal.get(Calendar.HOUR_OF_DAY);
        mi = cal.get(Calendar.MINUTE);
        s = cal.get(Calendar.SECOND);
        String yStr = y < 10 ? ("0" + y) : String.valueOf(y);
        String mStr = m < 10 ? ("0" + m) : String.valueOf(m);
        String dStr = d < 10 ? ("0" + d) : String.valueOf(d);
        String hStr = h < 10 ? ("0" + h) : String.valueOf(h);
        String miStr = mi < 10 ? ("0" + mi) : String.valueOf(mi);
        String sStr = s < 10 ? ("0" + s) : String.valueOf(s);
        String time = yStr + "-" + mStr + "-" + dStr + "-" + hStr + "-" + miStr + "-" + sStr;
        return time;
    }

    public static Long getTimeInMillis() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
    }

    public static boolean judgmentlate(String provisionsTime) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Calendar c1 = Calendar.getInstance();
        String nowtime = getTimeStr1();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(dateFormat.parse(nowtime));
            c2.setTime(dateFormat.parse(provisionsTime));
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return c1.compareTo(c2) > 0;
    }

    /**
     * 根据时间字符串获取月份
     *
     * @param dateTime
     * @return
     */
    public static int getMonth(String dateTime) {
        //dateTime = dateTime.substring(0,10);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(dateTime));
            return c.get(Calendar.MONTH) + 1;
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    /**
     * 根据时间字符串获取天
     *
     * @param dateTime
     * @return
     */
    public static int getDay(String dateTime) {
        //dateTime = dateTime.substring(0,10);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(dateTime));
            return c.get(Calendar.DATE);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    public static int getYear(String dateTime) {
        //dateTime = dateTime.substring(0,10);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(dateTime));
            return c.get(Calendar.YEAR);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    /**
     * 将字符串的时间转换为Date对象
     *
     * @param dateString 时间字符串
     * @return 对应Date对象
     */
    public static Date getDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        if (dateString != null && !dateString.equals("null") && !dateString.equals("")) {
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        }
        return date;
    }

    /**
     * 将String 的时间转换为Date的YMD时间
     *
     * @param dateString
     * @return
     */
    public static Date getDateFormatYMD(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (dateString != null && !dateString.equals("null")) {
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        }
        return date;
    }

    /**
     * 将时间转换为YMD格式
     *
     * @param time
     * @return
     */
    public static String getDateFormatYMDstr(Date time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String timeStr = "";
        if (time != null) {
            timeStr = format.format(time);
        }
        return timeStr;
    }

    /**
     * 将2012-12-12 12:12:12 格式转换为20121212121212
     *
     * @param moTimeStr 2012-12-12 12:12:12
     * @return 20121212121212
     */
    public static String getPeerMoTimeStr(String moTimeStr) {
        moTimeStr = moTimeStr.replace("-", "");
        moTimeStr = moTimeStr.replace(" ", "");
        moTimeStr = moTimeStr.replace(":", "");
        return moTimeStr;
    }

    /**
     * 获取日期内的每一天 	YMD
     *
     * @param begin 开始
     * @param end   结束
     * @return 日期数组
     */
    public static List<Date> getDaysBetween(String begin, String end) {
        List<Date> days = new ArrayList<Date>();
        Date nextDay = getDateFormatYMD(begin);
        days.add(nextDay);
        while (nextDay.before(getDateFormatYMD(end))) {
            nextDay = getNextDay(nextDay);
            days.add(nextDay);
        }
        return days;
    }

    /**
     * 根据参入日期获取下一天
     *
     * @param nextDay
     * @return
     */
    private static Date getNextDay(Date nextDay) {
        Calendar cal = Calendar.getInstance();
        long time = nextDay.getTime() + (24 * 60 * 60 * 1000);
        cal.setTimeInMillis(time);
        String str = getTimeStr(cal.getTime());
        return getDateFormatYMD(str);
    }

    /**
     * 根据参入日期获取昨日
     *
     * @return
     */
    public static Date getYesterday(Date date) {
        Calendar cal = Calendar.getInstance();
        long time = date.getTime() - (24 * 60 * 60 * 1000);
        cal.setTimeInMillis(time);
        String str = getTimeStr(cal.getTime());
        return getDateFormatYMD(str);
    }

    /**
     * 将2015-09-21 11:12:23转换为2015年9月21日11时12分23秒
     *
     * @param time
     * @return
     */
    public static String getTimeStrCh(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(format.parse(time));
            int y, m, d, h, mi, s;
            y = cal.get(Calendar.YEAR);
            m = cal.get(Calendar.MONTH) + 1;
            d = cal.get(Calendar.DATE);
            h = cal.get(Calendar.HOUR_OF_DAY);
            mi = cal.get(Calendar.MINUTE);
            s = cal.get(Calendar.SECOND);
            return y + "年" + m + "月" + d + "日" + h + "时" + mi + "分" + s + "秒";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        }

    }

    public static String getTimeStrCh(Date time) {
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String timeStr = "";
        if (time != null) {
            timeStr = format.format(time);
        }
        return timeStr;
    }

    /**
     * 获取今天开始时间
     *
     * @return
     */
    public static Date getTodayStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 获取今天结束时间
     *
     * @return
     */
    public static Date getTodayEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }
}
