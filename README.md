# movie crawler
用 akka + scala 爬[box office mojo](http://www.boxofficemojo.com/daily/chart/?view=1day&sortdate=2016-06-28&p=.htm) 美國電影票房，存到 Elasticsearch。

# pre-install
* sbt
* elastic search 5.5.2
* 我的 common library [shinest common](https://github.com/shine-st/common)


# run
## parameter
```
## whole year on 2016
sbt "run Y 2016"

## year range at 2016 ~ 2017
sbt "run YR 2016 2017"

## weekly on 2017W15
sbt "run W 2017-15"

## weekly and daily on 2017W15
sbt "run WD 2017-15"

## weekly range at  2017W1 2017W15
sbt "run WR 2017 1 2017 15"

## weekly and daily range at  2017W1 2017W15
sbt "run WRD 2017 1 2017 15"

## daily on 2017-10-04
sbt "run D 2017-10-04"
```

# box_office index schema
```
PUT box_office
{
  "settings": {
    "index": {
      "query.default_field": "movie_searchable"
    }
  },
  "mappings": {
    "movie": {
      "_all": {
        "enabled": false
      },
      "properties": {
        "name": {
          "type": "text",
          "fields": {
            "raw": {
              "type": "keyword"
            }
          },
          "copy_to": "movie_searchable"
        },
        "MPAA": {
          "type": "keyword"
        },
        "genre": {
          "type": "keyword"
        },
        "duration": {
          "type": "integer"
        },
        "budget": {
          "type": "keyword"
        },
        "distributor": {
          "type": "keyword"
        },
        "release": {
          "type": "nested",
          "properties": {
            "release_date": {
              "type": "date"
            },
            "region_name": {
              "type": "keyword"
            }
          }
        },
        "update_at": {
          "type": "date"
        }
      }
    },
    "weekly": {
      "_parent": {
        "type": "movie"
      },
      "_all": {
        "enabled": false
      },
      "properties": {
        "rank":{
          "type": "short"
        },
        "year": {
          "type": "short"
        },
        "week": {
          "type": "short"
        },
        "on_release_week": {
          "type": "short"
        },
        "weekly_gross": {
          "type": "integer"
        },
        "weekday_gross": {
          "type": "integer"
        },
        "weekend_gross": {
          "type": "integer"
        },
        "theaters": {
          "type": "integer"
        },
        "region_name": {
          "type": "keyword"
        },
        "studio": {
          "type": "keyword"
        },
        "update_at": {
          "type": "date"
        }
      }
    },
    "daily": {
      "_parent": {
        "type": "movie"
      },
      "_all": {
        "enabled": false
      },
      "properties": {
        "rank":{
          "type": "short"
        },
        "date": {
          "type": "date"
        },
        "gross": {
          "type": "integer"
        },
        "theaters": {
          "type": "integer"
        },
        "region_name": {
          "type": "keyword"
        },
        "studio": {
          "type": "keyword"
        },
        "update_at": {
          "type": "date"
        }
      }
    }
  }
}

```
