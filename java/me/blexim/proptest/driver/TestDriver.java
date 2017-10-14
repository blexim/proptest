package me.blexim.proptest.driver;

import com.google.common.collect.ImmutableList;
import me.blexim.proptest.common.Input;
import me.blexim.proptest.common.InputGenerator;
import me.blexim.proptest.common.InputGeneratorFactory;
import me.blexim.proptest.common.InputSequence;
import me.blexim.proptest.common.TestOracle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDriver<I extends Input> {
  private static final Logger logger = LoggerFactory.getLogger(TestDriver.class);

  private final InputGeneratorFactory<I> inputGeneratorFactory;
  private final TestOracle<I> testOracle;
  private final int seqLenth;

  private TestDriver(InputGeneratorFactory<I> inputGeneratorFactory,
      TestOracle<I> testOracle, int seqLength) {
    this.inputGeneratorFactory = inputGeneratorFactory;
    this.testOracle = testOracle;
    this.seqLenth = seqLength;
  }

  public static <I extends Input> TestDriver<I> create(
      InputGeneratorFactory<I> inputGeneratorFactory, TestOracle<I> testOracle,
      int seqLenth) {
    return new TestDriver<>(inputGeneratorFactory, testOracle, seqLenth);
  }

  private TestOracle.Result doTest() {
    InputSequence<I> inputs = generateInputs();

    try {
      return testOracle.runTest(inputs);
    } catch (Throwable t) {
      return TestOracle.Result.FAIL;
    }
  }

  private InputSequence<I> generateInputs() {
    ImmutableList.Builder<I> builder = ImmutableList.builder();
    InputGenerator<I> generator = inputGeneratorFactory.create();

    for (int i = 0; i < seqLenth; i++) {
      builder.add(generator.next());
    }

    return InputSequence.create(builder.build());
  }
}
