// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: json.prop

package com.finace.miscroservice.rpc.com.test;

/**
 * Protobuf type {@code SimpleList}
 */
public  final class SimpleList extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:SimpleList)
    SimpleListOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SimpleList.newBuilder() to construct.
  private SimpleList(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SimpleList() {
    simpleList_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private SimpleList(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
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
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
              simpleList_ = new java.util.ArrayList<com.finace.miscroservice.rpc.com.test.Simple>();
              mutable_bitField0_ |= 0x00000001;
            }
            simpleList_.add(
                input.readMessage(com.finace.miscroservice.rpc.com.test.Simple.parser(), extensionRegistry));
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
        simpleList_ = java.util.Collections.unmodifiableList(simpleList_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.finace.miscroservice.rpc.com.test.HelloStreamEntity.internal_static_SimpleList_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.finace.miscroservice.rpc.com.test.HelloStreamEntity.internal_static_SimpleList_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.finace.miscroservice.rpc.com.test.SimpleList.class, com.finace.miscroservice.rpc.com.test.SimpleList.Builder.class);
  }

  public static final int SIMPLELIST_FIELD_NUMBER = 1;
  private java.util.List<com.finace.miscroservice.rpc.com.test.Simple> simpleList_;
  /**
   * <code>repeated .Simple simpleList = 1;</code>
   */
  public java.util.List<com.finace.miscroservice.rpc.com.test.Simple> getSimpleListList() {
    return simpleList_;
  }
  /**
   * <code>repeated .Simple simpleList = 1;</code>
   */
  public java.util.List<? extends com.finace.miscroservice.rpc.com.test.SimpleOrBuilder> 
      getSimpleListOrBuilderList() {
    return simpleList_;
  }
  /**
   * <code>repeated .Simple simpleList = 1;</code>
   */
  public int getSimpleListCount() {
    return simpleList_.size();
  }
  /**
   * <code>repeated .Simple simpleList = 1;</code>
   */
  public com.finace.miscroservice.rpc.com.test.Simple getSimpleList(int index) {
    return simpleList_.get(index);
  }
  /**
   * <code>repeated .Simple simpleList = 1;</code>
   */
  public com.finace.miscroservice.rpc.com.test.SimpleOrBuilder getSimpleListOrBuilder(
      int index) {
    return simpleList_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < simpleList_.size(); i++) {
      output.writeMessage(1, simpleList_.get(i));
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < simpleList_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, simpleList_.get(i));
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.finace.miscroservice.rpc.com.test.SimpleList)) {
      return super.equals(obj);
    }
    com.finace.miscroservice.rpc.com.test.SimpleList other = (com.finace.miscroservice.rpc.com.test.SimpleList) obj;

    boolean result = true;
    result = result && getSimpleListList()
        .equals(other.getSimpleListList());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getSimpleListCount() > 0) {
      hash = (37 * hash) + SIMPLELIST_FIELD_NUMBER;
      hash = (53 * hash) + getSimpleListList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.finace.miscroservice.rpc.com.test.SimpleList parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.finace.miscroservice.rpc.com.test.SimpleList parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.finace.miscroservice.rpc.com.test.SimpleList prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code SimpleList}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:SimpleList)
      com.finace.miscroservice.rpc.com.test.SimpleListOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.finace.miscroservice.rpc.com.test.HelloStreamEntity.internal_static_SimpleList_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.finace.miscroservice.rpc.com.test.HelloStreamEntity.internal_static_SimpleList_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.finace.miscroservice.rpc.com.test.SimpleList.class, com.finace.miscroservice.rpc.com.test.SimpleList.Builder.class);
    }

    // Construct using com.finace.miscroservice.rpc.com.test.SimpleList.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getSimpleListFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      if (simpleListBuilder_ == null) {
        simpleList_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        simpleListBuilder_.clear();
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.finace.miscroservice.rpc.com.test.HelloStreamEntity.internal_static_SimpleList_descriptor;
    }

    public com.finace.miscroservice.rpc.com.test.SimpleList getDefaultInstanceForType() {
      return com.finace.miscroservice.rpc.com.test.SimpleList.getDefaultInstance();
    }

    public com.finace.miscroservice.rpc.com.test.SimpleList build() {
      com.finace.miscroservice.rpc.com.test.SimpleList result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.finace.miscroservice.rpc.com.test.SimpleList buildPartial() {
      com.finace.miscroservice.rpc.com.test.SimpleList result = new com.finace.miscroservice.rpc.com.test.SimpleList(this);
      int from_bitField0_ = bitField0_;
      if (simpleListBuilder_ == null) {
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          simpleList_ = java.util.Collections.unmodifiableList(simpleList_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.simpleList_ = simpleList_;
      } else {
        result.simpleList_ = simpleListBuilder_.build();
      }
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.finace.miscroservice.rpc.com.test.SimpleList) {
        return mergeFrom((com.finace.miscroservice.rpc.com.test.SimpleList)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.finace.miscroservice.rpc.com.test.SimpleList other) {
      if (other == com.finace.miscroservice.rpc.com.test.SimpleList.getDefaultInstance()) return this;
      if (simpleListBuilder_ == null) {
        if (!other.simpleList_.isEmpty()) {
          if (simpleList_.isEmpty()) {
            simpleList_ = other.simpleList_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureSimpleListIsMutable();
            simpleList_.addAll(other.simpleList_);
          }
          onChanged();
        }
      } else {
        if (!other.simpleList_.isEmpty()) {
          if (simpleListBuilder_.isEmpty()) {
            simpleListBuilder_.dispose();
            simpleListBuilder_ = null;
            simpleList_ = other.simpleList_;
            bitField0_ = (bitField0_ & ~0x00000001);
            simpleListBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getSimpleListFieldBuilder() : null;
          } else {
            simpleListBuilder_.addAllMessages(other.simpleList_);
          }
        }
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.finace.miscroservice.rpc.com.test.SimpleList parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.finace.miscroservice.rpc.com.test.SimpleList) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<com.finace.miscroservice.rpc.com.test.Simple> simpleList_ =
      java.util.Collections.emptyList();
    private void ensureSimpleListIsMutable() {
      if (!((bitField0_ & 0x00000001) == 0x00000001)) {
        simpleList_ = new java.util.ArrayList<com.finace.miscroservice.rpc.com.test.Simple>(simpleList_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.finace.miscroservice.rpc.com.test.Simple, com.finace.miscroservice.rpc.com.test.Simple.Builder, com.finace.miscroservice.rpc.com.test.SimpleOrBuilder> simpleListBuilder_;

    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public java.util.List<com.finace.miscroservice.rpc.com.test.Simple> getSimpleListList() {
      if (simpleListBuilder_ == null) {
        return java.util.Collections.unmodifiableList(simpleList_);
      } else {
        return simpleListBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public int getSimpleListCount() {
      if (simpleListBuilder_ == null) {
        return simpleList_.size();
      } else {
        return simpleListBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public com.finace.miscroservice.rpc.com.test.Simple getSimpleList(int index) {
      if (simpleListBuilder_ == null) {
        return simpleList_.get(index);
      } else {
        return simpleListBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public Builder setSimpleList(
        int index, com.finace.miscroservice.rpc.com.test.Simple value) {
      if (simpleListBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureSimpleListIsMutable();
        simpleList_.set(index, value);
        onChanged();
      } else {
        simpleListBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public Builder setSimpleList(
        int index, com.finace.miscroservice.rpc.com.test.Simple.Builder builderForValue) {
      if (simpleListBuilder_ == null) {
        ensureSimpleListIsMutable();
        simpleList_.set(index, builderForValue.build());
        onChanged();
      } else {
        simpleListBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public Builder addSimpleList(com.finace.miscroservice.rpc.com.test.Simple value) {
      if (simpleListBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureSimpleListIsMutable();
        simpleList_.add(value);
        onChanged();
      } else {
        simpleListBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public Builder addSimpleList(
        int index, com.finace.miscroservice.rpc.com.test.Simple value) {
      if (simpleListBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureSimpleListIsMutable();
        simpleList_.add(index, value);
        onChanged();
      } else {
        simpleListBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public Builder addSimpleList(
        com.finace.miscroservice.rpc.com.test.Simple.Builder builderForValue) {
      if (simpleListBuilder_ == null) {
        ensureSimpleListIsMutable();
        simpleList_.add(builderForValue.build());
        onChanged();
      } else {
        simpleListBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public Builder addSimpleList(
        int index, com.finace.miscroservice.rpc.com.test.Simple.Builder builderForValue) {
      if (simpleListBuilder_ == null) {
        ensureSimpleListIsMutable();
        simpleList_.add(index, builderForValue.build());
        onChanged();
      } else {
        simpleListBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public Builder addAllSimpleList(
        java.lang.Iterable<? extends com.finace.miscroservice.rpc.com.test.Simple> values) {
      if (simpleListBuilder_ == null) {
        ensureSimpleListIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, simpleList_);
        onChanged();
      } else {
        simpleListBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public Builder clearSimpleList() {
      if (simpleListBuilder_ == null) {
        simpleList_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        simpleListBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public Builder removeSimpleList(int index) {
      if (simpleListBuilder_ == null) {
        ensureSimpleListIsMutable();
        simpleList_.remove(index);
        onChanged();
      } else {
        simpleListBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public com.finace.miscroservice.rpc.com.test.Simple.Builder getSimpleListBuilder(
        int index) {
      return getSimpleListFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public com.finace.miscroservice.rpc.com.test.SimpleOrBuilder getSimpleListOrBuilder(
        int index) {
      if (simpleListBuilder_ == null) {
        return simpleList_.get(index);  } else {
        return simpleListBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public java.util.List<? extends com.finace.miscroservice.rpc.com.test.SimpleOrBuilder> 
         getSimpleListOrBuilderList() {
      if (simpleListBuilder_ != null) {
        return simpleListBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(simpleList_);
      }
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public com.finace.miscroservice.rpc.com.test.Simple.Builder addSimpleListBuilder() {
      return getSimpleListFieldBuilder().addBuilder(
          com.finace.miscroservice.rpc.com.test.Simple.getDefaultInstance());
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public com.finace.miscroservice.rpc.com.test.Simple.Builder addSimpleListBuilder(
        int index) {
      return getSimpleListFieldBuilder().addBuilder(
          index, com.finace.miscroservice.rpc.com.test.Simple.getDefaultInstance());
    }
    /**
     * <code>repeated .Simple simpleList = 1;</code>
     */
    public java.util.List<com.finace.miscroservice.rpc.com.test.Simple.Builder> 
         getSimpleListBuilderList() {
      return getSimpleListFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.finace.miscroservice.rpc.com.test.Simple, com.finace.miscroservice.rpc.com.test.Simple.Builder, com.finace.miscroservice.rpc.com.test.SimpleOrBuilder> 
        getSimpleListFieldBuilder() {
      if (simpleListBuilder_ == null) {
        simpleListBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            com.finace.miscroservice.rpc.com.test.Simple, com.finace.miscroservice.rpc.com.test.Simple.Builder, com.finace.miscroservice.rpc.com.test.SimpleOrBuilder>(
                simpleList_,
                ((bitField0_ & 0x00000001) == 0x00000001),
                getParentForChildren(),
                isClean());
        simpleList_ = null;
      }
      return simpleListBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:SimpleList)
  }

  // @@protoc_insertion_point(class_scope:SimpleList)
  private static final com.finace.miscroservice.rpc.com.test.SimpleList DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.finace.miscroservice.rpc.com.test.SimpleList();
  }

  public static com.finace.miscroservice.rpc.com.test.SimpleList getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SimpleList>
      PARSER = new com.google.protobuf.AbstractParser<SimpleList>() {
    public SimpleList parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new SimpleList(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<SimpleList> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<SimpleList> getParserForType() {
    return PARSER;
  }

  public com.finace.miscroservice.rpc.com.test.SimpleList getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

