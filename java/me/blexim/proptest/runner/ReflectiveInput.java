package me.blexim.proptest.runner;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Method;

@AutoValue
abstract class ReflectiveInput {
  abstract Method method();
  abstract ImmutableList<Object> args();

  public static ReflectiveInput create(Method method, ImmutableList<Object> args) {
    return new AutoValue_ReflectiveInput(method, args);
  }
}
