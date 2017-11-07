package me.blexim.proptest.common;

import java.util.Random;

public interface InputGeneratorFactory<I> {
  InputGenerator<I> create(Random random);
}
