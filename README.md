## movie crawler
爬[box office mojo](http://www.boxofficemojo.com/daily/chart/?view=1day&sortdate=2016-06-28&p=.htm)每日美國電影票房，存到Elastic search。

## 安裝工具
* elastic search 2.3.3

## package
1. 須先install 我的[common](https://github.com/ShineSteven/shinest_common)程式

2. package crawler
```
./bin/activator clean assembly
```

## run
default 爬3天前資料，

也可接收日期參數, 格式yyyy-MM-dd
* start_date: 開始日
* end_date: 結束日，若日期大於3天前，則一律改為3天前
```
java -jar shinest_crawler-assembly-1.0.1.SNAPSHOT.jar

java -jar shinest_crawler-assembly-1.0.1.SNAPSHOT.jar 2016-07-21

java -jar shinest_crawler-assembly-1.0.1.SNAPSHOT.jar 2016-07-20 2016-07-22
```

## index schema
```
PUT movie_predict
{
  "mappings": {
    "movie": {
      "_all": {
        "enabled": false
      },
      "properties": {
        "movie_name": {
          "type": "string",
          "fields": {
            "raw": {
              "type": "string",
              "index": "not_analyzed"
            }
          }
        },
        "MPAA": {
          "type": "string",
          "index": "not_analyzed"
        },
        "genre": {
          "type": "string"
        },
        "runtime": {
          "type": "integer"
        },
        "budget": {
          "type": "integer"
        },
        "studio": {
          "type": "string"
        }
      }
    },
    "daily": {
      "properties": {
        "date": {
          "type": "date",
          "format": "yyy-MM-dd HH:mm:ss||epoch_millis"
        },
        "gross": {
          "type": "integer"
        },
        "movie": {
          "properties": {
            "id": {
              "type": "integer",
              "index": "not_analyzed"
            },
            "movie_name": {
              "type": "string",
              "fields": {
                "raw": {
                  "type": "string",
                  "index": "not_analyzed"
                }
              }
            }
          }
        },
        "theaters": {
          "type": "integer"
        },
        "region_name": {
          "type": "string",
          "index": "not_analyzed"
        }
      }
    },
    "release": {
      "properties": {
        "date": {
          "type": "date",
          "format": "yyy-MM-dd HH:mm:ss||epoch_millis"
        },
        "movie_id": {
          "type": "string",
          "index": "not_analyzed"
        },
        "region_name": {
          "type": "string",
          "index": "not_analyzed"
        }
      }
    }
  }
}
```
