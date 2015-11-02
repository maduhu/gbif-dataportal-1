# ![http://geoserver.org//download/attachments/19005441/global.logo?version=1&parameter=finish.png](http://geoserver.org//download/attachments/19005441/global.logo?version=1&parameter=finish.png) Installing [Geoserver](http://geoserver.org/display/GEOS/What+is+Geoserver) #

There are different ways to install Geoserver:

  * Install Geoserver as a WAR in Apache Tomcat
  * Install Geoserver as a binary file (OS independent)


### Configure the system variables ###
Configure the following variables according to your system configuration.
  * JAVA\_HOME=_Your JAVA installation path_
  * JAVA\_OPTS=`"-XX:PermSize=512M -Xmx1g -Djava.awt.headless=true -Dcom.sun.management.jmxremote"`


### Install Geoserver as a WAR in Apache tomcat (recommended) ###

  * Get the Apache Tomcat from http://tomcat.apache.org/ (Binary Distributions - Core)
> In this case we are going to use [Apache Tomcat Zipped Binary Distribution 6.0.26](http://apache.multihomed.net/tomcat/tomcat-6/v6.0.26/bin/apache-tomcat-6.0.26.zip).

  * Get the Geoserver WAR file from http://geoserver.org/display/GEOS/Stable (Web Archive Format)
> In this case we are going to use [Geoserver 2.0.1](http://downloads.sourceforge.net/geoserver/geoserver-2.0.1-war.zip)

  * Unzip the tomcat file and place it in a location of your choice and put the Geoserver WAR file in the folder $TOMCAT/webapps/geoserver.war

  * Start Tomcat by running $TOMCAT/bin/startup.sh or  $TOMCAT/bin/startup.bat according to your OS.

  * Go to URL http://localhost:8080/geoserver and sign in using the default username: _admin_ password: _geoserver_

#### How to change the port ####
  * Shutdown Tomcat running $TOMCAT/bin/shutdown.sh or $TOMCAT/bin/shutdown.bat
  * Open file $TOMCAT/conf/server.xml
  * Search line `<Connector port="8080" protocol="HTTP/1.1"` and change the default to your convenience port.
  * Start Tomcat

### Install GeoServer as Binary (OS independent) ###

If you install Geoserver as Binary it will be run by a jetty server rather than Tomcat.

  * Get the bin folder from http://geoserver.org/display/GEOS/Stable (Binary - OS independent)
> In this case we are going to use [Geoserver 2.0.1](http://downloads.sourceforge.net/geoserver/geoserver-2.0.1-bin.zip)

  * Unzip the folder and place it in a location of your choice.

  * Run $GEOSERVER/bin/startup.bat or $GEOSERVER/bin/startup.sh according to your OS.

  * Go to URL http://localhost:8080/geoserver and sign in using the default username: _admin_ password: _geoserver_

#### How to change the port ####
  * Shutdown Jetty server running $GEOSERVER/bin/shutdown.sh or $GEOSERVER/bin/shutdown.bat
  * Open file $GEOSERVER/etc/jetty.xml
  * Search line `<Set name="port">8081</Set>` and change the default port.
  * Start Jetty


## Resources & libraries ##
### Shapefile ###
Copy the files corresponding to the country shape file
> _[country.dbf](http://ogc.gbif.org/data/data/shapefiles/country.dbf)  [country.fix](http://ogc.gbif.org/data/data/shapefiles/country.fix)  [country.ORG.dbf](http://ogc.gbif.org/data/data/shapefiles/country.ORG.dbf)  [country.prj](http://ogc.gbif.org/data/data/shapefiles/country.prj)  [country.qix](http://ogc.gbif.org/data/data/shapefiles/country.qix)  [country.shp](http://ogc.gbif.org/data/data/shapefiles/country.shp)  [country.shx](http://ogc.gbif.org/data/data/shapefiles/country.shx)_
to the shapefiles directory of the GeoServer `$GeoServer\data_dir\data\shapefiles`


### The gt-ala-tab library ###
Copy the library [![](http://code.google.com/hosting/images/paperclip.gif)](http://gbif-dataportal.googlecode.com/issues/attachment?aid=-7277987642525285492&name=gt-ala-tab-1.0-SNAPSHOT.jar&token=07e4864ec70f3d077b211eb2ec317d37)[gt-ala-tab-1.0-SNAPSHOT.jar](http://gbif-dataportal.googlecode.com/issues/attachment?aid=-7277987642525285492&name=gt-ala-tab-1.0-SNAPSHOT.jar&token=07e4864ec70f3d077b211eb2ec317d37) to the libraries directory of the GeoServer `$GeoServer\geoserver\WEB-INF\lib`
## Workspace, stores, styles & layers ##
### Workspace ###
Create the Workspace named _gbif_ with URI http://www.gbif.org and set it as default workspace
### Stores ###
#### country stores ####
Add the stores type _Shapefile_ with names _country\_borders_, _country\_fill_ and _country\_names_. With the following parameters:
  * Workspace: _gbif_
  * URL: `file:data/shapefiles/country.shp`
#### tab\_density store ####
Add the store _tab\_density_ of type _Tab Url DataStore_.With the following parameters:
  * Workspace: _gbif_
  * minx: -180
  * miny: -90
  * maxx: 180
  * maxy: 90

### Styles ###
Add the following SLD styles. Click on the _validate_ button to verify the style is a valid SLD document.

> #### country\_borders style ####
```
<?xml version="1.0" encoding="ISO-8859-1"?> 
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" 
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"> 
  <NamedLayer> 
    <Name>Countries Borders</Name> 
    <UserStyle> 
      <Title>Countries Borders</Title> 
      <Abstract>A style that just draws a 1 pixel stroke around each countries borders</Abstract> 
      <FeatureTypeStyle> 
        <Rule> 
          <Title>Polygon</Title> 
          <PolygonSymbolizer> 
            <Stroke> 
              <CssParameter name="stroke">#006600</CssParameter> 
              <CssParameter name="stroke-width">1</CssParameter> 
            </Stroke> 
          </PolygonSymbolizer> 
        </Rule> 
      </FeatureTypeStyle> 
    </UserStyle> 
  </NamedLayer> 
</StyledLayerDescriptor> 
```
> #### country\_fill style ####
```
<?xml version="1.0" encoding="ISO-8859-1"?> 
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" 
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"> 
  <NamedLayer> 
    <Name>Country Polygons Fill</Name> 
    <UserStyle> 
      <Title>Country Polygons style</Title> 
      <Abstract>A style that fills the countries polygons with a specific color</Abstract> 
      <FeatureTypeStyle> 
        <Rule> 
          <Title>Polygon</Title> 
          <PolygonSymbolizer> 
            <Fill> 
              <CssParameter name="fill">#003333</CssParameter> 
              <CssParameter name="fill-opacity"><ogc:Literal>1</ogc:Literal></CssParameter>               
            </Fill> 
          </PolygonSymbolizer> 
        </Rule> 
      </FeatureTypeStyle> 
    </UserStyle> 
  </NamedLayer> 
</StyledLayerDescriptor>
```
> #### country\_names style ####
```
<?xml version="1.0" encoding="ISO-8859-1"?> 
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" 
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"> 
  <NamedLayer> 
    <Name>Country Names</Name> 
    <UserStyle> 
      <Title>Country Names</Title> 
      <Abstract>Style that renders the names of the countries on the map</Abstract> 
      <FeatureTypeStyle> 
  <Rule> 
  <TextSymbolizer> 
    <Label> 
      <ogc:PropertyName>CNTRY_NAME</ogc:PropertyName> 
    </Label>   
    <Font> 
      <CssParameter name="font-family"><ogc:Literal>Lucida Sans</ogc:Literal></CssParameter> 
      <CssParameter name="font-style"><ogc:Literal>normal</ogc:Literal></CssParameter> 
      <CssParameter name="font-size"><ogc:Literal>10.0</ogc:Literal></CssParameter> 
      <CssParameter name="font-weight"><ogc:Literal>bold</ogc:Literal></CssParameter> 
    </Font>   
<LabelPlacement> 
<PointPlacement> 
<AnchorPoint> 
<AnchorPointX>0.5</AnchorPointX> 
<AnchorPointY>0.5</AnchorPointY> 
</AnchorPoint> 
</PointPlacement> 
</LabelPlacement> 
    <Fill> 
      <CssParameter name="fill">#6e8686</CssParameter> 
    </Fill> 
  </TextSymbolizer> 
  </Rule> 
      </FeatureTypeStyle> 
    </UserStyle> 
  </NamedLayer> 
</StyledLayerDescriptor>
```
> #### density\_layer style ####
```
<?xml version="1.0" encoding="UTF-8"?> 
<StyledLayerDescriptor version="1.0.0" 
 xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
 xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" 
 xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"> 
 <NamedLayer> 
   <Name>densityLayer</Name> 
   <UserStyle> 
     <FeatureTypeStyle> 
       <!-- If it is a point data, render as such --> 
       <Rule> 
         <ogc:Filter> 
           <ogc:PropertyIsEqualTo> 
             <ogc:Function name="geometryType"> 
               <ogc:PropertyName>geom</ogc:PropertyName> 
             </ogc:Function> 
             <ogc:Literal>Point</ogc:Literal> 
           </ogc:PropertyIsEqualTo> 
         </ogc:Filter> 
         <PointSymbolizer> 
           <Graphic> 
             <Mark> 
               <WellKnownName>circle</WellKnownName>
               <Fill> 
                 <CssParameter name="fill">#cc0000</CssParameter> 
                 <CssParameter name="fill-opacity">1.0</CssParameter> 
               </Fill> 
             </Mark> 
             <Size>6</Size> 
           </Graphic> 
         </PointSymbolizer> 
       </Rule> 
       <!-- 1-9 --> 
       <Rule> 
         <ogc:Filter> 
           <ogc:And> 
             <ogc:PropertyIsGreaterThanOrEqualTo> 
               <ogc:PropertyName>count</ogc:PropertyName> 
               <ogc:Literal>1</ogc:Literal> 
             </ogc:PropertyIsGreaterThanOrEqualTo> 
             <ogc:PropertyIsLessThan> 
               <ogc:PropertyName>count</ogc:PropertyName> 
               <ogc:Literal>10</ogc:Literal> 
             </ogc:PropertyIsLessThan> 
           </ogc:And> 
         </ogc:Filter> 
         <PolygonSymbolizer> 
           <Fill> 
             <CssParameter name="fill"> 
               <ogc:Literal>#ffff00</ogc:Literal> 
             </CssParameter> 
             <CssParameter name="fill-opacity"> 
               <ogc:Literal>1</ogc:Literal> 
             </CssParameter> 
           </Fill> 
          <Stroke> 
            <CssParameter name="stroke">#ffff00</CssParameter> 
            <CssParameter name="stroke-width">0</CssParameter> 
          </Stroke> 
         </PolygonSymbolizer> 
       </Rule> 
       <!-- 10-99 --> 
       <Rule> 
         <ogc:Filter> 
           <ogc:And> 
             <ogc:PropertyIsGreaterThanOrEqualTo> 
               <ogc:PropertyName>count</ogc:PropertyName> 
               <ogc:Literal>10</ogc:Literal> 
             </ogc:PropertyIsGreaterThanOrEqualTo> 
             <ogc:PropertyIsLessThan> 
               <ogc:PropertyName>count</ogc:PropertyName> 
               <ogc:Literal>100</ogc:Literal> 
             </ogc:PropertyIsLessThan> 
           </ogc:And> 
         </ogc:Filter> 
         <PolygonSymbolizer> 
           <Fill> 
             <CssParameter name="fill"> 
               <ogc:Literal>#ffcc00</ogc:Literal> 
             </CssParameter> 
             <CssParameter name="fill-opacity"> 
               <ogc:Literal>1</ogc:Literal> 
             </CssParameter> 
           </Fill> 
          <Stroke> 
            <CssParameter name="stroke">#ffcc00</CssParameter> 
            <CssParameter name="stroke-width">0</CssParameter> 
          </Stroke> 
         </PolygonSymbolizer> 
       </Rule> 
       <!-- 100-999 --> 
       <Rule> 
         <ogc:Filter> 
           <ogc:And> 
             <ogc:PropertyIsGreaterThanOrEqualTo> 
               <ogc:PropertyName>count</ogc:PropertyName> 
               <ogc:Literal>100</ogc:Literal> 
             </ogc:PropertyIsGreaterThanOrEqualTo> 
             <ogc:PropertyIsLessThan> 
               <ogc:PropertyName>count</ogc:PropertyName> 
               <ogc:Literal>1000</ogc:Literal> 
             </ogc:PropertyIsLessThan> 
           </ogc:And> 
         </ogc:Filter> 
         <PolygonSymbolizer> 
           <Fill> 
             <CssParameter name="fill"> 
               <ogc:Literal>#ff9900</ogc:Literal> 
             </CssParameter> 
             <CssParameter name="fill-opacity"> 
               <ogc:Literal>1</ogc:Literal> 
             </CssParameter> 
           </Fill> 
          <Stroke> 
            <CssParameter name="stroke">#ff9900</CssParameter> 
            <CssParameter name="stroke-width">0</CssParameter> 
          </Stroke> 
         </PolygonSymbolizer> 
       </Rule> 
       <!-- 1000-9999 --> 
       <Rule> 
         <ogc:Filter> 
           <ogc:And> 
             <ogc:PropertyIsGreaterThanOrEqualTo> 
               <ogc:PropertyName>count</ogc:PropertyName> 
               <ogc:Literal>1000</ogc:Literal> 
             </ogc:PropertyIsGreaterThanOrEqualTo> 
             <ogc:PropertyIsLessThan> 
               <ogc:PropertyName>count</ogc:PropertyName> 
               <ogc:Literal>10000</ogc:Literal> 
             </ogc:PropertyIsLessThan> 
           </ogc:And> 
         </ogc:Filter> 
         <PolygonSymbolizer> 
           <Fill> 
             <CssParameter name="fill"> 
               <ogc:Literal>#ff6600</ogc:Literal> 
             </CssParameter> 
             <CssParameter name="fill-opacity"> 
               <ogc:Literal>1</ogc:Literal> 
             </CssParameter> 
           </Fill> 
          <Stroke> 
            <CssParameter name="stroke">#ff6600</CssParameter> 
            <CssParameter name="stroke-width">0</CssParameter> 
          </Stroke> 
         </PolygonSymbolizer> 
       </Rule> 
       <!-- 10000-99999 --> 
       <Rule> 
         <ogc:Filter> 
           <ogc:And> 
             <ogc:PropertyIsGreaterThanOrEqualTo> 
               <ogc:PropertyName>count</ogc:PropertyName> 
               <ogc:Literal>10000</ogc:Literal> 
             </ogc:PropertyIsGreaterThanOrEqualTo> 
             <ogc:PropertyIsLessThan> 
               <ogc:PropertyName>count</ogc:PropertyName> 
               <ogc:Literal>100000</ogc:Literal> 
             </ogc:PropertyIsLessThan> 
           </ogc:And> 
         </ogc:Filter> 
         <PolygonSymbolizer> 
           <Fill> 
             <CssParameter name="fill"> 
               <ogc:Literal>#ff3300</ogc:Literal> 
             </CssParameter> 
             <CssParameter name="fill-opacity"> 
               <ogc:Literal>1</ogc:Literal> 
             </CssParameter> 
           </Fill> 
          <Stroke> 
            <CssParameter name="stroke">#ff3300</CssParameter> 
            <CssParameter name="stroke-width">0</CssParameter> 
          </Stroke> 
         </PolygonSymbolizer> 
       </Rule> 
       <!-- 100000+ --> 
       <Rule> 
         <ogc:Filter> 
           <ogc:PropertyIsGreaterThanOrEqualTo> 
             <ogc:PropertyName>count</ogc:PropertyName> 
             <ogc:Literal>100000</ogc:Literal> 
           </ogc:PropertyIsGreaterThanOrEqualTo> 
         </ogc:Filter> 
         <PolygonSymbolizer> 
           <Fill> 
             <CssParameter name="fill"> 
               <ogc:Literal>#cc0000</ogc:Literal> 
             </CssParameter> 
             <CssParameter name="fill-opacity"> 
               <ogc:Literal>1</ogc:Literal> 
             </CssParameter> 
           </Fill> 
          <Stroke> 
            <CssParameter name="stroke">#cc0000</CssParameter> 
            <CssParameter name="stroke-width">0</CssParameter> 
          </Stroke> 
         </PolygonSymbolizer> 
       </Rule> 
     </FeatureTypeStyle> 
   </UserStyle> 
 </NamedLayer> 
</StyledLayerDescriptor> 
```
> #### country\_borders\_black style ####
```
<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
 xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
 <NamedLayer>
   <Name>Countries Borders</Name>
   <UserStyle>
     <Title>Countries Borders</Title>
     <Abstract>A style that just draws a 1 pixel stroke around each countries borders</Abstract>
     <FeatureTypeStyle>
       <Rule>
         <Title>Polygon</Title>
         <PolygonSymbolizer>
           <Stroke>
             <CssParameter name="stroke">#000000</CssParameter>
             <CssParameter name="stroke-width">1</CssParameter>
           </Stroke>
         </PolygonSymbolizer>
       </Rule>
     </FeatureTypeStyle>
   </UserStyle>
 </NamedLayer>
</StyledLayerDescriptor>
```
> #### country\_names\_blue style ####
```
<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
 xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
 <NamedLayer>
   <Name>Country Names</Name>
   <UserStyle>
     <Title>Country Names</Title>
     <Abstract>Style that renders the names of the countries on the map</Abstract>
     <FeatureTypeStyle>
 <Rule>
 <TextSymbolizer>
   <Label>
     <ogc:PropertyName>CNTRY_NAME</ogc:PropertyName>
   </Label>
   <Font>
     <CssParameter name="font-family"><ogc:Literal>Lucida Sans</ogc:Literal></CssParameter>
     <CssParameter name="font-style"><ogc:Literal>normal</ogc:Literal></CssParameter>
     <CssParameter name="font-size"><ogc:Literal>13.0</ogc:Literal></CssParameter>
     <CssParameter name="font-weight"><ogc:Literal>bold</ogc:Literal></CssParameter>
   </Font>
 <LabelPlacement>
   <PointPlacement>
     <AnchorPoint>
       <AnchorPointX>0.5</AnchorPointX>
       <AnchorPointY>0.5</AnchorPointY>
     </AnchorPoint>
   </PointPlacement>
 </LabelPlacement>
   <Fill>
     <CssParameter name="fill">#6666FF </CssParameter>
   </Fill>
 </TextSymbolizer>
 </Rule>
     </FeatureTypeStyle>
   </UserStyle>
 </NamedLayer>
</StyledLayerDescriptor>
```
### Layers ###
Add the following layers in the layers menu
#### Adding the country layers ####
Add the layers from the stores gbif:country\_borders, gbif:country\_fill and gbif:country\_names, respectively with the names _country\_borders_, _country\_fill_ y _country\_names_. With the following parameters:

**Data tab**
  * Coordinate Reference Systems: The Native SRS is _UNKNOWN_ and the Declared SRS is _EPSG:4326_.
  * Bounding Boxes: In both Native Bounding Box and Lat/Lon Bounding Box, set the following:
|Min X|Min Y|Max X|Max Y|
|:----|:----|:----|:----|
|-180 |-90  |180  |83,623|

**Publishing tab**
  * Default Title: Set the Default Style as the respective layer name.

#### Adding the tabDensityLayer layer ####
Add the layer _tabDensityLayer_ from the store gbif:tab\_density. With the following parameters.

**Data tab**
  * Coordinate Reference Systems: The Declared SRS is _EPSG:4326_.
  * Bounding Boxes: In both Native Bounding Box and Lat/Lon Bounding Box, set the following:
|Min X|Min Y|Max X|Max Y|
|:----|:----|:----|:----|
|-180 |-90  |180  |90   |

**Publishing tab**
  * Default Title: Set the Default Style as the respective layer name.

#### Test the layers ####
To test if the country layers are working well, enter to the GWC GeoWebCache, then to "_A list of all the layers and automatic demos_" and check the behavior of the country layers, they should work according to their names.