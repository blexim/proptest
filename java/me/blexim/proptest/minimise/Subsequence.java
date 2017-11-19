package me.blexim.proptest.minimise;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Arrays;

@AutoValue
abstract class Subsequence {
  abstract ImmutableSet<Integer> idxes();

  static Subsequence create(Iterable<Integer> idxes) {
    return new AutoValue_Subsequence(ImmutableSet.copyOf(idxes));
  }

  static Subsequence create(Integer... idxes) {
    return create(Arrays.asList(idxes));
  }

  Subsequence union(Subsequence that) {
    return create(Sets.union(this.idxes(), that.idxes()));
  }
}
