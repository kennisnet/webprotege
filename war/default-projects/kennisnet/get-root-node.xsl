<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
		 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
         xmlns:bk="http://purl.edustandaard.nl/begrippenkader/"
         xmlns:dc="http://purl.org/dc/elements/1.1/"
         xmlns:foaf="http://xmlns.com/foaf/0.1/"
         xmlns:ore="http://www.openarchives.org/ore/terms/"
         xmlns:dcterms="http://purl.org/dc/terms/"
         xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
         xmlns:bkt="http://www.rnaproject.org/data/bkt/"
         xmlns:dctype="http://purl.org/dc/dcmitype/"
         xmlns:owl="http://www.w3.org/2002/07/owl#"
         xmlns:skos="http://www.w3.org/2004/02/skos/core#"
         xmlns:vs="http://www.w3.org/2003/06/sw-vocab-status/ns#"
         xmlns:wot="http://xmlns.com/wot/0.1/"
         xmlns:psys="http://proton.semanticweb.org/protonsys#"
         xmlns:rnax="http://www.rnaproject.org/data/rnax/"
         xmlns:rna="http://www.rnaproject.org/data/"
         xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
         xmlns:pext="http://proton.semanticweb.org/protonext#"
         xmlns:rdfg="http://www.w3.org/2004/03/trix/rdfg-1/"
	exclude-result-prefixes="#all"
>

	<xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*"/>
	
	<xsl:template match="/">
		<!-- zoek de proxy bij vak/leergebied begrip -->
		<xsl:variable name="vakLeergebied" select="//*[ore:proxyFor/@rdf:resource = 'http://purl.edustandaard.nl/begrippenkader/5c0413bd-cbaf-407d-93ff-4f7409559ff6']/@rdf:about/string(.)"/>
	
		<!-- zoek binnen project naar root door te zoeken naar proxy met proxy voor vak/leergebied als inhoudtype -->
		<root file="{replace(base-uri(/*), '^.*/([^/]*)', '$1')}"><xsl:value-of select="//*[bk:heeftBkInhoudType/@rdf:resource = $vakLeergebied]/@rdf:about"/></root>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	
</xsl:stylesheet>

