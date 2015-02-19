public class Main {

    public static void main(String[] args) throws InterruptedException {
        OppgaveHandterer oppgaveHandterer = new OppgaveHandterer();
        NavnHandterer navnHandterer = new NavnHandterer();
        MainPortal mainPortal = new MainPortal(oppgaveHandterer, navnHandterer);
        System.out.println(mainPortal.assemble());

        Thread.sleep(2000);
        oppgaveHandterer.shutdown();
        mainPortal.shutdown();
    }

}