<%-- 
    Document   : Purchases
    Created on : Apr 3, 2024, 4:48:17 AM
    Author     : CITStudent
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Member Purchases</title>
</head>
<body>
    <h1>Transactions on File for:</h1>
    <h2>${m.memid}</h2>
    <h2>${m.firstname} ${m.lastname}</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Purchase Dt</th>
                <th>Trans Type</th>
                <th>Trans Cd</th>
                <th>Trans Desc</th>
                <th>Amount</th>
            </tr>
   
        </thead>
        <tbody>
            <!-- Code here for output lines using JSTL forEACH -->
            <c:forEach var="purchase" items="${pur}">
                <tr>
                    <td>${purchase.purchasedt}</td>
                    <td>${purchase.transtype}</td>
                    <td>${purchase.transcd}</td>
                    <td>${purchase.transdesc}</td>
                    <td><fmt:formatNumber value="${purchase.amount}" type="currency"/></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <br>
    ${msg}
    <br><a href="MemberScreen.jsp">Back to Member Screen</a>
</body>
</html>
