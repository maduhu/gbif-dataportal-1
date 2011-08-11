<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7 ]> <html class="no-js ie6" lang="en"> <![endif]-->
<!--[if IE 7 ]>    <html class="no-js ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]>    <html class="no-js ie8" lang="en"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<html class="no-js" lang="en"> <!--<![endif]-->
<head>
  <meta charset="utf-8">
  <#if useGooglemaps!false>
  <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
  </#if>
<#-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
Remove this if you use the .htaccess -->
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

  <title>${title}</title>
  <meta name="description" content="">
  <meta name="author" content="GBIF">

  <!-- Mobile viewport optimized: j.mp/bplateviewport -->
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Place favicon.ico & apple-touch-icon.png in the root of your domain and delete these references -->
  <link rel="shortcut icon" href="/favicon.ico">
  <link rel="apple-touch-icon" href="/apple-touch-icon.png">

  <!-- CSS: implied media="all" -->
  <link rel="stylesheet" href="<@s.url value='/css/style.css?v=2'/>"/>
  <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/themes/base/jquery-ui.css"
        type="text/css" media="all"/>
  <!-- Uncomment if you are specifically targeting less enabled mobile browsers
<link rel="stylesheet" media="handheld" href="css/handheld.css?v=2">  -->

  <script src="<@s.url value='/js/modernizr-1.7.min.js'/>"></script>

  ${head}

</head>
<body class="${page.properties["body.class"]!}">
  <header>

    <!-- top -->
    <div id="top">
      <div class="content">
        <div class="account">
          <a href="#" class="login" title='<@s.text name="menu.login"/>'><@s.text name="menu.login"/></a> or
          <a href="<@s.url value='/user/register/step1'/>" title='<@s.text name="menu.register"/>'><@s.text name="menu.register"/></a>
        </div>

        <div id="logo">
          <a href="<@s.url value='/'/>" class="logo"><img src="<@s.url value='/img/header/logo.png'/>"/></a>

          <h1><a href="<@s.url value='/'/>" title="DATA.GBIF.ORG">DATA.GBIF.ORG</a></h1>
          <span>Free and open access to biodiversity data</span>
        </div>

        <#assign menu=(page.properties["meta.menu"])!"home" />
        <#assign menuItems=["occurrences","datasets","species"] />
        <nav>
          <ul>
            <#list menuItems as m>
              <li<#if menu==m> class="selected"</#if>>
                <@s.text id="menuName" name="menu.${m}" />
                <@s.a value="/${m}" title="%{menuName}"><@s.text name="menu.${m}"/></@s.a></li>
            </#list>
            <li><a href="#" class="more" title="<@s.text name="menu.more"/>"><@s.text name="menu.more"/><span class="more"></span></a>
            </li>
            <li class="search">
              <form href="<@s.url value='/datasets/search'/>" method="post">
                <span class="input_text">
                  <input type="text" name="q"/>
                </span>
              </form>
            </li>
          </ul>
        </nav>
      </div>
    </div>
    <!-- /top -->

  <#if page.properties["page.infoband"]?has_content>
    <div id="infoband">
      <div class="content">
        ${page.properties["page.infoband"]}
      </div>
    </div>
  </#if>

  <#if page.properties["page.tabs"]?has_content>
    <div id="tabs">
      <div class="content">
      ${page.properties["page.tabs"]}
      </div>
    </div>
  </#if>

  </header>

  <div id="content">

    ${body}

  </div>

  <footer>
    <div class="content">
      <ul>
        <li><h3>EXPLORE THE DATA</h3></li>
        <li><a href="<@s.url value='/occurrences'/>"><@s.text name="menu.occurrences"/></a></li>
        <li><a href="<@s.url value='/datasets'/>"><@s.text name="menu.datasets"/></a></li>
        <li><a href="<@s.url value='/species'/>"><@s.text name="menu.species"/></a></li>
        <li><a href="<@s.url value='/countries'/>"><@s.text name="menu.countries"/></a></li>
        <li><a href="<@s.url value='/members'/>"><@s.text name="menu.members"/></a></li>
        <li><a href="<@s.url value='/themes'/>"><@s.text name="menu.themes"/></a></li>
      </ul>

      <ul>
        <li><h3>VIEW THE STATISTICS (not implemented)</h3></li>
        <li><a href="#">Global numbers</a></li>
        <li><a href="#">Taxonomic coverage</a></li>
        <li><a href="#">Providers</a></li>
        <li><a href="#">Countries</a></li>
      </ul>

      <ul>
        <li><h3>JOIN THE COMMUNITY</h3></li>
        <li><a class="login" href="<@s.url value='/session/login'/>"><@s.text name="menu.login"/></a></li>
        <li><a href="<@s.url value='/user/register/step1'/>"><@s.text name="menu.register"/></a></li>
        <li><a href="<@s.url value='/terms'/>"><@s.text name="menu.terms"/></a></li>
        <li><a href="<@s.url value='/about'/>"><@s.text name="menu.about"/></a></li>
      </ul>

      <!--
      <ul class="first">
        <li><h3>LATEST FROM THE GBIF DEVELOPER BLOG (not implemented)</h3></li>
        <li>
          <p><a href="#">A Fake Blog Post</a>
            <span class="date">11th, April of 2011</span></p>
          <p>The GBIF development team has made a fake blog post for testing purposes...</p>
        </li>
      </ul>

      <ul>
        <li class="no_title">
          <p><a href="#">Another Blog Post</a>
            <span class="date">11th, April of 2011</span></p>
          <p>Another fake blog post for testing purposes...</p>
        </li>
      </ul>

      <ul>
        <li><h3>LATEST FROM THE GBIF NEWS (not implemented)</h3></li>
        <li>
          <p><a href="#">An Event at GBIF...</a>
            <span class="date">11th, April of 2011</span></p>
          <p>The GBIF Secretariat is pleased to announce a new unimplemented event for testing purposes</p>
        </li>
      </ul>
      -->
    </div>
  </footer>

  <div class="copyright">
    <p>2011 &copy; GBIF. Data publishers retain all rights to data.</p>
  </div>

  <#--<div style="text-align:left">-->
    <#--<p>Sitemesh debugging, page properties in decorator</p>-->
    <#--<br/>-->
    <#--<pre>-->
    <#--<#list page.properties?keys as k>-->
      <#--${k} = ${page.properties[k]!""}-->
    <#--</#list>-->
    <#--</pre>-->
  <#--</div>-->

  <!-- JavaScript at the bottom for fast page loading -->
  <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if necessary -->

  <!--<script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/jquery-ui.min.js"></script>
