
*** Vizzuality code notes ***

== original Ruby code
code at GitHub:
https://github.com/Vizzuality/Data.GBIF

Here's the explanation of what's inside those folders:
	assets  → contains images and files that are not processed by nanoc/sass
	content → contains css and erb files that are processed by nanoc/sass
	layouts → contains the main structure of the pages and several snippets of code that appear several times in the site (partials)
	output  → contains the result of the compilation and is what we upload to gbif.heroku.com

All the gems and ruby code used in the project are installed in this folder:
~/.rvm/gems/ruby-1.9.2-head@gbif/gems

In the project folder we have the following files:
	• config.rb: configuration file for compass
	• Rules: compilation rules for compass and nanoc
	• config.yaml: main configuration file for nanoc
	• Rakefile: custom tasks to compile the website
	• Gemfile: list of gems and versions used

== folders in the this webapp that contain vizzuality files:
src/webapp/css
All css and sass files taken from the output/css and content/css

src/webapp/external
These are example content files used for the mockups, mainly spcies images. We need to figure out how to deal with external images first, but this folder can ultimately be removed. For demo puposes keep it. It holds the folder and all their files from assets/img/photos, assets/img/logos, assets/img/slideshow

src/webapp/fonts
All fonts used in the design. Unclear if there are needed for the final layout. But we should keep them anyway. Taken from assets/fonts

src/webapp/img
All the static images used in the design. Taken from assets/img, but ignoring the folders copied to above paths already (e.g. photos). Also add the assets/favicon folder.

src/webapp/js
The javascript being used, incl jQuery libs. Taken from content/js

src/webapp/favicon.ico
Copied (again) from assets/favicon/favicon_32x32.ico

src/webapp/WEB-INF/pages
This is the bulk of the manual work. All files must be modified, see "Migrating html to freemarker templates"

== CSS
The css is generated via SASS source files and static Compass file includes.
The Compass files to update are: xyz


== Migrating html to freemarker templates
 - default decorator has been reworked quite a bit for menu, infoband, tabs and other variables. It also uses some i18n resources already. Best to update existing decorator instead of creating a new one from scratch
 - for each page use only pieces inside the content div
 - infoband content should go into <content tag="infoband">, see below
 - tabs content should go into <content tag="tabs">, see below
 - replace all links with correct ones, not using any suffix and proper urls in their singular form, e.g. occurrence, not occurrences. Use ids as url parameters, not query params.
 - we need to create good includes in the WEB-INF/inc folder that can be reused in various places. Good candidates for individual includes are the partials in the ruby app. Definite examples for includes are the map module and the taxonomic explorer. This hasnt been done at all!


*** STRUTS2 ***

== URL conventions (UPDATED BY OLIVER!)
we want to expose restful, pretty urls like the following.
The examples are mainly for datasets, but would be similar for species, occurrences, etc

/ (portal home)
/datasets (datasets home)
/datasets/search?q=puma (search datasets for the word puma)
/dataset/{UUID} (detail page for specific dataset)
/dataset/{UUID}/stats (stats page for specific dataset X)
/dataset/{UUID}/activity (activity page for specific dataset X)
/dataset/{UUID}/discussion (discussion page for specific dataset X)

=== Action mapping
we use wildcard mapping with the namedVariable pattern matcher for the default action mapper.
http://struts.apache.org/2.x/docs/wildcard-mappings.html

