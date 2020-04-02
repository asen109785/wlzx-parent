<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
    <#--
        插值表达式:       ${}
        list指令:         <#list xxList as item></#list>
        if指令：          <#if></#if>
    -->
    当前人为: ${stu.name} <br>
    他的好朋友的数量为: ${stu.friends?size} <br>
    他的好朋友们为:
    <ul>
        <#list stu.friends as item>
           <li>
               ${item_index+1}-${item.name}-${item.age}-${item.money!0}
               <#if ((item_index+1)<stu.friends?size)>
                   ,
               </#if>
           </li>
        </#list>
    </ul>
    <hr>
    遍历map<br>
    <#list stuMap?keys as item>
        ${item}-${stuMap[item].name},
    </#list>

    <hr>

<#-- ??判断对象是否为空 -->
<#if stuFriends??>
   <#list stuFriends as item>
   </#list>
</#if>












</body>
</html>