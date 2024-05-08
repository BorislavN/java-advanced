package algorithms.sumofcoins;

import java.util.*;
import java.util.stream.Collectors;

//Remove package name to pass tests in judge
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String[] elements = in.nextLine().substring(7).split(", ");
        int[] coins = new int[elements.length];
        for (int i = 0; i < coins.length; i++) {
            coins[i] = Integer.parseInt(elements[i]);
        }

        int targetSum = Integer.parseInt(in.nextLine().substring(5));


        Map<Integer, Integer> usedCoins = chooseCoins(coins, targetSum);

        for (Map.Entry<Integer, Integer> usedCoin : usedCoins.entrySet()) {
            System.out.println(usedCoin.getKey() + " -> " + usedCoin.getValue());
        }
    }

    public static Map<Integer, Integer> chooseCoins(int[] coins, int targetSum) {
        List<Integer> availableCoins = Arrays.stream(coins).distinct().boxed()
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        Map<Integer, Integer> selectedCoins = new LinkedHashMap<>();

        for (Integer coin : availableCoins) {
            if (targetSum <= 0) {
                break;
            }

            int count = targetSum / coin;

            if (count > 0) {
                selectedCoins.put(coin, count);
                targetSum -= coin * count;
            }
        }

        if (targetSum > 0) {
            throw new IllegalArgumentException("Error");
        }

        return selectedCoins;
    }
}