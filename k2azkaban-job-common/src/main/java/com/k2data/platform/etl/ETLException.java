package com.k2data.platform.etl;

/**
 * 自定义异常
 * 
 * @author lidong
 */
public class ETLException extends RuntimeException {

    private static final long serialVersionUID = -1813853431051643261L;

    public ETLException() {
        super();
    }
    
    public ETLException(String msg) {
        super(msg);
    }
    
    public ETLException(Throwable e) {
        super(e);
    }
    
    public ETLException(String msg, Throwable e) {
        super(msg, e);
    }
    
}
