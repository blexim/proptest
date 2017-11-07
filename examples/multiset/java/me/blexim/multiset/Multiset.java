package me.blexim.multiset;

import me.blexim.multiset.MultisetProtos;

import java.util.HashSet;

public class Multiset {
  private final HashSet<Integer> set;

  private int size;

  private Multiset() {
    this.set = new HashSet<>();
    this.size = 0;
  }

  public static Multiset create() {
    return new Multiset();
  }

  public void add(int x) {
    set.add(x);
    size++;
  }

  public void remove(int x) {
    set.remove(x);
    size--;
  }

  public void process(MultisetProtos.Op op) {
    switch (op.getOpCase()) {
      case ADD:
        add(op.getAdd().getKey());
        return;
      case REMOVE:
        remove(op.getRemove().getKey());
        return;
      default:
        // Ignore
    }
  }

  public boolean invariant() {
    return set.size() == size;
  }
}
