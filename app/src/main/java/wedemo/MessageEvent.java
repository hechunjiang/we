package wedemo;

public class MessageEvent {
    private String message;
    private Object data;
    public  MessageEvent(String message){
        this.message=message;
    }

    public MessageEvent(String message, Object data) {
        this.message = message;
        this.data = data;
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
