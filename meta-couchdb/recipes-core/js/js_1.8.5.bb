# SpiderMonkey

DESCRIPTION = "A JavaScript engine"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM="file://jsapi.cpp;beginline=5;endline=38;md5=f83ddfc6e78ef82f4ddd7c4c0567d10d"
SECTION = "libs/network"
DEPENDS = "readline nspr zip-native"

RCONFLICTS_${PN} = "js-threadsafe"
RREPLACES_${PN} = "js-threadsafe"

PR = "r12"

# js-1.8.5-array-recursion.patch, js-1.8.5-c++11.patch
# were importet from js-1.8.5-25.fc25.src.rpm
# NOTE: js-1.8.5-537701.patch will break dssip: #16330, #14457, #14572
SRC_URI = "http://ftp.mozilla.org/pub/mozilla.org/js/js185-1.0.0.tar.gz \
           file://use_prepared_config.patch;striplevel=3 \
           file://js-1.8.5-array-recursion.patch;striplevel=3 \
           file://js-1.8.5-c++11.patch;striplevel=3 \
           file://jsautocfg.tmp \
           file://js_configure_flags.patch;striplevel=3 \
           file://gcc9-fix.patch;striplevel=3 \
           "

# The 1.8.5 configure script specifies a value for -mfloat-abi wich conflicts with
# the value pre-set by poky for the imx6 in the cross-compiler command line.
SRC_URI_append_imx = "file://js_configure_mfloat_abi_fix.patch;striplevel=3"

# aarch64 platforms
# Includes some patches from the fedora source package js-1.8.5-25.fc24.src.rpm
SRC_URI_append_s905x = "file://js_configure_armv8_support.patch;striplevel=3 \
                        file://js-1.8.5-25.fc24.src.rpm_aarch64.patch;striplevel=3 \
                        file://mozjs1.8.5-tag.patch;striplevel=3 \
                        "

# needed for succesful compilation of debug version
#SRC_URI_append = "file://fix-debug-compilation.patch;striplevel=3"
# also remember to:
# --enable-debug
# --enable-debug-symbols
# --disable-optimize

SRC_URI[md5sum] = "a4574365938222adca0a6bd33329cb32"
SRC_URI[sha256sum] = "5d12f7e1f5b4a99436685d97b9b7b75f094d33580227aa998c406bbae6f2a687"

inherit autotools-brokensep pythonnative

S = "${WORKDIR}/js-${PV}/js/src"

export HOST_CC="gcc"
export HOST_CXX="g++"
export HOST_CFLAGS="-DXP_UNIX"
export HOST_CXXFLAGS="-DXP_UNIX"

JS_CONFIGURE_CPU_ARCH = ""
# Default CPU for the ARM architecture
JS_CONFIGURE_CPU_ARCH_arm = "--with-cpu-arch=armv5te"
# CPU overrides for specific machines
JS_CONFIGURE_CPU_ARCH_imx = "--with-cpu-arch=armv7"
JS_CONFIGURE_CPU_ARCH_s905x = "--with-cpu-arch=armv8"

EXTRA_OECONF = " \
  --enable-threadsafe \
  --enable-cpp-rtti \
  --enable-cpp-exceptions \
  --disable-methodjit-spew \
  --disable-profiling \
  --disable-pedantic \
  --disable-polyic \
  --disable-tracejit \
  --disable-tests \
  --without-x --disable-xterm-updates \
  --with-system-nspr \
  ${JS_CONFIGURE_CPU_ARCH}\
"
export EXTRA_LIBS = "-lnspr4 -lplc4 -lplds4"

# avoid running autotools_do_configure which forces autoreconf to run
do_configure() {
  install ${WORKDIR}/jsautocfg.tmp ${S}/jsautocfg.tmp

  nsprc=$(which nspr-config)
  if ! [ -f $nsprc.orig ]; then
      cp $nsprc $nsprc.orig
  fi
  {
      echo 'export includedir="${RECIPE_SYSROOT}/usr/include/nspr"'
      echo 'export libdir="${RECIPE_SYSROOT}/usr/lib"'
      echo 'nspr-config.orig $*'
  } > $nsprc

  oe_runconf
}

do_compile() {
  oe_runmake CROSS_COMPILE=${TARGET_ARCH}
}

do_install_append () {
    rm -rf ${D}/base
    install -d ${D}${bindir}
    install ${S}/shell/js ${D}${bindir}
    # js produces links to work directory... TODO: fix build scripts
    rm -f ${D}${libdir}/libmozjs185.so ${D}${libdir}/libmozjs185.so.1.0
    ln -s libmozjs185.so.1.0 ${D}${libdir}/libmozjs185.so
    ln -s libmozjs185.so.1.0.0 ${D}${libdir}/libmozjs185.so.1.0
}

PACKAGES =+ "libmozjs"
RDEPENDS_${PN} = "libmozjs"
FILES_${PN} = "${bindir}/js"
FILES_libmozjs = "${libdir}/lib*${SOLIBS} ${base_libdir}/*${SOLIBS}"
FILES_${PN}-dev += "${bindir}/js-config"
AUTO_LIBNAME_PKGS = "libmozjs"

# Fails to build with thumb
#| {standard input}: Assembler messages:
#| {standard input}:2172: Error: shifts in CMP/MOV instructions are only supported in unified syntax -- `mov r2,r1,LSR#20'
#| {standard input}:2173: Error: unshifted register required -- `bic r2,r2,#(1<<11)'
#| {standard input}:2174: Error: unshifted register required -- `orr r1,r1,#(1<<20)'
#| {standard input}:2176: Error: instruction not supported in Thumb16 mode -- `subs r2,r2,#0x300'
#| {standard input}:2178: Error: instruction not supported in Thumb16 mode -- `subs r5,r2,#52'
ARM_INSTRUCTION_SET = "arm"

#ERROR: js-1.8.5-r10 do_package: debugsrc symlink fixup failed with exit code 2 (cmd was find /home/dss-oe/poky-devel-nopkg-build/build/work/armv7ahf-neon-poky-linux-gnueabi/js/1.8.5-r10/package/usr/src/debug -type l -print0 -delete | sed s#/home/dss-oe/poky-devel-nopkg-build/build/work/armv7ahf-neon-poky-linux-gnueabi/js/1.8.5-r10/package/usr/src/debug/##g | (cd '/home/dss-oe/poky-devel-nopkg-build/build/work/armv7ahf-neon-poky-linux-gnueabi' ; cpio -pd0mL --no-preserve-owner '/home/dss-oe/poky-devel-nopkg-build/build/work/armv7ahf-neon-poky-linux-gnueabi/js/1.8.5-r10/package/usr/src/debug' 2>/dev/null))
#ERROR: js-1.8.5-r10 do_package: Function failed: split_and_strip_files

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

