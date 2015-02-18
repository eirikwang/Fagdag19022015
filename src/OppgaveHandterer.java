import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.security.Provider;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

public class OppgaveHandterer {
    private final ExecutorService executorService;
    AtomicLong sum = new AtomicLong();
    CountDownLatch countDownLatch = new CountDownLatch(1000);

    public OppgaveHandterer() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Med Feilhånderer-%d")
                .setUncaughtExceptionHandler(new Feilhandterer(this))
                .build();
        executorService = Executors.newFixedThreadPool(10, threadFactory);
    }

    public void utforBeregning() throws InterruptedException {

        long startTime = System.nanoTime();
        ContiguousSet<Long> ints = ContiguousSet.create(Range.closed(0L, countDownLatch.getCount()), DiscreteDomain.longs());
        ints.forEach(this::leggTil);
        ventPaaFullfort();
        System.out.println("Svar: " + sum + " - Tidsbruk: " + (System.nanoTime() - startTime) / 1000000 + "ms");

    }

    private void ventPaaFullfort() throws InterruptedException {
        countDownLatch.await(5000, TimeUnit.MILLISECONDS);
        executorService.shutdown();
        executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }

    private void beregn(long i) {
        sleep();
        if (new Random().nextInt(100) == 1) throw new FeilMedTall(i);
        if (i % 2 == 0) {
            sum.addAndGet(partall(i));
        } else {
            sum.addAndGet(oddetall(i));
        }
        countDownLatch.countDown();
    }


    private static long partall(long i) {
        return i % 9;
    }

    private static long oddetall(long i) {
        return i % 3;
    }

    private static void sleep() {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void leggTil(long tall) {
        executorService.execute(() -> beregn(tall));
    }
}
