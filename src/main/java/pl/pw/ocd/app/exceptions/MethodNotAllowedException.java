package pl.pw.ocd.app.exceptions;

public class MethodNotAllowedException extends Throwable {
    public MethodNotAllowedException() {
        super();
    }

    public MethodNotAllowedException(String message) {
        super(message);
    }
}
