package me.blexim.proptest.generator;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.OneofDescriptor;
import com.google.protobuf.DynamicMessage;
import java.util.List;
import java.util.Random;

class DynamicMessageGenerator implements Generator<DynamicMessage> {
  private final Descriptor descriptor;
  private final ProtobufGenerator protobufGenerator;
  private final GenericGenerator genericGenerator;

  private DynamicMessageGenerator(Descriptor descriptor, ProtobufGenerator protobufGenerator,
      GenericGenerator genericGenerator) {
    this.descriptor = descriptor;
    this.protobufGenerator = protobufGenerator;
    this.genericGenerator = genericGenerator;
  }

  static DynamicMessageGenerator create(Descriptor descriptor,
      ProtobufGenerator protobufGenerator, GenericGenerator genericGenerator) {
    return new DynamicMessageGenerator(descriptor, protobufGenerator, genericGenerator);
  }

  @Override
  public DynamicMessage next(Random rand) {
    DynamicMessage.Builder builder = DynamicMessage.newBuilder(descriptor);

    for (FieldDescriptor fieldDescriptor : descriptor.getFields()) {
      if (fieldDescriptor.getContainingOneof() == null) {
        if (fieldDescriptor.isRepeated()) {
          int reps = nextRepsCount(rand);
          for (int i = 0; i < reps; i++) {
            builder.addRepeatedField(fieldDescriptor, nextField(fieldDescriptor, rand));
          }
        } else {
          Object value = nextField(fieldDescriptor, rand);
          builder.setField(fieldDescriptor, value);
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
      case SINT32:
      case FIXED32:
      case SFIXED32:
        return genericGenerator.next(Integer.class, rand);
      case INT64:
      case UINT64:
      case SINT64:
      case FIXED64:
      case SFIXED64:
        return genericGenerator.next(Long.class, rand);
      case DOUBLE:
        return genericGenerator.next(Double.class, rand);
      case FLOAT:
        return genericGenerator.next(Float.class, rand);
      case MESSAGE:
        Descriptor messageType = fieldDescriptor.getMessageType();
        return protobufGenerator.nextProto(messageType, rand);
      case ENUM:
        return pick(fieldDescriptor.getEnumType().getValues(), rand);
      case STRING:
        return genericGenerator.next(String.class, rand);
      case BYTES:
        return ByteString.copyFromUtf8(genericGenerator.next(String.class, rand));
      default:
        return null;
    }
  }

  private static <V> V pick(List<V> values, Random rand) {
    int idx = rand.nextInt(values.size());
    return values.get(idx);
  }
}
