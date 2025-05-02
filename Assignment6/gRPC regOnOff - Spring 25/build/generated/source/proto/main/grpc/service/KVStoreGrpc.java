package service;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Key-Value Store service
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.49.1)",
    comments = "Source: services/kv.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class KVStoreGrpc {

  private KVStoreGrpc() {}

  public static final String SERVICE_NAME = "services.KVStore";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<service.KVPutRequest,
      service.KVPutResponse> getPutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Put",
      requestType = service.KVPutRequest.class,
      responseType = service.KVPutResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<service.KVPutRequest,
      service.KVPutResponse> getPutMethod() {
    io.grpc.MethodDescriptor<service.KVPutRequest, service.KVPutResponse> getPutMethod;
    if ((getPutMethod = KVStoreGrpc.getPutMethod) == null) {
      synchronized (KVStoreGrpc.class) {
        if ((getPutMethod = KVStoreGrpc.getPutMethod) == null) {
          KVStoreGrpc.getPutMethod = getPutMethod =
              io.grpc.MethodDescriptor.<service.KVPutRequest, service.KVPutResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Put"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.KVPutRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.KVPutResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVStoreMethodDescriptorSupplier("Put"))
              .build();
        }
      }
    }
    return getPutMethod;
  }

  private static volatile io.grpc.MethodDescriptor<service.KVGetRequest,
      service.KVGetResponse> getGetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Get",
      requestType = service.KVGetRequest.class,
      responseType = service.KVGetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<service.KVGetRequest,
      service.KVGetResponse> getGetMethod() {
    io.grpc.MethodDescriptor<service.KVGetRequest, service.KVGetResponse> getGetMethod;
    if ((getGetMethod = KVStoreGrpc.getGetMethod) == null) {
      synchronized (KVStoreGrpc.class) {
        if ((getGetMethod = KVStoreGrpc.getGetMethod) == null) {
          KVStoreGrpc.getGetMethod = getGetMethod =
              io.grpc.MethodDescriptor.<service.KVGetRequest, service.KVGetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Get"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.KVGetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.KVGetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVStoreMethodDescriptorSupplier("Get"))
              .build();
        }
      }
    }
    return getGetMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static KVStoreStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KVStoreStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KVStoreStub>() {
        @java.lang.Override
        public KVStoreStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KVStoreStub(channel, callOptions);
        }
      };
    return KVStoreStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static KVStoreBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KVStoreBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KVStoreBlockingStub>() {
        @java.lang.Override
        public KVStoreBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KVStoreBlockingStub(channel, callOptions);
        }
      };
    return KVStoreBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static KVStoreFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KVStoreFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KVStoreFutureStub>() {
        @java.lang.Override
        public KVStoreFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KVStoreFutureStub(channel, callOptions);
        }
      };
    return KVStoreFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Key-Value Store service
   * </pre>
   */
  public static abstract class KVStoreImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Store a key-value pair
     * </pre>
     */
    public void put(service.KVPutRequest request,
        io.grpc.stub.StreamObserver<service.KVPutResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPutMethod(), responseObserver);
    }

    /**
     * <pre>
     * Retrieve a value by key
     * </pre>
     */
    public void get(service.KVGetRequest request,
        io.grpc.stub.StreamObserver<service.KVGetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPutMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                service.KVPutRequest,
                service.KVPutResponse>(
                  this, METHODID_PUT)))
          .addMethod(
            getGetMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                service.KVGetRequest,
                service.KVGetResponse>(
                  this, METHODID_GET)))
          .build();
    }
  }

  /**
   * <pre>
   * Key-Value Store service
   * </pre>
   */
  public static final class KVStoreStub extends io.grpc.stub.AbstractAsyncStub<KVStoreStub> {
    private KVStoreStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KVStoreStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KVStoreStub(channel, callOptions);
    }

    /**
     * <pre>
     * Store a key-value pair
     * </pre>
     */
    public void put(service.KVPutRequest request,
        io.grpc.stub.StreamObserver<service.KVPutResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Retrieve a value by key
     * </pre>
     */
    public void get(service.KVGetRequest request,
        io.grpc.stub.StreamObserver<service.KVGetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Key-Value Store service
   * </pre>
   */
  public static final class KVStoreBlockingStub extends io.grpc.stub.AbstractBlockingStub<KVStoreBlockingStub> {
    private KVStoreBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KVStoreBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KVStoreBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Store a key-value pair
     * </pre>
     */
    public service.KVPutResponse put(service.KVPutRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPutMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Retrieve a value by key
     * </pre>
     */
    public service.KVGetResponse get(service.KVGetRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Key-Value Store service
   * </pre>
   */
  public static final class KVStoreFutureStub extends io.grpc.stub.AbstractFutureStub<KVStoreFutureStub> {
    private KVStoreFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KVStoreFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KVStoreFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Store a key-value pair
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<service.KVPutResponse> put(
        service.KVPutRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Retrieve a value by key
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<service.KVGetResponse> get(
        service.KVGetRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PUT = 0;
  private static final int METHODID_GET = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final KVStoreImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(KVStoreImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PUT:
          serviceImpl.put((service.KVPutRequest) request,
              (io.grpc.stub.StreamObserver<service.KVPutResponse>) responseObserver);
          break;
        case METHODID_GET:
          serviceImpl.get((service.KVGetRequest) request,
              (io.grpc.stub.StreamObserver<service.KVGetResponse>) responseObserver);
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

  private static abstract class KVStoreBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    KVStoreBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return service.KVStoreProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("KVStore");
    }
  }

  private static final class KVStoreFileDescriptorSupplier
      extends KVStoreBaseDescriptorSupplier {
    KVStoreFileDescriptorSupplier() {}
  }

  private static final class KVStoreMethodDescriptorSupplier
      extends KVStoreBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    KVStoreMethodDescriptorSupplier(String methodName) {
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
      synchronized (KVStoreGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new KVStoreFileDescriptorSupplier())
              .addMethod(getPutMethod())
              .addMethod(getGetMethod())
              .build();
        }
      }
    }
    return result;
  }
}
