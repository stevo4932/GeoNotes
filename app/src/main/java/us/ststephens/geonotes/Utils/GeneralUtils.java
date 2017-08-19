package us.ststephens.geonotes.Utils;

public class GeneralUtils {
    public static boolean objectsEqual(Object obj1, Object obj2) {
        boolean isOneNull = obj1 == null;
        boolean isTwoNull = obj2 == null;
        return isOneNull && isTwoNull || !(isOneNull || isTwoNull) && obj1.equals(obj2);
    }
}
