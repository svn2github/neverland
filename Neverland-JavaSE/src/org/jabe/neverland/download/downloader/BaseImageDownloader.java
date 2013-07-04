package org.jabe.neverland.download.downloader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaseImageDownloader implements Downloader {
	/** {@value} */
	public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000; // milliseconds
	/** {@value} */
	public static final int DEFAULT_HTTP_READ_TIMEOUT = 20 * 1000; // milliseconds

	/** {@value} */
	protected static final int BUFFER_SIZE = 32 * 1024; // 32 Kb
	/** {@value} */
	protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

	protected static final int MAX_REDIRECT_COUNT = 5;

	private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. "
			+ "You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";

	protected final int connectTimeout;
	protected final int readTimeout;

	public BaseImageDownloader() {
		this.connectTimeout = DEFAULT_HTTP_CONNECT_TIMEOUT;
		this.readTimeout = DEFAULT_HTTP_READ_TIMEOUT;
	}

	public BaseImageDownloader(int connectTimeout, int readTimeout) {
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	@Override
	public InputStream getStream(String imageUri, Object extra, SizeBean sb) throws IOException {
		switch (Scheme.ofUri(imageUri)) {
			case HTTP:
			case HTTPS:
				return getStreamFromNetwork(imageUri, extra);
			case FILE:
				return getStreamFromFile(imageUri, extra);
			case CONTENT:
//				return getStreamFromContent(imageUri, extra);
			case ASSETS:
//				return getStreamFromAssets(imageUri, extra);
			case DRAWABLE:
//				return getStreamFromDrawable(imageUri, extra);
			case UNKNOWN:
			default:
				return getStreamFromOtherSource(imageUri, extra);
		}
	}

	protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
		HttpURLConnection conn = createConnection(imageUri);
		int redirectCount = 0;
		while (conn.getResponseCode() / 100 == 3 && redirectCount < MAX_REDIRECT_COUNT) {
			conn = createConnection(conn.getHeaderField("Location"));
			redirectCount++;
		}
		return new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
	}

	protected HttpURLConnection createConnection(String url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		return conn;
	}

	protected InputStream getStreamFromFile(String imageUri, Object extra) throws IOException {
		String filePath = Scheme.FILE.crop(imageUri);
		return new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE);
	}

	protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
		throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_SCHEME, imageUri));
	}
}