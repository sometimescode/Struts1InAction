<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<HTML>
<HEAD>
<TITLE>Welcome!</TITLE>
<html:base/>
</HEAD>
<BODY>
<logic:present name="user">
<H3>Welcome <bean:write name="user" property="username"/>!</H3>
</logic:present>
<logic:notPresent scope="session" name="user">
<H3>Welcome World!</H3>
</logic:notPresent>
<html:errors/>
<UL>
<LI><html:link forward="logon">Sign in</html:link></LI>
<logic:present name="user">
<LI><html:link forward="logoff">Sign out</html:link></LI>
</logic:present>
</UL>
<IMG src='struts-power.gif' alt='Powered by Struts'>
</BODY>
</HTML>