SUMMARY = "a document-oriented database"
HOMEPAGE = "https://couchdb.apache.org"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=704805b3191efdd44d1f47b808c2d295"

SRC_URI = " \
    https://archive.apache.org/dist/couchdb/source/${PV}/apache-couchdb-${PV}.tar.gz \
    file://configure.patch \
    file://couchdb.service \
"
SRC_URI[sha256sum] = "5a601b173733ce3ed31b654805c793aa907131cd70b06d03825f169aa48c8627"

S = "${WORKDIR}/apache-couchdb-${PV}"

inherit autotools systemd

DEPENDS = "erlang-native js icu curl virtual/crypt"
RDEPENDS_${PN} = " \
    erlang erlang-crypto erlang-inets erlang-os-mon erlang-asn1 erlang-ssl erlang-public-key \
    erlang-syntax-tools erlang-compiler erlang-xmerl \
"

do_configure () {
    icu_config_bin=$(which icu-config)
    sed 's|^default_prefix=.*|default_prefix="${RECIPE_SYSROOT}/usr"|' -i "$icu_config_bin"

    oe_runconf
    sed 's|D_BSD_SOURCE|D_DEFAULT_SOURCE|' -i src/couchdb/priv/Makefile
}

do_compile () {
    oe_runmake
}

do_install_append () {
    find ${D} -type f | while read file; do
        sed 's|${RECIPE_SYSROOT_NATIVE}||g' -i $file
    done

    install -D -m 0644 ${WORKDIR}/couchdb.service ${D}${systemd_system_unitdir}/couchdb.service
    rm -rf ${D}/var/run
}

SYSTEMD_SERVICE_${PN} = "couchdb.service"
