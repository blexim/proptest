package me.blexim.multiset;

import java.util.HashSet;

public class CountingSetV3 implements CountingSet {
  private final HashSet<Integer> set;

  private int size;

  private CountingSetV3() {
    this.set = new HashSet<>();
    this.size = 0;
  }

  public static CountingSetV3 create() {
    return new CountingSetV3();
  }

  @Override
  public void add(int x) {
    if (set.add(x)) {
      size++;
    }
  }

  @Override
  public void remove(int x) {
    if (set.remove(x)) {
      size--;
    }
  }

  @Override
  public int getSetSize() {
    return set.size();
  }

  @Override
  public int getSize() {
    return size;
  }
}
