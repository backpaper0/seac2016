    select i.content
         , c.name as category
      from Issue i
inner join Category c
        on c.id = i.categoryId
