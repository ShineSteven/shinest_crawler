## movie crawler
爬[box office mojo](http://www.boxofficemojo.com/daily/chart/?view=1day&sortdate=2016-06-28&p=.htm)每日美國電影票房，存到Elastic search。

## 安裝工具
* elastic search 2.3.3

## package
打包bug排除中，目前只能import to Intelllij IDE run

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
