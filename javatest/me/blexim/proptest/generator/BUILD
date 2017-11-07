java_test(
    name = "test",
    visibility = ["//visibility:public"],
    test_class = "me.blexim.proptest.generator.MessageGeneratorTest",
    srcs = glob(["*.java"]),
    size = "small",
    deps = [
      "//java/me/blexim/proptest/generator",
      "//prototest/me/blexim/proptest:proptest_proto_java",
      "//third_party/guava",
      "//third_party/slf4j",
      "//third_party/testing",
    ],
)
