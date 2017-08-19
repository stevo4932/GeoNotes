package us.ststephens.geonotes.Utils;
import android.support.annotation.NonNull;
import java.util.Observable;

public class ObservableObject<T> extends Observable{
    private T object;

    public T get() {
        return object;
    }

    public void set(T object) {
        if (!GeneralUtils.objectsEqual(this.object, object)) {
            this.object = object;
            this.setChanged();
            this.notifyObservers(object);
        }
    }
}
