package me.blexim.proptest.common;

public interface InputGeneratorFactory<I extends Input> {
  InputGenerator<I> create(long seed);
}
