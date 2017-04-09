package server;

public enum CommandType {
    PING(0),
    REQUEST_TO_JOIN(1),
    JOB(2),
    ACK_JOB(3),
    DONE_NOT_FOUND(4),
    DONE_FOUND(5),
    NOT_DONE(6),
    SHUTDOWN(7),
    HASH(8),
    ILLEGAL(9);

    private int code;
    private CommandType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
