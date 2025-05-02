package service;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.49.1)",
    comments = "Source: services/fitness.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FitnessGrpc {

  private FitnessGrpc() {}

  public static final String SERVICE_NAME = "services.Fitness";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<service.AddRequest,
      service.AddResponse> getAddExerciseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "addExercise",
      requestType = service.AddRequest.class,
      responseType = service.AddResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<service.AddRequest,
      service.AddResponse> getAddExerciseMethod() {
    io.grpc.MethodDescriptor<service.AddRequest, service.AddResponse> getAddExerciseMethod;
    if ((getAddExerciseMethod = FitnessGrpc.getAddExerciseMethod) == null) {
      synchronized (FitnessGrpc.class) {
        if ((getAddExerciseMethod = FitnessGrpc.getAddExerciseMethod) == null) {
          FitnessGrpc.getAddExerciseMethod = getAddExerciseMethod =
              io.grpc.MethodDescriptor.<service.AddRequest, service.AddResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "addExercise"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.AddRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.AddResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FitnessMethodDescriptorSupplier("addExercise"))
              .build();
        }
      }
    }
    return getAddExerciseMethod;
  }

  private static volatile io.grpc.MethodDescriptor<service.GetRequest,
      service.GetResponse> getGetExerciseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getExercise",
      requestType = service.GetRequest.class,
      responseType = service.GetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<service.GetRequest,
      service.GetResponse> getGetExerciseMethod() {
    io.grpc.MethodDescriptor<service.GetRequest, service.GetResponse> getGetExerciseMethod;
    if ((getGetExerciseMethod = FitnessGrpc.getGetExerciseMethod) == null) {
      synchronized (FitnessGrpc.class) {
        if ((getGetExerciseMethod = FitnessGrpc.getGetExerciseMethod) == null) {
          FitnessGrpc.getGetExerciseMethod = getGetExerciseMethod =
              io.grpc.MethodDescriptor.<service.GetRequest, service.GetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getExercise"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.GetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.GetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FitnessMethodDescriptorSupplier("getExercise"))
              .build();
        }
      }
    }
    return getGetExerciseMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FitnessStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FitnessStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FitnessStub>() {
        @java.lang.Override
        public FitnessStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FitnessStub(channel, callOptions);
        }
      };
    return FitnessStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FitnessBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FitnessBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FitnessBlockingStub>() {
        @java.lang.Override
        public FitnessBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FitnessBlockingStub(channel, callOptions);
        }
      };
    return FitnessBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FitnessFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FitnessFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FitnessFutureStub>() {
        @java.lang.Override
        public FitnessFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FitnessFutureStub(channel, callOptions);
        }
      };
    return FitnessFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class FitnessImplBase implements io.grpc.BindableService {

    /**
     */
    public void addExercise(service.AddRequest request,
        io.grpc.stub.StreamObserver<service.AddResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddExerciseMethod(), responseObserver);
    }

    /**
     */
    public void getExercise(service.GetRequest request,
        io.grpc.stub.StreamObserver<service.GetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetExerciseMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getAddExerciseMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                service.AddRequest,
                service.AddResponse>(
                  this, METHODID_ADD_EXERCISE)))
          .addMethod(
            getGetExerciseMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                service.GetRequest,
                service.GetResponse>(
                  this, METHODID_GET_EXERCISE)))
          .build();
    }
  }

  /**
   */
  public static final class FitnessStub extends io.grpc.stub.AbstractAsyncStub<FitnessStub> {
    private FitnessStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FitnessStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FitnessStub(channel, callOptions);
    }

    /**
     */
    public void addExercise(service.AddRequest request,
        io.grpc.stub.StreamObserver<service.AddResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddExerciseMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getExercise(service.GetRequest request,
        io.grpc.stub.StreamObserver<service.GetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetExerciseMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class FitnessBlockingStub extends io.grpc.stub.AbstractBlockingStub<FitnessBlockingStub> {
    private FitnessBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FitnessBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FitnessBlockingStub(channel, callOptions);
    }

    /**
     */
    public service.AddResponse addExercise(service.AddRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddExerciseMethod(), getCallOptions(), request);
    }

    /**
     */
    public service.GetResponse getExercise(service.GetRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetExerciseMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class FitnessFutureStub extends io.grpc.stub.AbstractFutureStub<FitnessFutureStub> {
    private FitnessFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FitnessFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FitnessFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<service.AddResponse> addExercise(
        service.AddRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddExerciseMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<service.GetResponse> getExercise(
        service.GetRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetExerciseMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ADD_EXERCISE = 0;
  private static final int METHODID_GET_EXERCISE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FitnessImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(FitnessImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ADD_EXERCISE:
          serviceImpl.addExercise((service.AddRequest) request,
              (io.grpc.stub.StreamObserver<service.AddResponse>) responseObserver);
          break;
        case METHODID_GET_EXERCISE:
          serviceImpl.getExercise((service.GetRequest) request,
              (io.grpc.stub.StreamObserver<service.GetResponse>) responseObserver);
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

  private static abstract class FitnessBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FitnessBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return service.FitnessProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Fitness");
    }
  }

  private static final class FitnessFileDescriptorSupplier
      extends FitnessBaseDescriptorSupplier {
    FitnessFileDescriptorSupplier() {}
  }

  private static final class FitnessMethodDescriptorSupplier
      extends FitnessBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    FitnessMethodDescriptorSupplier(String methodName) {
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
      synchronized (FitnessGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FitnessFileDescriptorSupplier())
              .addMethod(getAddExerciseMethod())
              .addMethod(getGetExerciseMethod())
              .build();
        }
      }
    }
    return result;
  }
}
