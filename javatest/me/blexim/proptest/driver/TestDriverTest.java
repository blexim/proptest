package me.blexim.proptest.driver;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.Optional;
import java.util.Random;
import me.blexim.proptest.common.InputGenerator;
import me.blexim.proptest.common.TestOracle;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TestDriverTest {
  private enum I {
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
    Optional<ImmutableList<I>> bad = search(this::containsA, 10);
    assertThat(bad)
        .isEqualTo(Optional.of(ImmutableList.of(I.A)));
  }

  @Test
  public void testPairMinimises() {
    Optional<ImmutableList<I>> bad = search(this::containsABeforeB, 5);
    assertThat(bad)
        .isEqualTo(Optional.of(ImmutableList.of(I.A, I.B)));
  }

  @Test
  public void testImpossibleSpecFails() {
    Optional<ImmutableList<I>> bad = search(this::containsABeforeB, 1);
    assertThat(bad)
        .isEqualTo(Optional.empty());
  }

  private TestOracle.Result containsA(Iterable<I> inputs) {
    if (Iterables.contains(inputs, I.A)) {
      return TestOracle.Result.FAIL;
    } else {
      return TestOracle.Result.PASS;
    }
  }

  private TestOracle.Result containsABeforeB(Iterable<I> inputs) {
    boolean sawA = false;

    for (I input : inputs) {
      if (input == I.A) {
        sawA = true;
      } else if (input == I.B && sawA) {
        return TestOracle.Result.FAIL;
      }
    }

    return TestOracle.Result.PASS;
  }

  private Optional<ImmutableList<I>> search(TestOracle<I> oracle, int seqLength) {
    TestDriver<I> driver = TestDriver.create(IGenerator::new, oracle, seqLength, INITIAL_SEED);
    return driver.search(NUM_ITERS);
  }

  private static class IGenerator implements InputGenerator<I> {
    private final Random rand;

    private IGenerator(Random rand) {
      this.rand = rand;
    }

    @Override
    public I next() {
      int idx = rand.nextInt(I.values().length);
      return I.values()[idx];
    }
  }
}