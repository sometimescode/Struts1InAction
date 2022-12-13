<%@ taglib uri="/tags/struts-html" prefix="html" %>
<html><head><title>Sign in, Please!</title></head>
<body>
<html:errors/>
<html:form action="/LogonSubmit" focus="username">
<table border="0" width="100%">
<tr>
<th align="right">Username: </th>
<td align="left"><html:text property="username"/></td>
</tr>
<tr><th align="right">Password: </th>
<td align="left"><html:password property="password"/></td>
</tr>
<tr>
<td align="right"><html:submit property="submit" value="Submit"/></td>
<td align="left"><html:reset/></td>
</tr>
</table>
</html:form>
</body>
</html>