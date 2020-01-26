package pl.pw.ocd.app.exceptions;

public class ForbiddenException extends Throwable {
    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
