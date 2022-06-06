package top.duwd.leetcodeforfintech.util;

import java.util.Random;

public class RandomUtil {

    /*
    int nextlnt()
    生成一个随机的 int 值，该值介于 int 的区间，也就是 -231~231-1。

    int nextlnt(int n)	生成一个随机的 int 值，该值介于 [0,n)，包含 0 而不包含 n。

     */

    public static final Random R = new Random();

    public static int r(int min, int max) {

        int count = 0;
        while (true) {

            int bound = Math.abs(min) >= Math.abs(max) ? Math.abs(min) + 1 : Math.abs(max) + 1;
            count++;
            int i = R.nextInt(bound);
            if (min < 0) {
                int np = R.nextBoolean() ? 1 : -1;
                i = np * i;
            }

            if (i <= max && i >= min) {
                System.out.println("count=" + count);

                return i;
            }
        }
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            int r = r(-25, 10);
            System.out.println("index=" + i + " " + r);
        }
    }
}
