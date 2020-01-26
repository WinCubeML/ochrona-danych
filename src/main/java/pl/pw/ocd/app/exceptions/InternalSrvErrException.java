package pl.pw.ocd.app.exceptions;

public class InternalSrvErrException extends Throwable {
    public InternalSrvErrException() {
        super();
    }

    public InternalSrvErrException(String message) {
        super(message);
    }
}
