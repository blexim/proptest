package me.blexim.proptest.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class InputSequence<I> {
  private final ImmutableList<I> inputs;

  private InputSequence(ImmutableList<I> inputs) {
    this.inputs = inputs;
  }

  public static <I> InputSequence<I> create(ImmutableList<I> inputs) {
    return new InputSequence<>(inputs);
  }

  public static <I> InputSequence<I> create(Iterable<I> inputs) {
    return new InputSequence<>(ImmutableList.copyOf(inputs));
  }

  public static <I> InputSequence<I> create(I... inputs) {
    return new InputSequence<>(ImmutableList.copyOf(Arrays.asList(inputs)));
  }

  public ImmutableList<I> getInputs() {
    return inputs;
  }

  public int size() {
    return inputs.size();
  }

  public ImmutableList<Subsequence> split(int numPartitions) {
    int itemsPerSet = (inputs.size() + numPartitions - 1) / numPartitions;
    List<List<Integer>> partitions = Lists.partition(IntStream.range(0, inputs.size())
            .mapToObj(Integer::valueOf)
            .collect(ImmutableList.toImmutableList()),
        itemsPerSet);
    return partitions.stream()
        .map(Subsequence::create)
        .collect(ImmutableList.toImmutableList());
  }

  public InputSequence<I> minus(Subsequence subsequence) {
    ImmutableList.Builder<I> builder = ImmutableList.builder();

    for (int i = 0; i < inputs.size(); i++) {
      if (!subsequence.getIdxes().contains(i)) {
        builder.add(inputs.get(i));
      }
    }

    return create(builder.build());
  }
}
