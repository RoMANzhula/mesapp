<#include "security.ftlh">
<#import "login.ftlh" as l>

<nav class="navbar navbar-expand-lg bg-body-tertiary" data-bs-theme="dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Mesapp</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="/">Home</a>
                </li>
                <#if user??>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/main">Messages</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/user-messages/${currentUserId}">My messages</a>
                    </li>
                </#if>
                <#if isAdmin>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/user">User list</a>
                    </li>
                </#if>
                <#if user??> <!-- якщо юзер авторизован -->
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/user/profile">Profile</a>
                    </li>
                </#if>
            </ul>

            <div class="navbar-text me-3"><#if user??>${name}<#else>Login or registration</#if></div>

            <@l.logout />
        </div>
    </div>
</nav>
