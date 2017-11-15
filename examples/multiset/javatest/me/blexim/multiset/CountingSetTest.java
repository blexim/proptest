package me.blexim.multiset;

import com.google.common.collect.ImmutableList;
import me.blexim.multiset.testing.OpApplier;
import me.blexim.proptest.common.InputGenerator;
import me.blexim.proptest.common.InputGeneratorFactory;
import me.blexim.proptest.common.TestOracle;
import me.blexim.proptest.driver.TestDriver;
import me.blexim.proptest.generator.MessageGenerator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import static com.google.common.truth.Truth.assertThat;

public class CountingSetTest {
  private static final Logger logger = LoggerFactory.getLogger(CountingSetTest.class);

  private static final int TEST_LENGTH = 50;
  private static final long SEED = 420L;
  private static final int ITERS = 5;

  @Test
  public void v1SetHasBugs() {
    assertThat(search(CountingSetV1::create))
        .isNotEqualTo(Optional.empty());
  }

  @Test
  public void v2SetHasBugs() {
    assertThat(search(CountingSetV2::create))
        .isNotEqualTo(Optional.empty());
  }

  @Test
  public void v3SetIsCorrect() {
    assertThat(search(CountingSetV3::create))
        .isEqualTo(Optional.empty());
  }

  private Optional<ImmutableList<MultisetProtos.Op>> search(Supplier<CountingSet> setSupplier) {
    return newDriver(setSupplier).search(ITERS);
  }

  private TestDriver<MultisetProtos.Op> newDriver(Supplier<CountingSet> setSupplier) {
    TestHarness harness = new TestHarness(setSupplier);
    return TestDriver.create(harness, harness, TEST_LENGTH, SEED);
  }

  private static class TestHarness implements TestOracle<MultisetProtos.Op>,
      InputGeneratorFactory<MultisetProtos.Op> {
    private final Supplier<CountingSet> factory;
    private final MessageGenerator<MultisetProtos.Op> generator;

    private TestHarness(Supplier<CountingSet> factory) {
      this.factory = factory;
      this.generator = MessageGenerator.create(MultisetProtos.Op.class);
    }

    @Override
    public Result runTest(Iterable<MultisetProtos.Op> inputs) {
      CountingSet countingSet = factory.get();
      OpApplier.applyOps(countingSet, inputs);

      if (countingSet.getSetSize() == countingSet.getSize()) {
        return TestOracle.Result.PASS;
      } else {
        return TestOracle.Result.FAIL;
      }
    }

    @Override
    public InputGenerator<MultisetProtos.Op> makeGenerator(Random random) {
      return () -> generator.next(random);
    }
  }
}
