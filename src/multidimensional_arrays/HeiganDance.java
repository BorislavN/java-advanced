package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class HeiganDance {
    private static int playerX = 7;
    private static int playerY = 7;
    private static String playerXCoordinates = "7:7";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int playerHealth = 18500;
        double heiganHealth = 3000000;

        double playerDamage = Double.parseDouble(reader.readLine());

        String lastSpell = "";

        while (playerHealth > 0) {
            heiganHealth -= playerDamage;
            playerHealth -= "Plague Cloud".equals(lastSpell) ? 3500 : 0;

            if (heiganHealth <= 0 || playerHealth <= 0) {
                break;
            }

            String[] data = reader.readLine().split("\\s+");
            int attackX = Integer.parseInt(data[1]);
            int attackY = Integer.parseInt(data[2]);

            int damage = "Cloud".equals(data[0]) ? 3500 : 6000;

            Set<String> currentDangerZone = getDangerZone(attackX, attackY);

            if (currentDangerZone.contains(playerXCoordinates)) {
                if (!tryToDodge(currentDangerZone)) {
                    playerHealth -= damage;

                    lastSpell = "Cloud".equals(data[0]) ? "Plague Cloud" : data[0];
                } else {
                    lastSpell = "";
                }
            }
        }

        System.out.printf("Heigan: %s%n", heiganHealth <= 0 ? "Defeated!" : String.format("%.2f", heiganHealth));
        System.out.printf("Player: %s%n", playerHealth <= 0 ? "Killed by " + lastSpell : playerHealth);
        System.out.printf("Final position: %d, %d%n", playerX, playerY);
    }

    private static boolean tryToDodge(Set<String> dangerZone) {
        int tempX = playerX;
        int tempY = playerY;

        if (moveTo(dangerZone, tempX - 1, tempY)) {//Up
            return true;
        }

        if (moveTo(dangerZone, tempX, tempY + 1)) {//Right
            return true;
        }

        if (moveTo(dangerZone, tempX + 1, tempY)) {//Down
            return true;
        }

        return moveTo(dangerZone, tempX, tempY - 1); //Left
    }

    private static boolean moveTo(Set<String> dangerZone, int x, int y) {
        if (isInRange(x, y)) {
            String tempCoordinates = String.format("%d:%d", x, y);

            if (!dangerZone.contains(tempCoordinates)) {
                playerXCoordinates = tempCoordinates;
                playerX = x;
                playerY = y;

                return true;
            }
        }

        return false;
    }

    private static Set<String> getDangerZone(int x, int y) {
        Set<String> dangerZone = new HashSet<>();

        for (int row = x - 1; row <= x + 1; row++) {
            for (int col = y - 1; col <= y + 1; col++) {
                if (isInRange(row, col)) {
                    dangerZone.add(String.format("%d:%d", row, col));
                }
            }
        }

        return dangerZone;
    }

    private static boolean isInRange(int x, int y) {
        return x >= 0 && x < 15 && y >= 0 && y < 15;
    }
}