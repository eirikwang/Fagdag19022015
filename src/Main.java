import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    final static ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("Håndterer feil-%d")
            .setUncaughtExceptionHandler(new Feilhandterer())
            .build();
    static ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory);
    static AtomicLong sum = new AtomicLong();

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.nanoTime();

        ContiguousSet<Integer> ints = ContiguousSet.create(Range.closed(0, 1000), DiscreteDomain.integers());
        ints.forEach(i -> {
            executorService.execute(() -> beregn(i));
        });
        executorService.shutdown();
        executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        System.out.println(sum + " - " + (System.nanoTime() - startTime) / 1000000);
    }

    private static void beregn(int i) {
        sleep();
        if (new Random().nextInt(100) == 1) throw new FeilMedTall(i);
        if (i % 2 == 0) {
            sum.addAndGet(partall(i));
        } else {
            sum.addAndGet(oddetall(i));
        }
    }


    private static long partall(int i) {
        return i % 9;
    }

    private static long oddetall(int i) {
        return i % 3;
    }

    private static void sleep() {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPrime(int num) {
        if (num % 2 == 0) return false;
        for (int i = 3; i * i <= num; i += 2)
            if (num % i == 0) return false;
        return true;
    }

    static class Feilhandterer implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("retry");
            if (e instanceof FeilMedTall)
                executorService.execute(() -> beregn(((FeilMedTall) e).tall));
        }
    }

    static class FeilMedTall extends RuntimeException {
        public final int tall;

        public FeilMedTall(int tall) {
            this.tall = tall;
        }

    }
}