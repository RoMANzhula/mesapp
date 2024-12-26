<#import "blocks/common.ftlh" as c>

<@c.page>
    <h5 class="container text-center">Profile page</h5>
    <h5>${username}</h5>
    ${message?ifExists}

    <form method="post">
        <div class="row g-3 align-items-center mb-3 mt-3">
            <div class="col-1">
                <span class="badge text-bg-dark">
                    <label for="inputPassword6" class="col-form-label">Password</label>
                </span>
            </div>
            <div class="col-sm-7">
                <input type="password" id="inputPassword6" class="form-control"
                       aria-describedby="passwordHelpInline" placeholder="Please input your password"
                       name="password"
                />
            </div>
        </div>

        <div class="row g-3 align-items-center mb-3 mt-3">
            <div class="col-1">
            <span class="badge text-bg-dark">
                <label for="inputPassword6" class="col-form-label">Email</label>
            </span>
            </div>
            <div class="col-sm-7">
                <input type="email" id="inputPassword6" class="form-control"
                       aria-describedby="passwordHelpInline" placeholder="Please input your email"
                       name="email" value="${email!''}"
                />
            </div>
        </div>

        <div class="container text-center" data-bs-theme="dark">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-dark col-2 mt-4">Save</button>
        </div>

    </form>
</@c.page>