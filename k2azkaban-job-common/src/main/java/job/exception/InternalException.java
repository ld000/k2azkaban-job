package job.exception;

/**
 * 内部错误
 *
 * @author lidong
 */
public class InternalException extends RuntimeException {

    public InternalException() {
        super();
    }

    public InternalException(String msg) {
        super(msg);
    }

    public InternalException(Throwable e) {
        super(e);
    }

    public InternalException(String msg, Throwable e) {
        super(msg, e);
    }

}
