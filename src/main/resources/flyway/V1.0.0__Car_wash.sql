create table OWNERS
(
    ID           serial primary key,
    NAME         varchar(100) not null,
    PHONE_NUMBER varchar(100) not null,
    EMAIL        varchar(100) not null
);
create table CARS
(
    ID            serial primary key,
    MODEL         varchar(100) not null,
    YEAR          varchar(100) not null,
    LICENSE_PLATE varchar(100) not null,
    OWNER_ID      int          not null,
    constraint FK_OWNERS foreign key (OWNER_ID) references OWNERS (ID)
);
create table USERS
(
    ID       serial primary key,
    USERNAME varchar(100) not null,
    PASSWORD varchar(100) not null,
    ROLE     varchar(50)  not null
);