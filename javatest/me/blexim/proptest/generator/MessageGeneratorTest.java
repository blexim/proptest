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
    generator = MessageGenerator.create(Foo.class);
    rand = new Random();
  }

  @Test
  public void testRandFoo() {
    Foo f = generator.next(rand);
    assertThat(f).isNotEqualTo(Foo.getDefaultInstance());
  }
}