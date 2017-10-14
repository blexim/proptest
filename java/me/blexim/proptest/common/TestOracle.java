package me.blexim.proptest.common;

import java.util.function.Predicate;

public interface TestOracle<I extends Input> {
  enum Result {
    PASS,
    FAIL
  }

  Result runTest(InputSequence<I> inputs);

  static <I extends Input> TestOracle<I> create(Predicate<InputSequence<I>> predicate) {
    return inputs -> predicate.test(inputs) ? Result.PASS : Result.FAIL;
  }
}
