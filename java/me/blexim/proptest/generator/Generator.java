package me.blexim.proptest.generator;

import java.util.Random;

public interface Generator<T> {
  T next(Random rand);
}
