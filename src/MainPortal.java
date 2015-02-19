import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.*;

public class MainPortal {
    private final OppgaveHandterer oppgaveHandterer;
    private final NavnHandterer navnHandterer;
    ExecutorService executor = Executors.newFixedThreadPool(4);
    CompletionService<Long> completionService = new ExecutorCompletionService<>(executor);

    public MainPortal(OppgaveHandterer oppgaveHandterer, NavnHandterer navnHandterer){
        this.oppgaveHandterer = oppgaveHandterer;
        this.navnHandterer = navnHandterer;
    }
    public String assemble() throws InterruptedException {
        Future<String> navn = navnHandterer.getName();
        Future<Long> resultat = oppgaveHandterer.utforBeregning();

        return "hei " + withTimeout(navn, "[Utilgjengelig]") + ". Result is: " + withTimeout(resultat, "[Utilgjengelig]");
    }

    private String withTimeout(Future<?> navn, String defaultString) {
        try {
            return navn.get(400, TimeUnit.MILLISECONDS).toString();
        } catch (Exception ignored) {}
        return defaultString;
    }

    public void shutdown(){
        executor.shutdown();
    }
}
