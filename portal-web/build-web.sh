cd ../portal-service
mvn -npu -o -Dmaven.test.skip=true clean install source:jar 
mvn install:install-file -Dfile=target/portal-service-1.0-SNAPSHOT-sources.jar -DartifactId=portal-service -DgroupId=portal -Dpackaging=zip -Dversion=1.0-SNAPSHOT 
cd ../portal-web
mvn -npu -o clean install
ant -buildfile deploy.xml deploy-clean
