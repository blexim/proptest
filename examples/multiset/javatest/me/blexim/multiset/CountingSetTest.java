package me.blexim.multiset;

import com.google.common.collect.ImmutableList;
import me.blexim.proptest.common.Action;
import me.blexim.proptest.driver.ReflectiveTestDriver;
import me.blexim.proptest.runner.ReflectiveInput;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.truth.Truth.assertThat;

public class CountingSetTest {
  private static final Logger logger = LoggerFactory.getLogger(CountingSetTest.class);

  private static final int TEST_LENGTH = 50;
  private static final long SEED = 420L;
  private static final int ITERS = 5;

  @Test
  public void v1SetHasBugs() {
    assertThat(search(CountingSetV1::create))
        .isNotEqualTo(Optional.empty());
  }

  @Test
  public void v2SetHasBugs() {
    assertThat(search(CountingSetV2::create))
        .isNotEqualTo(Optional.empty());
  }

  @Test
  public void v3SetIsCorrect() {
    assertThat(search(CountingSetV3::create))
        .isEqualTo(Optional.empty());
  }

  public static class CountingSetChecker implements CountingSet {
    private final CountingSet set;

    private CountingSetChecker(CountingSet set) {
      this.set = set;
    }

    private static Supplier<CountingSetChecker> wrap(Supplier<CountingSet> supplier) {
      return () -> new CountingSetChecker(supplier.get());
    }

    @Action
    @Override
    public void add(int x) {
      set.add(x);
    }

    @Action
    @Override
    public void remove(int x) {
      set.remove(x);
    }

    @Action
    @Override
    public int getSetSize() {
      return set.getSetSize();
    }

    @Action
    @Override
    public int getSize() {
      return set.getSize();
    }

    @Action
    public void checkInvariant() {
      assertThat(set.getSetSize()).isEqualTo(set.getSize());
    }
  }

  private Optional<String> search(Supplier<CountingSet> setSupplier) {
    return ReflectiveTestDriver.create(CountingSetChecker.wrap(setSupplier), TEST_LENGTH, SEED)
        .search(ITERS);
  }
}
