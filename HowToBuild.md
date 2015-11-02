# How To Build #

The following instructions will guide you through the compiling, packaging and deployment process for the data portal's web application.

## 1. Checking out the source code ##

The web application consists of 4 projects:
  * portal-core
  * portal-index
  * portal-service
  * portal-web

The instructions for checking out from Google Code's SVN can be found [here](http://code.google.com/p/gbif-dataportal/source/checkout)

## 2. Building and packaging the project ##

If you need to build the web application for the first time, you need to execute the script **first-build-all.sh** found on the portal-web project (portal-web/first-build-all.sh)

After the first build, you may use the **build-all.sh** script located at portal-web/build-all.sh

After this is done, there will be a **portal.war** file that has been generated to be deployed on an application server (we will refer to this WAR file later in the documentation)

## 3. Creating the index backend database ##

The data portal needs a database where it stores all its data.

On the portal-core project there are two files:

  * portal-core/db/portal.ddl : Used for building the initial structure of the database
  * portal-core/db/initPortal.data: For populating the database with initial needed for the web application to run

The steps that need to be followed are:

  1. mysql> create database portal;
  1. mysql -u USERNAME -pPASSWORD < PATH\_TO\_PORTAL\_WEB/db/portal.ddl
  1. mysql -u USERNAME -pPASSWORD < PATH\_TO\_PORTAL\_WEB/db/initPortal.ddl

## 4. Deploy and link the web application with the index database ##

To deploy the web application you have built you need to take the portal.war file (found at portal-web/target/portal.war) and place it into the webapplication folders in your application server. In [Tomcat](http://tomcat.apache.org) this will be found at TOMCAT\_HOME/webapps/. You might need to start your application server in order to _expand_ the WAR file.

Once you have your web application into your application server, you need to **link** the data portal with the created index database. To do this you need to go into your _expanded_ web application folder to the following file:

TOMCAT\_HOME/portal/WEB-INF/classes/portal.properties



In here you will find the following code, which you need to configure according to your database settings:

`# Portal index credentials`

`dataSource.username=root`

`dataSource.password=`

`dataSource.url=jdbc:mysql://localhost:3306/portal?autoReconnect=true&useUnicode=true&characterEncoding=UTF8&characterSetResults=UTF8`



`# Portal indexer database credentials`

`harvestingDataSource.username=root`

`harvestingDataSource.password=`

`harvestingDataSource.url=jdbc:mysql://localhost:3306/portal?autoReconnect=true&useUnicode=true&characterEncoding=UTF8&characterSetResults=UTF8`



`# Database credentials for the database that is used for writing the log entries `

`logDataSource.username=root`

`logDataSource.password=`

`logDataSource.url=jdbc:mysql://localhost:3306/portal?autoReconnect=true&useUnicode=true&characterEncoding=UTF8&characterSetResults=UTF8`



After doing this you need to restart your application server and should be able to access your newly created data portal at [http://localhost:8080/portal](http://localhost:8080)



_If you have any comments or questions about this documentation, please don't hesitate to submit a [Documentation Issue](http://code.google.com/p/gbif-dataportal/issues/list), so that we can enhance our data portal documentation and make it as rich as possible for the users._