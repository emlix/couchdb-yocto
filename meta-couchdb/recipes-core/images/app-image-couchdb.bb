SUMMARY = "couchdb app image"

inherit core-image

IMAGE_FSTYPES = "squashfs-xz wic"
IMAGE_FEATURES += "read-only-rootfs empty-root-password"
IMAGE_LINGUAS = ""

CORE_IMAGE_BASE_INSTALL = " \
    base-files \
    base-passwd \
    busybox \
    systemd \
    init-overlay-service \
    couchdb \
    test-couchdb \
"

QB_DEFAULT_FSTYPE = "wic"
QB_FSINFO = "wic:no-kernel-in-fs"
QB_OPT_APPEND = "--initrd ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.squashfs-xz"
QB_KERNEL_ROOT = "/dev/ram0"
