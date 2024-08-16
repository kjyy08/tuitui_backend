package suftware.tuitui.common.exception;

import lombok.Getter;
import suftware.tuitui.common.enumType.MsgCode;

@Getter
public class CustomException extends RuntimeException{
    MsgCode msg;
    String code;
    Object obj;

    public CustomException(MsgCode msg){
        this.msg = msg;
        this.obj = null;
    }

    public CustomException(MsgCode msg, String code){
        this.msg = msg;
        this.code = code;
    }

    public CustomException(MsgCode msg, Object obj){
        this.msg = msg;
        this.obj = obj;
    }

    public CustomException(MsgCode msg, String code, Object obj){
        this.msg = msg;
        this.code = code;
        this.obj = obj;
    }

    public CustomException(MsgCode msgCode, Exception e) {
    }
}
