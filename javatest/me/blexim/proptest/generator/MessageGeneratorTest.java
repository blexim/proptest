package me.blexim.proptest.generator;

import java.util.Random;
import me.blexim.proptest.test.TestProtos.Foo;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class MessageGeneratorTest {
  private MessageGenerator<Foo> generator;
  private Random rand;

  @Before
  public void setUp() throws Exception {
    GenericGenerator genericGenerator = GenericGeneratorImpl.create();
    ProtobufGenerator protobufGenerator = ProtobufGenerator.create(genericGenerator);
    generator = MessageGenerator.create(Foo.class, protobufGenerator, genericGenerator);
    rand = new Random();
  }

  @Test
  public void testRandFoo() {
    Foo f = generator.next(rand);
    assertThat(f).isNotEqualTo(Foo.getDefaultInstance());
  }
}