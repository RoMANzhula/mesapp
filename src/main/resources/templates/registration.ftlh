<#import "blocks/common.ftlh" as c>
<#import "blocks/login.ftlh" as l>


<@c.page>
<h4 class="container text-center">Registration</h4>
${message?ifExists}
<@l.login "/registration" true />
</@c.page>