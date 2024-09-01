package suftware.tuitui.common.jwt;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
    private final JwtMsgCode msg;

    public JwtException(JwtMsgCode msg) {
        this.msg = msg;
    }
}
