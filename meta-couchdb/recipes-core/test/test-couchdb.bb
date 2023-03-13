SUMMARY = "couchdb test scripts"

LICENSE = "CLOSED"

SRC_URI = " \
    file://test-compaction.sh \
"

RDEPENDS_${PN} = "curl jq"

test_libdir = "${libdir}/${PN}"

do_install () {
    install -D -m 0755 ${WORKDIR}/test-compaction.sh ${D}${test_libdir}/test-compaction.sh
}
