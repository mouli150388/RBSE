package com.tutorix.tutorialspoint.players;

import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.BaseDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A {@link DataSource} for reading local files.
 */
public final class SDCardDataSource extends BaseDataSource {


    private RandomAccessFile file;
    private Uri uri;
    private long bytesRemaining;
    private long fileType = 0;
    private boolean opened;

    public SDCardDataSource() {
        super(false);
    }

    /**
     * @param listener An optional listener.
     */
    public SDCardDataSource(TransferListener listener) {
        this();
        if (listener != null) {
            addTransferListener(listener);
        }

    }

    @Override
    public long open(DataSpec dataSpec) throws FileDataSource.FileDataSourceException {
        try {
            uri = dataSpec.uri;
            transferInitializing(dataSpec);
            file = new RandomAccessFile(dataSpec.uri.getPath(), "r");
            file.seek(dataSpec.position);
            bytesRemaining = dataSpec.length == C.LENGTH_UNSET ? file.length() - dataSpec.position
                    : dataSpec.length;
            if (bytesRemaining < 0) {
                throw new EOFException();
            }
        } catch (IOException e) {
            throw new FileDataSource.FileDataSourceException(e);
        }

        opened = true;
        transferStarted(dataSpec);
        fileType = uri.toString().contains("file.key") ? 1 : 0;
        return bytesRemaining;
    }

    @Override
    public int read(byte[] buffer, int offset, int readLength) throws FileDataSource.FileDataSourceException {

        if (readLength == 0) {
            return 0;
        } else if (bytesRemaining == 0) {
            return C.RESULT_END_OF_INPUT;
        } else {
            int bytesRead;
            try {
                bytesRead = file.read(buffer, offset, (int) Math.min(bytesRemaining, readLength));
                if (fileType == 1) {

                    for (int i = 0; i < 4; i++) {
                        byte temp = buffer[i];
                        buffer[i] = buffer[8 - i - 1];
                        buffer[8 - i - 1] = temp;
                    }

                    for (int i = 8; i < 12; i++) {
                        byte temp = buffer[i];
                        buffer[i] = buffer[16 + 8 - i - 1];
                        buffer[16 + 8 - i - 1] = temp;
                    }
                }
            } catch (IOException e) {
                throw new FileDataSource.FileDataSourceException(e);
            }

            if (bytesRead > 0) {
                bytesRemaining -= bytesRead;
                if (fileType == 1) {
                    bytesRemaining = 0;
                }
                bytesTransferred(bytesRead);
            }
            return bytesRead;
        }
    }

    @Override
    public Uri getUri() {
        return uri;
    }

    @Override
    public void close() throws FileDataSource.FileDataSourceException {
        uri = null;
        try {
            if (file != null) {
                file.close();
            }
        } catch (IOException e) {
            throw new FileDataSource.FileDataSourceException(e);
        } finally {
            file = null;
            if (opened) {
                opened = false;
                transferEnded();
            }
        }
    }

}
