package me.blexim.proptest.generator;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;
import java.util.HashMap;
import java.util.Random;

public class ProtobufGenerator {
  private final HashMap<Descriptor, Generator<DynamicMessage>> messageGenerators;
  private final GenericGenerator genericGenerator;

  private ProtobufGenerator(GenericGenerator genericGenerator) {
    this.messageGenerators = new HashMap<>();
    this.genericGenerator = genericGenerator;
  }

  static ProtobufGenerator create(GenericGenerator genericGenerator) {
    return new ProtobufGenerator(genericGenerator);
  }

  DynamicMessage nextProto(Descriptor descriptor, Random rand) {
    return messageGenerator(descriptor).next(rand);
  }

  private Generator<DynamicMessage> messageGenerator(Descriptor descriptor) {
    return messageGenerators.computeIfAbsent(descriptor, this::createMessageGenerator);
  }

  private Generator<DynamicMessage> createMessageGenerator(Descriptor descriptor) {
    return MemoizedGenerator.create(DynamicMessageGenerator.create(descriptor, this,
        genericGenerator));
  }
}
