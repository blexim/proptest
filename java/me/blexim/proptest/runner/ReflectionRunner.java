package me.blexim.proptest.runner;

import me.blexim.proptest.common.TestOracle;

import java.lang.reflect.InvocationTargetException;

public class ReflectionRunner implements TestOracle<ReflectiveInput> {
  private final Object target;

  private ReflectionRunner(Object target) {
    this.target = target;
  }

  public static <T> ReflectionRunner create(T target) {
    return new ReflectionRunner(target);
  }

  @Override
  public Result runTest(Iterable<ReflectiveInput> inputs) {
    for (ReflectiveInput input : inputs) {
      try {
        runAction(input);
      } catch (Throwable t) {
        return Result.FAIL;
      }
    }

    return Result.PASS;
  }

  private void runAction(ReflectiveInput input)
      throws IllegalAccessException, InvocationTargetException {
    input.method().invoke(target, input.args().toArray());
  }
}
