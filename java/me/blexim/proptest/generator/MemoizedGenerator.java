package me.blexim.proptest.generator;

import java.util.ArrayList;
import java.util.Random;

class MemoizedGenerator<T> implements Generator<T> {
  private static final double REUSE_PROB = 0.1;

  private final Generator<T> generator;
  private final ArrayList<T> memoTable;

  private MemoizedGenerator(Generator<T> generator) {
    this.generator = generator;
    this.memoTable = new ArrayList<T>();
  }

  static <T> MemoizedGenerator<T> create(Generator<T> generator) {
    return new MemoizedGenerator<>(generator);
  }

  @Override
  public T next(Random rand) {
    if (!memoTable.isEmpty() && rand.nextDouble() <= REUSE_PROB) {
      return randomMemo(rand);
    } else {
      T ret = generator.next(rand);
      memoTable.add(ret);
      return ret;
    }
  }

  private T randomMemo(Random rand) {
    int idx = rand.nextInt(memoTable.size());
    return memoTable.get(idx);
  }
}
