java_test(
    name = "test",
    visibility = ["//visibility:public"],
    srcs = glob(["*.java"]),
    test_class = "me.blexim.proptest.driver.TestDriverTest",
    size = "small",
    deps = [
      "//java/me/blexim/proptest/common",
      "//java/me/blexim/proptest/driver",
      "//java/me/blexim/proptest/minimise",
      "//third_party/slf4j",
      "//third_party/testing",
      "@guava//jar",
    ],
)
