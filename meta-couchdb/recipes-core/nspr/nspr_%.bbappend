#SYSROOT_DIRS += "${bindir}"

sysroot_stage_all_append() {
    install -d ${SYSROOT_DESTDIR}${bindir}/crossscripts
    install -m 0755 ${D}${bindir}/nspr-config ${SYSROOT_DESTDIR}${bindir}/crossscripts/nspr-config
}
