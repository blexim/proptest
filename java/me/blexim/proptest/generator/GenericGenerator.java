package me.blexim.proptest.generator;

import java.util.Random;

public interface GenericGenerator {
  <T> T next(Class<T> clazz, Random rand);
}
