package pl.pw.ocd.app.exceptions;

public class ForbiddenCookieException extends Throwable {
    public ForbiddenCookieException() {
        super();
    }

    public ForbiddenCookieException(String message) {
        super(message);
    }
}
