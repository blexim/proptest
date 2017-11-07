package me.blexim.multiset;

public interface CountingSet {
  void add(int x);

  void remove(int x);

  int getSetSize();

  int getSize();
}
