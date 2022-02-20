### es_nba_search_demo

使用ElasticSearch7对nba球员数据进行搜索的demo，数据从网上爬的，保存在我的postgresql数据库中。

代码的内容包含如下几部分：

- 读取postgresql数据库，将NBA球员数据存储到ElasticSearch中。
- ElasticSearch文档（球员信息）的增删改查
- 进一步复杂一点的查询，如 精确查询、模糊查询、查询结果分页、排序、组合查询、范围查询等。


#### 数据库表
数据存储在PostgreSQL中，数据表如下：
```sql
CREATE TABLE public.nba_players (
	pid SERIAL  NOT NULL,
	cname VARCHAR(64) NOT NULL,
	ename VARCHAR(128) NOT NULL,
	team_name VARCHAR(64) ,
	birthday DATE,
	career INTEGER,
	height INTEGER,
    uniform_number INTEGER,
	position VARCHAR(64),
	salary INTEGER,
	weight REAL,
	PRIMARY KEY ("pid")
);
```

#### 索引的mapping

在ElasticSearch中创建了名为nba的索引，索引的mapping如下：

```json
{
"properties" : {
        "birthday" : {
          "type" : "date"
        },
        "career" : {
          "type" : "integer"
        },
        "cname" : {
          "type" : "text"
        },
        "ename" : {
          "type" : "text"
        },
        "height" : {
          "type" : "integer"
        },
        "pid" : {
          "type" : "integer"
        },
        "position" : {
          "type" : "text"
        },
        "salary" : {
          "type" : "integer"
        },
        "team_name" : {
          "type" : "keyword"
        },
        "uniform_number" : {
          "type" : "integer"
        },
        "weight" : {
          "type" : "integer"
        }
      }
}
```