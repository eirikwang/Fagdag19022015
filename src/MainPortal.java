import java.util.concurrent.*;

public class MainPortal {
    private final OppgaveHandterer oppgaveHandterer;
    private final NavnHandterer navnHandterer;
    ExecutorService executor = Executors.newFixedThreadPool(4);

    public MainPortal(OppgaveHandterer oppgaveHandterer, NavnHandterer navnHandterer) {
        this.oppgaveHandterer = oppgaveHandterer;
        this.navnHandterer = navnHandterer;
    }

    public CompletableFuture<String> assemble() throws InterruptedException {
        CompletableFuture<String> navn = navnHandterer.getName();
        CompletableFuture<Long> resultat = oppgaveHandterer.utforBeregning();
        CompletableFuture<Void> done = CompletableFuture.allOf(navn, resultat);
        return done.thenApply((a) -> "hei " + withTimeout(navn, "[Utilgjengelig]") + ". Result is: " + withTimeout(resultat, "[Utilgjengelig]"));
    }

    private String withTimeout(Future<?> navn, String defaultString) {
        try {
            return navn.get(400, TimeUnit.MILLISECONDS).toString();
        } catch (Exception ignored) {
        }
        return defaultString;
    }

    public void shutdown() {
        executor.shutdown();
    }
}
