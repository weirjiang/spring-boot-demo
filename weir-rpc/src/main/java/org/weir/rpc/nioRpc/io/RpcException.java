package org.weir.rpc.nioRpc.io;


/**
 * @author mindwind
 * @version 1.0, Aug 8, 2014
 */
public final class RpcException extends RuntimeException {

	
	private static final long serialVersionUID  = -4168884981656035910L;
	
	
	public static final byte UNKNOWN         = 0 ;
	public static final byte NETWORK         = 10;
	public static final byte CLIENT_BAD_REQ  = 40;
	public static final byte CLIENT_TIMEOUT  = 41;
    public static final byte SERVER_ERROR    = 50;
    public static final byte SERVER_TIMEOUT  = 51;
    public static final byte SERVER_OVERLOAD = 52;
	
	
	 public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	private byte code;
	
	
	public RpcException() {}
	
	public RpcException(byte code, String message) {
        super(message);
        this.code = code;
    }

	public RpcException(byte code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
}