<script>window.jQuery || document.write("<script src='/js/jquery-1.6.1.min.js'>\x3C/script>"); document.write("<script src='/js/jquery-ui.min.js'>\x3C/script>");</script>
-->

  <!-- scripts concatenated and minified via ant build script  -->
  <script src="<@s.url value='/js/vendor/jquery-1.6.1.min.js'/>"></script>
  <script src="<@s.url value='/js/vendor/jquery-ui.min.js'/>"></script>
  <script src="<@s.url value='/js/vendor/autocomplete.js'/>"></script>
  <script type="text/javascript" src="<@s.url value='/js/vendor/mousewheel.js'/>"></script>
  <script type="text/javascript" src="<@s.url value='/js/vendor/jscrollpane.min.js'/>"></script>
  <script src="<@s.url value='/js/vendor/jquery-scrollTo-1.4.2-min.js'/>"></script>
  <script src="<@s.url value='/js/vendor/underscore-min.js'/>"></script>
  <script src="<@s.url value='/js/helpers.js'/>"></script>
  <script src="<@s.url value='/js/widgets.js'/>"></script>
  <script src="<@s.url value='/js/app.js'/>"></script>
  <script src="<@s.url value='/js/graphs.js'/>"></script>
  <script src="<@s.url value='/js/vendor/jquery.uniform.min.js" type="text/javascript' />"></script>
  <script src="<@s.url value='/js/vendor/OpenLayers.js'/>"></script>
  <script src="<@s.url value='/js/full_map.js'/>"></script>
  <script src="<@s.url value='/js/types_map.js'/>"></script>
  <script src="<@s.url value='/js/single_map.js'/>"></script>
  <script src="<@s.url value='/js/openlayers_addons.js'/>"></script>
  <script src="<@s.url value='/js/Infowindow.js'/>"></script>
  <script src="<@s.url value='/js/vendor/raphael-min.js'/>"></script>
  <!-- end scripts-->

  <!--[if lt IE 7 ]>
  <script src="<@s.url value='/js/libs/dd_belatedpng.js'/>"></script>
  <script>DD_belatedPNG
          .fix("img, .png_bg"); // Fix any <img> or .png_bg bg-images. Also, please read goo.gl/mZiyb </script>
  <![endif]-->

  <script type="text/javascript">
    $(function() {
      $('nav ul li a.more').bindLinkPopover({
        links:{
          "<@s.text name="menu.countries"/>":"<@s.url value='/countries'/>",
          "<@s.text name="menu.members"/>":"<@s.url value='/members'/>",
          "<@s.text name="menu.themes"/>":"<@s.url value='/themes'/>",
          "<@s.text name="menu.stats"/>":"<@s.url value='/stats'/>",
          "<@s.text name="menu.about"/>":"<@s.url value='/about'/>"
        }
      });
    });
  </script>

  <#if page.properties["page.extra_scripts"]?has_content>
    ${page.properties["page.extra_scripts"]}
  </#if>

</body>
</html>
