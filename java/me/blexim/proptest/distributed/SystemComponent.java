package me.blexim.proptest.distributed;

import com.google.auto.value.AutoValue;
import me.blexim.proptest.common.Input;
import me.blexim.proptest.common.InputGeneratorFactory;

@AutoValue
public abstract class SystemComponent {
  public abstract Controller controller();
  public abstract InputGeneratorFactory<Input> inputGeneratorFactory();

  public static SystemComponent create(Controller controller,
      InputGeneratorFactory<Input> inputGeneratorFactory) {
    return new AutoValue_SystemComponent(controller, inputGeneratorFactory);
  }
}
