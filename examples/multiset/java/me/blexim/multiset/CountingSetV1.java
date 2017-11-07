package me.blexim.multiset;

import java.util.HashSet;

public class CountingSetV1 implements CountingSet {
  private final HashSet<Integer> set;

  private int size;

  private CountingSetV1() {
    this.set = new HashSet<>();
    this.size = 0;
  }

  public static CountingSetV1 create() {
    return new CountingSetV1();
  }

  @Override
  public void add(int x) {
    set.add(x);
    size++;
  }

  @Override
  public void remove(int x) {
    set.remove(x);
    size--;
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
