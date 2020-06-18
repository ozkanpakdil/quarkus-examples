<!DOCTYPE html>
<html>
<head>
  <title>${title}</title>
</head>
<body>

<textarea style="width: 844px; height: 196px;">
${request.headers()}
</textarea>

  <h1>${title}</h1>

  <p>${exampleObject.name} by ${exampleObject.developer}</p>

  <ul>
    <#list systems as system>
      <li>${system_index + 1}. ${system.name} from ${system.developer}</li>
    </#list>
  </ul>

</body>
</html>