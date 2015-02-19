import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        OppgaveHandterer oppgaveHandterer = new OppgaveHandterer();
        NavnHandterer navnHandterer = new NavnHandterer();
        MainPortal mainPortal = new MainPortal(oppgaveHandterer, navnHandterer);
        System.out.println(mainPortal.assemble().get());

        Thread.sleep(2000);
        oppgaveHandterer.shutdown();
        mainPortal.shutdown();
    }

}