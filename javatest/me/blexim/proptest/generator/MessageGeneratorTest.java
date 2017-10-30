package me.blexim.proptest.generator;

import java.util.Random;
import me.blexim.proptest.test.TestProtos.Foo;
import org.junit.Before;
import org.junit.Test;

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
    System.out.println("Running....");
    System.out.printf("Generated %s\n", generator.next(rand).toString());
  }
}