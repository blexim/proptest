package me.blexim.proptest.common;

public interface TestOracle<I extends Input> {
  enum Result {
    PASS,
    FAIL
  }

  Result runTest(InputSequence<I> inputs);
}
