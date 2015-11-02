# Guiding Principles #
The PortalAPI is developed respecting the recommendations specified in the Resource Oriented Architecture by Leonard Richarson and Sam Ruby found in [Building RESTful web services](http://oreilly.com/catalog/9780596529260).   Particular attention is taken in the following areas.
<dl>
<dt>Addressability</dt>
<dd>Content negotiation is avoided where possible by making use of canonical URI forms.  E.g. use <code>/tutorial/1.en</code> returning a <code>307 redirect</code> to <code>/tutorial/1</code> rather than using <code>HTTP header ACCEPT-LANGUAGE</code>.</dd>
<dt>Caching</dt>
<dd>Resource caching is promoted by providing a <code>Last-Modified</code> and <code>Cache-Control</code> header on all resource representations wherever possible, and supporting condition <code>HTTP GET</code> requests containing the header <code>If-Modified-Since</code> which might receive an <code>HTTP 304</code>.</dd>
<dt>Statelessness</dt>
<dd>The server will not store application state, ensuring requests are treated in isolation and allowing for scalability through load balancing.</dd>
<dt>Connectedness</dt>
<dd>Representations of resources will include links to additional resources of interest wherever possible; examples include a link to the next page of results or a link to a resource representing the verbatim data from which a derived view is calculated.</dd>
<dt>Safety and idempotence</dt>
<dd><code>HTTP GET</code> and <code>HEAD</code> are considered <i>safe</i>, as they trigger no server changes.  <code>HTTP PUT</code> and <code>DELETE</code> are considered idempotent; making more than one request has the same affect as calling once.  Safe requests are idempotent by definition.</dd>
<dt>Uniform interface</dt>
<dd>HTTP response codes are strictly adhered to.  The unified interface offered through HTTP is used as follows:<br>
<ul><li><b><code>HTTP GET</code></b>: Retrieve an instance.  Query parameters are used only when the response is the result of some server side algorithm (e.g. <code>/occurrence/search?dwc:scientificName=Puma concolor</code>) otherwise paths are used (e.g. <code>/occurrence/1234/annotation/2</code>).<br>
</li><li><b><code>HTTP PUT</code></b>: Create a new instance, or update the existing instance for the named resource.  Note that the client knows the complete identifier for the resource, otherwise an <code>HTTP POST</code> is used.<br>
</li><li><b><code>HTTP POST</code></b>: create a new resource with a server generated identifier by appending to a parent resource (“create a new Annotation resource on the parent Occurrence resource”) or where required, by calling a special “factory resource” to create the resource.  Overloading <code>HTTP POST</code> to perform RPC style operations (e.g. posting a dataset to <code>/dataset/publish</code>) is avoided, as is using parameters in the posted request (<code>action=”publish”</code>).  Instead a new resource is created which is considered the response to some process and treated as such with the uniform interface (e.g. <code>/publishEvent/dataset/123</code> returning a <code>303 see other</code> with <code>location</code> of <code>/dataset/123</code>).<br>
</li><li><b><code>HTTP HEAD</code></b>: returns the same as <code>GET</code>, but without the payload.  In particular the <code>Last-Modified</code> header is preserved.<br>
</li><li><b><code>HTTP OPTIONS</code></b>: Lists the available options on the resource to the authenticated user, if any.<br>
</li><li><b><code>HTTP DELETE</code></b>: Deletes the resources</dd>
</dl>