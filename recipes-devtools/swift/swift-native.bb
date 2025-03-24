include swift-version.inc
# Swift 5.9 and above do not support self-hosted builds, i.e. they require a
# Swift compiler to build. Ensure Swift 5.8.1 is built first so we have a
# compiler available.
DEPENDS += "swift5-native"
include swift-native.inc
