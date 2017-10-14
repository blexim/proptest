package me.blexim.proptest.common;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class InputSequenceTest {
  @Test
  public void evenSplit() {
    InputSequence<Integer> seq = InputSequence.create(0, 1, 2, 3);
    assertThat(seq.split(2))
        .containsExactly(Subsequence.create(0, 1),
            Subsequence.create(2, 3));
  }

  @Test
  public void oddSplit() {
    InputSequence<Integer> seq = InputSequence.create(0, 1, 2, 3, 4);
    assertThat(seq.split(2))
        .containsExactly(Subsequence.create(0, 1, 2),
            Subsequence.create(3, 4));
  }

  @Test
  public void oddSplitInto3() {
    InputSequence<Integer> seq = InputSequence.create(0, 1, 2, 3, 4);
    assertThat(seq.split(3))
        .containsExactly(Subsequence.create(0, 1),
            Subsequence.create(2, 3),
            Subsequence.create(4));
  }

  @Test
  public void dropWorks() {
    InputSequence<Integer> seq = InputSequence.create(0, 1, 2, 3, 4);
    assertThat(seq.minus(Subsequence.create(0, 2)))
        .isEqualTo(InputSequence.create(1, 3, 4));
  }
}