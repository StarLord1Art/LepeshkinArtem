# Задание 1
## Вывести количество пользователей, которые не создали ни одного поста.

### Код:
```
SELECT COUNT(*) FROM (SELECT profile_id FROM profile 
EXCEPT
SELECT DISTINCT profile_id FROM post) AS X
```

### Результат:
| |count|
|-|------|
|1|5|

# Задание 2
## Вывести по возрастанию ID всех постов, у которых 2 комментария, title начинается с цифры, а длина content больше 20 (все три условия должны соблюдаться одновременно).

### Код:
```
SELECT post_id FROM post WHERE
(title LIKE '0%' OR title LIKE '1%' OR title LIKE '2%' OR title LIKE '3%' OR title LIKE '4%' OR title LIKE '5%' 
OR title LIKE '6%' OR title LIKE '7%' OR title LIKE '8%' OR title LIKE '9%')
INTERSECT
SELECT DISTINCT post_id FROM comment GROUP BY (post_id) HAVING COUNT(*) = 2
INTERSECT SELECT post_id FROM post WHERE LENGTH(content) > 20 
ORDER BY (post_id)
```

### Результат:
| |post_id|
|-|-------|
|1|22|
|2|24|
|3|26|
|4|28|
|5|32|
|6|34|
|7|36|
|8|38|
|9|42|
|10|44|

# Задание 3
## Вывести по возрастанию ID первых 10 постов, у которых либо нет комментариев, либо он один.

### Код:
```
SELECT post_id from comment GROUP BY (post_id) HAVING COUNT(*) <= 1
ORDER BY (post_id)
LIMIT 10
```

### Результат:
| |post_id|
|-|-------|
|1|1|
|2|3|
|3|5|
|4|7|
|5|9|
|6|11|
|7|13|
|8|15|
|9|17|
|10|19|
