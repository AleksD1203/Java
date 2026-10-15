import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Lab1Variant7 {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            System.out.print("Введіть натуральне число n (n >= 2): ");

            if (!scanner.hasNextInt()) {
                System.err.println("Помилка: введене значення не є цілим числом типу int.");
                return;
            }

            int n = scanner.nextInt();
            if (n < 2) {
                System.err.println("Помилка: найменше просте число — 2. Введіть n >= 2.");
                return;
            }

            long startTime = System.nanoTime();

            boolean[] isPrime = sieveOfEratosthenes(n);

            int maxZeros = -1;
            int firstBestPrime = -1;
            int lastBestPrime = -1;
            int winnersCount = 0;

            for (int i = 2; i <= n; i++) {
                if (isPrime[i]) {
                    int zeros = countSignificantZeros(i);
                    if (zeros > maxZeros) {
                        maxZeros = zeros;
                        firstBestPrime = i;
                        lastBestPrime = i;
                        winnersCount = 1;
                    } else if (zeros == maxZeros) {
                        lastBestPrime = i;
                        winnersCount++;
                    }
                }
            }

            int[] allBestPrimes = new int[winnersCount];
            int idx = 0;
            for (int i = 2; i <= n; i++) {
                if (isPrime[i] && countSignificantZeros(i) == maxZeros) {
                    allBestPrimes[idx++] = i;
                }
            }

            long elapsedMicros = (System.nanoTime() - startTime) / 1000;

            System.out.println("Верхня межа діапазону (n): " + n);
            System.out.println("Максимальна кількість нулів у двійковій формі: " + maxZeros);
            System.out.println("Шукане просте число (перше знайдене): " + firstBestPrime
                    + " -> " + toBinaryStringCustom(firstBestPrime) + "(2)");

            if (winnersCount > 1) {
                System.out.println("Шукане просте число (найбільше в діапазоні): " + lastBestPrime
                        + " -> " + toBinaryStringCustom(lastBestPrime) + "(2)");
                System.out.print("Усі прості числа з " + maxZeros + " нулями (" + winnersCount + " шт.): ");
                for (int i = 0; i < allBestPrimes.length; i++) {
                    System.out.print(allBestPrimes[i] + " [" + toBinaryStringCustom(allBestPrimes[i]) + "]");
                    if (i < allBestPrimes.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
            System.out.println("Час обчислення: " + elapsedMicros + " мкс");
        }
    }

    public static int countSignificantZeros(int x) {
        int zeros = 0;
        while (x > 0) {
            if ((x & 1) == 0) {
                zeros++;
            }
            x >>>= 1;
        }
        return zeros;
    }

    public static boolean[] sieveOfEratosthenes(int max) {
        boolean[] isPrime = new boolean[max + 1];
        for (int i = 2; i <= max; i++) {
            isPrime[i] = true;
        }
        for (int p = 2; p <= max / p; p++) {
            if (isPrime[p]) {
                for (int multiple = p * p; multiple <= max; multiple += p) {
                    isPrime[multiple] = false;
                }
            }
        }
        return isPrime;
    }

    public static String toBinaryStringCustom(int x) {
        if (x == 0) {
            return "0";
        }
        char[] buffer = new char[32];
        int pos = 32;
        while (x > 0) {
            buffer[--pos] = (char) ('0' + (x & 1));
            x >>>= 1;
        }
        return new String(buffer, pos, 32 - pos);
    }
}