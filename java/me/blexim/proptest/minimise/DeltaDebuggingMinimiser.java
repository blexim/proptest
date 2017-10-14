package me.blexim.proptest.minimise;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import me.blexim.proptest.common.Input;
import me.blexim.proptest.common.InputSequence;
import me.blexim.proptest.common.Subsequence;
import me.blexim.proptest.common.TestOracle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeltaDebuggingMinimiser<I extends Input> implements TestMinimiser<I> {
  private static final Logger logger = LoggerFactory.getLogger(DeltaDebuggingMinimiser.class);

  private final TestOracle<I> testOracle;

  private DeltaDebuggingMinimiser(TestOracle<I> testOracle) {
    this.testOracle = testOracle;
  }

  public static <I extends Input> DeltaDebuggingMinimiser<I> create(TestOracle<I> testOracle) {
    return new DeltaDebuggingMinimiser<>(testOracle);
  }

  public InputSequence<I> minimise(InputSequence<I> inputs) {
    Preconditions.checkArgument(testOracle.runTest(inputs) == TestOracle.Result.FAIL);
    InputSequence<I> ret = inputs;
    int n = 2;

    while (ret.size() > 1) {
      n = Math.min(n, ret.size());
      InputSequence<I> candidate = splitAndSubtract(ret, n);

      if (candidate.size() < ret.size()) {
        ret = candidate;
      } else {
        if (n == ret.size()) {
          break;
        } else {
          n *= 2;
        }
      }
    }

    return ret;
  }

  private InputSequence<I> splitAndSubtract(InputSequence<I> inputs, int numPartitions) {
    ImmutableList<Subsequence> partitions = inputs.split(numPartitions);
    Subsequence dropped = Subsequence.create();

    for (Subsequence partition : partitions) {
      Subsequence candidateDropped = dropped.union(partition);
      InputSequence<I> candidateInputs = inputs.minus(candidateDropped);

      if (testOracle.runTest(candidateInputs) == TestOracle.Result.FAIL) {
        dropped = candidateDropped;
      }
    }

    return inputs.minus(dropped);
  }
}
