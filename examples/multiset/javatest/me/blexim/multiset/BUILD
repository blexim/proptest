java_test(
    name = "test",
    size = "small",
    srcs = glob(["*.java"]),
    test_class = "me.blexim.multiset.MultisetTest",
    visibility = ["//visibility:public"],
    deps = [
        "//examples/multiset/java/me/blexim/multiset",
        "//examples/multiset/proto/me/blexim/multiset:multiset_proto_java",
        "//java/me/blexim/proptest/common",
        "//java/me/blexim/proptest/driver",
        "//java/me/blexim/proptest/generator",
        "//java/me/blexim/proptest/minimise",
        "//third_party/guava",
        "//third_party/protobuf",
        "//third_party/slf4j",
        "//third_party/testing",
    ],
)
