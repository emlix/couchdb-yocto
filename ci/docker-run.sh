#!/bin/sh -e
#
# create and run docker build env
#
CMDPATH=$(cd $(dirname $0) && pwd)
BASEDIR=${CMDPATH%/*}
PROJECT=couchdb-build-env

echo "==> create docker image"
cd $BASEDIR/ci
docker build \
  --build-arg UID=$(id -u) --build-arg GID=$(id -g) \
  --tag $PROJECT .

echo "==> run $PROJECT container"
mkdir -p $BASEDIR/bbcache
docker run --rm -it -e "TERM=$TERM" \
  -v $BASEDIR:/base -w /base $PROJECT $@

# to build and run a minimal image
# --------------------------------
# bitbake image-busybox
# runqemu nographic slirp ramfs
# (quit qemu by <ctrl> a + x)
