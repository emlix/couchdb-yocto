SUMMARY = "mount ramdisk overlays"

LICENSE = "CLOSED"

SRC_URI = " \
    file://init-overlay.service \
    file://init-overlay.sh \
"

inherit systemd
systemd_scriptdir = "${sysconfdir}/systemd/scripts"

do_install () {
    # install service
    install -D -m 0755 ${WORKDIR}/init-overlay.sh ${D}${systemd_scriptdir}/init-overlay
    install -D -m 0644 ${WORKDIR}/init-overlay.service ${D}${systemd_system_unitdir}/init-overlay.service
}

SYSTEMD_SERVICE_${PN} = "init-overlay.service"
