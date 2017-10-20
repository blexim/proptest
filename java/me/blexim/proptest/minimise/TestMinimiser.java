package me.blexim.proptest.minimise;

import com.google.common.collect.ImmutableList;

public interface TestMinimiser<I> {
  ImmutableList<I> minimise(Iterable<I> inputs);
}
