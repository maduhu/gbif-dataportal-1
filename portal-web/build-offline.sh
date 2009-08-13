cd ../portal-core
mvn -Dmaven.test.skip=true clean install source:jar -npu -o
mvn install:install-file -Dfile=target/portal-core-1.0-SNAPSHOT-sources.jar -DartifactId=portal-core -DgroupId=portal -Dpackaging=zip -Dversion=1.0-SNAPSHOT -npu -o 

cd ../portal-index
mvn -Dmaven.test.skip=true clean install source:jar -npu -o 
mvn install:install-file -Dfile=target/portal-index-1.0-SNAPSHOT-sources.jar -DartifactId=portal-index -DgroupId=portal -Dpackaging=zip -Dversion=1.0-SNAPSHOT -npu -o 

cd ../portal-service 
mvn -Dmaven.test.skip=true clean install source:jar -npu -o 
mvn install:install-file -Dfile=target/portal-service-1.0-SNAPSHOT-sources.jar -DartifactId=portal-service -DgroupId=portal -Dpackaging=zip -Dversion=1.0-SNAPSHOT -npu -o 

cd ../portal-webservices
mvn -Dmaven.test.skip=true clean install source:jar -npu -o 

cd ../portal-web
mvn clean install