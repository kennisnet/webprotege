<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
	xmlns:owl="http://www.w3.org/2002/07/owl#"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:bk="http://purl.edustandaard.nl/begrippenkader/"
	xmlns:skos="http://www.w3.org/2004/02/skos/core#"
	exclude-result-prefixes="#all"
>

	<xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>
	<xsl:preserve-space elements="*"/>
	
	<xsl:variable name="roots" select="doc('../default-projects/kennisnet/roots.xml')/*/*"/>
	<xsl:variable name="projects" select="doc('../default-projects/metaproject/projects.xml')/*/*"/>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="/">
		<xsl:variable name="template" select="*" />
		<xsl:for-each select="$roots">
			<xsl:variable name="project" select="$projects[file = current()/@file]/id/string(.)"/>
			<xsl:result-document href="data-store/project-data/{$project}/configuration-data/ui-configuration.xml">
				<xsl:apply-templates select="$template">
					<xsl:with-param name="root" select="." tunnel="yes"/>
				</xsl:apply-templates>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="entry[string = 'topClass']">
		<xsl:param name="root" tunnel="yes"/>
		<entry>
			<string>topClass</string>
			<string><xsl:value-of select="$root"/></string>
		</entry>
	</xsl:template>
	
</xsl:stylesheet>

