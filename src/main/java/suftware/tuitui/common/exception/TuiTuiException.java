package suftware.tuitui.common.exception;

import lombok.Getter;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;

@Getter
public class TuiTuiException extends RuntimeException{
    private final TuiTuiMsgCode msg;
    private final Object obj;

    public TuiTuiException(TuiTuiMsgCode msg){
        this.msg = msg;
        this.obj = null;
    }

    public TuiTuiException(TuiTuiMsgCode msg, Object obj){
        this.msg = msg;
        this.obj = obj;
    }
}
