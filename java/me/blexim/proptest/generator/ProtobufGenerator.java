package me.blexim.proptest.generator;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;
import java.util.HashMap;
import java.util.Random;

public class ProtobufGenerator {
  private final HashMap<Descriptor, Generator<DynamicMessage>> messageGenerators;
  private final Generator<Long> longGenerator;
  private final Generator<Double> doubleGenerator;

  private ProtobufGenerator() {
    this.messageGenerators = new HashMap<>();
    this.longGenerator = MemoizedGenerator.create(ProtobufGenerator::nextLong);
    this.doubleGenerator = MemoizedGenerator.create(ProtobufGenerator::nextDouble);
  }

  private Generator<DynamicMessage> messageGenerator(Descriptor descriptor) {
    return messageGenerators.computeIfAbsent(descriptor, this::createMessageGenerator);
  }

  private Generator<DynamicMessage> createMessageGenerator(Descriptor descriptor) {
    return MemoizedGenerator.create(DynamicMessageGenerator.create(descriptor, this));
  }

  private static long nextLong(Random rand) {
    return rand.nextLong();
  }

  private static double nextDouble(Random rand) {
    return rand.nextDouble();
  }
}
