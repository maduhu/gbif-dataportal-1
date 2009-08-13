<%@ include file="/common/taglibs.jsp"%>
<%@ attribute name="latitude" required="true" rtexprvalue="true" type="java.lang.Float"%>
<%@ attribute name="longitude" required="true" rtexprvalue="true" type="java.lang.Float"%>
<c:if test="${latitude!=null}"><gbif:decimal noDecimalPlaces="4">${latitude<0 ? (latitude*-1): latitude}</gbif:decimal>&deg;${latitude>0?'N':'S'}</c:if>${latitude!=null && longitude!=null ?', ':''} 
<c:if test="${longitude!=null}"><gbif:decimal noDecimalPlaces="4">${longitude<0 ? (longitude*-1): longitude}</gbif:decimal>&deg;${longitude>0?'E':'W'}</c:if>
