<#import "blocks/common.ftlh" as c>
<#import "blocks/login.ftlh" as l>

<@c.page>
    <h5 class="container text-center">Login page</h5>
    <!-- для виводу помилок при валідації полів які є в Entity -->
    <#if SPRING_SECURITY_LAST_EXCEPTION??>
        <div class="alert alert-danger" role="alert">
            ${SPRING_SECURITY_LAST_EXCEPTION.message}
        </div>
    </#if>

    <!-- для виводу помилки при валідації в контролері -->
    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>
    <@l.login "/login" false />
</@c.page>