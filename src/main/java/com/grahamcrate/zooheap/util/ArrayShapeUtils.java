package com.grahamcrate.zooheap.util;

import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author grahamcrate
 */
public class ArrayShapeUtils {

    public static char[][] makeCopy(char[][] array) {
        char[][] b = new char[array.length][];

        for (int row = 0; row < array.length; ++row) {
            b[row] = new char[array[row].length];
            for (int col = 0; col < b[row].length; ++col) {
                b[row][col] = array[row][col];
            }
        }
        return b;
    }
    
    public static boolean[][] makeCopy(boolean[][] array) {
        boolean[][] b = new boolean[array.length][];

        for (int row = 0; row < array.length; ++row) {
            b[row] = new boolean[array[row].length];
            for (int col = 0; col < b[row].length; ++col) {
                b[row][col] = array[row][col];
            }
        }
        return b;
    }

    public static boolean[][] copyAndRotate90(boolean[][] arr) {
        boolean[][] newArray = new boolean[arr[0].length][arr.length];

        int ii = 0;
        int jj = 0;
        for (int i = 0; i < arr[0].length; i++) {
            for (int j = arr.length - 1; j >= 0; j--) {
                newArray[i][j] = arr[j][i];
            }
        }
        return newArray;
    }

    public static boolean[][] copyAndRotate180(boolean[][] arr) {
        boolean[][] newArray = makeCopy(arr);

        ArrayUtils.reverse(newArray);
        for (int i = 0; i < arr.length; i++) {
            ArrayUtils.reverse(newArray[i]);
        }
        return newArray;
    }

    public static void print(char arr[][]) {
        // Loop through all rows 
        for (int i = 0; i < arr.length; i++) // Loop through all elements of current row 
        {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print((arr[i][j] != '\u0000' ? arr[i][j] : "-") + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
