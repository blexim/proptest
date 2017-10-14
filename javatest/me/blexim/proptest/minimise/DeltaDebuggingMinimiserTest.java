package me.blexim.proptest.minimise;

import me.blexim.proptest.common.Input;
import me.blexim.proptest.common.InputSequence;
import me.blexim.proptest.common.TestOracle;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DeltaDebuggingMinimiserTest {
  private static enum I implements Input {
    A,
    B,
    C
  }

  @Test
  public void testSimpleMinimises() {
    InputSequence<I> minimised = minimise(this::containsA, I.A, I.B, I.B, I.A);
    assertThat(minimised.getInputs()).containsExactly(I.A);
  }

  @Test(expected = IllegalArgumentException.class)
  public void throwsIfInputPasses() {
    minimise(this::containsA, I.B);
  }

  @Test
  public void testPairMinimises() {
    InputSequence<I> minimised = minimise(this::containsABeforeB,
        I.B, I.A, I.A, I.C, I.B, I.B, I.A);
    assertThat(minimised.getInputs()).containsExactly(I.A, I.B);
  }

  private TestOracle.Result containsA(InputSequence<I> inputs) {
    if (inputs.getInputs().contains(I.A)) {
      return TestOracle.Result.FAIL;
    } else {
      return TestOracle.Result.PASS;
    }
  }

  private TestOracle.Result containsABeforeB(InputSequence<I> inputs) {
    boolean sawA = false;

    for (I input : inputs.getInputs()) {
      if (input == I.A) {
        sawA = true;
      } else if (input == I.B && sawA) {
        return TestOracle.Result.FAIL;
      }
    }

    return TestOracle.Result.PASS;
  }

  private InputSequence<I> minimise(TestOracle<I> oracle, I... inputs) {
    DeltaDebuggingMinimiser<I> minimiser = DeltaDebuggingMinimiser.create(oracle);
    InputSequence<I> inputSequence = InputSequence.create(inputs);
    return minimiser.minimise(inputSequence);
  }
}
