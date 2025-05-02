package example.grpcclient;

import io.grpc.stub.StreamObserver;
import com.google.protobuf.Empty;
import service.CoffeePotGrpc;
import service.BrewResponse;
import service.GetCupResponse;
import service.BrewStatusResponse;
import service.BrewStatus;

/**
 * Implementation of the CoffeePot gRPC service.
 */
public class CoffeePotImpl extends CoffeePotGrpc.CoffeePotImplBase {
    private final int capacity = 5;
    private int currentCups = 0;
    private long brewStartTime = 0;
    private boolean isBrewing = false;
    private final Object lock = new Object();

    @Override
    public void brew(Empty req, StreamObserver<BrewResponse> obs) {
        BrewResponse.Builder b = BrewResponse.newBuilder();
        synchronized (lock) {
            if (isBrewing) {
                b.setIsSuccess(false).setError("Already brewing");
            } else if (currentCups > 0) {
                b.setIsSuccess(false).setError("Pot not empty (" + currentCups + " cups remain)");
            } else {
                isBrewing = true;
                brewStartTime = System.currentTimeMillis();
                b.setIsSuccess(true).setMessage("Brewing started");
            }
        }
        obs.onNext(b.build());
        obs.onCompleted();
    }

    @Override
    public void getCup(Empty req, StreamObserver<GetCupResponse> obs) {
        GetCupResponse.Builder b = GetCupResponse.newBuilder();
        synchronized (lock) {
            // finalize brewing if time has elapsed
            if (isBrewing) {
                long elapsed = System.currentTimeMillis() - brewStartTime;
                if (elapsed >= 30000) {
                    isBrewing = false;
                    currentCups = capacity;
                }
            }
            if (isBrewing) {
                long rem = 30000 - (System.currentTimeMillis() - brewStartTime);
                rem = Math.max(rem, 0);
                b.setIsSuccess(false).setError("Brewing: " + (rem / 1000) + "s left");
            } else if (currentCups > 0) {
                currentCups--;
                b.setIsSuccess(true).setMessage("Enjoy! " + currentCups + " cups left");
            } else {
                b.setIsSuccess(false).setError("No coffee. Please brew first");
            }
        }
        obs.onNext(b.build());
        obs.onCompleted();
    }

    @Override
    public void brewStatus(Empty req, StreamObserver<BrewStatusResponse> obs) {
        BrewStatusResponse.Builder rb = BrewStatusResponse.newBuilder();
        BrewStatus.Builder s = BrewStatus.newBuilder();
        synchronized (lock) {
            if (isBrewing) {
                long rem = 30000 - (System.currentTimeMillis() - brewStartTime);
                if (rem <= 0) {
                    isBrewing = false;
                    currentCups = capacity;
                    rem = 0;
                }
                int secs = (int) (rem / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                s.setMinutes(mins).setSeconds(secs)
                        .setMessage("Brewing: " + mins + "m" + secs + "s left");
            } else {
                s.setMinutes(0).setSeconds(0)
                        .setMessage("Ready: " + currentCups + " cups available");
            }
        }
        obs.onNext(rb.setStatus(s).build());
        obs.onCompleted();
    }
}
