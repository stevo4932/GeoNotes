package us.ststephens.geonotes.network;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import us.ststephens.geonotes.models.Note;

public class NoteApi extends BaseApi{
    private RequestQueue requestQueue;

    public NoteApi(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public GsonRequest<List<Note>> getNoteList() {
        Type type = new TypeToken<List<Note>>() {}.getType();
        Uri uri = parseUrl("notes");
        GsonRequest<List<Note>> request = new GsonRequest<>(Request.Method.GET, uri.toString(), type, null);
        request.setTag(Request.Method.GET);
        requestQueue.add(request);
        return request;
    }
}
