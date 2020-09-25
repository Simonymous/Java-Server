package model;

public class PrimeHelper {
    public PrimeHelper() {
    }

    /**
     * Überprüft ob eine Zahl Primzahl ist
     * @param number
     * @return
     */
    public boolean isPrime(int number) {
        if (number > 1) {
            for (int i = 2; i <= number / 2; ++i) {
                if (number % i == 0) {
                    return false;
                }
            }
        } else {
            return false;
        }


        return true;
    }
}
