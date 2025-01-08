SUMMARY = "libdispatch"
DESCRIPTION = "The libdispatch Project, (a.k.a. Grand Central Dispatch), for concurrency on multicore hardware"
HOMEPAGE = "https://github.com/swiftlang/swift-corelibs-libdispatch"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=1cd73afe3fb82e8d5c899b9d926451d0"

require swift-version.inc
PV = "${SWIFT_VERSION}"

DEPENDS = "swift-stdlib"

SRC_URI = "git://github.com/swiftlang/swift-corelibs-libdispatch.git;protocol=https;tag=swift-${PV}-RELEASE;nobranch=1"

#RDEPENDS:${PN} = "ncurses"

S = "${WORKDIR}/git"
LIBDISPATCH_BUILDDIR = "${WORKDIR}/build"

inherit swift-cmake-base

TARGET_LDFLAGS += "-L${STAGING_DIR_TARGET}/usr/lib/swift/linux"

# Enable Swift parts
EXTRA_OECMAKE += "-DENABLE_SWIFT=YES"

# Ensure the right CPU is targeted
cmake_do_generate_toolchain_file:append() {
    sed -i 's/set([ ]*CMAKE_SYSTEM_PROCESSOR .*[ ]*)/set(CMAKE_SYSTEM_PROCESSOR ${TARGET_CPU_NAME})/' ${WORKDIR}/toolchain.cmake
}

do_install:append() {
    # move the header files out of /usr/lib/swift and into /usr/include,
    # because we want Swift C shims to be able to pick them up without fancy
    # SwiftPM magic
    install -d ${D}${includedir}
    (cd ${D}${libdir}/swift; mv Block os dispatch ${D}${includedir})
}

FILES:${PN} = "\
    ${libdir}/swift \
    ${libdir}/cmake/dispatch \
    ${includedir}/Block \
    ${includedir}/os \
    ${includedir}/dispatch \
"

INSANE_SKIP:${PN} = "file-rdeps"
