package me.blexim.proptest.runner;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

public class ReflectionPrinter {
  public static String printTestCase(ImmutableList<ReflectiveInput> inputs) {
    StringBuilder builder = new StringBuilder();
    builder.append("\n");
    inputs.forEach(input -> appendInput(builder, input));
    return builder.toString();
  }

  private static void appendInput(StringBuilder builder, ReflectiveInput input) {
    append(builder, "target.%s(%s);\n",
        input.method().getName(), Joiner.on(", ").join(input.args()));
  }

  private static void append(StringBuilder builder, String fmt, Object... args) {
    builder.append(String.format(fmt, args));
  }
}
