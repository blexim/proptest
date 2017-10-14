licenses(["unencumbered"])

java_library(
  name = "auto_value",
  exported_plugins = [
    ":auto_value_plugin",
  ],
  exports = [
    "@auto_value//jar",
  ],
)

java_plugin(
  name = "auto_value_plugin",
  processor_class = "com.google.auto.value.processor.AutoValueProcessor",
  deps = [
    "@auto_value//jar",
  ],
)
