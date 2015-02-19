public class Main {

    public static void main(String[] args) throws InterruptedException {
        OppgaveHandterer oppgaveHandterer = new OppgaveHandterer();
        System.out.println(new MainPortal(oppgaveHandterer, new NavnHandterer()).assemble());

        Thread.sleep(2000);
        oppgaveHandterer.shutdown();
    }

}