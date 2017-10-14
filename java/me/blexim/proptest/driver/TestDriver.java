package me.blexim.proptest.driver;

import com.google.common.collect.ImmutableList;
import me.blexim.proptest.common.Input;
import me.blexim.proptest.common.InputGenerator;
import me.blexim.proptest.common.InputGeneratorFactory;
import me.blexim.proptest.common.InputSequence;
import me.blexim.proptest.common.TestOracle;
import me.blexim.proptest.minimise.DeltaDebuggingMinimiser;
import me.blexim.proptest.minimise.TestMinimiser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;

public class TestDriver<I extends Input> {
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

  public static <I extends Input> TestDriver<I> create(
      InputGeneratorFactory<I> inputGeneratorFactory, TestOracle<I> testOracle,
      int seqLenth, long initialSeed) {
    return new TestDriver<>(inputGeneratorFactory, testOracle, seqLenth,
        DeltaDebuggingMinimiser.create(testOracle), initialSeed);
  }

  public static <I extends Input> TestDriver<I> create(
      InputGeneratorFactory<I> inputGeneratorFactory, TestOracle<I> testOracle,
      int seqLenth) {
    return new TestDriver<>(inputGeneratorFactory, testOracle, seqLenth,
        DeltaDebuggingMinimiser.create(testOracle), new Random().nextLong());
  }

  public Optional<InputSequence<I>> search(int numIterations) {
    return findBadInput(numIterations)
        .map(minimiser::minimise);
  }

  private Optional<InputSequence<I>> findBadInput(int numIterations) {
    for (int i = 0; i < numIterations; i++) {
      InputSequence<I> inputs = generateInputs();
      logger.info("Testing {}", inputs);

      if (testOracle.runTest(inputs) == TestOracle.Result.FAIL) {
        return Optional.of(inputs);
      }
    }

    return Optional.empty();
  }

  private InputSequence<I> generateInputs() {
    ImmutableList.Builder<I> builder = ImmutableList.builder();
    InputGenerator<I> generator = inputGeneratorFactory.create(seed);
    seed++;

    for (int i = 0; i < seqLenth; i++) {
      builder.add(generator.next());
    }

    return InputSequence.create(builder.build());
  }
}
