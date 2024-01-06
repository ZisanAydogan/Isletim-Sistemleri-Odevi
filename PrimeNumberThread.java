import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrimeNumberThread extends Thread {
    private static List<Integer> numbers = new ArrayList<>();
    private static List<Integer> ciftNumbers = new ArrayList<>();
    private static List<Integer> tekNumbers = new ArrayList<>();
    private static List<Integer> asalNumbers = new ArrayList<>();
    private static Object lockCift = new Object();
    private static Object lockTek = new Object();
    private static Object lockAsal = new Object();

    private final int start;
    private final int end;

    public PrimeNumberThread(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            int num = i + 1;

            // Çift sayıları kontrol et ve ciftNumbers listesine ekle
            if (num % 2 == 0) {
                synchronized (lockCift) {
                    ciftNumbers.add(num);
                }
            } else {
                // Tek sayıları kontrol et ve tekNumbers listesine ekle
                synchronized (lockTek) {
                    tekNumbers.add(num);
                }
            }

            // Asal sayıları kontrol et ve asalNumbers listesine ekle
            if (isPrime(num)) {
                synchronized (lockAsal) {
                    asalNumbers.add(num);
                }
            }
        }
    }

    private boolean isPrime(int num) {
        if (num < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // 1'den 1.000.000'e kadar olan sayıları ArrayList'e ekle
        for (int i = 1; i <= 1000000; i++) {
            numbers.add(i);
        }

        // Thread'leri oluştur ve çalıştır
        PrimeNumberThread thread1 = new PrimeNumberThread(0, 250000);
        PrimeNumberThread thread2 = new PrimeNumberThread(250000, 500000);
        PrimeNumberThread thread3 = new PrimeNumberThread(500000, 750000);
        PrimeNumberThread thread4 = new PrimeNumberThread(750000, 1000000);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            // Thread'lerin bitmesini bekleyerek devam et
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Asal sayıları sırala
        synchronized (lockAsal) {
            Collections.sort(asalNumbers);
        }

        // Tek ve çift sayıları sırala
        synchronized (lockTek) {
            Collections.sort(tekNumbers);
        }

        synchronized (lockCift) {
            Collections.sort(ciftNumbers);
        }

        // Sonuçları yazdır
        System.out.println("Tek Sayılar: " + tekNumbers);
        System.out.println("Çift Sayılar: " + ciftNumbers);
        System.out.println("Asal Sayılar: " + asalNumbers);
    }
}
