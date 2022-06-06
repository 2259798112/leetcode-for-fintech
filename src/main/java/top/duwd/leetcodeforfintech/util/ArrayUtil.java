package top.duwd.leetcodeforfintech.util;

public class ArrayUtil {

    public static void print(int[] array) {
        if (array == null || array.length == 0) {
            System.out.println("empty array");
            return;
        }

        for (int i = 0; i < array.length - 1; i++) {
            System.out.print(array[i] + ",");
        }
        System.out.println(array[array.length - 1]);
    }
}
