<#include "security.ftlh">
<#import "pagenator.ftlh" as p>

<@p.pager url!"", page!"" />

<div class="row row-cols-1 row-cols-md-3 g-4" id="message-list">
    <#list page.content as message>
        <div>
            <div class="card my-2" style="width: 18rem;" data-id="${message.id}">
                <div>
                    <#if message.filename??>
                        <img src="/img/${message.filename}" class="card-img-top" />
                    </#if>
                </div>
                <div class="m-2">
                    <span>${message.text}</span><br/>
                    <i>##${message.tag}</i>
                </div>
                <div class="card-footer">
                    <a href="/user-messages/${message.author.id}">${message.authorName}</a>
                    <#if message.author.id == currentUserId>
                        <a class="btn btn-dark"
                           style="--bs-btn-padding-y: .25rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .75rem;"
                           href="/user-messages/${message.author.id}?message=${message.id}">
                            Edit
                        </a>

                        <a class="col btn btn-outline-danger"
                           style="--bs-btn-padding-y: .25rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .75rem;"
                           href="/del-user-messages/${message.author.id}?message=${message.id}">
                            Delete
                        </a>
                    </#if>
                </div>
            </div>
        </div>
    <#else>
        <div class="mb-4">
            No message
        </div>
    </#list>
</div>

<@p.pager url!"", page!"" />