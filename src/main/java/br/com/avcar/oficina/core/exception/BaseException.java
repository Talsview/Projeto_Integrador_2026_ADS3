package br.com.avcar.oficina.core.exception;

public abstract class BaseException extends RuntimeException {

    private final String title;
    private final String motive;
    private final Severity severity;

    protected BaseException(String title, String message, String motive, Severity severity) {
        super(message);
        this.title = title;
        this.motive = motive;
        this.severity = severity;
    }

    public String getTitle() {
        return title;
    }

    public String getMotive() {
        return motive;
    }

    public Severity getSeverity() {
        return severity;
    }
}
