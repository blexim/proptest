package me.blexim.proptest.common;

import java.util.function.Predicate;

public interface TestOracle<I> {
  enum Result {
    PASS,
    FAIL
  }

  Result runTest(Iterable<I> inputs);

  static <I> TestOracle<I> create(Predicate<Iterable<I>> predicate) {
    return inputs -> predicate.test(inputs) ? Result.PASS : Result.FAIL;
  }
}
