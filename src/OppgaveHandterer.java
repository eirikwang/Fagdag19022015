import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class OppgaveHandterer {
    private final ExecutorService executorService;
    private final ExecutorService timeoutExecutorService;
    AtomicLong sum = new AtomicLong();
    CountDownLatch countDownLatch = new CountDownLatch(1001);
    final List<Long> finishedOps = Collections.synchronizedList(new ArrayList<>());
    TimeLimiter limiter;


    public OppgaveHandterer() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Med Feilhånderer-%d")
                .setUncaughtExceptionHandler(new Feilhandterer(this))
                .build();
        executorService = Executors.newFixedThreadPool(10, threadFactory);
        timeoutExecutorService = Executors.newFixedThreadPool(10, threadFactory);
        limiter = new SimpleTimeLimiter(timeoutExecutorService);
    }

    public Long utforBeregning() throws InterruptedException {

        long startTime = System.nanoTime();
        ContiguousSet<Long> ints = ContiguousSet.create(Range.closed(0L, countDownLatch.getCount() - 1), DiscreteDomain.longs());
        ints.forEach(this::leggTil);
        ventPaaFullfort();
        System.out.println("Svar: " + sum + " - Tidsbruk: " + (System.nanoTime() - startTime) / 1000000 + "ms");
        return sum.get();
    }

    private void ventPaaFullfort() throws InterruptedException {
        countDownLatch.await(5000, TimeUnit.MILLISECONDS);
    }

    private void simulerTimeout(Runnable beregning, long tall) {
        try {
            limiter.newProxy(beregning, Runnable.class, 10, TimeUnit.MILLISECONDS).run();
        } catch (UncheckedTimeoutException ex) {
            throw new FeilMedTimeout(tall);
        }
    }

    private Long simulerKall(Long tall) {
        sleep(2);
        Long resultat = beregn(tall);
        if(ensureNotAdded(tall)){
            sum.addAndGet(resultat);
            countDownLatch.countDown();
        }
        return 1L;
    }

    private boolean ensureNotAdded(Long tall) {
        synchronized (finishedOps){
            if(finishedOps.contains(tall)) return false;
            finishedOps.add(tall);
        }
        return true;
    }

    private final static Random rand = new Random();

    private static Long beregn(long i) {
        if (rand.nextInt(100) == 1) throw new FeilMedTall(i);
        if (rand.nextInt(100) == 2) sleep(20);
        if (i % 2 == 0) {
            return partall(i);
        } else {
            return oddetall(i);
        }
    }


    private static long partall(long i) {
        return i % 9;
    }

    private static long oddetall(long i) {
        return i % 3;
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public void leggTil(long tall) {
        executorService.execute(() ->
                simulerTimeout(() ->
                        simulerKall(tall), tall));
    }

    public void shutdown(){
        executorService.shutdownNow();
        timeoutExecutorService.shutdownNow();
    }
}
