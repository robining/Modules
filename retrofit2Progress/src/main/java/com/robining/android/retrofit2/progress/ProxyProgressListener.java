package com.robining.android.retrofit2.progress;

import okhttp3.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

public class ProxyProgressListener extends EventListener implements ProgressListener {
    private ProgressListener progressListener;
    private EventListener eventListener;

    public ProgressListener getProgressListener() {
        return progressListener;
    }

    public ProxyProgressListener setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public ProxyProgressListener setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
        return this;
    }

    @Override
    public void onUpProgress(long progress, long total) {
        if (progressListener == null) {
            return;
        }
        progressListener.onUpProgress(progress, total);
    }

    @Override
    public void onDownProgress(long progress, long total, long fixTotal) {
        if (progressListener == null) {
            return;
        }
        progressListener.onDownProgress(progress, total, fixTotal);
    }

    @Override
    public void onExceptionProgress(Throwable ex) {
        if (progressListener == null) {
            return;
        }
        progressListener.onExceptionProgress(ex);
    }

    @Override
    public void callStart(Call call) {
        if (eventListener == null) {
            return;
        }
        eventListener.callStart(call);
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        if (eventListener == null) {
            return;
        }
        eventListener.dnsStart(call, domainName);
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        if (eventListener == null) {
            return;
        }
        eventListener.dnsEnd(call, domainName, inetAddressList);
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        if (eventListener == null) {
            return;
        }
        eventListener.connectStart(call, inetSocketAddress, proxy);
    }

    @Override
    public void secureConnectStart(Call call) {
        if (eventListener == null) {
            return;
        }
        eventListener.secureConnectStart(call);
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        if (eventListener == null) {
            return;
        }
        eventListener.secureConnectEnd(call, handshake);
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        if (eventListener == null) {
            return;
        }
        eventListener.connectEnd(call, inetSocketAddress, proxy, protocol);
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        if (eventListener == null) {
            return;
        }
        eventListener.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        if (eventListener == null) {
            return;
        }
        eventListener.connectionAcquired(call, connection);
    }

    @Override
    public void connectionReleased(Call call, Connection connection) {
        if (eventListener == null) {
            return;
        }
        eventListener.connectionReleased(call, connection);
    }

    @Override
    public void requestHeadersStart(Call call) {
        if (eventListener == null) {
            return;
        }
        eventListener.requestHeadersStart(call);
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        if (eventListener == null) {
            return;
        }
        eventListener.requestHeadersEnd(call, request);
    }

    @Override
    public void requestBodyStart(Call call) {
        if (eventListener == null) {
            return;
        }
        eventListener.requestBodyStart(call);
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        if (eventListener == null) {
            return;
        }
        eventListener.requestBodyEnd(call, byteCount);
    }

    @Override
    public void responseHeadersStart(Call call) {
        if (eventListener == null) {
            return;
        }
        eventListener.responseHeadersStart(call);
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        if (eventListener == null) {
            return;
        }
        eventListener.responseHeadersEnd(call, response);
    }

    @Override
    public void responseBodyStart(Call call) {
        if (eventListener == null) {
            return;
        }
        eventListener.responseBodyStart(call);
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        if (eventListener == null) {
            return;
        }
        eventListener.responseBodyEnd(call, byteCount);
    }

    @Override
    public void callEnd(Call call) {
        if (eventListener == null) {
            return;
        }
        eventListener.callEnd(call);
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        if (eventListener == null) {
            return;
        }
        eventListener.callFailed(call, ioe);
    }
}
