<#import "blocks/common.ftlh" as c>

<@c.page>
    List of users

    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr> <!-- string -->
                <td>${user.username}</td> <!-- column -->
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a href="/user/${user.id}">edit_role</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>