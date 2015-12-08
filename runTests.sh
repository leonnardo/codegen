#!/bin/sh

for f in target/generated-test-sources/teste*; do echo "Processing file $f"; lli $f; echo "";  done
