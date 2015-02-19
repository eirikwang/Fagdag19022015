import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class NavnHandterer {
    public ListenableFuture<String> getName() {
        return Futures.immediateCheckedFuture("Random " + Math.random());
    }
}
