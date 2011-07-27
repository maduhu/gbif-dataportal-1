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
	<link rel="stylesheet" href="/css/style.css?v=2">
	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/themes/base/jquery-ui.css"
				type="text/css" media="all"/>
	<!-- Uncomment if you are specifically targeting less enabled mobile browsers
<link rel="stylesheet" media="handheld" href="css/handheld.css?v=2">  -->

	<script src="/js/modernizr-1.7.min.js"></script>

	${head}

</head>
<body class="${page.properties["body.class"]!}">
	<header>

		<!-- top -->
		<div id="top">
			<div class="content">
				<div class="account">
					<a href="#" class="login" title="Sign in">Sign in</a> or <a href="/user/register/step0.html"
																																			title="Create your account">Create your
					account</a>
				</div>

				<div id="logo">
					<a href="/reference.html" class="logo"><img src="/img/header/logo.png"/></a>

					<h1><a href="/reference.html" title="DATA.GBIF.ORG">DATA.GBIF.ORG</a></h1>
					<span>Free and open access to biodiversity data</span>
				</div>


				<nav>
					<ul>
						<li><a href="/occurrence/home" title="Occurrences">Occurrences</a></li>
						<li class="selected"><a href="/dataset/home" title="Datasets">Datasets</a></li>
						<li><a href="/species/home" title="Species">Species</a></li>
						<li><a href="#" class="more" title="More">More<span class="more"></span></a></li>
						<li class="search">
							<form action="/dataset/home" method="post">
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

	</header>

	<div id="content">

		${body}

	</div>

	<footer>
		<div class="content">
			<ul>
				<li><h3>EXPLORE THE DATA</h3></li>
				<li><a href="/occurrences/home">Occurrences</a></li>
				<li><a href="/dataset/home">Datasets</a></li>
				<li><a href="/species/home">Species</a></li>
				<li><a href="/countries/home">Countries</a></li>
				<li><a href="/members/home">GBIF Network Members</a></li>
				<li><a href="/themes/home">Themes</a></li>
			</ul>

			<ul>
				<li><h3>VIEW THE STATISTICS</h3></li>
				<li><a href="#">Global numbers</a></li>
				<li><a href="#">Taxonomic coverage</a></li>
				<li><a href="#">Providers</a></li>
				<li><a href="#">Countries</a></li>
			</ul>

			<ul>
				<li><h3>JOIN THE COMMUNITY</h3></li>
				<li><a class="login" href="#">Sign in</a></li>
				<li><a href="/user/register/step0.html">Sign up</a></li>
				<li><a href="/static/terms_and_conditions.html">Terms and conditions</a></li>
				<li><a href="/static/about.html">About GBIF</a></li>
			</ul>

			<ul class="first">
				<li><h3>LATEST FROM THE GBIF DEVELOPER BLOG</h3></li>
				<li>
					<p><a href="#">The GBIF Development Team</a>
						<span class="date">11th, April of 2011</span></p>

					<p>Recently the GBIF development group have been asked to communicate more on the work being carried out
						in...</p>
				</li>
			</ul>

			<ul>
				<li class="no_title">
					<p><a href="#">Setting up a Hadoop cluster - Part 1...</a>
						<span class="date">11th, April of 2011</span></p>

					<p>Recently the GBIF development group have been asked to communicate more on the work being carried out in
						the se...</p>
				</li>
			</ul>

			<ul>
				<li><h3>LATEST FROM THE GBIF NEWS</h3></li>
				<li>
					<p><a href="#">Experts Workshop on the GBIF...</a>
						<span class="date">11th, April of 2011</span></p>

					<p>The GBIF Secretariat is pleased to announce a new training opportunity for GBIF Participants: an
						Experts...</p>
				</li>
			</ul>
		</div>
	</footer>

	<div class="copyright">
		<p>2011 © GBIF. Data publishers retain all rights to data.</p>
	</div>

	<div style="text-align:left">
		<p>Sitemesh debugging, page properties in decorator</p>
		<br/>
		<pre>
		<#list page.properties?keys as k>
			${k} = ${page.properties[k]!""}
		</#list>
		</pre>
		<!-- TESTING i18n -->
		<@s.text name="menu.species"/>
	</div>

	<!-- JavaScript at the bottom for fast page loading -->
	<!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if necessary -->

	<!--<script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/jquery-ui.min.js"></script>
<script>window.jQuery || document.write("<script src='/js/jquery-1.6.1.min.js'>\x3C/script>"); document.write("<script src='/js/jquery-ui.min.js'>\x3C/script>");</script>
-->

	<!-- scripts concatenated and minified via ant build script  -->
	<script src="/js/jquery-1.6.1.min.js"></script>
	<script src="/js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="/js/mousewheel.js"></script>
	<script type="text/javascript" src="/js/jscrollpane.min.js"></script>
	<script src="/js/jquery-scrollTo-1.4.2-min.js"></script>
	<script src="/js/underscore-min.js"></script>
	<script src="/js/helpers.js"></script>
	<script src="/js/popovers.js"></script>
	<script src="/js/graphs.js"></script>
	<script src="/js/app.js"></script>
	<script src="/js/jquery.uniform.min.js" type="text/javascript"></script>
	<script src="/js/raphael-min.js"></script>
	<!-- end scripts-->
	<!--[if lt IE 7 ]>
	<script src="/js/libs/dd_belatedpng.js"></script>
	<script>DD_belatedPNG
					.fix("img, .png_bg"); // Fix any <img> or .png_bg bg-images. Also, please read goo.gl/mZiyb </script>
	<![endif]-->

	<script type="text/javascript">
		$(function() {
			$('nav ul li a.more').bindLinkPopover({
				links:{
					"Countries":"/countries/index.html",
					"GBIF network":"/members/index.html",
					"Themes":"/themes/index.html",
					"Stats":"/stats/index.html",
					"About":"/static/about.html"
				}
			});
		});
	</script>

</body>
</html>
