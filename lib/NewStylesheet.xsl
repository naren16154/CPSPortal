<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
				<style type="text/css">
					.TFtable tr:nth-child(odd){
					background: #dae5f4;
					}
					.TFtable tr:nth-child(even){
					background: white;
					}
				</style>
				<script type="text/javascript">
					function filter(){
					var tr =
					document.getElementsByTagName("tr");
					for(var i=0;
					i&lt;tr.length;i++)
					{
					tr[i].style.display = 'none';
					}
					var chk =
					document.getElementsByTagName("input");
					for(var k=0;
					k&lt;chk.length;k++)
					{
					if(true == chk[k].checked)
					{
					for(var j=0;
					j&lt;tr.length;j++)
					{
					if((chk[k].id == tr[j].id) &#124;&#124;
					("header" == tr[j].id))
					{
					tr[j].style.display = '';
					}
					}
					}
					}
					return false;
					}
					function noFilter(){
					var chk =
					document.getElementsByTagName("input");
					for(var k=0;
					k&lt;chk.length;k++)
					{
					chk[k].checked=false;
					}
					var t =
					document.getElementsByTagName("tr");
					for(var j=0;
					j&lt;t.length;j++)
					{
					if("DEBUG" != t[j].id)
					{
					t[j].style.display = '';
					}
					else
					{
					t[j].style.display = 'none';
					}
					}
					return false;
					}
					function debugFilter(){
					var chk =
					document.getElementById("DEBUG");
					var t =
					document.getElementsByTagName("tr");
					for(var j=0;
					j&lt;t.length;j++)
					{
					if("DEBUG" == t[j].id)
					{
					if(true == chk.checked)
					{
					t[j].style.display
					= '';
					}
					else
					{
					t[j].style.display = 'none';
					}
					}
					}
					return false;
					}
				</script>
			</head>
			<body style="font-family: inherit; font-size: small">
				<div style="background-color: inactiveborder;">
					Filter Here
					<br />
					<input type="checkbox" id="INFO"
						onchange="filter(); return false;" />
					Info
					<input type="checkbox" name="debug" id="DEBUG"
						onchange="debugFilter(); return false;" />
					Debug
					<input type="checkbox" id="VERIFICATION_POINT"
						onchange="filter(); return false;" />
					Verification points
					<input type="checkbox" id="ACTION"
						onchange="filter(); return false;" />
					Actions
					<input type="checkbox" id="METHOD"
						onchange="filter(); return false;" />
					Methods
					<input type="checkbox" id="ERROR"
						onchange="filter(); return false;" />
					Errors
					<input type="checkbox" id="FATAL"
						onchange="filter(); return false;" />
					Fatal
					<input type="checkbox" id="WARNINGS"
						onchange="filter(); return false;" />
					Warnings
					<button id="default" onclick="noFilter(); return false;">Default</button>
				</div>
				<table class="TFtable" style="width: 100%;">
					<tr id="header"
						style="background-color: orange; color: white; font-weight: bolder; ">
						<td>Type</td>
						<td>Message</td>
					</tr>
					<xsl:for-each select="Adflog/log">
						<tr id="{@level}" valign="top"
							style="font-family: inherit; font-size: small">
							<xsl:variable name="color">
								<xsl:choose>
									<xsl:when
										test="@level='INFO' or @level='DEBUG' or @level='ACTION' or @level='STATUS'">
										black
									</xsl:when>
									<xsl:when test="@level='METHOD'">
										brown
									</xsl:when>
									<xsl:when
										test="@level='VERIFICATION_POINT' and contains(@message, 'result=true')">
										green
									</xsl:when>
									<xsl:otherwise>
										red
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<td style="color:{$color};">
								<xsl:value-of select="@level"></xsl:value-of>
							</td>
							<td style="white-space: pre-line;">
								<xsl:value-of select="@message"></xsl:value-of>
								<xsl:if test="@file">
									<xsl:text>&#xa;</xsl:text>
									<a href="{concat('images\',@file)}" target="_blank">Screenshot</a>
								</xsl:if>
							</td>
						</tr>
					</xsl:for-each>
					<script type="text/javascript">
						noFilter();
					</script>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>