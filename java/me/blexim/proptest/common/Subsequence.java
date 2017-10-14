package me.blexim.proptest.common;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Arrays;

@AutoValue
public abstract class Subsequence {
  public abstract ImmutableSet<Integer> idxes();

  public static Subsequence create(ImmutableSet<Integer> idxes) {
    return new AutoValue_Subsequence(idxes);
  }

  public static Subsequence create(Iterable<Integer> idxes) {
    return new AutoValue_Subsequence(ImmutableSet.copyOf(idxes));
  }

  public static Subsequence create(Integer... idxes) {
    return create(Arrays.asList(idxes));
  }

  public Subsequence union(Subsequence that) {
    return create(Sets.union(this.idxes(), that.idxes()));
  }
}
