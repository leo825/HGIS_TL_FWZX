package cn.geobeans.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局对象，主要用于缓存全局变量
 * Created by ice on 2016/3/23.
 */
public class Global {

    /**
     * 最近分享的文件个数
     * key为分组唯一标识符，可以是会议ID
     */
    public static Map<String, Integer> GROUP_ATTACHMENTS = new HashMap<String, Integer>();

    /**
     * 个人接收到的最近的分享文件个数
     * key为用户ID
     */
    public static Map<String, Integer> PERSONAL_ATTACHMENTS = new HashMap<String, Integer>();

    /**
     * 电子白板
     * key为分组唯一标识符，可以是会议ID
     */
    public static Map<String, String> GROUP_WHITEBOARD = new HashMap  <String, String>();
}
