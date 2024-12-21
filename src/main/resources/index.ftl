<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Главная страница</title>
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/gh/yegor256/tacit@gh-pages/tacit-css-1.6.0.min.css"/>
</head>

<body>

<h1>Список статей</h1>
<table>
  <tr>
    <th>Название</th>
    <th>количество комментариев</th>
  </tr>
    <#list articles as article>
      <tr>
        <td>${article.name}</td>
        <td>${article.countOfComments}</td>
      </tr>
    </#list>
</table>

</body>

</html>