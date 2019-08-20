# My Image Scraper

## Introduction

The project has following components that are loosely coupled:

*  Html/JavaScript based front-end making ajax calls to restful APIs
* Python based scrapper service consuming a message queue
* Spring Boot based APIs that handle CRUD with Database
* PostgreSQL and RabbitMQ services encapsulated in docker images

## To Use

- #### Front-end

```
user@ubuntu:~/my-scraper$ cd web-dev/
user@ubuntu:~/my-scraper/web-dev$ chmod +x runserver.sh
user@ubuntu:~/my-scraper/web-dev$ ./runserver.sh 
Serving HTTP on 0.0.0.0 port 8088 ...
```

- #### Database and Message Queue

```
user@ubuntu:~/my-scraper$ cd RabbitMQ/
user@ubuntu:~/my-scraper/RabbitMQ$ docker-compose up -d
```

```
user@ubuntu:~/my-scraper$ cd PostgreSQL/
user@ubuntu:~/my-scraper/PostgreSQL$ docker build -t "my-postgres" .
user@ubuntu:~/my-scraper/PostgreSQL$ docker run --name postgres1 -p 5432:5432 -d my-postgres
```

- #### Spring Boot based RESTful APIs

```
user@ubuntu:~/my-scraper$ gradle run
```

- #### Python Scrapper Service

```
user@ubuntu:~/my-scraper$ cd python2-scrapper/src
user@ubuntu:~/python2-scrapper/src$ python2 worker.py
```

## RESTful CRUD APIs

| METHODs | URL                              | Payload              | description              |
| ------- | -------------------------------- | -------------------- | ------------------------ |
| GET     | http://localhost:8080/scrapes    | N/A                  | Load an array of scrapes |
| DELETE  | http://localhost:8080/scrapes/id | N/A                  | Delete a scrape          |
| POST    | http://localhost:8080/scrapes    | data: {url: newUrl } | Add a scrape             |
| PATCH   | http://localhost:8080/scrapes/id | data: {url: newUrl } | Update a scrape          |

Using the URL:

GET `http://localhost:8080/scrapes/`

Will return the following result:

```
{
    "_embedded": {
        "scrapes": [
            {
                "url": "https://www.ebay.com",
                "summary": "Largest Image File: https://i.ebayimg.com/images/g/XbUAAOSwIipdCmHV/s-l1600.jpg Sized: 69027",
                "created": "2019-07-01T07:53:40.887+0000",
                "status": "Success",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/scrapes/1"
                    },
                    "scrape": {
                        "href": "http://localhost:8080/scrapes/1"
                    }
                }
            },
            {
                "url": "https://jd.com",
                "summary": "Scrape Failed. Reason: max() arg is an empty sequence",
                "created": "2019-07-01T08:28:03.774+0000",
                "status": "Failure",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/scrapes/4"
                    },
                    "scrape": {
                        "href": "http://localhost:8080/scrapes/4"
                    }
                }
            },
            {
                "url": "https://www.nytimes.com",
                "summary": "Largest Image File: https://static01.nyt.com/images/2019/07/01/world/01hk-briefing-top/01hk-briefing-top-videoSixteenByNine3000.jpg Sized: 1274759",
                "created": "2019-07-01T08:28:58.485+0000",
                "status": "Success",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/scrapes/5"
                    },
                    "scrape": {
                        "href": "http://localhost:8080/scrapes/5"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/scrapes"
        },
        "profile": {
            "href": "http://localhost:8080/profile/scrapes"
        },
        "search": {
            "href": "http://localhost:8080/scrapes/search"
        }
    }
}
```

## Scrapped Images

![](https://harolz.com/assets/img/scrapped.png)