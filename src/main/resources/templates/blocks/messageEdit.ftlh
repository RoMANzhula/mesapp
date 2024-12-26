<a class="btn btn-dark container text-right" data-bs-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
   aria-controls="collapseExample">
    Create or edit message
</a>

<div class="collapse <#if message??>show</#if>" id="collapseExample">
    <div class="form-group">
        <form method="post" enctype="multipart/form-data">
            <div class="input-group mt-3">
                <input type="text" class="form-control ${(textError??)?string('is-invalid', '')}" name="text"
                       placeholder="Input message" value="<#if message??>${message.text}</#if>" />
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </div>
            <div class="input-group mt-3">
                <input type="tag" class="form-control" name="tag" placeholder="Input tag"
                       value="<#if message??>${message.tag}</#if>" />
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>
            <div class="input-group mt-3">
                <div class="input-group mb-3">
                    <input type="file" name="file" class="form-control" />
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <input type="hidden" name="id" value="<#if message??>${message.id}</#if>" />
            <div class="input-group ">
                <button type="submit" class="btn btn-dark container text-right">Save message</button>
            </div>
        </form>
    </div>
</div>
