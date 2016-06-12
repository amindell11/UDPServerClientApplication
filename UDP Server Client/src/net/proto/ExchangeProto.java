// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/Exchange.proto

package net.proto;

public final class ExchangeProto {
  private ExchangeProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface exchangeOrBuilder extends
      // @@protoc_insertion_point(interface_extends:exchange)
      com.google.protobuf.GeneratedMessage.
          ExtendableMessageOrBuilder<exchange> {

    /**
     * <code>optional int32 id = 1;</code>
     */
    boolean hasId();
    /**
     * <code>optional int32 id = 1;</code>
     */
    int getId();

    /**
     * <code>optional int32 time_stamp = 2;</code>
     */
    boolean hasTimeStamp();
    /**
     * <code>optional int32 time_stamp = 2;</code>
     */
    int getTimeStamp();

    /**
     * <code>optional int32 packet_number = 3;</code>
     */
    boolean hasPacketNumber();
    /**
     * <code>optional int32 packet_number = 3;</code>
     */
    int getPacketNumber();
  }
  /**
   * Protobuf type {@code exchange}
   */
  public  static final class exchange extends
      com.google.protobuf.GeneratedMessage.ExtendableMessage<
        exchange> implements
      // @@protoc_insertion_point(message_implements:exchange)
      exchangeOrBuilder {
    // Use exchange.newBuilder() to construct.
    private exchange(com.google.protobuf.GeneratedMessage.ExtendableBuilder<net.proto.ExchangeProto.exchange, ?> builder) {
      super(builder);
    }
    private exchange() {
      id_ = 0;
      timeStamp_ = 0;
      packetNumber_ = 0;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private exchange(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) {
      this();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              id_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              timeStamp_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              packetNumber_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw new RuntimeException(e.setUnfinishedMessage(this));
      } catch (java.io.IOException e) {
        throw new RuntimeException(
            new com.google.protobuf.InvalidProtocolBufferException(
                e.getMessage()).setUnfinishedMessage(this));
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return net.proto.ExchangeProto.internal_static_exchange_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return net.proto.ExchangeProto.internal_static_exchange_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              net.proto.ExchangeProto.exchange.class, net.proto.ExchangeProto.exchange.Builder.class);
    }

    private int bitField0_;
    public static final int ID_FIELD_NUMBER = 1;
    private int id_;
    /**
     * <code>optional int32 id = 1;</code>
     */
    public boolean hasId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional int32 id = 1;</code>
     */
    public int getId() {
      return id_;
    }

    public static final int TIME_STAMP_FIELD_NUMBER = 2;
    private int timeStamp_;
    /**
     * <code>optional int32 time_stamp = 2;</code>
     */
    public boolean hasTimeStamp() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int32 time_stamp = 2;</code>
     */
    public int getTimeStamp() {
      return timeStamp_;
    }

    public static final int PACKET_NUMBER_FIELD_NUMBER = 3;
    private int packetNumber_;
    /**
     * <code>optional int32 packet_number = 3;</code>
     */
    public boolean hasPacketNumber() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional int32 packet_number = 3;</code>
     */
    public int getPacketNumber() {
      return packetNumber_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!extensionsAreInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      com.google.protobuf.GeneratedMessage
        .ExtendableMessage<net.proto.ExchangeProto.exchange>.ExtensionWriter
          extensionWriter = newExtensionWriter();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, timeStamp_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, packetNumber_);
      }
      extensionWriter.writeUntil(536870912, output);
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, timeStamp_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, packetNumber_);
      }
      size += extensionsSerializedSize();
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    public static net.proto.ExchangeProto.exchange parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static net.proto.ExchangeProto.exchange parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static net.proto.ExchangeProto.exchange parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static net.proto.ExchangeProto.exchange parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static net.proto.ExchangeProto.exchange parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static net.proto.ExchangeProto.exchange parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static net.proto.ExchangeProto.exchange parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static net.proto.ExchangeProto.exchange parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static net.proto.ExchangeProto.exchange parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static net.proto.ExchangeProto.exchange parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(net.proto.ExchangeProto.exchange prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code exchange}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.ExtendableBuilder<
          net.proto.ExchangeProto.exchange, Builder> implements
        // @@protoc_insertion_point(builder_implements:exchange)
        net.proto.ExchangeProto.exchangeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return net.proto.ExchangeProto.internal_static_exchange_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return net.proto.ExchangeProto.internal_static_exchange_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                net.proto.ExchangeProto.exchange.class, net.proto.ExchangeProto.exchange.Builder.class);
      }

      // Construct using net.proto.ExchangeProto.exchange.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        id_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        timeStamp_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        packetNumber_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return net.proto.ExchangeProto.internal_static_exchange_descriptor;
      }

      public net.proto.ExchangeProto.exchange getDefaultInstanceForType() {
        return net.proto.ExchangeProto.exchange.getDefaultInstance();
      }

      public net.proto.ExchangeProto.exchange build() {
        net.proto.ExchangeProto.exchange result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public net.proto.ExchangeProto.exchange buildPartial() {
        net.proto.ExchangeProto.exchange result = new net.proto.ExchangeProto.exchange(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.id_ = id_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.timeStamp_ = timeStamp_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.packetNumber_ = packetNumber_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof net.proto.ExchangeProto.exchange) {
          return mergeFrom((net.proto.ExchangeProto.exchange)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(net.proto.ExchangeProto.exchange other) {
        if (other == net.proto.ExchangeProto.exchange.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasTimeStamp()) {
          setTimeStamp(other.getTimeStamp());
        }
        if (other.hasPacketNumber()) {
          setPacketNumber(other.getPacketNumber());
        }
        this.mergeExtensionFields(other);
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        if (!extensionsAreInitialized()) {
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        net.proto.ExchangeProto.exchange parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (net.proto.ExchangeProto.exchange) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int id_ ;
      /**
       * <code>optional int32 id = 1;</code>
       */
      public boolean hasId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional int32 id = 1;</code>
       */
      public int getId() {
        return id_;
      }
      /**
       * <code>optional int32 id = 1;</code>
       */
      public Builder setId(int value) {
        bitField0_ |= 0x00000001;
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 id = 1;</code>
       */
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        id_ = 0;
        onChanged();
        return this;
      }

      private int timeStamp_ ;
      /**
       * <code>optional int32 time_stamp = 2;</code>
       */
      public boolean hasTimeStamp() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional int32 time_stamp = 2;</code>
       */
      public int getTimeStamp() {
        return timeStamp_;
      }
      /**
       * <code>optional int32 time_stamp = 2;</code>
       */
      public Builder setTimeStamp(int value) {
        bitField0_ |= 0x00000002;
        timeStamp_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 time_stamp = 2;</code>
       */
      public Builder clearTimeStamp() {
        bitField0_ = (bitField0_ & ~0x00000002);
        timeStamp_ = 0;
        onChanged();
        return this;
      }

      private int packetNumber_ ;
      /**
       * <code>optional int32 packet_number = 3;</code>
       */
      public boolean hasPacketNumber() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional int32 packet_number = 3;</code>
       */
      public int getPacketNumber() {
        return packetNumber_;
      }
      /**
       * <code>optional int32 packet_number = 3;</code>
       */
      public Builder setPacketNumber(int value) {
        bitField0_ |= 0x00000004;
        packetNumber_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 packet_number = 3;</code>
       */
      public Builder clearPacketNumber() {
        bitField0_ = (bitField0_ & ~0x00000004);
        packetNumber_ = 0;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:exchange)
    }

    // @@protoc_insertion_point(class_scope:exchange)
    private static final net.proto.ExchangeProto.exchange DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new net.proto.ExchangeProto.exchange();
    }

    public static net.proto.ExchangeProto.exchange getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<exchange>
        PARSER = new com.google.protobuf.AbstractParser<exchange>() {
      public exchange parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        try {
          return new exchange(input, extensionRegistry);
        } catch (RuntimeException e) {
          if (e.getCause() instanceof
              com.google.protobuf.InvalidProtocolBufferException) {
            throw (com.google.protobuf.InvalidProtocolBufferException)
                e.getCause();
          }
          throw e;
        }
      }
    };

    public static com.google.protobuf.Parser<exchange> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<exchange> getParserForType() {
      return PARSER;
    }

    public net.proto.ExchangeProto.exchange getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_exchange_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_exchange_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\024proto/Exchange.proto\"K\n\010exchange\022\n\n\002id" +
      "\030\001 \001(\005\022\022\n\ntime_stamp\030\002 \001(\005\022\025\n\rpacket_num" +
      "ber\030\003 \001(\005*\010\010\004\020\200\200\200\200\002B\032\n\tnet.protoB\rExchan" +
      "geProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_exchange_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_exchange_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_exchange_descriptor,
        new java.lang.String[] { "Id", "TimeStamp", "PacketNumber", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}