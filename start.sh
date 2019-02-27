#!/usr/bin/env bash

BUILD=0
INSTALL_SOLR=0

while [[ $# -gt 0 ]]; do
  key="$1"
  case $key in
    -b)
      BUILD=1
      ;;
    -solr)
      INSTALL_SOLR=1
      ;;
    *)
      # unknown option
      ;;
  esac
  shift # past argument or value
done

if [ "$BUILD" == "1" ]; then
  (cd indexer && gradle clean shadowJar)
  [ $? -eq 0 ] || exit 1
fi

if [ "$INSTALL_SOLR" == "1" ]; then
  (cd /tmp && curl -O http://mirror.apache-kr.org/lucene/solr/7.7.0/solr-7.7.0.tgz)
  (cd /tmp && tar -zxvf solr-7.7.0.tgz)
  SOLR_HOME=/tmp/solr-7.7.0/
  (cd $SOLR_HOME && ./bin/solr start -cloud -noprompt -verbose )
  [ $? -eq 0 ] || exit 1
fi

IS_LOCAL=true
JAR_FILE="indexer/build/libs/filmography-indexer-1.0-SNAPSHOT-all.jar"

RUN_COMMAND="java -jar ${JAR_FILE} --local ${IS_LOCAL}"

echo "$RUN_COMMAND"
eval "$RUN_COMMAND"

