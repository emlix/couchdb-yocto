SUMMARY = "minimal test kernel"

LINUX_KERNEL_TYPE = "minimal"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION = "5.3.18"

KBRANCH = "linux-5.3.y"
SRCREV = "d4f3318ed8fab6316cb7a269b8f42306632a3876"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

PV = "${LINUX_VERSION}+${SRCPV}"

SRC_URI = " \
    git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git;branch=${KBRANCH} \
    file://base.cfg \
"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)} coreutils-native"

# cleanup/empty KERNEL_FEATURES
KERNEL_FEATURES_remove = "features/debug/printk.scc features/kernel-sample/kernel-sample.scc"
