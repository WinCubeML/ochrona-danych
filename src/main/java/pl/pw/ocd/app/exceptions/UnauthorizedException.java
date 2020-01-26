package pl.pw.ocd.app.exceptions;

public class UnauthorizedException extends Throwable {
    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
