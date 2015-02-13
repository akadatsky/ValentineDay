package com.andreykadatsky.valentineday;

import android.content.Context;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

public class WebUtil {

    public static class RequestResult<T> {
        private Exception e;
        private T result;

        public RequestResult(Exception e) {
            this.e = e;
        }

        public RequestResult(T result) {
            this.result = result;
        }

        public Exception getException() {
            return e;
        }

        public T getResult() {
            return result;
        }

        public boolean haveException() {
            return e != null;
        }

        public boolean resultOk() {
            return e == null;
        }
    }

    private static WebUtil instance;

    private Context context;

    public static WebUtil getInstance(Context context) {
        if (instance == null) {
            instance = new WebUtil();
        }
        instance.setContext(context);
        return instance;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    private static JSONObject getRequest(String url, List<NameValuePair> params) throws Exception {
        if (params != null) {
            String paramString = URLEncodedUtils.format(params, "utf-8");
            url += "?" + paramString;
        }

        int timeoutConnection = 3000;
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String output = EntityUtils.toString(httpEntity);
        return new JSONObject(output);
    }

    public RequestResult<String> getWish() {
        String url = "http://staging.mobindustry.net:20220/Wish/General";
        try {
            JSONObject response = getRequest(url, null);
            return new RequestResult<String>(response.getString("wish"));
        } catch (Exception e) {
            return new RequestResult<String>(e);
        }

    }


}
