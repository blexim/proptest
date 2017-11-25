package me.blexim.proptest.generator;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Random;

public class GenericGeneratorImpl implements GenericGenerator {
  private static final Logger logger = LoggerFactory.getLogger(GenericGeneratorImpl.class);

  private static final String CHARS = "abcdefgABCDEFG '#0123459";
  private static final int STRING_LEN = 5;

  private final HashMap<Class<?>, MemoizedGenerator<?>> generators;
  private final ProtobufGenerator protobufGenerator;

  private GenericGeneratorImpl() {
    this.generators = new HashMap<>();
    this.protobufGenerator = ProtobufGenerator.create(this);
  }

  public static GenericGeneratorImpl create() {
    return new GenericGeneratorImpl();
  }

  @Override
  public <T> T next(Class<T> clazz, Random rand) {
    Generator<T> generator = (MemoizedGenerator<T>) generators.computeIfAbsent(clazz,
        this::newMemoizedGenerator);
    return generator.next(rand);
  }

  private <T> Generator<T> newGenerator(Class<T> clazz) {
    if (Message.class.isAssignableFrom(clazz)) {
      return (Generator<T>) MessageGenerator.create((Class<? extends Message>) clazz,
          protobufGenerator, this);
    } else if (clazz.isAssignableFrom(Long.class) || clazz.isAssignableFrom(Long.TYPE)) {
      return r -> (T) Long.valueOf(r.nextLong());
    } else if (clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(Integer.TYPE)) {
      return r -> (T) Integer.valueOf(r.nextInt());
    } else if (clazz.isAssignableFrom(Double.class) || clazz.isAssignableFrom(Double.TYPE)) {
      return r -> (T) Double.valueOf(r.nextDouble());
    } else if (clazz.isAssignableFrom(Float.class) || clazz.isAssignableFrom(Float.TYPE)) {
      return r -> (T) Float.valueOf(r.nextFloat());
    } else if (clazz.isAssignableFrom(String.class)) {
      return r -> (T) randString(STRING_LEN, r);
    } else {
      throw new RuntimeException(String.format("Couldn't make generator for class %s",
          clazz.getCanonicalName()));
    }
  }

  private <T> MemoizedGenerator<T> newMemoizedGenerator(Class<T> clazz) {
    return MemoizedGenerator.create(newGenerator(clazz));
  }

  private static String randString(int len, Random rand) {
    StringBuilder builder = new StringBuilder(len);

    for (int i = 0; i < len; i++) {
      builder.append(CHARS.charAt(rand.nextInt(CHARS.length())));
    }

    return builder.toString();
  }
}