The conventions plugin (http://struts.apache.org/2.x/docs/convention-plugin.html) looked good, but failed to do the above urls with id params in the middle of the urls.
It also isnt as configurable as the explicit struts.xml definitions, for example to change http headers, content type, serving binary (eg image) streams, etc so it was finally not used.

=== Case sensitivity
URLs should be case insensitive. Struts, like servlets, seems case sensitive by default.
We need to see if we can change a setting or use a rewrite to lower case filter?

== internationalisation
not done yet as we expect mayor changes in the html still and its less work to replace the real strings once we reached a considerable stable state. Otherwise we also run into lots of orphaned entries.

use the struts tags for i18n, for example <@s.text name="menu.species"/>
Consider replacing the native struts2 text provider with a much simpler one we use in the IPT that increased page rendering with many getText lookups by more than 100% as the native one does an extensive resource bundle search across various classpaths and other places that we dont need if we stick to a single resource file.

== Freemarker vs struts tags
Freemarker has support for iterating lists, displaying properties, including other templates, macro's, and so on. There is a small performance cost when using the struts tags instead of the Freemarker equivalent (eg. <s:property value="foo"/> should be replaced by ${foo}), so use as much of native freemarker as you can!

=== Freemarker Tips:
Common pitfalls with freemarker are:

You need to handle null value explicitly. If nulls are encountered in places you render the variable, an exception will occurr. You need to explicitly tell freemarker what to do with nulls. The default operator (!) can be used or ?? to test for not null. To also test for empty string use ?has_content. Example ${dataset.title!"No title given"}

Freemarker renders non strings for humans by default based on the current locale. You can instruct freemarker to use different renderings. This is very important for numbers as parameters, as you will be surprised with commas otherwise. Render numbers for "computers" is done with ?c, e.g. ${u.taxonID?c}. Booleans also require special attention. To render simple true/false strings use ?string, e.g. ${u.isSynonym?string}, you can also specify the strings to be used.

BIG NOTE: if you have a freemarker error (exceptions) sitemesh will swallow it and you can't see it unless you remove sitemesh (sitemesh filter from struts).  Do we need Sitemesh
at all?  Markus thinks not, Tim thinks yes.

== Sitemesh
version3 is still in alpha and caused some NIO blocking with Jetty & Tomcat 7 and above in my tests.
The struts2 plugin for sitemesh also does not work with sitemesh3 and still waits to be rewritten.
We therefore still use the old 2.4.2 version with the 2 config files sitemesh.xml and decorators.xml

=== content fragments
as decorators cant access freemarker vars from the main page, we have to use custom sitemesh tags in the main page to pass content fragments to the decorators, for example to populate the green "infoband":

main page:
<content tag="infoband">
		<h2>Search datasets</h2>
		<form>
			<input type="text" name="search"/>
		</form>
</content>

inside the decorator these content tags can be reached via:
  ${page.properties["page.infoband"]}

=== Decorators
we use a single default.ftl decorator

Sitemesh exposes specific variables that contain content from the main page to be used in the decorators:
 <head><title> ==> ${title}
 <head> ==> ${head}
 <body> ==> ${body}
 <body class="xyz"> ==> ${page.properties["body.class"]!}

the default decorator is aware of the following content fragments and variables apart from the main ones above:

 meta.menu
 content tag="infoband"
 content tag="tabs"

=== Errors
If the underlying action results in an uncaught error, the sitemesh filter fails with a NPE and you cannot see the original struts stacktrace.
Only way I found to change that is disabling the SiteMeshFilter in the PortalListener and restart the server to try the same query again.


== Paging
discuss how we implement result paging in general
Options:
 * simple paging class (PageableAction) and freemarker template (pager.ftl) exists in this project
 * DisplayTag for jsp? http://www.displaytag.org/1.2/ requires the jsp support servlet so freemarker can use this custom tag library and hasnt been updated since 1.5 years, but we used it before
 * ajax clientside

not done at all yet, but an interceptor to protect pages requiring logins exist. This interceptor along with the base action keep the current user in the session, but treat it as an unspecific Object right now

== Authentication/login
A RequireLoginInterceptor exists to protect actions or entire packages to only allow logged in users.
It looks for a user object in the session, which needs to be created by the actual login method and the authentication framework of your choice.
Evaluate CAS and Shiro for the central authentication.

OLIVER NOTES
- species/distribution.html and species/bibliography.html don't exist (weren't contracted from vizz but exist as links)
- status: occurrences, users and session haven't been touched; dataset started, using proper struts tags for links; all others complete but not using struts tags (links broken when in context)
