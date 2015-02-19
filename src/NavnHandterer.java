import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

public class NavnHandterer {
    public CompletableFuture<String> getName() {
        return CompletableFuture.supplyAsync(()->"Random " + Math.random());
    }
}
