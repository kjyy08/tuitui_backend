package suftware.tuitui.config.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private Integer status;
    private String message;
    private Object data;

    public Message(){
        this.status = null;
        this.message = null;
        this.data = null;
    }
}
