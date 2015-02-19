
public class MainPortal {
    private final OppgaveHandterer oppgaveHandterer;
    private final NavnHandterer navnHandterer;

    public MainPortal(OppgaveHandterer oppgaveHandterer, NavnHandterer navnHandterer){
        this.oppgaveHandterer = oppgaveHandterer;
        this.navnHandterer = navnHandterer;
    }
    public String assemble() throws InterruptedException {
        return "hei " + navnHandterer.getName() + ". Result is: " + oppgaveHandterer.utforBeregning();
    }
}
