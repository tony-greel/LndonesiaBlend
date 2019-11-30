package com.weiyun.liveness.libsaasclient.backend;
import android.os.Build;
import com.oliveapp.liblivenesscommon.utility.Metric;
import com.weiyun.liveness.libsaasclient.datatype.AccessInfo;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.PublicKey;
import java.util.Iterator;

/**
 * Created by jthao on 3/6/15.
 */
public class RequestWithSignatureHelper {
    private final static String TAG = RequestWithSignatureHelper.class.getSimpleName();
    private final static int MAX_BUFFER_SIZE = 1 * 1024 * 1024;
    public final static String sDefaultAlias = "yitutest";

    private final static String sPublicKeyContent = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiLegYm89cDdoDPp8SnaRY2CLe\n" +
            "pviV9y1ve+zgSOz8j2aE2ous4NuxgF38OqnqCbWTzbf1B9vvWgsFTvS+givHDRbo\n" +
            "2fhYkNUu36DS/4ltCkZlgZ00GXWUIks9WA1U7ACqyDvvj5MAigqS6wtONTI3wyqM\n" +
            "kW0MzeWP2qc5DQUKxQIDAQAB";

    public static JSONObject requestWithSignature(URL url, String method, AccessInfo accessInfo, String deviceId,
                                                  String requestMessage, int connectTimeoutMilli, int readTimeoutMilli) throws IOException, JSONException {

        URLConnection urlConn = url.openConnection();
        if (urlConn == null) {
            throw new IOException("cannot open url " + url.toString());
        }

        HttpURLConnection conn = (HttpURLConnection) urlConn;
        try {
            prepareConn(conn, method, accessInfo, deviceId, requestMessage,
                    connectTimeoutMilli, readTimeoutMilli);
        } catch (Exception e) {
            //LogUtil.e(TAG, "failed to prepareConn", e);
        }
        String responseString = doRequest(conn, requestMessage);
        return new JSONObject(responseString);
    }

    public static JSONObject requestWithSignature(URL url, String method, AccessInfo accessInfo, String deviceId,
                                                  JSONObject requestParams, int connectTimeoutMilli, int readTimeoutMilli) throws IOException, JSONException {
        URL finalUrl;
        switch (method.toUpperCase()) {
            case "GET":
                // generate URL parameters
                Iterator<String> iter = requestParams.keys();
                String query = "";
                while (iter.hasNext()) {
                    String key = iter.next();
                    if (query.length() != 0)
                        query += "&";
                    query += (key + "=" + requestParams.getString(key));
                }
                finalUrl = new URL(url.toString() + "?" + query);
                break;
            default:
                finalUrl = url;
        }

        String requestContent;
        requestContent = requestParams.toString();

        return requestWithSignature(finalUrl, method, accessInfo, deviceId, requestContent, connectTimeoutMilli, readTimeoutMilli);
    }


    private static void prepareConn(HttpURLConnection conn, String method, AccessInfo accessInfo, String deviceId, String requestContent,
                                    int connectTimeoutMilli, int readTimeoutMilli) throws Exception {
        conn.setRequestMethod(method);
        conn.setRequestProperty("Accept-Encoding", "");
        conn.setDoOutput(true);
        conn.setConnectTimeout(connectTimeoutMilli);
        conn.setReadTimeout(readTimeoutMilli);


        conn.setRequestProperty("x-access-id", accessInfo.getAccessId());
        conn.setRequestProperty("x-device-id", deviceId);
        PublicKey publicKey = SignatureUtil.RSAHelper.loadPublicKey(sPublicKeyContent);
        String md5hash = SignatureUtil.generateSignature(publicKey, accessInfo.getAccessKey(), requestContent, "");
        conn.setRequestProperty("x-signature", md5hash);

        conn.setRequestProperty("x-device-model", Build.MODEL);
        conn.setRequestProperty("x-os-version-release", Build.VERSION.RELEASE);
        conn.setRequestProperty("x-os-version-sdk", Build.VERSION.SDK_INT + "");
        conn.setRequestProperty("x-device-brand", Build.BRAND);
        conn.setRequestProperty("x-device-manufacturer", Build.MANUFACTURER);

        conn.setUseCaches(false);
    }

    public static String doRequest(HttpURLConnection conn, String requestContent) throws IOException, JSONException {
        if (conn == null) {
            throw new IOException("cannot open connection");
        }
        String responseString;
        try {
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            byte[] requestPaylod = requestContent.getBytes("UTF8");
            outputStream.write(requestPaylod, 0, requestPaylod.length);
            outputStream.flush();
            outputStream.close();
            Metric.addAsDouble(Metric.VERIFY_DEBUG_INFO, "Out traffic", requestPaylod.length);
            BufferedReader in = null;
            if (conn.getResponseCode() != 200) {
                in = new BufferedReader(new InputStreamReader(
                        conn.getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
            }
            StringBuffer responseBuf = new StringBuffer();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                responseBuf.append(inputLine);
            }
            in.close();
            Metric.addAsDouble(Metric.VERIFY_DEBUG_INFO, "In traffic", responseBuf.length());
            responseString = responseBuf.toString();
        } catch (Exception e) {
            responseString = new JSONObject().put("rtn", conn.getResponseCode()).put("message", conn.getResponseMessage()).toString();
            throw e;
        }
        return responseString;
    }
}
