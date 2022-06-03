package cn.onecolour.leetcode.mid;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yang
 * @date 2022/4/4
 * @title 36. 有效的数独
 * @description 请你判断一个 9 x 9 的数独是否有效。只需要 根据以下规则 ，验证已经填入的数字是否有效即可。
 * 数字 1-9 在每一行只能出现一次。
 * 数字 1-9 在每一列只能出现一次。
 * 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。（请参考示例图）
 * <p>
 * 注意：
 * <p>
 * 一个有效的数独（部分已被填充）不一定是可解的。
 * 只需要根据以上规则，验证已经填入的数字是否有效即可。
 * 空白格用 '.'表示。
 */
@SuppressWarnings("unused")
public class L39_is_valid_sudoku {
    public static void main(String[] args) {
        char[][] sudoku = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };

        System.out.println(isValidSudoku1(sudoku));
    }

    private static boolean isValidSudoku(char[][] board) {
        Set<Character> row = new HashSet<>(9);
        Set<Character> column = new HashSet<>(9);

        // 先验证行和列
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    if (row.contains(board[i][j])) {
                        return false;
                    } else {
                        row.add(board[i][j]);
                    }
                }
                // 验证列
                if (board[j][i] != '.') {
                    if (column.contains(board[j][i])) {
                        return false;
                    } else {
                        column.add(board[j][i]);
                    }
                }
            }
            row.clear();
            column.clear();
        }

        //noinspection UnnecessaryLocalVariable
        Set<Character> squared = row;
        int rowIndex;
        int columnIndex;
        // 验证九宫格
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rowIndex = i * 3;
                columnIndex = j * 3;
                for (int k = 0; k < 9; k++) {
                    if (board[rowIndex][columnIndex] != '.') {
                        if (squared.contains(board[rowIndex][columnIndex])) {
                            return false;
                        } else {
                            squared.add(board[rowIndex][columnIndex]);
                        }
                    }

                    if ((k + 1) % 3 == 0) {
                        rowIndex++;
                        columnIndex = j * 3;
                    } else {
                        columnIndex++;
                    }
                }
                squared.clear();
            }
        }
        return true;
    }

    private static boolean isValidSudoku1(char[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (isNotValid(board, i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // 相同行/列/九宫格有相同元素返回 ture
    private static boolean isNotValid(char[][] board, int row, int column) {
        // 获取 row行 column 列 数独元素的值
        char val = board[row][column];
        if (val == '.') {
            return false;
        }

        for (int i = 0; i < 9; i++) {
            // 验证每行是否有相同元素
            if (i != column && board[row][i] == val) {
                return true;
            }
            // 验证每列是否有相同元素
            if (i != row && board[i][column] == val) {
                return true;
            }
        }
        // 九宫格开始行号、列号
        int startRowIndex = row / 3 * 3;
        int startColumnIndex = column / 3 * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((startRowIndex + i) != row && (startColumnIndex + j) != column && board[startRowIndex + i][startColumnIndex + j] == val) {
                    return true;
                }
            }
        }
        return false;
    }
}
