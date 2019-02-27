SOLR_HOME=/tmp/solr-7.7.0/
SOLR_TAR=/tmp/solr-7.7.0.tgz
(cd $SOLR_HOME && ./bin/solr stop -all )

rm -rf $SOLR_HOME
rm -rf $SOLR_TAR