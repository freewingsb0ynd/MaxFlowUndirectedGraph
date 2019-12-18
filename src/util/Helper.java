package util;

import java.util.ArrayList;

public class Helper {
    public static void increaseAtIndex(ArrayList<Integer> array, int index ,int delta){
        array.set(index, array.get(index) + delta);
    }
}
