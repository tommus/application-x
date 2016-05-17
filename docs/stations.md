This endpoint accepts and returns the following content types:

- application/json
- application/xml

Retrieve stations
-----------------

Url:

```apib
/stations
```

Sample request:

```bash
curl -H "Accept: application/json" -X GET "http://localhost:8080/stations"
```

Sample response:

```json
[
    {
        "id": 1,
        "name": "Foo"
    },
    {
        "id": 2,
        "name": "Bar"
    },
    {
        "id": 3,
        "name": "Ban"
    }
]
```

Retrieve station
----------------

Url:

```apib
/stations/{id}
```

Query parameters:

| Parameter | Description                                           |
|:----------|:------------------------------------------------------|
| **id**    | An ID of the station which details will be returned.  |

Sample request:

```bash
curl -H "Accept: application/json" -X GET "http://localhost:8080/stations/2"
```

Sample response:

```json
{
    "id": 2,
    "name": "Bar",
    "schedule": [
        {
            "brief": "Winter is coming.",
            "name": "Game of Thrones",
            "start_time": 1463424000000
        }
    ]
}
```
