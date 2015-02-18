public class Feilhandterer implements Thread.UncaughtExceptionHandler {

    private final OppgaveHandterer oppgaveHanderer;

    public Feilhandterer(OppgaveHandterer oppgaveHandterer) {
        this.oppgaveHanderer = oppgaveHandterer;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof FeilMedTall) {
            oppgaveHanderer.leggTil(((FeilMedTall) e).tall);
        }
    }
}