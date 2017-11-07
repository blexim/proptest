package me.blexim.proptest.minimise;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import me.blexim.proptest.common.TestOracle;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DeltaDebuggingMinimiserTest {
  private enum I {
    A,
    B,
    C
  }

  @Test
  public void testSimpleMinimises() {
    ImmutableList<I> minimised = minimise(this::containsA, I.A, I.B, I.B, I.A);
    assertThat(minimised).containsExactly(I.A);
  }

  @Test(expected = IllegalArgumentException.class)
  public void throwsIfInputPasses() {
    minimise(this::containsA, I.B);
  }

  @Test
  public void testPairMinimises() {
    ImmutableList<I> minimised = minimise(this::containsABeforeB,
        I.B, I.A, I.A, I.C, I.B, I.B, I.A);
    assertThat(minimised).containsExactly(I.A, I.B);
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

  private ImmutableList<I> minimise(TestOracle<I> oracle, I... inputs) {
    DeltaDebuggingMinimiser<I> minimiser = DeltaDebuggingMinimiser.create(oracle);
    return minimiser.minimise(Arrays.asList(inputs));
  }
}
