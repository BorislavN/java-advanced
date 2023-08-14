package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class SixDimensionalMatrix {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[][][][][][] cube = new String[2][2][2][3][3][3];
        int val = 1;

        for (String[][][][][] height : cube) {
            for (String[][][][] rows : height) {
                for (String[][][] cols : rows) {

                    for (String[][] internalHeight : cols) {
                        for (String[] internalRows : internalHeight) {
                            Arrays.fill(internalRows, "c" + val);
                        }
                    }

                    val++;
                }
            }
        }

        for (String[][][][][] height : cube) {
            for (String[][][][] rows : height) {
                for (String[][][] cols : rows) {

                    for (String[][] internalHeight : cols) {
                        System.out.println(Arrays.deepToString(internalHeight));
                    }
                    System.out.println();
                }
            }
            System.out.printf("%n--------------------/height/--------------------%n%n");
        }

        //6d matrix  - to visualize it imagine a 2x2x2 cube where every cell is a 3x3x3 cube
        //or with other words, imagine a cube of cubes
    }
}