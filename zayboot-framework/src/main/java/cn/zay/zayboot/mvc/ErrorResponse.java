package cn.zay.zayboot.mvc;

import cn.zay.zayboot.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * @author ZAY
 */
@Getter
@ToString
@NoArgsConstructor
public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = DateUtil.now();
    }
}
