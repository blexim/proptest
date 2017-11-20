package me.blexim.proptest.driver;

import com.google.common.collect.ImmutableList;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import me.blexim.proptest.common.InputGenerator;
import me.blexim.proptest.common.InputGeneratorFactory;
import me.blexim.proptest.common.TestOracle;
import me.blexim.proptest.minimise.DeltaDebuggingMinimiser;
import me.blexim.proptest.minimise.TestMinimiser;
import me.blexim.proptest.runner.ReflectionGenerator;
import me.blexim.proptest.runner.ReflectionRunner;
import me.blexim.proptest.runner.ReflectiveInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDriver<I> {
  private static final Logger logger = LoggerFactory.getLogger(TestDriver.class);

  private final InputGeneratorFactory<I> inputGeneratorFactory;
  private final TestOracle<I> testOracle;
  private final int seqLenth;
  private final TestMinimiser<I> minimiser;

  private long seed;

  private TestDriver(InputGeneratorFactory<I> inputGeneratorFactory,
      TestOracle<I> testOracle, int seqLength, TestMinimiser<I> minimiser,
      long initialSeed) {
    this.inputGeneratorFactory = inputGeneratorFactory;
    this.testOracle = testOracle;
    this.seqLenth = seqLength;
    this.minimiser = minimiser;
    this.seed = initialSeed;
  }

  public static <I> TestDriver<I> create(
      InputGeneratorFactory<I> inputGeneratorFactory, TestOracle<I> testOracle,
      int seqLenth, long initialSeed) {
    return new TestDriver<>(inputGeneratorFactory, testOracle, seqLenth,
        DeltaDebuggingMinimiser.create(testOracle), initialSeed);
  }

  public static <I> TestDriver<I> create(
      InputGeneratorFactory<I> inputGeneratorFactory, TestOracle<I> testOracle,
      int seqLenth) {
    return new TestDriver<>(inputGeneratorFactory, testOracle, seqLenth,
        DeltaDebuggingMinimiser.create(testOracle), new Random().nextLong());
  }

  public static <T> TestDriver<ReflectiveInput> create(Supplier<T> supplier, int seqLenth,
      long initialSeed) {
    TestOracle<ReflectiveInput> oracle =
        inputs -> ReflectionRunner.create(supplier.get()).runTest(inputs);
    InputGeneratorFactory<ReflectiveInput> generatorFactory =
        rand -> ReflectionGenerator.create(supplier.get().getClass(), rand);
    return TestDriver.create(generatorFactory, oracle, seqLenth, initialSeed);
  }

  public static <T> TestDriver<ReflectiveInput> create(Supplier<T> supplier, int seqLenth) {
    return create(supplier, seqLenth, new Random().nextLong());
  }

    public Optional<ImmutableList<I>> search(int numIterations) {
    return findBadInput(numIterations)
        .map(minimiser::minimise);
  }

  private Optional<ImmutableList<I>> findBadInput(int numIterations) {
    for (int i = 0; i < numIterations; i++) {
      ImmutableList<I> inputs = generateInputs();
      logger.info("Testing {}", inputs);

      if (testOracle.runTest(inputs) == TestOracle.Result.FAIL) {
        return Optional.of(inputs);
      }
    }

    return Optional.empty();
  }

  private ImmutableList<I> generateInputs() {
    ImmutableList.Builder<I> builder = ImmutableList.builder();
    Random rand = new Random(seed);
    InputGenerator<I> generator = inputGeneratorFactory.makeGenerator(rand);
    seed++;

    for (int i = 0; i < seqLenth; i++) {
      builder.add(generator.nextInput());
    }

    return builder.build();
  }
}
