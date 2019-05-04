SOLR_HOME=~/solr-7.6.0/
SOLR_TAR=~/solr-7.6.0.tgz
(cd $SOLR_HOME && ./bin/solr stop -all )

rm -rf $SOLR_HOME
rm -rf $SOLR_TAR