<%@ include file="/common/taglibs.jsp"%>
<style>

.breakdown { border:1px solid #CCCCCC; border-collapse: collapse;  margin-bottom:20px;margin-top:10px; width:650px;}
.breakdown td, .breakdown th { border:1px solid #CCCCCC;  padding: 5px 5px 0px 5px; }
.breakdown tr, .breakdown th { border:1px solid #CCCCCC; }
.sidelabel {color: #9d4c05; }

</style>
<h4>Record counts by Kingdom</h4>

<table class="breakdown">
<thead>
<th> </th>
<th>Observations</th>
<th>Specimens</th>
<th>Living</th>
<th>Germplasm</th>
<th>Fossil</th>
<th>Other</th>
<th class="total">Total</th>
</thead>
<tbody>
<c:forEach items="${kingdomBasisOfRecord}" var="row" varStatus="rawStatus">
<tr>
<td class="sidelabel">${row[0].entity3Name!=null ? row[0].entity3Name : 'Unknown'}</td>
<c:forEach items="${row}" var="cell" varStatus="cellStatus">
	<td><fmt:formatNumber pattern="###,###" value="${cell.count}"/></td>
</c:forEach>
</tr>
</c:forEach>
</tbody>
</table>