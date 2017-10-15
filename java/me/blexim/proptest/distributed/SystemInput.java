package me.blexim.proptest.distributed;

import com.google.auto.value.AutoValue;
import me.blexim.proptest.common.Input;

@AutoValue
abstract class SystemInput implements Input {
  abstract int componentIdx();
  abstract Input input();

  static SystemInput create(int componentIdx, Input input) {
    return new AutoValue_SystemInput(componentIdx, input);
  }
}
