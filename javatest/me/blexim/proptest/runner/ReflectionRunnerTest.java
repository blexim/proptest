package me.blexim.proptest.runner;

import me.blexim.proptest.common.Action;
import me.blexim.proptest.common.TestOracle;
import me.blexim.proptest.driver.TestDriver;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

import static com.google.common.truth.Truth.assertThat;

public class ReflectionRunnerTest {
  private static final int SEQ_LEN = 10;

  private TestDriver<ReflectiveInput> driver;

  @Before
  public void setUp() {
    driver = TestDriver.create(Target::new, SEQ_LEN);
  }

  @Test
  public void findsSimpleCounterexample() {
    assertThat(driver.search(10)).isNotEqualTo(Optional.empty());
  }

  public static class Target {
    @Action
    public void action(int a) {
      assertThat(a % 2).isEqualTo(0);
    }
  }
}
