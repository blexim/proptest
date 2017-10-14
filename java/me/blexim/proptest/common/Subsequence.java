package me.blexim.proptest.common;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class Subsequence {
  private final ImmutableSet<Integer> idxes;

  private Subsequence(ImmutableSet<Integer> idxes) {
    this.idxes = idxes;
  }

  public static Subsequence create(Iterable<Integer> idxes) {
    return new Subsequence(ImmutableSet.copyOf(idxes));
  }

  public static Subsequence create(ImmutableSet<Integer> idxes) {
    return new Subsequence(idxes);
  }

  public static Subsequence empty() {
    return new Subsequence(ImmutableSet.of());
  }

  public Subsequence union(Subsequence that) {
    return create(Sets.union(this.idxes, that.idxes));
  }

  ImmutableSet<Integer> getIdxes() {
    return idxes;
  }
}
