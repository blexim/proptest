package me.blexim.proptest.driver;

import me.blexim.proptest.common.Input;
import me.blexim.proptest.common.InputGenerator;
import me.blexim.proptest.common.InputSequence;
import me.blexim.proptest.common.TestOracle;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

import static com.google.common.truth.Truth.assertThat;

public class TestDriverTest {
  private enum I implements Input {
    A,
    B,
    C,
    D,
    E,
    F,
    G,
    H
  }

  private static final long INITIAL_SEED = 420;
  private static final int NUM_ITERS = 10;

  @Test
  public void testSimpleSearch() {
    Optional<InputSequence<I>> bad = search(this::containsA, 10);
    assertThat(bad)
        .isEqualTo(Optional.of(InputSequence.create(I.A)));
  }

  @Test
  public void testPairMinimises() {
    Optional<InputSequence<I>> bad = search(this::containsABeforeB, 5);
    assertThat(bad)
        .isEqualTo(Optional.of(InputSequence.create(I.A, I.B)));
  }

  @Test
  public void testImpossibleSpecFails() {
    Optional<InputSequence<I>> bad = search(this::containsABeforeB, 1);
    assertThat(bad)
        .isEqualTo(Optional.empty());
  }

  private TestOracle.Result containsA(InputSequence<I> inputs) {
    if (inputs.inputs().contains(I.A)) {
      return TestOracle.Result.FAIL;
    } else {
      return TestOracle.Result.PASS;
    }
  }

  private TestOracle.Result containsABeforeB(InputSequence<I> inputs) {
    boolean sawA = false;

    for (I input : inputs.inputs()) {
      if (input == I.A) {
        sawA = true;
      } else if (input == I.B && sawA) {
        return TestOracle.Result.FAIL;
      }
    }

    return TestOracle.Result.PASS;
  }

  private Optional<InputSequence<I>> search(TestOracle<I> oracle, int seqLength) {
    TestDriver<I> driver = TestDriver.create(IGenerator::new, oracle, seqLength, INITIAL_SEED);
    return driver.search(NUM_ITERS);
  }

  private static class IGenerator implements InputGenerator<I> {
    private final Random rand;

    private IGenerator(long seed) {
      this.rand = new Random(seed);
    }

    @Override
    public I next() {
      int idx = rand.nextInt(I.values().length);
      return I.values()[idx];
    }
  }
}