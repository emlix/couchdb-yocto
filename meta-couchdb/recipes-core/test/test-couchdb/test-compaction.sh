#!/bin/sh -e

echo "==> couchdb test compaction"

couch="http://127.0.0.1:5984"

curl $couch/

echo "-> create db and doc"

curl -X PUT $couch/testdb
curl -X PUT $couch/testdb/testdoc -d '{ "Key" : "Value" }'

rev=$(curl -s $couch/testdb/testdoc | jq -r ._rev)
curl -X PUT $couch/testdb/testdoc -d '{ "_rev" : "'"$rev"'" }'

echo "-> request compaction"

curl -X POST -H 'Content-Type: application/json' $couch/testdb/_compact

echo
echo "now try to run 'curl $couch/testdb'"
