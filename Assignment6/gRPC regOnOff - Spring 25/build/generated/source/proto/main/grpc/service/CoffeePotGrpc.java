package service;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.49.1)",
    comments = "Source: services/coffepot.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CoffeePotGrpc {

  private CoffeePotGrpc() {}

  public static final String SERVICE_NAME = "services.CoffeePot";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      service.BrewResponse> getBrewMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "brew",
      requestType = com.google.protobuf.Empty.class,
      responseType = service.BrewResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      service.BrewResponse> getBrewMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, service.BrewResponse> getBrewMethod;
    if ((getBrewMethod = CoffeePotGrpc.getBrewMethod) == null) {
      synchronized (CoffeePotGrpc.class) {
        if ((getBrewMethod = CoffeePotGrpc.getBrewMethod) == null) {
          CoffeePotGrpc.getBrewMethod = getBrewMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, service.BrewResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "brew"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.BrewResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoffeePotMethodDescriptorSupplier("brew"))
              .build();
        }
      }
    }
    return getBrewMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      service.GetCupResponse> getGetCupMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getCup",
      requestType = com.google.protobuf.Empty.class,
      responseType = service.GetCupResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      service.GetCupResponse> getGetCupMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, service.GetCupResponse> getGetCupMethod;
    if ((getGetCupMethod = CoffeePotGrpc.getGetCupMethod) == null) {
      synchronized (CoffeePotGrpc.class) {
        if ((getGetCupMethod = CoffeePotGrpc.getGetCupMethod) == null) {
          CoffeePotGrpc.getGetCupMethod = getGetCupMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, service.GetCupResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getCup"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.GetCupResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoffeePotMethodDescriptorSupplier("getCup"))
              .build();
        }
      }
    }
    return getGetCupMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      service.BrewStatusResponse> getBrewStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "brewStatus",
      requestType = com.google.protobuf.Empty.class,
      responseType = service.BrewStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      service.BrewStatusResponse> getBrewStatusMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, service.BrewStatusResponse> getBrewStatusMethod;
    if ((getBrewStatusMethod = CoffeePotGrpc.getBrewStatusMethod) == null) {
      synchronized (CoffeePotGrpc.class) {
        if ((getBrewStatusMethod = CoffeePotGrpc.getBrewStatusMethod) == null) {
          CoffeePotGrpc.getBrewStatusMethod = getBrewStatusMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, service.BrewStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "brewStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.BrewStatusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CoffeePotMethodDescriptorSupplier("brewStatus"))
              .build();
        }
      }
    }
    return getBrewStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CoffeePotStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoffeePotStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoffeePotStub>() {
        @java.lang.Override
        public CoffeePotStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoffeePotStub(channel, callOptions);
        }
      };
    return CoffeePotStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CoffeePotBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoffeePotBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoffeePotBlockingStub>() {
        @java.lang.Override
        public CoffeePotBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoffeePotBlockingStub(channel, callOptions);
        }
      };
    return CoffeePotBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CoffeePotFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CoffeePotFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CoffeePotFutureStub>() {
        @java.lang.Override
        public CoffeePotFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CoffeePotFutureStub(channel, callOptions);
        }
      };
    return CoffeePotFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class CoffeePotImplBase implements io.grpc.BindableService {

    /**
     */
    public void brew(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<service.BrewResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBrewMethod(), responseObserver);
    }

    /**
     */
    public void getCup(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<service.GetCupResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetCupMethod(), responseObserver);
    }

    /**
     */
    public void brewStatus(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<service.BrewStatusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBrewStatusMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getBrewMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                service.BrewResponse>(
                  this, METHODID_BREW)))
          .addMethod(
            getGetCupMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                service.GetCupResponse>(
                  this, METHODID_GET_CUP)))
          .addMethod(
            getBrewStatusMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                service.BrewStatusResponse>(
                  this, METHODID_BREW_STATUS)))
          .build();
    }
  }

  /**
   */
  public static final class CoffeePotStub extends io.grpc.stub.AbstractAsyncStub<CoffeePotStub> {
    private CoffeePotStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoffeePotStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoffeePotStub(channel, callOptions);
    }

    /**
     */
    public void brew(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<service.BrewResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBrewMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCup(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<service.GetCupResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetCupMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void brewStatus(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<service.BrewStatusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBrewStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CoffeePotBlockingStub extends io.grpc.stub.AbstractBlockingStub<CoffeePotBlockingStub> {
    private CoffeePotBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoffeePotBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoffeePotBlockingStub(channel, callOptions);
    }

    /**
     */
    public service.BrewResponse brew(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBrewMethod(), getCallOptions(), request);
    }

    /**
     */
    public service.GetCupResponse getCup(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetCupMethod(), getCallOptions(), request);
    }

    /**
     */
    public service.BrewStatusResponse brewStatus(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBrewStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CoffeePotFutureStub extends io.grpc.stub.AbstractFutureStub<CoffeePotFutureStub> {
    private CoffeePotFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CoffeePotFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CoffeePotFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<service.BrewResponse> brew(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBrewMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<service.GetCupResponse> getCup(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetCupMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<service.BrewStatusResponse> brewStatus(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBrewStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_BREW = 0;
  private static final int METHODID_GET_CUP = 1;
  private static final int METHODID_BREW_STATUS = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CoffeePotImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CoffeePotImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_BREW:
          serviceImpl.brew((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<service.BrewResponse>) responseObserver);
          break;
        case METHODID_GET_CUP:
          serviceImpl.getCup((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<service.GetCupResponse>) responseObserver);
          break;
        case METHODID_BREW_STATUS:
          serviceImpl.brewStatus((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<service.BrewStatusResponse>) responseObserver);
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

  private static abstract class CoffeePotBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CoffeePotBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return service.CoffePotProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CoffeePot");
    }
  }

  private static final class CoffeePotFileDescriptorSupplier
      extends CoffeePotBaseDescriptorSupplier {
    CoffeePotFileDescriptorSupplier() {}
  }

  private static final class CoffeePotMethodDescriptorSupplier
      extends CoffeePotBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CoffeePotMethodDescriptorSupplier(String methodName) {
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
      synchronized (CoffeePotGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CoffeePotFileDescriptorSupplier())
              .addMethod(getBrewMethod())
              .addMethod(getGetCupMethod())
              .addMethod(getBrewStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
