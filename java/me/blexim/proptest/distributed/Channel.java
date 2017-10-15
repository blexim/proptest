package me.blexim.proptest.distributed;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

public class Channel<M> {
  private final Consumer<M> consumer;
  private final Queue<M> queue;

  private Channel(Consumer<M> consumer) {
    this.consumer = consumer;
    this.queue = new ArrayDeque<>();
  }

  public static <M> Channel<M> create(Consumer<M> consumer) {
    return new Channel<>(consumer);
  }

  public void add(M message) {
    queue.offer(message);
  }

  public void deliver() {
    M message = queue.poll();

    if (message != null) {
      consumer.accept(message);
    }
  }

  public void drop() {
    queue.poll();
  }
}
