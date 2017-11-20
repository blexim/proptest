package me.blexim.proptest.runner;

import com.google.common.collect.ImmutableList;
import me.blexim.proptest.common.Action;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class ReflectionRunner {
  public <T> void findActions(Class<T> clazz) {
    ImmutableList<Method> actionMethods = Stream.of(clazz.getMethods())
        .filter(m -> m.getAnnotation(Action.class) != null)
        .collect(ImmutableList.toImmutableList());
  }
}
