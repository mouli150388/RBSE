package com.tutorix.tutorialspoint.players;

import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource.BaseFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource.Factory;
import com.google.android.exoplayer2.upstream.TransferListener;

/**
 * A {@link Factory} that produces {@link DefaultHttpDataSource} instances.
 */
public final class DefaultHttpDataSourceFactorya extends BaseFactory {

    private final String userAgent;
    private final TransferListener  listener;
    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;
    private final boolean allowCrossProtocolRedirects;

// --Commented out by Inspection START (5/31/2018 4:03 PM):
//    public DefaultHttpDataSourceFactorya(String userAgent) {
//        this(userAgent, null);
//    }
// --Commented out by Inspection STOP (5/31/2018 4:03 PM)

    public DefaultHttpDataSourceFactorya(
            String userAgent, TransferListener  listener) {
        this(userAgent, listener, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, false);
    }

    public DefaultHttpDataSourceFactorya(String userAgent,
                                         TransferListener  listener, int connectTimeoutMillis,
                                         int readTimeoutMillis, boolean allowCrossProtocolRedirects) {
        this.userAgent = userAgent;
        this.listener = listener;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.allowCrossProtocolRedirects = allowCrossProtocolRedirects;
    }

    @Override
    public DefaultHttpDataSourceInternal createDataSourceInternal(
            HttpDataSource.RequestProperties defaultRequestProperties) {
        DefaultHttpDataSourceInternal d = new DefaultHttpDataSourceInternal(userAgent, null, listener, connectTimeoutMillis,
                readTimeoutMillis, allowCrossProtocolRedirects, defaultRequestProperties);
        d.setRequestProperty("STRING", "POST");
        return d;
    }

}

