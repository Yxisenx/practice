package cn.onecolour.simple;

import java.util.*;

/**
 * @author yang
 * @date 2021/12/21
 * @title 14. 最长公共前缀
 * @description 编写一个函数来查找字符串数组中的最长公共前缀。
 * 如果不存在公共前缀，返回空字符串 ""。
 */
public class L14_longest_common_prefix {
    public static void main(String[] args) {
        System.out.println(Solution.longestCommonPrefix3(new String[]{"ab", "a"}));
    }


    static class Solution {
        public  static String longestCommonPrefix(String[] strs) {
            int len = strs.length;
            if (len == 1) {
                return strs[0];
            }
            List<String> strList = Arrays.asList(strs);
            String minLenStr = strList.stream().min(Comparator.comparingInt(String::length)).get();
            int minLen = minLenStr.length();
            if (minLen == 0) {
                return "";
            }

            if (minLen == 1) {
                return strList.stream().filter(s -> s.startsWith(minLenStr)).count() == len ? minLenStr : "";
            }
            for (int i = 1; i <= minLen; i++) {
                String substring = minLenStr.substring(0, i);
                if (strList.stream().filter(s -> s.startsWith(substring)).count() != len){
                    return substring.substring(0, substring.length() - 1);
                }
            }
            return minLenStr;
        }

        /**
         * 分治算法求最长公共前缀
         * @param strs 字符串数组
         * @return 前缀
         */
        public  static String longestCommonPrefix2(String[] strs) {
            int len = strs.length;
            if (len == 1) {
                return strs[0];
            }
            return null;
        }

        public static String longestCommonPrefix3(String[] strs) {
            int len = strs.length;
            if (len == 1) {
                return strs[0];
            }

            String s = null;

            for (String str : strs) {
                if (s == null) {
                    s = str;
                }
                if (Objects.equals(str, "")) {
                    return "";
                }
                // 求s和str的公共前缀
                String shortStr;
                String longStr;
                if (s.length() >= str.length()) {
                    shortStr = str;
                    longStr = s;
                } else {
                    shortStr = s;
                    longStr = str;
                }
                for (int j = 0; j < shortStr.length(); j++) {
                    if (shortStr.charAt(j) != longStr.charAt(j)) {
                        s = shortStr.substring(0, j);
                        break;
                    } else if (j == shortStr.length() - 1) {
                        s = shortStr;
                    }
                }
                if (s.length() == 0) {
                    return "";
                }
            }
            return s;

        }



    }
}
