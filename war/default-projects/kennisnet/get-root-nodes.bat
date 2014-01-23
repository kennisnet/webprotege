@echo off

echo ^<roots^> > roots.xml
call saxon9 Aardrijkskunde_Bovenbouw_HAVO_VWO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Aardrijkskunde_Bovenbouw_VMBO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Aardrijkskunde_Onderbouw_HAVO_VWO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Aardrijkskunde_Onderbouw_VMBO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Aardrijkskunde_PO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Engels_Bovenbouw_HAVO_VWO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Engels_Bovenbouw_VMBO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Engels_HO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Engels_MBO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Engels_Onderbouw_HAVO_VWO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Engels_Onderbouw_VMBO_Trim.rdf get-root-node.xsl >> roots.xml
call saxon9 Engels_PO_Trim.rdf get-root-node.xsl >> roots.xml
echo ^</roots^> >> roots.xml

type roots.xml
