licenses(["unencumbered"])

java_library(
  name = "auto_value",
  visibility = ["//visibility:public"],
  exported_plugins = [
    ":auto_value_plugin",
  ],
  exports = [
    "@auto_value_annotations_artifact//jar",
    "@auto_value_artifact//jar",
  ],
)

java_plugin(
  name = "auto_value_plugin",
  processor_class = "com.google.auto.value.processor.AutoValueProcessor",
  deps = [
    "@auto_value_annotations_artifact//jar",
    "@auto_value_artifact//jar",
  ],
)
