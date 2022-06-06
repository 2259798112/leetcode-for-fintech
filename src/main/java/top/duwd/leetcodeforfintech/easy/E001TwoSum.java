package top.duwd.leetcodeforfintech.easy;

import top.duwd.leetcodeforfintech.util.ArrayUtil;
import top.duwd.leetcodeforfintech.util.RandomUtil;

/*
https://leetcode.com/problems/two-sum/
Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.

You may assume that each input would have exactly one solution, and you may not use the same element twice.

You can return the answer in any order.

Example 1:

Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].


Example 2:

Input: nums = [3,2,4], target = 6
Output: [1,2]


Example 3:

Input: nums = [3,3], target = 6
Output: [0,1]


Constraints:

2 <= nums.length <= 104
-109 <= nums[i] <= 109
-109 <= target <= 109
Only one valid answer exists.

Follow-up: Can you come up with an algorithm that is less than O(n2) time complexity?
 */
public class E001TwoSum {

    public static void main(String[] args) {
        int[] nums = nums(100);
        int target = 25;
        int[] result = a1(nums, target);
        System.out.print("nums=");
        ArrayUtil.print(nums);
        System.out.print("result=");
        ArrayUtil.print(result);

    }


    public static int[] nums(int length) {
        int[] nums = new int[length];
        for (int i = 0; i < length; i++) {
            int r = RandomUtil.r(-109, 109);
            nums[i] = r;
        }
        return nums;
    }

    public static int[] a1(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            int t = target - nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] == t) {
                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }

        return new int[0];
    }
}



