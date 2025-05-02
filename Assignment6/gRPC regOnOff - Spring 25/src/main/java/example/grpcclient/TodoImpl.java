package example.grpcclient;

import io.grpc.stub.StreamObserver;
import service.TodoGrpc;
import service.AddTaskRequest;
import service.AddTaskResponse;
import service.ListTasksRequest;
import service.ListTasksResponse;
import service.TaskEntry;
import service.MarkDoneRequest;
import service.MarkDoneResponse;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * In-memory To-Do list service implementation.
 */
public class TodoImpl extends TodoGrpc.TodoImplBase {
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, TaskEntry> tasks = new ConcurrentHashMap<>();

    @Override
    public void addTask(AddTaskRequest req, StreamObserver<AddTaskResponse> obs) {
        int id = nextId.getAndIncrement();
        TaskEntry entry = TaskEntry.newBuilder()
                .setId(id)
                .setText(req.getText())
                .setDone(false)
                .build();
        tasks.put(id, entry);

        AddTaskResponse res = AddTaskResponse.newBuilder()
                .setSuccess(true)
                .setId(id)
                .build();
        obs.onNext(res);
        obs.onCompleted();
    }

    @Override
    public void listTasks(ListTasksRequest req, StreamObserver<ListTasksResponse> obs) {
        ListTasksResponse.Builder resB = ListTasksResponse.newBuilder();
        for (TaskEntry t : tasks.values()) {
            resB.addTasks(t);
        }
        obs.onNext(resB.build());
        obs.onCompleted();
    }

    @Override
    public void markDone(MarkDoneRequest req, StreamObserver<MarkDoneResponse> obs) {
        int id = req.getId();
        MarkDoneResponse.Builder resB = MarkDoneResponse.newBuilder();
        TaskEntry entry = tasks.get(id);
        if (entry == null) {
            resB.setSuccess(false)
                    .setError("No task with id " + id);
        } else {
            TaskEntry updated = TaskEntry.newBuilder(entry)
                    .setDone(true)
                    .build();
            tasks.put(id, updated);
            resB.setSuccess(true);
        }
        obs.onNext(resB.build());
        obs.onCompleted();
    }
}