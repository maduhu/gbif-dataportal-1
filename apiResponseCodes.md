This section provides reference information about the response codes in use with the PortalAPI

## List of Response codes ##
The following table lists PortalAPI response codes and their definitions.

| **Code** | **Definition** | **Purpose** |
|:---------|:---------------|:------------|
| `200`	   | `OK`           | The request was accepted and the response entity-body contains the resource representation, if any is applicable |
| `201`	   | `Created`      | The request was accepted and resulted in the creation of a new resource.  The response Location header contains the created resource URI |
| `202`	   | `Accepted`     | The client request has been accepted but is not handled in real time.  The response Location header contains the URI of a resource which can be used to check for progress |
| `204`	   | `No content`   | The client request was accepted but no resource representation is sent in response.  A `GET`, `POST`, `PUT` or `DELETE` might typically warrant this response. |
| `301`	   | `Moved permanently` | Sent when a client triggers an action that causes the URI of a resource change, or if an old URI was requested.  An example could be an update to a resource that causes a version increment |
| `303`	   | `See other`    | The request has been processed, and the client is recommended to see another resource, which is likely to be the resource they are seeking.  This is used only for `POST`, `PUT` or `DELETE` when the result might be a resource of interest.  For the PortalAPI a `GET` will always return a `307` instead of a `303`. |
| `304`	   | `Not modified` | Issued when the client provided the `If-Modified-Since` header and the representation has not changed.  No response entity-body will be given. |
| `307`	   | `Temporary redirect` | The request has not been processed, and the client is recommended to see another resource, which is likely to be the resource they are seeking.  This is returned should a client request a resource in the canonical form (e.g. without a version number), which has a default representation as another resource (e.g. the latest version). |
| `400`	   | `Bad request`  | There was a problem on the client side, and further information, if any, is available will be given in the response entity-body |
| `401`	   | `Unauthorized` | The proper authentication credentials were not supplied to operate on the protected resource.  Note that `403` will be used when credentials are supplied and correct, but still disallowed |
| `403`	   | `Forbidden`    | The authentication credentials supplied were correct, but do not allow operation on the protected resource |
| `404`	   | `Not found`    | The server does not know the resource being requested |
| `409`	   | `Conflict`     | Indicates that the operation was not accepted since it would leave one or more resources in an inconsistent state.  An example could be the deletion of a user account, which is referenced by annotations made by the user. |
| `500`	   | `Internal Server Error` | There was a problem on the server side, and diagnostic information, if any, is available in the response entity-body |
| `503`	   | `Service Unavailable` | The server is responding, but the underlying systems are not operating correctly, such as through resource starvation while under heavy load.  A `Retry-After` header will be provided to indicate the period at which the server suggest the client try again |