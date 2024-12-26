<#import "blocks/common.ftlh" as c>

<@c.page>
    <form class="row g-3">
        <div class="col-md-4 d-flex mb-3">
            <form method="get" action="/main" class="d-flex">
                <input type="text" name="filter" class="form-control" value="${filter?ifExists}"
                       placeholder="Input tag for search message"
                />
                <button type="submit" class="btn btn-dark ms-3">Search</button>
            </form>
        </div>
    </form>

    <#include  "blocks/messageEdit.ftlh" />

    <#include "blocks/messageList.ftlh" />

</@c.page>