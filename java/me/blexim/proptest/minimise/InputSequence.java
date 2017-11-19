package me.blexim.proptest.minimise;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@AutoValue
abstract class InputSequence<I> {
  abstract ImmutableList<I> inputs();

  static <I> InputSequence<I> create(ImmutableList<I> inputs) {
    return new AutoValue_InputSequence<>(inputs);
  }

  static <I> InputSequence<I> create(Iterable<I> inputs) {
    return new AutoValue_InputSequence<>(ImmutableList.copyOf(inputs));
  }

  static <I> InputSequence<I> create(I... inputs) {
    return new AutoValue_InputSequence<>(ImmutableList.copyOf(Arrays.asList(inputs)));
  }

  int size() {
    return inputs().size();
  }

  ImmutableList<Subsequence> split(int numPartitions) {
    int itemsPerSet = (size() + numPartitions - 1) / numPartitions;
    List<List<Integer>> partitions = Lists.partition(IntStream.range(0, size())
            .mapToObj(Integer::valueOf)
            .collect(ImmutableList.toImmutableList()),
        itemsPerSet);
    return partitions.stream()
        .map(Subsequence::create)
        .collect(ImmutableList.toImmutableList());
  }

  InputSequence<I> minus(Subsequence subsequence) {
    ImmutableList.Builder<I> builder = ImmutableList.builder();

    for (int i = 0; i < size(); i++) {
      if (!subsequence.idxes().contains(i)) {
        builder.add(inputs().get(i));
      }
    }

    return create(builder.build());
  }
}
