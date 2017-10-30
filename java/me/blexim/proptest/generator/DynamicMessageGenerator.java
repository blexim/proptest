package me.blexim.proptest.generator;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;
import java.util.Random;

class DynamicMessageGenerator implements Generator<DynamicMessage> {
  private final Descriptor descriptor;
  private final ProtobufGenerator protobufGenerator;

  private DynamicMessageGenerator(Descriptor descriptor, ProtobufGenerator protobufGenerator) {
    this.descriptor = descriptor;
    this.protobufGenerator = protobufGenerator;
  }

  static DynamicMessageGenerator create(Descriptor descriptor, ProtobufGenerator protobufGenerator) {
    return new DynamicMessageGenerator(descriptor, protobufGenerator);
  }

  DynamicMessage next(Random rand) {
    DynamicMessage.Builder builder = DynamicMessage.newBuilder(descriptor);
    return builder.build();
  }
}
