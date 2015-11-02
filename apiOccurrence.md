# Occurrence #
## Description ##
This implementation of the operation uses only response headers that are common to most responses. For more information, see [Common Response Headers](apiCommonResponseHeaders.md).
This implementation of the operation does not return special errors. For general information about the PortalAPI errors and a list of error codes, see [Error Responses](apiErrorResponses.md).
## Sytnax ##
`GET /occurrence[.{format}]?{query}[&start={start}]`
## Request parameters ##
## Examples ##
`/occurrence.json?dwc:year=1999&start=1000`