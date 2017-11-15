package me.blexim.proptest.generator;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class MessageGenerator<T extends Message> implements Generator<T> {
  private static final Logger logger = LoggerFactory.getLogger(MessageGenerator.class);

  private final DynamicMessageGenerator dynamicGenerator;
  private final T defaultInstance;

  private MessageGenerator(DynamicMessageGenerator dynamicGenerator, T defaultInstance) {
    this.dynamicGenerator = dynamicGenerator;
    this.defaultInstance = defaultInstance;
  }

  public static <T extends Message> MessageGenerator<T> create(T defaultInstance) {
    Descriptor descriptor = defaultInstance.getDescriptorForType();
    ProtobufGenerator protobufGenerator = ProtobufGenerator.create();
    return new MessageGenerator<>(DynamicMessageGenerator.create(descriptor, protobufGenerator),
        defaultInstance);
  }

  public static <T extends Message> MessageGenerator<T> create(Class<T> clazz) {
    try {
      T defaultInstance = (T) clazz.getMethod("getDefaultInstance").invoke(null);
      return create(defaultInstance);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      logger.error("Couldn't get default message instance", e);
      return null;
    }
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
