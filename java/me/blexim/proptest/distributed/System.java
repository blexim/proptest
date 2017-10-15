package me.blexim.proptest.distributed;

import com.google.common.collect.ImmutableList;
import me.blexim.proptest.common.Input;
import me.blexim.proptest.common.InputGenerator;
import me.blexim.proptest.common.InputGeneratorFactory;

public class System implements InputGeneratorFactory<Input> {
  private final ImmutableList<SystemComponent> components;

  private System(ImmutableList<SystemComponent> components) {
    this.components = components;
  }

  @Override
  public InputGenerator<Input> create(long seed) {
    ImmutableList<InputGenerator<Input>> componentGenerators = components.stream()
        .map(component -> component.inputGeneratorFactory().create(seed))
        .collect(ImmutableList.toImmutableList());
    return SystemInputGenerator.create(componentGenerators, seed);
  }

  public SystemController makeController() {
    ImmutableList<Controller> componentControllers = components.stream()
        .map(SystemComponent::controller)
        .collect(ImmutableList.toImmutableList());
    return SystemController.create(componentControllers);
  }
}
