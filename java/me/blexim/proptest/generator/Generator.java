package me.blexim.proptest.generator;

import java.util.Random;

interface Generator<T> {
  T next(Random rand);
}
