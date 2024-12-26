<#include "security.ftlh">

<#macro login path isRegistrForm>
    <form action="${path}" method="post">
        <div class="row g-3 align-items-center mb-3 mt-3">
            <div class="col-1 me-4">
                <span class="badge text-bg-dark">
                    <label for="inputUsername" class="col-form-label">User Name</label>
                </span>
            </div>
            <div class="col-sm-7">
                <input type="text" id="inputUsername" class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       aria-describedby="passwordHelpInline" placeholder="Please input your user name"
                       name="username" value="<#if user??>${user.username}</#if>"
                />
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>

        <div class="row g-3 align-items-center mb-3 mt-3">
            <div class="col-1 me-4">
                <span class="badge text-bg-dark">
                    <label for="inputPassword6" class="col-form-label">Password</label>
                </span>
            </div>
            <div class="col-sm-7">
                <input type="password" id="inputPassword6" class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       aria-describedby="passwordHelpInline" placeholder="Please input your password"
                       name="password"
                />
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>

        <#if isRegistrForm>
            <div class="row g-3 align-items-center mb-3 mt-3">
                <div class="col-1 me-4">
                <span class="badge text-bg-dark">
                    <label for="inputPassword6" class="col-form-label">Password Confirm</label>
                </span>
                </div>
                <div class="col-sm-7">
                    <input type="password" id="inputPassword6"
                           class="form-control ${(passwordConfirmError??)?string('is-invalid', '')}"
                           aria-describedby="passwordHelpInline" placeholder="Please input your password confirm"
                           name="passwordConfirm"
                    />
                    <#if passwordConfirmError??>
                        <div class="invalid-feedback">
                            ${passwordConfirmError}
                        </div>
                    </#if>
                </div>
            </div>

            <div class="row g-3 align-items-center mb-3 mt-3">
                <div class="col-1 me-4">
                    <span class="badge text-bg-dark">
                        <label for="inputEmail" class="col-form-label">Email</label>
                    </span>
                </div>
                <div class="col-sm-7">
                    <input type="email" id="inputEmail" value="<#if user??>${user.email}</#if>"
                           class="form-control ${(emailError??)?string('is-invalid', '')}"
                           placeholder="Please input your email" name="email"
                    />
                    <#if emailError??>
                        <div class="invalid-feedback">
                            ${emailError}
                        </div>
                    </#if>
                </div>
            </div>

            <div class="col-sm-3 container text-center">
                <div class="g-recaptcha" data-sitekey="6Lf2sJ8oAAAAAMwEneJkOI22woB4fYtqjZ2p31GL"></div>
                <#if reCaptchaError??>
                    <div class="alert alert-danger" role="alert">
                        ${reCaptchaError}
                    </div>
                </#if>
            </div>

        </#if>

        <div class="container text-center" data-bs-theme="dark">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-dark col-2 mt-4"><#if isRegistrForm>Create<#else>Sign in</#if></button>
        </div>

        <#if !isRegistrForm>
            <div class="container text-center mt-3">
                <a class="btn btn-info" href="/registration">Registration</a>
            </div>
        </#if>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-light"
                style="--bs-btn-padding-y: .25rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .75rem;"
                type="submit"><#if user??>Sign Out<#else>Log in</#if></button>
    </form>
</#macro>