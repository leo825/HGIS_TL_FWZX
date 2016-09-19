package cn.geobeans.common.model;

import cn.geobeans.common.enums.JsonResponseStatusEnum;

/**
 * Created by ghx on 2015/1/6.
 * 用于封装REST服务返回的JSON，
 * 其中data表示主要结果内容
 */
public class JsonResponse {
    private String status = JsonResponseStatusEnum.SUCCESS.toString();
    private String message="";
    private Object data=null;

    public JsonResponse() {
    }

    public JsonResponse(Object data) {
        this.data = data;
    }

    public JsonResponse(JsonResponseStatusEnum status, String message) {
        this.status = status.toString();
        this.message = message;
    }

    public JsonResponse(JsonResponseStatusEnum status, String message, Object data) {
        this.status = status.toString();
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
