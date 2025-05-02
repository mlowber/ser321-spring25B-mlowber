package example.grpcclient;

import io.grpc.stub.StreamObserver;
import service.KVStoreGrpc;
import service.KVPutRequest;
import service.KVPutResponse;
import service.KVGetRequest;
import service.KVGetResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * In-memory key–value store.
 */
public class KVStoreImpl extends KVStoreGrpc.KVStoreImplBase {
    private final Map<String, String> store = new ConcurrentHashMap<>();

    @Override
    public void put(KVPutRequest req, StreamObserver<KVPutResponse> obs) {
        KVPutResponse.Builder resp = KVPutResponse.newBuilder();
        store.put(req.getKey(), req.getValue());
        resp.setSuccess(true);
        obs.onNext(resp.build());
        obs.onCompleted();
    }

    @Override
    public void get(KVGetRequest req, StreamObserver<KVGetResponse> obs) {
        KVGetResponse.Builder resp = KVGetResponse.newBuilder();
        String key = req.getKey();
        if (store.containsKey(key)) {
            resp.setFound(true)
                    .setValue(store.get(key));
        } else {
            resp.setFound(false)
                    .setError("Key not found: " + key);
        }
        obs.onNext(resp.build());
        obs.onCompleted();
    }
}