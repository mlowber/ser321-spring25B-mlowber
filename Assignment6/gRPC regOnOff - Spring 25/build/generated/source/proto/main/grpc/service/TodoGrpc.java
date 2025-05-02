package service;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * To-Do List service
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.49.1)",
    comments = "Source: services/todo.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TodoGrpc {

  private TodoGrpc() {}

  public static final String SERVICE_NAME = "services.Todo";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<service.AddTaskRequest,
      service.AddTaskResponse> getAddTaskMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddTask",
      requestType = service.AddTaskRequest.class,
      responseType = service.AddTaskResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<service.AddTaskRequest,
      service.AddTaskResponse> getAddTaskMethod() {
    io.grpc.MethodDescriptor<service.AddTaskRequest, service.AddTaskResponse> getAddTaskMethod;
    if ((getAddTaskMethod = TodoGrpc.getAddTaskMethod) == null) {
      synchronized (TodoGrpc.class) {
        if ((getAddTaskMethod = TodoGrpc.getAddTaskMethod) == null) {
          TodoGrpc.getAddTaskMethod = getAddTaskMethod =
              io.grpc.MethodDescriptor.<service.AddTaskRequest, service.AddTaskResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddTask"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.AddTaskRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.AddTaskResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TodoMethodDescriptorSupplier("AddTask"))
              .build();
        }
      }
    }
    return getAddTaskMethod;
  }

  private static volatile io.grpc.MethodDescriptor<service.ListTasksRequest,
      service.ListTasksResponse> getListTasksMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListTasks",
      requestType = service.ListTasksRequest.class,
      responseType = service.ListTasksResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<service.ListTasksRequest,
      service.ListTasksResponse> getListTasksMethod() {
    io.grpc.MethodDescriptor<service.ListTasksRequest, service.ListTasksResponse> getListTasksMethod;
    if ((getListTasksMethod = TodoGrpc.getListTasksMethod) == null) {
      synchronized (TodoGrpc.class) {
        if ((getListTasksMethod = TodoGrpc.getListTasksMethod) == null) {
          TodoGrpc.getListTasksMethod = getListTasksMethod =
              io.grpc.MethodDescriptor.<service.ListTasksRequest, service.ListTasksResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListTasks"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.ListTasksRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.ListTasksResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TodoMethodDescriptorSupplier("ListTasks"))
              .build();
        }
      }
    }
    return getListTasksMethod;
  }

  private static volatile io.grpc.MethodDescriptor<service.MarkDoneRequest,
      service.MarkDoneResponse> getMarkDoneMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MarkDone",
      requestType = service.MarkDoneRequest.class,
      responseType = service.MarkDoneResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<service.MarkDoneRequest,
      service.MarkDoneResponse> getMarkDoneMethod() {
    io.grpc.MethodDescriptor<service.MarkDoneRequest, service.MarkDoneResponse> getMarkDoneMethod;
    if ((getMarkDoneMethod = TodoGrpc.getMarkDoneMethod) == null) {
      synchronized (TodoGrpc.class) {
        if ((getMarkDoneMethod = TodoGrpc.getMarkDoneMethod) == null) {
          TodoGrpc.getMarkDoneMethod = getMarkDoneMethod =
              io.grpc.MethodDescriptor.<service.MarkDoneRequest, service.MarkDoneResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MarkDone"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.MarkDoneRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.MarkDoneResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TodoMethodDescriptorSupplier("MarkDone"))
              .build();
        }
      }
    }
    return getMarkDoneMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TodoStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TodoStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TodoStub>() {
        @java.lang.Override
        public TodoStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TodoStub(channel, callOptions);
        }
      };
    return TodoStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TodoBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TodoBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TodoBlockingStub>() {
        @java.lang.Override
        public TodoBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TodoBlockingStub(channel, callOptions);
        }
      };
    return TodoBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TodoFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TodoFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TodoFutureStub>() {
        @java.lang.Override
        public TodoFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TodoFutureStub(channel, callOptions);
        }
      };
    return TodoFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * To-Do List service
   * </pre>
   */
  public static abstract class TodoImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Add a new task
     * </pre>
     */
    public void addTask(service.AddTaskRequest request,
        io.grpc.stub.StreamObserver<service.AddTaskResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddTaskMethod(), responseObserver);
    }

    /**
     * <pre>
     * List all tasks
     * </pre>
     */
    public void listTasks(service.ListTasksRequest request,
        io.grpc.stub.StreamObserver<service.ListTasksResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListTasksMethod(), responseObserver);
    }

    /**
     * <pre>
     * Mark an existing task as done
     * </pre>
     */
    public void markDone(service.MarkDoneRequest request,
        io.grpc.stub.StreamObserver<service.MarkDoneResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMarkDoneMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getAddTaskMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                service.AddTaskRequest,
                service.AddTaskResponse>(
                  this, METHODID_ADD_TASK)))
          .addMethod(
            getListTasksMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                service.ListTasksRequest,
                service.ListTasksResponse>(
                  this, METHODID_LIST_TASKS)))
          .addMethod(
            getMarkDoneMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                service.MarkDoneRequest,
                service.MarkDoneResponse>(
                  this, METHODID_MARK_DONE)))
          .build();
    }
  }

  /**
   * <pre>
   * To-Do List service
   * </pre>
   */
  public static final class TodoStub extends io.grpc.stub.AbstractAsyncStub<TodoStub> {
    private TodoStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TodoStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TodoStub(channel, callOptions);
    }

    /**
     * <pre>
     * Add a new task
     * </pre>
     */
    public void addTask(service.AddTaskRequest request,
        io.grpc.stub.StreamObserver<service.AddTaskResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddTaskMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * List all tasks
     * </pre>
     */
    public void listTasks(service.ListTasksRequest request,
        io.grpc.stub.StreamObserver<service.ListTasksResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListTasksMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Mark an existing task as done
     * </pre>
     */
    public void markDone(service.MarkDoneRequest request,
        io.grpc.stub.StreamObserver<service.MarkDoneResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMarkDoneMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * To-Do List service
   * </pre>
   */
  public static final class TodoBlockingStub extends io.grpc.stub.AbstractBlockingStub<TodoBlockingStub> {
    private TodoBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TodoBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TodoBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Add a new task
     * </pre>
     */
    public service.AddTaskResponse addTask(service.AddTaskRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddTaskMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * List all tasks
     * </pre>
     */
    public service.ListTasksResponse listTasks(service.ListTasksRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListTasksMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Mark an existing task as done
     * </pre>
     */
    public service.MarkDoneResponse markDone(service.MarkDoneRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMarkDoneMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * To-Do List service
   * </pre>
   */
  public static final class TodoFutureStub extends io.grpc.stub.AbstractFutureStub<TodoFutureStub> {
    private TodoFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TodoFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TodoFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Add a new task
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<service.AddTaskResponse> addTask(
        service.AddTaskRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddTaskMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * List all tasks
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<service.ListTasksResponse> listTasks(
        service.ListTasksRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListTasksMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Mark an existing task as done
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<service.MarkDoneResponse> markDone(
        service.MarkDoneRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMarkDoneMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ADD_TASK = 0;
  private static final int METHODID_LIST_TASKS = 1;
  private static final int METHODID_MARK_DONE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TodoImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TodoImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ADD_TASK:
          serviceImpl.addTask((service.AddTaskRequest) request,
              (io.grpc.stub.StreamObserver<service.AddTaskResponse>) responseObserver);
          break;
        case METHODID_LIST_TASKS:
          serviceImpl.listTasks((service.ListTasksRequest) request,
              (io.grpc.stub.StreamObserver<service.ListTasksResponse>) responseObserver);
          break;
        case METHODID_MARK_DONE:
          serviceImpl.markDone((service.MarkDoneRequest) request,
              (io.grpc.stub.StreamObserver<service.MarkDoneResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class TodoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TodoBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return service.TodoProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Todo");
    }
  }

  private static final class TodoFileDescriptorSupplier
      extends TodoBaseDescriptorSupplier {
    TodoFileDescriptorSupplier() {}
  }

  private static final class TodoMethodDescriptorSupplier
      extends TodoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TodoMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TodoGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TodoFileDescriptorSupplier())
              .addMethod(getAddTaskMethod())
              .addMethod(getListTasksMethod())
              .addMethod(getMarkDoneMethod())
              .build();
        }
      }
    }
    return result;
  }
}
