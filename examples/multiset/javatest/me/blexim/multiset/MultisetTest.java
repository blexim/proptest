package me.blexim.multiset;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.util.JsonFormat;
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
    driver = TestDriver.create(this::newGenerator, this::runTest, 50);
  }

  @Test
  public void doTest() throws Exception {
    Optional<ImmutableList<MultisetProtos.Op>> badTrace = driver.search(5);
    JsonFormat.Printer printer = JsonFormat.printer();

    if (badTrace.isPresent()) {
      for (MultisetProtos.Op op : badTrace.get()) {
        System.out.println(printer.print(op));
      }
    }

    assertThat(badTrace)
        .isEqualTo(Optional.empty());
  }

  private TestOracle.Result runTest(Iterable<MultisetProtos.Op> ops) {
    Multiset multiset = Multiset.create();
    ops.forEach(multiset::process);
    return multiset.invariant() ? TestOracle.Result.PASS : TestOracle.Result.FAIL;
  }

  private InputGenerator<MultisetProtos.Op> newGenerator(Random rand) {
    return () -> opGenerator.next(rand);
  }
}
