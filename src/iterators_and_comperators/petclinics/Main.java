package iterators_and_comperators.petclinics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());

        Map<String, Clinic> clinics = new HashMap<>();
        Map<String, Pet> pets = new HashMap<>();

        while (n-- > 0) {
            String[] data = reader.readLine().split("\\s+");

            try {
                switch (data[0]) {
                    case "Create":
                        if ("Pet".equals(data[1])) {
                            pets.putIfAbsent(data[2], new Pet(data[2], Integer.parseInt(data[3]), data[4]));
                        }

                        if ("Clinic".equals(data[1])) {
                            clinics.putIfAbsent(data[2], new Clinic(data[2], Integer.parseInt(data[3])));
                        }

                        break;

                    case "Add":
                        checkKey(pets, data[1]);
                        checkKey(clinics, data[2]);

                        System.out.println(clinics.get(data[2]).add(pets.get(data[1])));

                        break;

                    case "Release":
                        checkKey(clinics, data[1]);

                        System.out.println(clinics.get(data[1]).release());

                        break;

                    case "HasEmptyRooms":
                        checkKey(clinics, data[1]);

                        System.out.println(clinics.get(data[1]).hasRooms());

                        break;

                    case "Print":
                        checkKey(clinics, data[1]);

                        Clinic current = clinics.get(data[1]);

                        if (data.length == 2) {
                            current.print();
                        } else {
                            current.print(Integer.parseInt(data[2])-1);
                        }

                        break;
                }
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    private static void checkKey(Map<String, ?> map, String name) {
        if (!map.containsKey(name)) {
            throw new IllegalArgumentException("Invalid Operation!");
        }
    }
}
