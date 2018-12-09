// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: moving.proto

package com.doubleknd26.moving.proto;

/**
 * <pre>
 * This type is about the sources that extract data what we need. 
 * </pre>
 *
 * Protobuf enum {@code SourceType}
 */
public enum SourceType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>NAVER = 0;</code>
   */
  NAVER(0),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>NAVER = 0;</code>
   */
  public static final int NAVER_VALUE = 0;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static SourceType valueOf(int value) {
    return forNumber(value);
  }

  public static SourceType forNumber(int value) {
    switch (value) {
      case 0: return NAVER;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<SourceType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      SourceType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<SourceType>() {
          public SourceType findValueByNumber(int number) {
            return SourceType.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return com.doubleknd26.moving.proto.Moving.getDescriptor()
        .getEnumTypes().get(0);
  }

  private static final SourceType[] VALUES = values();

  public static SourceType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private SourceType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:SourceType)
}
