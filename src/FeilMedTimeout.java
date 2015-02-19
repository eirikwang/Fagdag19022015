public class FeilMedTimeout extends RuntimeException {
    public final long tall;

    public FeilMedTimeout(long tall) {
        this.tall = tall;
    }

}