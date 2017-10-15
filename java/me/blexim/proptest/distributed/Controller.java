package me.blexim.proptest.distributed;

import me.blexim.proptest.common.Input;

public interface Controller {
  void accept(Input input);
}
