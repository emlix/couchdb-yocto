#!/bin/bash -e
#
# yocto runqemu wrapper
#
CMDPATH=$(cd $(dirname $0) && pwd)
BASEDIR=${CMDPATH%/*}

# setup
cd "$BASEDIR"
if ! command -v runqemu >/dev/null; then
    . ci/yocto-build.env
fi

DEPLOYDIR=$BUILDDIR/tmp/deploy/images/qemux86-64-test

# build
bitbake app-image-couchdb linux-minimal

# run
exec runqemu nographic \
    "$DEPLOYDIR"/app-image-couchdb-qemux86-64-test.qemuboot.conf \
    "$DEPLOYDIR"/bzImage
