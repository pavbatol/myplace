create sequence if not exists seq_countries minvalue 1 start with 1 increment 1;
create table if not exists countries
(
    country_id  bigint default nextval('seq_countries')     not null,
    code        varchar(2)                                  not null,
    name        varchar(150)                                not null,
    constraint pk_countries primary key (country_id),
    constraint uq_countries_name unique (name)
);

create sequence if not exists seq_regions minvalue 1 start with 1 increment 1;
create table if not exists regions
(
    region_id    bigint default nextval('seq_regions')       not null,
    country_id   bigint                                      not null,
    name         varchar(255)                                not null,
    constraint pk_regions primary key (region_id),
    constraint fk_regions_country_id foreign key (country_id) references countries (country_id) on delete cascade,
    constraint uq_regions_country_id_name unique (country_id, name)
);

create sequence if not exists seq_districts minvalue 1 start with 1 increment 1;
create table if not exists districts
(
    district_id  bigint default nextval('seq_districts')     not null,
    region_id    bigint                                      not null,
    name         varchar(255)                                not null,
    constraint pk_districts primary key (district_id),
    constraint fk_districts_region_id foreign key (region_id) references regions (region_id) on delete cascade,
    constraint uq_districts_region_id_name unique (region_id, name)
);

create sequence if not exists seq_cities minvalue 1 start with 1 increment 1;
create table if not exists cities
(
    city_id      bigint default nextval('seq_cities')        not null,
    district_id  bigint                                      not null,
    name         varchar(150)                                not null,
    constraint pk_cities primary key (city_id),
    constraint fk_cities_district_id foreign key (district_id) references districts (district_id) on delete cascade,
    constraint uq_cities_district_id_name unique (district_id, name)
);

create sequence if not exists seq_streets minvalue 1 start with 1 increment 1;
create table if not exists streets
(
    street_id      bigint default nextval('seq_streets')        not null,
    city_id        bigint                                       not null,
    name           varchar(150)                                 not null,
    constraint pk_streets primary key (street_id),
    constraint fk_streets_city_id foreign key (city_id) references cities (city_id) on delete cascade,
    constraint uq_streets_city_id_name unique (city_id, name)
);

create sequence if not exists seq_houses minvalue 1 start with 1 increment 1;
create table if not exists houses
(
    house_id     bigint default nextval('seq_houses')           not null,
    street_id    bigint                                         not null,
    number       varchar(10)                                    not null,
    lat          double precision                                       ,
    lon          double precision                                       ,
    constraint pk_houses primary key (house_id),
    constraint fk_houses_street_id foreign key (street_id) references streets (street_id) on delete cascade,
    constraint uq_hoses_street_id_number unique (street_id, number)
);

create sequence if not exists seq_profiles minvalue 1 start with 1 increment 1;
create table if not exists profiles
(
    profile_id              bigint default nextval('seq_profiles')      not null,
    user_id                 bigint                                      not null,
    email                   varchar(255)                                not null,
    trusted_email           varchar(255)                                        ,
    mobile_number           varchar(12)                                         ,
    trusted_mobile_number   varchar(12)                                         ,
    first_name              varchar(100)                                        ,
    second_name             varchar(100)                                        ,
    birthday                timestamp without time zone                         ,
    gender                  varchar(50)                                         ,
    house_id                bigint                                              ,
    apartment               varchar(10)                                         ,
    avatar                  bytea                                               ,
    status                  varchar(15)                                 not null,
    created_on              timestamp without time zone default now()   not null,
    constraint pk_profiles primary key (profile_id),
    constraint fk_profiles_house_id foreign key (house_id) references houses (house_id),
    constraint uq_profiles_user_id unique (user_id),
    constraint uq_profiles_email unique (email),
    constraint uq_profiles_mobile_number unique (mobile_number)
);
