create sequence if not exists seq_countries minvalue 1 start with 1 increment 1;
create table if not exists countries
(
    country_id  bigint default nextval('seq_countries')     not null,
    code        varchar(2)                                  not null,
    name_en     varchar(150)                                not null,
    name_ru     varchar(150)                                not null,
    constraint pk_countries primary key (country_id),
    constraint uq_countries_name_en unique (name_en),
    constraint uq_countries_name_ru unique (name_ru)
);

create sequence if not exists seq_regions minvalue 1 start with 1 increment 1;
create table if not exists regions
(
    regions_id   bigint default nextval('seq_regions')       not null,
    country_id   bigint                                      not null,
    name_en      varchar(255)                                not null,
    name_ru      varchar(255)                                not null,
    constraint pk_regions primary key (regions_id),
    constraint uq_regions_country_id_name_en_name_ru unique (country_id, name_en, name_ru)
);

create sequence if not exists seq_provinces minvalue 1 start with 1 increment 1;
create table if not exists provinces
(
    province_id  bigint default nextval('seq_provinces')     not null,
    regions_id   bigint                                      not null,
    name_en      varchar(255)                                not null,
    name_ru      varchar(255)                                not null,
    constraint pk_provinces primary key (province_id),
    constraint uq_provinces_regions_id_name_en_name_ru unique (regions_id, name_en, name_ru)
);

create sequence if not exists seq_cities minvalue 1 start with 1 increment 1;
create table if not exists cities
(
    city_id      bigint default nextval('seq_cities')        not null,
    province_id  bigint                                      not null,
    name_en      varchar(150)                                not null,
    name_ru      varchar(150)                                not null,
    constraint pk_cities primary key (city_id),
    constraint uq_cities_province_id_name_en_name_ru unique (province_id, name_en, name_ru)
);

create sequence if not exists seq_streets minvalue 1 start with 1 increment 1;
create table if not exists streets
(
    street_id      bigint default nextval('seq_streets')        not null,
    city_id        bigint                                       not null,
    name_en        varchar(150)                                 not null,
    name_ru        varchar(150)                                 not null,
    lat            double precision                                     ,
    lon            double precision                                     ,
    constraint pk_streets primary key (street_id),
    constraint uq_streets_city_id_name_en_name_ru_lat_lon unique (city_id, name_en, name_ru, lat, lon)
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
    country_id              bigint                                              ,
    house_id                bigint                                              ,
    apartment               varchar(10)                                         ,
    avatar                  bytea                                               ,
    status                  varchar(15)                                 not null,
    created_on              timestamp without time zone default now()   not null,
    constraint pk_profiles primary key (profile_id),
    constraint uq_profiles_user_id unique (user_id),
    constraint uq_profiles_email unique (email),
    constraint uq_profiles_mobile_number unique (mobile_number)
);
