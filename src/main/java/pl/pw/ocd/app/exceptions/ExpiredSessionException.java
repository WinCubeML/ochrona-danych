package pl.pw.ocd.app.exceptions;

public class ExpiredSessionException extends Throwable {
    public ExpiredSessionException() {
        super();
    }

    public ExpiredSessionException(String message) {
        super(message);
    }
}
