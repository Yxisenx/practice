package cn.onecolour.simple;

/**
 * @author yang
 * @date 2021/12/20
 * @title 9.回文数
 * @description 给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。
 * 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * 例如，121 是回文，而 123 不是。
 */
public class L9_palindrome_number {
    public static void main(String[] args) {
        System.out.println(Solution.isPalindrome(10101));
    }

    static class Solution {
        public static boolean isPalindrome(int x) {
            if (x < 0) {
                return false;
            }
            if (x < 10) {
                return true;
            }
            String str = String.valueOf(x);
            int len = str.length();
            String str1;
            String str2;
            str1 = str.substring(0, len / 2);
            if (len % 2 == 0) {
                str2 = new StringBuilder(str.substring(len / 2, len)).reverse().toString();
            } else {
                str2 = new StringBuilder(str.substring(len / 2 + 1, len)).reverse().toString();
            }
            return str1.equals(str2);
        }
    }
}
