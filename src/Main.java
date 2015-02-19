public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(new MainPortal(new OppgaveHandterer(), new NavnHandterer()).assemble());
    }

}