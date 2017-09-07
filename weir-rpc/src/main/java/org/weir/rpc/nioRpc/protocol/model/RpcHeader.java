package org.weir.rpc.nioRpc.protocol.model;

import java.io.Serializable;

import org.weir.rpc.nioRpc.util.ByteUtil;


/**
 * Represents a RPC header field.
 * <p>
 * 
 * The RPC header fields use the generic format as follows:
 * 
 * <pre>
 * 000-------------------------------------------------------------------015-------------------------------------------------------------------031
 * |                                 magic                                |                                header size                           |                      
 * 032--------------------------------039-----------044----045----046----047--------------------------------055--------------------------------063
 * |                version            |      st     |  hb  |  ow  |  rp  |        status code               |            reserved               |                
 * 064-----------------------------------------------------------------------------------------------------------------------------------------095
 * |                                                                 message id                                                                  |
 * |                                                                                                                                             |
 * 096-----------------------------------------------------------------------------------------------------------------------------------------127
 * |                                                                  body size                                                                  |
 * 128-----------------------------------------------------------------------------------------------------------------------------------------159
 * 
 * st = serialization type.
 * hb = heartbeat flag, set '0000 0100' means it is a heatbeat message.
 * ow = one way   flag, set '0000 0010' means it is one way message, the client doesn't wait for a response.
 * rp = response  flag, set '0000 0001' means it is response message, otherwise it's a request message.
 * </pre>
 * 
 * @author mindwind
 * @version 1.0, Jul 18, 2014
 */
public class RpcHeader implements Serializable {

	private static final long serialVersionUID = -67119913240566784L;
	private static final byte ST_MASK = (byte) 0x1f;
	private static final byte HB_MASK = (byte) 0x20;
	private static final byte OW_MASK = (byte) 0x40;
	private static final byte RP_MASK = (byte) 0x80;
	public static final short MAGIC = (short) 0xcaf6;
	public static final short HEADER_SIZE = (short) 20;
	public static final byte MAGIC_0 = ByteUtil.short2bytes(MAGIC)[0];
	public static final byte MAGIC_1 = ByteUtil.short2bytes(MAGIC)[1];
	public static final byte VERSION = (byte) 1;

	private short magic = MAGIC;
	private short headerSize = HEADER_SIZE;
	private byte version = VERSION;
	private byte st = (byte) 1;
	public byte getSt() {
		return st;
	}

	private byte hb = (byte) 0;
	private byte ow = (byte) 0;
	private byte rp = (byte) 0;
	private byte statusCode = (byte) 0;
	private byte reserved = (byte) 0;
	private long id = (long) 0;
	private int bodySize = (int) 0;

	public void setSt(byte st) {
		this.st = (byte) (st & ST_MASK);
	}

	public void setHb() {
		this.hb = HB_MASK;
	}

	public byte getHb() {
		return hb;
	}

	public byte getOw() {
		return ow;
	}

	public byte getRp() {
		return rp;
	}

	public void setHb(byte hb) {
		this.hb = (byte) (hb & HB_MASK);
	}

	public boolean isHb() {
		return hb == HB_MASK;
	}

	public void setOw() {
		this.ow = OW_MASK;
	}

	public void setOw(byte ow) {
		this.ow = (byte) (ow & OW_MASK);
	}

	public boolean isOw() {
		return ow == OW_MASK;
	}

	public void setRp() {
		this.rp = RP_MASK;
	}

	public void setRp(byte rp) {
		this.rp = (byte) (rp & RP_MASK);
	}

	public boolean isRp() {
		return rp == RP_MASK;
	}

	public short getMagic() {
		return magic;
	}

	public void setMagic(short magic) {
		this.magic = magic;
	}

	public short getHeaderSize() {
		return headerSize;
	}

	public void setHeaderSize(short headerSize) {
		this.headerSize = headerSize;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(byte statusCode) {
		this.statusCode = statusCode;
	}

	public byte getReserved() {
		return reserved;
	}

	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBodySize() {
		return bodySize;
	}

	public void setBodySize(int bodySize) {
		this.bodySize = bodySize;
	}

}
