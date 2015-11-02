| **Format** | **Response** |
|:-----------|:-------------|
| `json`     | Response requested in JSON format.  If the format is not explicitly provided in the URI, then this will become the default response format (by means of a `307 redirect`) |
| `xml `     |  Response requested in XML format.  XML response formats reference XSD schemas where one exists |
| `csv`      | Response requested in CSV format.  All values are enclosed by “” and the header row contain column titles.  This is only suitable for resources that represent lists of Occurrence or Taxon, such as search results |
| `atom`     | Response requested in atom form.  This is only suitable for resources that represent lists, such as search results |
| `dwca`     | Response requested in a DarwinCore Archive format.  This is only suitable for resources that represent lists, such as search results |