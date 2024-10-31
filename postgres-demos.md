ConcDistSys demos using PostgreSQL
==================================

[Install PostgreSQL and create a database](https://www.postgresql.org/docs/current/tutorial-start.html).
I did this (on macOS):

```shell
brew install postgresql
initdb --locale=C -E UTF-8 /usr/local/var/postgresql@14
createdb martin
/usr/local/opt/postgresql@14/bin/postgres -D /usr/local/var/postgresql@14
```

Deadlock detection
------------------

Run the following in `psql`:

```sql
create table bank_accounts (owner text primary key, balance decimal(10,2) not null);
insert into bank_accounts (owner, balance) values ('alice', 100), ('bob', 100);
select * from bank_accounts;
begin;
update bank_accounts set balance = balance - 10 where owner = 'alice';
update bank_accounts set balance = balance + 10 where owner = 'bob';
commit;
select * from bank_accounts;
```

Now open a second terminal, and perform two concurrent transactions -- one in which Alice transfers
money to Bob, and another one vice versa. In each terminal, first run the `begin` and then the
first `update`. Then run the second `update` in one of the terminals, which should block. Then run
the second `update` in the other terminal, which should fail and abort that transaction.

Transaction atomicity
---------------------

Run the following in `psql`:

```sql
select * from bank_accounts;
begin;
update bank_accounts set balance = balance - 10 where owner = 'alice';
select * from bank_accounts;
```

In a separate terminal, run `ps aux | grep postgres` to find the server process, and kill it with
`kill -9`. Try running something in `psql`, but it has disconnected, so `psql` needs to be
restarted. On restart, the transaction has been rolled back.

Serializable isolation in Postgres
----------------------------------

Postgres implements serializability using an optimistic algorithm (serializable snapshot isolation).

Run two separate `psql` sessions. First make an update outside of a transaction, and show that it
immediately shows up in the other window. Then start a transaction in one window and make an update,
but don't commit yet, then query in the other to demonstrate snapshot isolation.

To demonstrate serializability, run this in two windows:

```sql
begin;
set transaction isolation level serializable;
select * from bank_accounts;
insert into bank_accounts (owner, balance) values('carol', 10); -- in one terminal
insert into bank_accounts (owner, balance) values('dave', 20);  -- in the other
select * from bank_accounts;
commit;
```

Serializable isolation in MySQL
-------------------------------

It's interesting to compare with MySQL, which uses strict 2PL for serializable transactions.
I installed it like this:

```shell
brew install mysql
/usr/local/opt/mysql/bin/mysqld_safe --datadir=/usr/local/var/mysql
echo 'create database test;' | mysql -u root
```

Then run `mysql -u root test` and enter the following:

```sql
create table bank_accounts (owner varchar(50) primary key, balance decimal(10,2) not null) engine=innodb;
insert into bank_accounts (owner, balance) values ('alice', 100), ('bob', 100);
set session transaction isolation level serializable;
begin;
update bank_accounts set balance = balance - 10 where owner = 'alice';
update bank_accounts set balance = balance + 10 where owner = 'bob';
```

In another terminal running `mysql -u root test` side-by-side:

```sql
set session transaction isolation level serializable;
begin;
select balance from bank_accounts where owner = 'alice'; -- blocks until the other transaction commits
select balance from bank_accounts where owner = 'bob';
```

However, two read-only transactions can proceed concurrently, since MySQL uses a MRSW lock on each row.

This also applies to reading a whole table, for example:

```sql
set session transaction isolation level serializable;
begin;
select * from bank_accounts; -- one terminal
insert into bank_accounts (owner, balance) values('carol', 10); -- other terminal, blocks
commit; -- both
```

That insert blocks, since the `select *` in the other window has taken a MRSW lock on the whole table.

Next see what happens if both transactions first read the entire table, and then insert:

```sql
set session transaction isolation level serializable;
begin;
select * from bank_accounts; -- both terminals
insert into bank_accounts (owner, balance) values('carol', 10); -- one terminal, blocks
insert into bank_accounts (owner, balance) values('dave', 10); -- other terminal, triggers deadlock detector
```
