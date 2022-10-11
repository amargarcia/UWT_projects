/**
 * @author AJ Garcia
 */
import java.util.Scanner;

/**
 * Takes minesweeper matrix and counts how many mines are around each spot
 */
public class Minesweeper {
    /**
     * Driver of program
     * @param theArgs
     */
    public static void main(String[] theArgs) {
        // Scan input
        Scanner input  = new Scanner(System.in);

        // default storage values to be altered by program
        int column = 0;
        int row = 0;
        int rCount = 1;
        char[][] matrix = new char[column][row];
        String line;
        int fieldNum = 1;

        // parses input
        while (input.hasNextLine()) {
            // checks for lines beginning with ints
            if (input.hasNextInt()) {
                row = input.nextInt();
                // ensures row is not 0, otherwise exits
                if (row == 0) {
                    System.exit(0);
                }
                column = input.nextInt();
                input.nextLine();
                //creates matrix based on input
                matrix = new char[row + 2][column + 2];
                rCount = 1;
                System.out.println("Field #" + fieldNum + ":");
                fieldNum++;
            // pares lines that dont start with in
            } else {
                checkRange(row, column); // ensures rows and columns are within range
                line = input.nextLine();
                // stores each char in line into the matrix (2D array)
                for (int i = 1; i < column + 1; i++) {
                    matrix[rCount][i] = line.charAt(i-1);
                }
                rCount++;
            }
            // checks to see if we reached the assumed end of rows
            if (rCount == row +1) {
                printMatrix(matrix);
            }

        }
    }

    /**
     * Checks range of matrix to ensure rows or columns do not exceed 100 or less than 0.
     * @param theC
     * @param theR
     */
    public static void checkRange(int theC, int theR) {
        if (theC > 100 || theC < 0 || theR > 100 || theR < 0) {
            System.exit(-1);
        }
    }

    /**
     * Prints matrix with number of mines around cell.
     * @param theM
     */
    public static void printMatrix(char[][] theM){
        int mines = 0;
        for (int r = 1; r < theM.length - 1; r++){
            for (int c = 1; c < theM[r].length - 1; c++) {
                if (theM[r][c] != '*') {
                    if (theM[r-1][c-1] == '*') {
                        mines++;
                    }
                    if (theM[r-1][c] == '*') {
                        mines++;
                    }
                    if (theM[r-1][c+1] == '*') {
                        mines++;
                    }
                    if (theM[r][c-1] == '*') {
                        mines++;
                    }
                    if (theM[r][c+1] == '*') {
                        mines++;
                    }
                    if (theM[r+1][c-1] == '*') {
                        mines++;
                    }
                    if (theM[r+1][c] == '*') {
                        mines++;
                    }
                    if (theM[r+1][c+1] == '*') {
                        mines++;
                    }
                    System.out.print(mines);
                    mines = 0;
                } else {
                    System.out.print(theM[r][c]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}