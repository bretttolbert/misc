<?xml version="1.0" encoding="utf-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="exslt">
  <sch:ns uri="http://exslt.org/dynamic" prefix="dyn"/>
  <sch:ns uri="http://example.com/example-sports" prefix="sports"/>
  <sch:ns uri="urn:ietf:params:xml:ns:netconf:base:1.0" prefix="nc"/>
  <sch:let name="root" value="/nc:data"/>
  <sch:pattern id="example-sports">
    <sch:rule context="/nc:data/sports:sports/sports:person">
      <sch:report test="preceding-sibling::sports:person[sports:name=current()/sports:name]">Duplicate key "sports:name"</sch:report>
    </sch:rule>
    <sch:rule context="/nc:data/sports:sports/sports:team">
      <sch:report test="preceding-sibling::sports:team[sports:name=current()/sports:name]">Duplicate key "sports:name"</sch:report>
    </sch:rule>
    <sch:rule context="/nc:data/sports:sports/sports:team/sports:player">
      <sch:report test="preceding-sibling::sports:player[sports:name=current()/sports:name and sports:season=current()/sports:season]">Duplicate key "sports:name sports:season"</sch:report>
      <sch:report test="preceding-sibling::sports:player[sports:number=current()/sports:number]">Violated uniqueness for "sports:number"</sch:report>
    </sch:rule>
    <sch:rule context="/nc:data/sports:sports/sports:team/sports:player/sports:name">
      <sch:report test="not($root/sports:sports/sports:person/sports:name=.)">Leaf "/nc:data/sports:sports/sports:person/sports:name" does not exist for leafref value "<sch:value-of select="."/>"</sch:report>
    </sch:rule>
  </sch:pattern>
</sch:schema>
