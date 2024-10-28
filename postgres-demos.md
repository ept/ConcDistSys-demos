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
