package me.blexim.proptest.runner;

import com.google.common.collect.ImmutableList;
import me.blexim.proptest.common.Action;
import me.blexim.proptest.common.InputGenerator;
import me.blexim.proptest.generator.GenericGenerator;
import me.blexim.proptest.generator.GenericGeneratorImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.stream.Stream;

public class ReflectionGenerator implements InputGenerator<ReflectiveInput> {
  private final GenericGenerator genericGenerator;
  private final ImmutableList<Method> actions;
  private final Random rand;

  private ReflectionGenerator(GenericGenerator genericGenerator, ImmutableList<Method> actions,
      Random rand) {
    this.genericGenerator = genericGenerator;
    this.actions = actions;
    this.rand = rand;
  }

  public static <T> ReflectionGenerator create(Class<T> clazz, Random rand) {
    GenericGenerator genericGenerator = GenericGeneratorImpl.create();
    ImmutableList<Method> actions = findActions(clazz);
    return new ReflectionGenerator(genericGenerator, actions, rand);
  }

  @Override
  public ReflectiveInput nextInput() {
    int actionIdx = rand.nextInt(actions.size());
    Method action = actions.get(actionIdx);
    return randAction(action);
  }

  private static <T> ImmutableList<Method> findActions(Class<T> clazz) {
    return Stream.of(clazz.getMethods())
        .filter(m -> m.isAnnotationPresent(Action.class))
        .collect(ImmutableList.toImmutableList());
  }

  private ReflectiveInput randAction(Method action) {
    Class<?>[] paramTypes = action.getParameterTypes();
    ImmutableList<Object> args = Stream.of(paramTypes)
        .map(clazz -> genericGenerator.next(clazz, rand))
        .collect(ImmutableList.toImmutableList());
    return ReflectiveInput.create(action, args);
  }
}
