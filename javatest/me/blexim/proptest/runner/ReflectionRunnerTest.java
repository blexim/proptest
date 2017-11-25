package me.blexim.proptest.runner;

import me.blexim.proptest.common.Action;
import me.blexim.proptest.driver.ReflectiveTestDriver;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.truth.Truth.assertThat;

public class ReflectionRunnerTest {
  private static final int SEQ_LEN = 10;

  @Test
  public void findsSimpleCounterexample() {
    testTarget(SimpleTarget::new);
  }

  @Test
  public void findMoreComplexCounterexample() {
    testTarget(IncreasingTarget::new);
  }

  private <T> void testTarget(Supplier<T> supplier) {
    ReflectiveTestDriver driver = ReflectiveTestDriver.create(supplier, SEQ_LEN);
    assertThat(driver.search(10)).isNotEqualTo(Optional.empty());
  }

  public static class SimpleTarget {
    @Action
    public void action(int a) {
      assertThat(a % 2).isEqualTo(0);
    }
  }

  public static class IncreasingTarget {
    int last = Integer.MIN_VALUE;

    @Action
    public void shouldIncrease(int x) {
      assertThat(x).isAtLeast(last);
      last = x;
    }
  }
}
