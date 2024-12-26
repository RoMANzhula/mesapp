<#macro pager url page >

    <#if page.getTotalPages() gt 5 >
        <#assign
        totalPages = page.getTotalPages()
        pageCurrentNumber = page.getNumber() + 1

        firstPart = (pageCurrentNumber > 4)?then([1, -1], [1, 2, 3])
        lastPart = (pageCurrentNumber < totalPages - 3)?then([-1, totalPages], [totalPages - 2, totalPages - 1, totalPages])
        middleFirst = (pageCurrentNumber > 4 &amp;&amp; pageCurrentNumber < totalPages - 1)?then([pageCurrentNumber -  1], [])
        middleSecond = (pageCurrentNumber > 2 &amp;&amp; pageCurrentNumber < totalPages - 3)?then([pageCurrentNumber + 1], [])

        pagesView = firstPart + middleFirst + (pageCurrentNumber > 3 &amp;&amp; pageCurrentNumber < totalPages - 2)?then([pageCurrentNumber], []) + middleSecond + lastPart
        >

    <#else>
        <#assign pagesView = 1..page.getTotalPages() >
    </#if>
    <div class="container mt-3" data-bs-theme="dark">
        <div class="row">
            <ul class="pagination col justify-content-center">
                <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1">Pages</a>
                </li>
                <#list pagesView as p>
                    <#if (p - 1) == page.getNumber()>
                        <li class="page-item active">
                            <a class="page-link" href="#" tabindex="-1">${p}</a>
                        </li>
                    <#elseIf p == -1>
                        <li class="page-item disabled">
                            <a class="page-link" href="#" tabindex="-1">...</a>
                        </li>
                    <#else>
                        <li class="page-item">
                            <a class="page-link" href="${url}?page=${p - 1}&amp;size=${page.getSize()}"
                               tabindex="-1">${p}</a>
                        </li>
                    </#if>
                </#list>
            </ul>

            <ul class="pagination col justify-content-center">
                <form method="get" action="/main" class="form-inline">
                    <div class="stack gap-3">
                        <span class="badge text-bg-info">
                            <input class="form-control" type="text" name="filter"
                                   value="${filter?ifExists}" placeholder="Вy tag" />
                        </span>
                        <button type="submit" class="btn btn-dark">Search</button>
                    </div>
                </form>
            </ul>

            <ul class="pagination col justify-content-center">
                <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1">Elements on page</a>
                </li>
                <#list [4, 8, 12] as c>
                    <#if c == page.getSize()>
                        <li class="page-item active">
                            <a class="page-link" href="#" tabindex="-1">${c}</a>
                        </li>
                    <#else>
                        <li class="page-item">
                            <a class="page-link" href="${url}?page=${page.getNumber()}&amp;size=${c}" tabindex="-1">${c}</a>
                        </li>
                    </#if>
                </#list>
            </ul>
        </div>
    </div>
</#macro>

<#--<#macro pagenator url page >-->
<#--    <#if page.getTotalPages() gt 7>-->
<#--        <#assign-->
<#--            totalPages = page.getTotalPages()-->
<#--            currentPageNumber = page.getNumber() + 1-->

<#--            head = (currentPageNumber > 4)?then([1, -1], [1, 2, 3])-->
<#--            tail = (currentPageNumber < totalPages - 3)?then([-1, totalPages], [totalPages - 2, totalPages - 1, totalPages])-->
<#--            bodyStart = (currentPageNumber > 4 && currentPageNumber < totalPages - 1)?then([currentPageNumber - 2, currentPageNumber - 1], [])-->
<#--            bodyFinish = (currentPageNumber > 2 && currentPageNumber < totalPages - 3)?then([currentPageNumber + 1, currentPageNumber + 2], [])-->

<#--            body = head + bodyStart + (currentPageNumber > 3 && currentPageNumber < totalPages - 2)?then([currentPageNumber], []) + bodyFinish + tail-->
<#--        />-->
<#--    <#else>-->
<#--        <#assign-->
<#--            body = 1..page.getTotalPages()-->
<#--        />-->
<#--    </#if>-->
<#--    <div class="mt-3">-->
<#--        <ul class="pagination">-->
<#--            <li class="page-item">-->
<#--                <a class="page-link" href="#" aria-label="Previous">-->
<#--                    <span aria-hidden="true">&laquo;</span>-->
<#--                </a>-->
<#--            </li>-->
<#--            <#list body as p>-->
<#--                <#if (p - 1) == page.getNumber()>-->
<#--                    <li class="page-item active">-->
<#--                        <a class="page-link" href="#">${p}</a>-->
<#--                    </li>-->
<#--                <#elseif p == -1>-->
<#--                    <li class="page-item active">-->
<#--                        <a class="page-link" href="#">...</a>-->
<#--                    </li>-->
<#--                <#else>-->
<#--                    <li class="page-item">-->
<#--                        <a class="page-link" href="${url}?page=${p - 1}&size=${page.getSize()}">${p}</a>-->
<#--                    </li>-->
<#--                </#if>-->
<#--            </#list>-->
<#--            <li class="page-item">-->
<#--                <a class="page-link" href="#" aria-label="Next">-->
<#--                    <span aria-hidden="true">&raquo;</span>-->
<#--                </a>-->
<#--            </li>-->
<#--        </ul>-->

<#--        <ul class="pagination">-->
<#--            <li class="page-item">-->
<#--                <a class="page-link" aria-label="Previous">-->
<#--                    Elements on the page-->
<#--                </a>-->
<#--            </li>-->
<#--            <#list [5, 10, 25, 50] as c>-->
<#--                <#if c == page.getSize()>-->
<#--                    <li class="page-item active">-->
<#--                        <a class="page-link" href="#">${c}</a>-->
<#--                    </li>-->
<#--                <#else>-->
<#--                    <li class="page-item">-->
<#--                        <a class="page-link" href="${url}?page=${page.getNumber()}&size=${c}">${c}</a>-->
<#--                    </li>-->
<#--                </#if>-->
<#--            </#list>-->

<#--        </ul>-->
<#--    </div>-->
<#--</#macro>-->