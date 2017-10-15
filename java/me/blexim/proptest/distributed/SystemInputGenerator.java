package me.blexim.proptest.distributed;

import com.google.common.collect.ImmutableList;
import me.blexim.proptest.common.Input;
import me.blexim.proptest.common.InputGenerator;

import java.util.Random;

class SystemInputGenerator implements InputGenerator<Input> {
  private final ImmutableList<InputGenerator<Input>> componentGenerators;
  private final Random rand;

  private SystemInputGenerator(ImmutableList<InputGenerator<Input>> componentGenerators,
      Random rand) {
    this.componentGenerators = componentGenerators;
    this.rand = rand;
  }

  static SystemInputGenerator create(ImmutableList<InputGenerator<Input>> componentGenerators,
      long seed) {
    return new SystemInputGenerator(componentGenerators, new Random(seed));
  }

  @Override
  public Input next() {
    int componentIdx = rand.nextInt(componentGenerators.size());
    Input componentInput = componentGenerators.get(componentIdx).next();
    return SystemInput.create(componentIdx, componentInput);
  }
}
