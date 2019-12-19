package util;

import java.util.ArrayList;

public class Helper {
    public static void increaseAtIndex(ArrayList<Integer> array, int index ,int delta){
        array.set(index, array.get(index) + delta);
    }

    public static <T> T getLast(ArrayList<T> array)
    {
        return array.get(array.size() - 1);
    }

    public static void popLast(ArrayList<?> array)
    {
        array.remove(array.size() - 1);
    }

}
