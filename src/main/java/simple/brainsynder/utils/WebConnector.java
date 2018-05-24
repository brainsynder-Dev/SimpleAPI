package simple.brainsynder.utils;

import com.jcabi.aspects.Async;
import com.jcabi.aspects.Cacheable;
import lombok.NonNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class WebConnector {
    @Async @Cacheable(unit = TimeUnit.HOURS, asyncUpdate = true) @NonNull
    public static Future<OutputStream> getOutputStream(String link) {
        try {
            System.setProperty("http.agent", "Chrome");
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0");
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            return new CompletedFuture<>(connection.getOutputStream());
        } catch (Exception ignored) {}
        return null;
    }

    public static InputStream getInputStream(String link) {
        try {
            System.setProperty("http.agent", "Chrome");
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0");
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            return connection.getInputStream();
        } catch (Exception ignored) {}
        return null;
    }

    private static class CompletedFuture<T> implements Future<T> {
        T value;

        CompletedFuture(T value) {
            this.value = value;
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        public boolean isCancelled() {
            return false;
        }

        public boolean isDone() {
            return true;
        }

        public T get() {
            return this.value;
        }

        public T get(long timeout, TimeUnit unit) {
            return this.value;
        }
    }
}