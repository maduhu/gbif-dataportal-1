
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


== CSS
The css is generated via SASS source files and static Compass file includes.
The Compass files to update are: xyz


== Migrating html to struts2 webapp
 - default decorator has been reworked quite a bit for menu, infoband and other variables. Best to update existing decorator instead of creating a new one from scratch
 - for each page use only pieces inside the content div
 - infoband content should go into <content tag="infoband">, see below
 - replace all links with correct ones, not using any suffix and proper package names in their singular form, e.g. occurrence, not occurrences
 - we need to create good includes in the WEB-INF/inc folder that can be reused in various places. Good candidates for individual includes are the partials in the ruby app


*** STRUTS2 ***

== URLs
struts has case sensitive URLs, a problem?

== package/namespace names
package names in struts define namespaces, i.e. parent url paths to the terminal action.
Names should use the singular form, e.g. occurrence, not occurrences

== internationalisation
not done yet as we expect mayor changes in the html still and its less work to replace the real strings once we reached a considerable stable state. Otherwise we also run into lots of orphaned entries.

use the struts tags for i18n, for example <@s.text name="menu.species"/>
Consider replacing the native struts2 text provider with a much simpler one we use in the IPT that increased page rendering with many getText lookups by more than 100% as the native one does an extensive resource bundle search across various classpaths and other places that we dont need.

== Action suffices, URL paths, servlet filters
to be discussed, but struts doesnt work with url parameters and actions without suffix have also proven problematic in the past. A simple servlet filter converts integers and uuids to id parameters and adds in a configurable action suffix like .do

== Sitemesh vs Freemarker includes
version3 is still in alpha and caused some NIO blocking with Jetty in my tests. The struts2 plugin for sitemesh also is not working with sitemesh3 now and needs to be rewritten. We therefore still use the old 2.4.2 version with the 2 config files sitemesh.xml and decorators.xml

Sitemesh exposes specific variables that contain content from the main page to be used in the decorators:
 <head><title> ==> ${title}
 <head> ==> ${head}
 <body> ==> ${body}
 <body class="xyz"> ==> ${page.properties["body.class"]!}

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
