package me.blexim.proptest.generator;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import java.util.Random;

public class MessageGenerator<T extends Message> implements Generator<T> {
  private final DynamicMessageGenerator dynamicGenerator;
  private final T defaultInstance;

  private MessageGenerator(DynamicMessageGenerator dynamicGenerator, T defaultInstance) {
    this.dynamicGenerator = dynamicGenerator;
    this.defaultInstance = defaultInstance;
  }

  static <T extends Message> MessageGenerator<T> create(T defaultInstance) {
    Descriptor descriptor = defaultInstance.getDescriptorForType();
    ProtobufGenerator protobufGenerator = ProtobufGenerator.create();
    return new MessageGenerator<>(DynamicMessageGenerator.create(descriptor, protobufGenerator),
        defaultInstance);
  }

  static <T extends Message> MessageGenerator<T> create(Class<T> clazz) throws Exception {
    T defaultInstance = (T) clazz.getMethod("getDefaultInstance").invoke(null);
    return create(defaultInstance);
  }

  @Override
  public T next(Random rand) {
    try {
      DynamicMessage dynamicMessage = dynamicGenerator.next(rand);
      return (T) defaultInstance.getParserForType()
          .parseFrom(dynamicMessage.toByteString());
    } catch (InvalidProtocolBufferException e) {
      // NOTREACHED
      return null;
    }
  }
}
