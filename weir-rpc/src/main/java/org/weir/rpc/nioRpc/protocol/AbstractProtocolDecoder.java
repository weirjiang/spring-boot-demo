package org.weir.rpc.nioRpc.protocol;

import org.weir.rpc.nioRpc.util.ByteArrayBuffer;


/**
 * A protocol decoder base class.
 * <p>
 * It contains three index for concrete protocol decoder using:
 * - splitIndex : The separator index position according to specific protocol, indicates next byte nearby last complete protocol object.<br>
 * - searchIndex: The cursor index position for protocol process, indicates next byte would be process by protocol codec.<br>
 * - stateIndex : The index position for protocol state machine process.
 * 
 * 
 * @author mindwind
 * @version 1.0, Feb 3, 2013
 */
abstract public class AbstractProtocolDecoder extends AbstractProtocolCodec {
	
	
	protected static final int START = 0 ;
	protected static final int END   = -1;
	

	  protected int             splitIndex        = 0                                     ;
	  protected int             searchIndex       = 0                                     ;
	  protected int             stateIndex        = 0                                     ;
	         protected int             state             = START                                 ;
	  protected int             defaultBufferSize = 2048                                  ;
	  protected int             maxSize           = defaultBufferSize * 1024              ;
	  protected ByteArrayBuffer buf               = new ByteArrayBuffer(defaultBufferSize);
	
	
	// ~ ----------------------------------------------------------------------------------------------------------

	
	public void reset() {
		splitIndex  = 0                                     ;
		searchIndex = 0                                     ;
		stateIndex  = 0                                     ; 
		state       = START                                 ;
		buf         = new ByteArrayBuffer(defaultBufferSize);
	}
	
	protected void adapt() {
		if (splitIndex > 0 && splitIndex < buf.length()) {
			byte[] tailBytes = buf.array(splitIndex, buf.length());
			buf.clear();
			buf.append(tailBytes);
			splitIndex = 0;
			searchIndex = buf.length();
		}
		
		if (splitIndex > 0 && splitIndex == buf.length()) {
			buf.clear();
			splitIndex = searchIndex = 0;
		}
		
		if (buf.length() == 0 && buf.capacity() > maxSize * 2) {
			buf.reset(defaultBufferSize);
		}
	}
	
}
