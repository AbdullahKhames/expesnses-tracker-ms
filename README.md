# tikgik-expenses

# docker commands
docker run --name accountsDb -e POSTGRES_USER=dev -e POSTGRES_PASSWORD=123 -p 5432:5432 -v accountdb-data:/var/lib/postgresql/data -v D:\dev\demos\expesnses-tracker-ms\account\init.sql:/docker-entrypoint-initdb.d/init.sql -d postgres
---
docker run --name expensessDb -e POSTGRES_USER=dev -e POSTGRES_PASSWORD=123 -p 5433:5432 -v expensesdb-data:/var/lib/postgresql/data -v D:\dev\demos\expesnses-tracker-ms\expense\init.sql:/docker-entrypoint-initdb.d/init.sql -d postgres
---
docker run --name transactionsDb -e POSTGRES_USER=dev -e POSTGRES_PASSWORD=123 -p 5434:5432 -v transactiondb-data:/var/lib/postgresql/data -v D:\dev\demos\expesnses-tracker-ms\transaction\init.sql:/docker-entrypoint-initdb.d/init.sql -d postgres
---
docker run -d -p 6379:6379 --name tikgikRedis  -v redisdb-data:/data redis redis-server --appendonly yes
---

[//]: # (docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management)
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 -v rabbitmq_data:/var/lib/rabbitmq rabbitmq:4.0-management
---
download the apache httpd to load test the apis with ab.exe from this link
https://www.apachelounge.com/download/

ab.exe -n 10 -c 2 -v 3 http://localhost:8072/tikgik/transaction/api/v1/transactions/contact
---
# google jib command
mvn compile jib:dockerBuild
---