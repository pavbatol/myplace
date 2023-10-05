create sequence if not exists seq_roles minvalue 1 start with 1 increment 1;
create table if not exists roles
(
    role_id bigint default nextval('seq_roles')     primary key not null,
    role_name varchar(50) unique                                not null
);

create sequence if not exists seq_users minvalue 1 start with 1 increment 1;
create table if not exists users
(
    user_id     bigint default nextval('seq_users')     not null,
    user_uuid   uuid                                    not null,
    login       varchar(255)                            not null,
    password    varchar(255)                            not null,
    deleted     boolean                                 not null,
    constraint pk_users primary key (user_id),
    constraint uq_users_uuid unique (user_uuid),
    constraint uq_users_login unique (login)
);

create table if not exists users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    constraint fk_users_roules_to_user foreign key (user_id) references users (user_id) on delete cascade,
    constraint fk_users_roules_to_role foreign key (role_id) references roles (role_id) on delete cascade,
    constraint pk_users_roles primary key (user_id, role_id)
);
