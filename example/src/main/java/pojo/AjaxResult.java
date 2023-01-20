package pojo;

import java.util.HashMap;

/**
 * 靠, 这个东西往前端传数据真的好用
 * @author ZAY
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    private AjaxResult() {
    }
    public AjaxResult(int code, String msg) {
        super.put("code", code);
        super.put("msg", msg);
    }
    public AjaxResult(int code, String msg, Object data) {
        super.put("code", code);
        super.put("msg", msg);
        if ( data!= null) {
            super.put("data", data);
        }
    }
    public static AjaxResult success() {
        return success("操作成功");
    }
    public static AjaxResult success(Object data) {
        return success("操作成功", data);
    }
    public static AjaxResult success(String msg) {
        return success(msg, (Object) null);
    }
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(200, msg, data);
    }

    public static AjaxResult error() {
        return error("操作失败");
    }
    public static AjaxResult error(String msg) {
        return error(msg, (Object)null);
    }
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(500, msg, data);
    }
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, (Object)null);
    }
}
