This endpoint accepts and returns the following content types:

- application/json
- application/xml

Retrieve programs
-----------------

Url:

```apib
/programs&time={time}
```

Query parameters:

| Parameter | Description                                                         |
|:----------|:--------------------------------------------------------------------|
| **time**  | Point in time related to which ongoing programs will be retrieved.  |

Sample request:

```bash
curl -H "Accept: application/json" -X GET "http://localhost:8080/programs?time=1463424600000"
```

Sample response:

```json
[
    {
        "brief": "Winter is coming.",
        "name": "Game of Thrones",
        "time_left": 45,
        "time_passed": 10
    },
    {
        "brief": "A skilled pizza delivery boy tries to work off his driving record.",
        "name": "Taxi",
        "time_left": 76,
        "time_passed": 10
    }
]
```
