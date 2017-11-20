package me.blexim.proptest.runner;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import java.util.function.Supplier;

public class ReflectionPrinter {
  public static <T> String printTestCase(Supplier<T> supplier,
      ImmutableList<ReflectiveInput> inputs) {
    StringBuilder builder = new StringBuilder();
    Class<T> clazz = (Class<T>) supplier.get().getClass();

    append(builder, "\npublic void testCase(%s target) {\n", clazz.getSimpleName());
    inputs.forEach(input -> appendInput(builder, input));
    append(builder, "}\n");
    return builder.toString();
  }

  private static void appendInput(StringBuilder builder, ReflectiveInput input) {
    append(builder, "  target.%s(%s);\n",
        input.method().getName(), Joiner.on(", ").join(input.args()));
  }

  private static void append(StringBuilder builder, String fmt, Object... args) {
    builder.append(String.format(fmt, args));
  }
}
