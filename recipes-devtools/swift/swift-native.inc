SUMMARY = "The Swift Programming Language"
HOMEPAGE = "https://swift.org/install/"

TOOLCHAIN = "clang"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=f6c482a0548ea60d6c2e015776534035"

PV = "${SWIFT_VERSION}"

SWIFT_ARCH_SUFFIX = "${@swift_native_arch_suffix(d)}"

SRC_URI = "git://github.com/swiftlang/swift.git;protocol=https;tag=swift-${PV}-RELEASE;nobranch=1;"

DEPENDS = "curl ninja cmake clang-native python3-native"
RDEPENDS:${PN} = "ncurses-native"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit native

do_update_checkout[network] = "1"
do_update_checkout[vardepsexclude] = "BB_ORIGENV"

python do_update_checkout() {
    import subprocess
    import os

    s = d.getVar('S')
    b = d.getVar('B')
    pv = d.getVar('PV')
    tag = f'swift-{pv}-RELEASE'

    ssh_auth_sock = d.getVar('BB_ORIGENV')['SSH_AUTH_SOCK']

    env = os.environ.copy()
    env['SSH_AUTH_SOCK'] = ssh_auth_sock

    ret = subprocess.call(['python3', 'utils/update-checkout', '--clone', '--tag', tag], cwd=s, env=env)
    if ret != 0:
        bb.fatal('update-checkout failed')
}

addtask update_checkout after do_unpack before do_compile

do_compile() {
    cd ${S}
    utils/build-script \
        --preset=buildbot_linux,no_test \
        install_destdir=${D} \
        installable_package=${WORKDIR}/${PN}-${PV}-RELEASE.tar.gz
}

do_install() {
}

#FILES:${PN} += "${base_prefix}/*"
