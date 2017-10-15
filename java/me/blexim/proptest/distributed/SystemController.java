package me.blexim.proptest.distributed;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import me.blexim.proptest.common.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SystemController implements Controller {
  private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

  private final ImmutableList<Controller> components;

  private SystemController(ImmutableList<Controller> components) {
    this.components = components;
  }

  static SystemController create(ImmutableList<Controller> components) {
    return new SystemController(components);
  }

  @Override
  public void accept(Input input) {
    if (input instanceof SystemInput) {
      SystemInput systemInput = (SystemInput) input;
      int componentIdx = systemInput.componentIdx();
      Input componentInput = systemInput.input();

      Preconditions.checkArgument(componentIdx >= 0 && componentIdx < components.size());
      components.get(componentIdx).accept(componentInput);
    } else {
      logger.error("Got an input I didn't understand! {}", input);
    }
  }
}
