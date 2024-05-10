package exam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClearSkies {
    private static int jetX = -1;
    private static int jetY = -1;
    private static int enemies = 4;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());

        char[][] airspace = new char[n][n];

        for (int row = 0; row < n; row++) {
            String line = reader.readLine();

            if (jetX == -1) {
                int jet = line.indexOf("J");

                if (jet != -1) {
                    jetX = row;
                    jetY = jet;
                }
            }

            airspace[row] = line.toCharArray();
        }

        int armor = 300;

        while (armor > 0 && enemies > 0) {
            String command = reader.readLine();

            int points = switch (command) {
                case "up" -> move(airspace, jetX - 1, jetY);

                case "down" -> move(airspace, jetX + 1, jetY);

                case "left" -> move(airspace, jetX, jetY - 1);

                case "right" -> move(airspace, jetX, jetY + 1);

                default -> 0;
            };

            if (points > 0) {
                armor = points;
            }

            if (points < 0) {
                armor += points;
            }
        }

        StringBuilder output = new StringBuilder();

        if (enemies == 0) {
            output.append("Mission accomplished, you neutralized the aerial threat!");
        } else {
            output.append(String.format("Mission failed, your jetfighter was shot down! Last coordinates [%d, %d]!", jetX, jetY));
        }

        output.append(System.lineSeparator());

        for (char[] row : airspace) {
            output.append(row).append(System.lineSeparator());
        }

        System.out.println(output);
    }

    private static int move(char[][] field, int x, int y) {
        if (isInBounds(x, field.length) && isInBounds(y, field[x].length)) {
            char cell = field[x][y];

            setCell(field, x, y);

            if (cell == 'R') {
                return 300;
            }

            if (cell == 'E') {
                return enemies-- > 1 ? -100 : 0;
            }
        }

        return 0;
    }

    private static void setCell(char[][] field, int x, int y) {
        field[x][y] = 'J';
        field[jetX][jetY] = '-';

        jetX = x;
        jetY = y;
    }

    private static boolean isInBounds(int index, int limit) {
        return index >= 0 && index < limit;
    }
}