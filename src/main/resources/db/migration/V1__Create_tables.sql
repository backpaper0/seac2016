create table Category (
    id identity primary key
    ,name varchar(255)
);

create table Issue (
    id identity primary key
    ,content varchar(255)
    ,categoryId bigint
    ,foreign key (categoryId) references Category (id)
);

