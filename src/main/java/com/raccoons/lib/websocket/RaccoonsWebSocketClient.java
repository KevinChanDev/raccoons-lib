package com.raccoons.lib.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

public class RaccoonsWebSocketClient extends WebSocketClient {

    private SocketEventListener socketEventListener;

    public RaccoonsWebSocketClient(URI serverURI) {
        super(serverURI);
    }

    public RaccoonsWebSocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public RaccoonsWebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        if (socketEventListener != null) {
            socketEventListener.onOpen();
        }
    }

    @Override
    public void onMessage(String message) {
        if (socketEventListener != null) {
            socketEventListener.onMessage(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (socketEventListener != null) {
            socketEventListener.onClose(code, reason, remote);
        }
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void setSocketEventListener(SocketEventListener socketEventListener) {
        this.socketEventListener = socketEventListener;
    }

    public interface SocketEventListener {

        void onOpen();

        void onMessage(String message);

        void onClose(int code, String reason, boolean remote);

    }
}