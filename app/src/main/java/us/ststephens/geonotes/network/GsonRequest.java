package us.ststephens.geonotes.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Type type;
    private byte[] body;
    private Response.Listener<T> listener;

    public GsonRequest(int method, String url, Type type, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.type = type;
    }

    public GsonRequest(int method, String url, Type type, Response.ErrorListener errorListener, Object body) {
        super(method, url, errorListener);
        this.type = type;
        this.body = gson.toJson(body).getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError{
        return super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=UTF-8";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body;
    }

    public void setListener(Response.Listener<T> listener) {
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            T parsed = gson.fromJson(json, type);
            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException|JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
