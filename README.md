### es_nba_search_demo

使用ElasticSearch7对nba球员数据进行搜索的demo，数据从网上爬的，保存在我的postgresql数据库中。

代码的内容包含如下几部分：

- 读取postgresql数据库，将NBA球员数据存储到ElasticSearch中。
- ElasticSearch文档（球员信息）的增删改查
- 进一步复杂一点的查询，如 精确查询、模糊查询、查询结果分页、排序、组合查询、范围查询等。

没有写controller层代码，service层的方法通过写测试用例调用查看。