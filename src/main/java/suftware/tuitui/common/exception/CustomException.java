package suftware.tuitui.common.exception;

import lombok.Getter;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;

@Getter
public class CustomException extends RuntimeException{
    private TuiTuiMsgCode msg;
    private String code;
    private Object obj;

    public CustomException(TuiTuiMsgCode msg){
        this.msg = msg;
        this.obj = null;
    }

    public CustomException(TuiTuiMsgCode msg, String code){
        this.msg = msg;
        this.code = code;
    }

    public CustomException(TuiTuiMsgCode msg, Object obj){
        this.msg = msg;
        this.obj = obj;
    }

    public CustomException(TuiTuiMsgCode msg, String code, Object obj){
        this.msg = msg;
        this.code = code;
        this.obj = obj;
    }

    public CustomException(TuiTuiMsgCode tuiTuiMsgCode, Exception e) {
    }
}
