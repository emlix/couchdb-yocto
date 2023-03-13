# couchdb build env

steps to start:

init repo and start docker image
```
git submodule update --init
ci/docker-run.sh
```

build and run qemu couchdb image (in docker):
```
ci/qemu-run.sh
```
(quit qemu by <ctrl> a + x)

run couchdb test (in qemu):
```
/usr/lib/test-couchdb/test-compaction.sh
```
