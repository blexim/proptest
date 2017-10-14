package me.blexim.proptest.minimise;

import me.blexim.proptest.common.Input;
import me.blexim.proptest.common.InputSequence;

public interface TestMinimiser<I extends Input> {
  InputSequence<I> minimise(InputSequence<I> inputs);
}
