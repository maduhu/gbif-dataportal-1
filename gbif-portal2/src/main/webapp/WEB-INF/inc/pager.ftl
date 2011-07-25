[#ftl]
    <div class="pager">
  [#if pagerRecords?size==ps || p>1]
     page ${p} 
    [#if p > 1]<a class="btn" href="${domain}/${pagerLink}p=1">first</a>[/#if]
    [#if p > 2]<a class="btn" href="${domain}/${pagerLink}p=${p-1}">previous</a>[/#if]
    [#if pagerRecords?size==ps]<a class="btn" href="${domain}/${pagerLink}p=${p+1}">next</a>[/#if]
  [/#if] 
    </div>
