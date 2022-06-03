package cn.onecolour.leetcode.mid;

/**
 * @author yang
 * @date 2022/4/3
 * @title 19. 删除链表的倒数第 N 个结点
 * @description 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 * 链表中结点的数目为 sz
 * 1 <= sz <= 30
 * 0 <= Node.val <= 100
 * 1 <= n <= sz
 */
public class L19_remove_nth_from_end {

    public static void main(String[] args) {
        for (int sz = 1; sz <= 30; sz++) {
            int[] array = new int[sz];
            for (int i = 1; i <= array.length; i++) {
                array[i - 1] = i;
            }
            for (int n = 1; n <= sz; n++) {
                ListNode node = ListNode.generateListNode(array);
                node = Solution.removeNthFromEnd1(node, n);
                System.out.printf("[%s]%n", node);
            }
        }
        /*ListNode node = ListNode.generateListNode(1, 2);
        node = Solution.removeNthFromEnd(node, 1);
        System.out.printf("[%s]", node);*/
    }

    public static class ListNode {
        int val;
        ListNode next;

        public static ListNode generateListNode(int... values) {
            if (values == null || values.length == 0) {
                return null;
            }

            if (values.length == 1) {
                return new ListNode(values[0]);
            }

            ListNode tmp = null;

            for (int i = values.length - 1; i >= 0; i--) {
                tmp = new ListNode(values[i], tmp);
            }
            return tmp;
        }

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder().append(val);
            ListNode nextNode = this.next;
            while (nextNode != null) {
                sb.append(",").append(nextNode.val);
                nextNode = nextNode.next;
            }
            return sb.toString();
        }
    }

    static class Solution {
        // 解法1
        public static ListNode removeNthFromEnd(ListNode head, int n) {
            int size = 1;
            ListNode node = head.next;
            // 第一遍遍历统计链表长度
            while (node != null) {
                size++;
                node = node.next;
            }
            // 第二遍遍历, 清除倒数n个元素
            ListNode result;
            ListNode tmp = head;
            if (size == n) {
                // 清除第一个元素
                result = tmp.next;
            } else {
                // 此时开头必为head
                result = head;
                for (int i = 1; i <= size - 1; i++) {
                    if (n == size - i) {
                        tmp.next = tmp.next.next;
                        break;
                    }
                    tmp = tmp.next;
                }
            }
            return result;
        }

        // 回溯法遍历,
        public static ListNode removeNthFromEnd1(ListNode head, int n) {
            int traverse = traverse(head, n);
            if(traverse == n)
                return head.next;
            return head;
        }

        private static int traverse(ListNode node, int n) {
            if(node == null)
                return 0;
            int num = traverse(node.next, n);
            if(num == n)
                node.next = node.next.next;
            return num + 1;
        }
    }

}
