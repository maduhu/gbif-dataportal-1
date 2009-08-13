
<div id="map" style="height:300px; width:600px; border: 1px solid #cccccc; margin-top:50px;"></div>

<script type="text/javascript" src="http://www.openlayers.org/api/OpenLayers.js"></script>

<script>
        function initMap(){
            var map = new OpenLayers.Map('map');
            var gbif = new OpenLayers.Layer.WMS( "GBIF",
                "http://geoserver.gbif.org/wms?", {layers: "gbif:gbifDensityLayer", version: "1.0.0", transparent: "true", 
                format: "image/png", filter:"${param.wmsFilter}"} );

			var ol_wms = new OpenLayers.Layer.WMS( "World Map",
              "http://labs.metacarta.com/wms-c/Basic.py?", {layers: 'basic', format: 'image/png' } );

            map.addLayers([ol_wms, gbif]);
            map.addControl(new OpenLayers.Control.LayerSwitcher());
			map.zoomToMaxExtent();
        }
        
        initMap();
</script>

