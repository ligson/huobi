package org.ligson.huobi.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {
    private static Charset defaultCharset = Charset.forName("UTF-8");

    public static byte[] compress(byte[] buf) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream outputStream = new GZIPOutputStream(bos);
            outputStream.write(buf);
            outputStream.close();
            return bos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] compress(String str) {
        return compress(str, defaultCharset);
    }

    public static byte[] compress(String str, Charset charset) {
        return compress(str.getBytes(charset));

    }

    public static byte[] decompress(byte[] buf) {
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(buf));
            byte[] buffer = new byte[512];
            int n = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((n = gzipInputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, n);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    public static String decompressAsString(byte[] buf, Charset charset) {
        byte[] buffer = decompress(buf);
        if (buffer == null) {
            return null;
        } else {

            return new String(buffer, charset);

        }
    }

    public static String decompressAsString(byte[] buf) {
        return decompressAsString(buf, defaultCharset);
    }

}
