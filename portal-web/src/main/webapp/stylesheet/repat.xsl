<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<xsl:output encoding="UTF-8" method="text"/>
<xsl:strip-space elements="question"/>
  <xsl:template match="/">
    <xsl:apply-templates/>
 </xsl:template>
 <xsl:template name="headers" match="/repatriation/country[1]">
    <xsl:text>&#x9;</xsl:text><xsl:for-each select="child::host"><xsl:value-of select="@name"/><xsl:text>&#x9;</xsl:text></xsl:for-each>
    <xsl:for-each select="//country"><xsl:text>&#xA;</xsl:text><xsl:variable name="country"><xsl:value-of select="@name"/></xsl:variable>
      <xsl:copy-of select="$country" /><xsl:text>&#x9;</xsl:text><xsl:for-each select="child::host"><xsl:value-of select="@count"/><xsl:text>&#x9;</xsl:text></xsl:for-each>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>