
*** Vizzuality code notes ***

== original Ruby code
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



*** STRUTS2 ***

== internationalisation
not done yet as we expect mayor changes in the html still and its less work to replace the real strings once we reached a considerable stable state. Otherwise we also run into lots of orphaned entries.

== Action suffices, URL paths, servlet filters
to be discussed, but struts doesnt work with url parameters and actions without suffix have also proven problematic in the past. A simple servlet filter converts integers and uuids to id parameters and adds in a configurable action suffix like .do

== Sitemesh
version3 is still in alpha and caused some NIO blocking with Jetty in my tests. The struts2 plugin for sitemesh also is not working with sitemesh3 now and needs to be rewritten. We therefore still use the old 2.4.2 version with the 2 config files sitemesh.xml and decorators.xml

== Paging
discuss how we implement result paging in general, maybe even clientside?
Options:
 * simple paging class (PageableAction) and freemarker template (pager.ftl) existing in this project
 * DisplayTag for jsp? http://www.displaytag.org/1.2/ requires the jsp support servlet and hasnt been updated since 1.5 years

== Authentication/login
not done at all yet, but an interceptor to protect pages requiring logins exist. This interceptor along with the base action keep the current user in the session, but treat it as an unspecific Object right now
