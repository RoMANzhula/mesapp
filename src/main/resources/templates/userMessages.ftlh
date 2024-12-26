<#import "blocks/common.ftlh" as c>

<@c.page>
    <h3>${userChanel.username}</h3>
    <#if !isCurrentUser>
        <#if isSubscriber>
            <a class="btn btn-danger"
               style="--bs-btn-padding-y: .25rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .75rem;"
               href="/user/unsubscribe/${userChanel.id}">Unsubscribe</a>
        <#else>
            <a class="btn btn-dark"
               style="--bs-btn-padding-y: .25rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .75rem;"
               href="/user/subscribe/${userChanel.id}">Subscribe</a>
        </#if>
    </#if>
    <div class="container my-3">
        <div class="row">
            <div class="col">
                <div class="card text-center">
                    <div class="card-title">Subscriptions</div>
                    <h3 class="card-text mb-1">
                        <a href="/user/subscriptions/${userChanel.id}/list">${subscriptionsCount}</a>
                    </h3>
                </div>
            </div>
            <div class="col">
                <div class="card text-center">
                    <div class="card-title">Subscribers</div>
                    <h3 class="card-text mb-1">
                        <a href="/user/subscribers/${userChanel.id}/list">${subscribersCount}</a>
                    </h3>
                </div>
            </div>
        </div>
    </div>
    <#if isCurrentUser>
        <#include  "blocks/messageEdit.ftlh" />
    </#if>

    <#include "blocks/messageList.ftlh" />
</@c.page>