package me.blexim.proptest.generator;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.OneofDescriptor;
import com.google.protobuf.DynamicMessage;
import java.util.List;
import java.util.Random;

class DynamicMessageGenerator implements Generator<DynamicMessage> {
  private final Descriptor descriptor;
  private final ProtobufGenerator protobufGenerator;

  private DynamicMessageGenerator(Descriptor descriptor, ProtobufGenerator protobufGenerator) {
    this.descriptor = descriptor;
    this.protobufGenerator = protobufGenerator;
  }

  static DynamicMessageGenerator create(Descriptor descriptor,
      ProtobufGenerator protobufGenerator) {
    return new DynamicMessageGenerator(descriptor, protobufGenerator);
  }

  @Override
  public DynamicMessage next(Random rand) {
    DynamicMessage.Builder builder = DynamicMessage.newBuilder(descriptor);

    for (FieldDescriptor fieldDescriptor : descriptor.getFields()) {
      if (fieldDescriptor.getContainingOneof() != null) {
        if (fieldDescriptor.isRepeated()) {
          int reps = nextRepsCount(rand);
          for (int i = 0; i < reps; i++) {
            builder.addRepeatedField(fieldDescriptor, nextField(fieldDescriptor, rand));
          }
        } else {
          builder.setField(fieldDescriptor, nextField(fieldDescriptor, rand));
        }
      }
    }

    for (OneofDescriptor oneofDescriptor : descriptor.getOneofs()) {
      FieldDescriptor fieldDescriptor = pick(oneofDescriptor.getFields(), rand);
      builder.setField(fieldDescriptor, nextField(fieldDescriptor, rand));
    }

    return builder.build();
  }

  private int nextRepsCount(Random rand) {
    return rand.nextInt(5);
  }

  private Object nextField(FieldDescriptor fieldDescriptor, Random rand) {
    switch (fieldDescriptor.getType()) {
      case INT32:
      case UINT32:
      case INT64:
      case UINT64:
      case SINT32:
      case SINT64:
      case FIXED32:
      case FIXED64:
      case SFIXED32:
      case SFIXED64:
        return protobufGenerator.nextLong(rand);
      case DOUBLE:
      case FLOAT:
        return protobufGenerator.nextDouble(rand);
      case MESSAGE:
        Descriptor messageType = fieldDescriptor.getMessageType();
        return protobufGenerator.nextProto(messageType, rand);
      case ENUM:
        return pick(fieldDescriptor.getEnumType().getValues(), rand);
      default:
        return null;
    }
  }

  private static <V> V pick(List<V> values, Random rand) {
    int idx = rand.nextInt(values.size());
    return values.get(idx);
  }
}
