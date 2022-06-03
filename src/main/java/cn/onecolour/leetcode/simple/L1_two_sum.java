package cn.onecolour.leetcode.simple;

import java.util.*;

/**
 * @author yang
 * @date 2021/12/20
 * @title 1. 两数之和
 * @description 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 * 你可以按任意顺序返回答案。
 */
public class L1_two_sum {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(Solution.twoSum2(new int[]{3,2,4}, 6)));
    }


    static class Solution {
        public static int[] twoSum(int[] nums, int target) {
            Map<Integer, Integer> map = new HashMap<>();
            int temp;
            for (int i = 0; i < nums.length; i++) {
                temp = target - nums[i];
                if (map.containsKey(temp)) {
                    return new int[]{map.get(target - nums[i]), i};
                } else {
                    map.put(nums[i], i);
                }
            }
            return null;
        }

        public static int[] twoSum2(int[] nums, int target) {
            List<Integer> list = new ArrayList<>();
            int temp;
            for (int i = 0; i < nums.length; i++) {
                temp = target - nums[i];
                if (list.contains(temp)) {
                    return new int[]{list.indexOf(temp), i};
                } else {
                    list.add(nums[i]);
                }
            }
            return null;
        }
    }
}
