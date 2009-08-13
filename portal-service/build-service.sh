cd ../portal-service
mvn -npu -o -Dmaven.test.skip=true clean install
mvn source:jar
mvn install:install-file -Dfile=target/portal-service-1.0-SNAPSHOT-sources.jar -DartifactId=portal-service -DgroupId=portal -Dpackaging=zip -Dversion=1.0-SNAPSHOT
