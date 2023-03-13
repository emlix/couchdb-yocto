#!/bin/sh -e

echo "==> Mounting ramdisk overlays"

mkdir -p /var/volatile/etc /var/volatile/.etc-work

mount -o lowerdir=/etc,upperdir=/var/volatile/etc,workdir=/var/volatile/.etc-work \
    -t overlay overlay /etc
