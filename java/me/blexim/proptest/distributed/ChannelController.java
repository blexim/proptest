package me.blexim.proptest.distributed;

import com.google.auto.value.AutoValue;
import me.blexim.proptest.common.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelController<M> implements Controller {
  private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);
  public interface ChannelOp<M> extends Input {
  }

  @AutoValue
  public static abstract class DeliverOp<N> implements ChannelOp<N> {
    public static <N> DeliverOp<N> create() {
      return new AutoValue_ChannelController_DeliverOp<>();
    }
  }

  @AutoValue
  public static abstract class DropOp<N> implements ChannelOp<N> {
    public static <N> DropOp<N> create() {
      return new AutoValue_ChannelController_DropOp<>();
    }
  }

  @AutoValue
  public static abstract class AddOp<N> implements ChannelOp<N> {
    public abstract N message();

    public static <N> AddOp<N> create(N message) {
      return new AutoValue_ChannelController_AddOp(message);
    }
  }

  private final Channel<M> channel;

  private ChannelController(Channel<M> channel) {
    this.channel = channel;
  }

  public static <M> ChannelController<M> create(Channel<M> channel) {
    return new ChannelController<>(channel);
  }

  @Override
  public void accept(Input message) {
    if (message instanceof DeliverOp) {
      channel.deliver();
    } else if (message instanceof DropOp) {
      channel.drop();
    } else if (message instanceof AddOp) {
      AddOp<M> addOp = (AddOp<M>) message;
      channel.add(addOp.message());
    } else {
      logger.error("Received message I don't understand! {}", message);
    }
  }
}
