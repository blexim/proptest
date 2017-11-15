package me.blexim.proptest.common;

import java.util.Random;

public interface InputGeneratorFactory<I> {
  InputGenerator<I> makeGenerator(Random random);
}
