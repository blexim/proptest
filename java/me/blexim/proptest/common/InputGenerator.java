package me.blexim.proptest.common;

public interface InputGenerator<I extends Input> {
  I next();
}
