package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class RadioactiveMutantVampireBunnies {
    private static char[][] lair;
    private static int playerX;
    private static int playerY;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int rows = Integer.parseInt(reader.readLine().split("\\s+")[0]);
        lair = new char[rows][];

        for (int row = 0; row < rows; row++) {
            String input = reader.readLine();
            int temp = input.indexOf('P');

            lair[row] = input.toCharArray();

            if (temp != -1) {
                playerX = row;
                playerY = temp;
            }
        }

        String moves = reader.readLine();
        String lastCoordinates = playerX + " " + playerY;

        for (int index = 0; index < moves.length(); index++) {
            HashSet<String> newlyCreated = new HashSet<>();

            move(moves.charAt(index));

            if (isInRange(playerX, playerY)) {
                lastCoordinates = playerX + " " + playerY;
            }

            for (int row = 0; row < lair.length; row++) {
                for (int col = 0; col < lair[row].length; col++) {
                    if (lair[row][col] == 'B' && !newlyCreated.contains(getKey(row, col))) {
                        multiply(newlyCreated, row, col);
                    }
                }
            }

            if (!isInRange(playerX, playerY) || lair[playerX][playerY] == 'B') {
                break;
            }
        }

        StringBuilder output = new StringBuilder();

        for (char[] row : lair) {
            output.append(row);
            output.append(System.lineSeparator());
        }

        System.out.println(output.toString().trim());
        System.out.printf("%s: %s%n", isInRange(playerX, playerY) ? "dead" : "won", lastCoordinates);
    }

    private static void move(char direction) {
        lair[playerX][playerY] = '.';

        switch (direction) {
            case 'U':
                tryToMove(playerX - 1, playerY);
                break;
            case 'R':
                tryToMove(playerX, playerY + 1);
                break;
            case 'D':
                tryToMove(playerX + 1, playerY);
                break;
            case 'L':
                tryToMove(playerX, playerY - 1);
                break;
        }
    }

    private static void tryToMove(int row, int col) {
        playerX = row;
        playerY = col;

        if (isInRange(row, col) && lair[row][col] != 'B') {
            lair[row][col] = 'P';
        }
    }

    private static boolean isInRange(int row, int col) {
        return row >= 0 && row < lair.length && col >= 0 && col < lair[row].length;
    }

    private static void multiply(HashSet<String> newlyCreated, int row, int col) {
        setBunny(newlyCreated, row - 1, col);//Up
        setBunny(newlyCreated, row + 1, col);//Down
        setBunny(newlyCreated, row, col - 1);//Left
        setBunny(newlyCreated, row, col + 1);//Right
    }

    private static void setBunny(HashSet<String> newlyCreated, int row, int col) {
        if (isInRange(row, col)) {
            if (lair[row][col] != 'B') {
                newlyCreated.add(getKey(row, col));
            }

            lair[row][col] = 'B';
        }
    }

    private static String getKey(int row, int col) {
        return String.format("%d:%d", row, col);
    }
}