package me.blexim.multiset;

import me.blexim.proptest.common.InputGenerator;
import me.blexim.proptest.common.TestOracle;
import me.blexim.proptest.driver.TestDriver;
import me.blexim.proptest.generator.MessageGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

import static com.google.common.truth.Truth.assertThat;

public class MultisetTest {
  private TestDriver<MultisetProtos.Op> driver;
  private MessageGenerator<MultisetProtos.Op> opGenerator;

  @Before
  public void setUp() throws Exception {
    opGenerator = MessageGenerator.create(MultisetProtos.Op.class);
    driver = TestDriver.create(this::newGenerator, this::runTest, 5);
  }

  @Test
  public void doTest() {
    assertThat(driver.search(5))
        .isEqualTo(Optional.empty());
  }

  private TestOracle.Result runTest(Iterable<MultisetProtos.Op> ops) {
    System.out.printf("Testing %s\n", ops);
    Multiset multiset = Multiset.create();
    ops.forEach(multiset::process);
    return multiset.invariant() ? TestOracle.Result.PASS : TestOracle.Result.FAIL;
  }

  private InputGenerator<MultisetProtos.Op> newGenerator(Random rand) {
    return () -> opGenerator.next(rand);
  }
}
