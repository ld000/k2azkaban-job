package com.k2data.platform.kmx;

public class KmxException extends RuntimeException {

    private static final long serialVersionUID = -1402473483872245308L;
    
    public KmxException() {
        super();
    }
    
    public KmxException(String msg) {
        super(msg);
    }
    
    public KmxException(Throwable e) {
        super(e);
    }
    
    public KmxException(String msg, Throwable e) {
        super(msg, e);
    }

}
