SUMMARY = "minimal busybox image"

inherit core-image

IMAGE_FSTYPES = "cpio.gz"
IMAGE_FEATURES += "read-only-rootfs empty-root-password"
IMAGE_LINGUAS = ""

CORE_IMAGE_BASE_INSTALL = "base-files base-passwd busybox"
