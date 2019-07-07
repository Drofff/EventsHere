create database if not exists events;
create table if not exists user_info ( id serial primary key, username varchar not null, password varchar not null, active boolean );
create table if not exists user_role ( user_id bigint not null, role varchar not null, PRIMARY KEY (user_id, role));
create table if not exists session_token (user_id bigint not null, token varchar not null, PRIMARY KEY(user_id, token));
create table if not exists events ( id serial primary key, name varchar not null, description varchar not null, address varchar, city varchar, owner_id bigint not null, photo_url varchar, date_time timestamp );
create table if not exists event_likes (event_id bigint not null, profile_id bigint not null, PRIMARY KEY (event_id, profile_id));
create table if not exists event_members (event_id bigint not null, profile_id bigint not null, PRIMARY KEY (event_id, profile_id));
create table if not exists event_tags (event_id bigint not null, tag_id bigint not null, PRIMARY KEY(event_id, tag_id));
create table if not exists hashtag ( id serial primary key, name varchar not null );
create table if not exists profile ( id serial primary key, first_name varchar not null, last_name varchar not null, user_id bigint not null, photo_url varchar, phone_number varchar, status varchar, notify_me boolean );
create table if not exists subscription_info (subscriber_id bigint not null, channel_id bigint not null, PRIMARY KEY(subscriber_id, channel_id));

insert into user_info ( username, password, active ) values ('admin@gmail.com', 'MTIzNDU2', true);
insert into user_role ( user_id, role ) values (1, 'ADMIN');

insert into profile ( first_name, last_name, user_id, photo_url, phone_number, status, notify_me) values ( 'Mykhailo', 'Palahuta', 1, 'https://scontent-lht6-1.cdninstagram.com/vp/bdda48f50569d0e4d53a5ad0046820ad/5D9DBAC5/t51.2885-15/e35/54513294_124651898638902_2486136797481288242_n.jpg?_nc_ht=scontent-lht6-1.cdninstagram.com&se=8&ig_cache_key=MjAwNjc4OTA4NzY4NTQwOTY2Nw%3D%3D.2', '380662859459', 'Owner of EventsHere', false);

insert into events ( name, description, owner_id, photo_url, date_time ) values ( 'DevOps meeting', 'Nice event. Address: Avenue 55', 1, '', '2019-07-05 23:08:12');

insert into events ( name, description, owner_id, photo_url, date_time ) values ( 'Java meeting', 'The best event. Address: Avenue 55', 1, '', '2019-07-06 23:08:12');