package pl.pw.ocd.app.exceptions;

public class BadRequestException extends Throwable {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
