package suftware.tuitui.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import suftware.tuitui.common.enumType.MsgCode;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    MsgCode msgCode;
}
