package org.weir.socket.tcp;

/**
 * Created by tong on 16/10/4.
 */
public interface Connection {
    void connect() throws Exception;

    void disconnect();

    boolean isConnected();
}
