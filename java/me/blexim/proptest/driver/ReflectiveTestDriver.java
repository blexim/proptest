package me.blexim.proptest.driver;

import com.google.common.collect.ImmutableList;
import me.blexim.proptest.common.InputGeneratorFactory;
import me.blexim.proptest.common.TestOracle;
import me.blexim.proptest.runner.ReflectionGenerator;
import me.blexim.proptest.runner.ReflectionRunner;
import me.blexim.proptest.runner.ReflectiveInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class ReflectiveTestDriver {
  private static final Logger logger = LoggerFactory.getLogger(ReflectiveTestDriver.class);

  private final TestDriver<ReflectiveInput> testDriver;

  private ReflectiveTestDriver(TestDriver<ReflectiveInput> testDriver) {
    this.testDriver = testDriver;
  }

  public Optional<ImmutableList<ReflectiveInput>> search(int numIterations) {
    return testDriver.search(numIterations);
  }

  public static <T> ReflectiveTestDriver create(Supplier<T> supplier, int seqLenth,
      long initialSeed) {
    Class<T> clazz = (Class<T>) supplier.get().getClass();
    logger.info("Made reflective driver for {}", clazz.getName());
    TestOracle<ReflectiveInput> oracle =
        inputs -> ReflectionRunner.create(supplier.get()).runTest(inputs);
    InputGeneratorFactory<ReflectiveInput> generatorFactory =
        rand -> ReflectionGenerator.create(clazz, rand);
    TestDriver<ReflectiveInput> driver = TestDriver.create(generatorFactory, oracle, seqLenth,
        initialSeed);
    return new ReflectiveTestDriver(driver);
  }

  public static <T> ReflectiveTestDriver create(Supplier<T> supplier, int seqLenth) {
    return create(supplier, seqLenth, new Random().nextLong());
  }
}
