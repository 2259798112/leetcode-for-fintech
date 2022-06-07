package top.duwd.leetcodeforfintech.util;

public class HashCodeForString {
    public static void main(String[] args) {
        String str = "abc";
        int h1 = str.hashCode();
        System.out.println(h1);
        int h2 = hashCode(null);
        System.out.println(h2);
    }


    /**
     * Returns a hash code for this string. The hash code for a
     * {@code String} object is computed as
     * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
     * s[i] is the character of the string,
     * n is the length
     * (The hash value of the empty string is zero.)
     *
     * 为什么以31为底
     * https://www.cnblogs.com/taotaobaibai/articles/13726618.html
     */
    public static int hashCode(String str) {
        if (str == null) {
            throw new RuntimeException("hashCode() get NullPointException");
        }

        int length = str.length();
        if (length == 0) return 0;

        int hash = 0;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            int cc = (int)Math.pow(31,length-1-i);
            hash += c * cc;
        }

        return hash;
    }


}
