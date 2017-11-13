package me.blexim.multiset.testing;

import me.blexim.multiset.CountingSet;
import me.blexim.multiset.MultisetProtos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpApplier {
  private static final Logger logger = LoggerFactory.getLogger(OpApplier.class);

  public static void applyOp(CountingSet set, MultisetProtos.Op op) {
    switch (op.getOpCase()) {
      case ADD:
        set.add(op.getAdd().getKey());
        return;
      case REMOVE:
        set.remove(op.getRemove().getKey());
        return;
      default:
        logger.warn("Unknown operation: {}", op);
    }
  }

  public static void applyOps(CountingSet set, Iterable<MultisetProtos.Op> ops) {
    ops.forEach(op -> applyOp(set, op));
  }
}
