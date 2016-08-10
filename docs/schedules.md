This endpoint accepts and returns the following content types:

- application/json
- application/xml

Schedule program
----------------

Url:

```apib
/stations/{id}/schedule
```

Query parameters:

| Parameter | Description                                           |
|:----------|:------------------------------------------------------|
| **id**    | An ID of the station which schedule will be modified. |

Body parameters:

| Parameter      | Required | Description                           |
|:---------------|:--------:|:--------------------------------------|
| **show_id**    | yes      | An ID of show that will be scheduled. |
| **start_time** | yes      | A starting time for given program.    |

Response codes:

| Code                              | Description                                                                |
|:----------------------------------|:---------------------------------------------------------------------------|
| HTTP 201 (CREATED)                | When schedule operation ends successfully.                                 |
| HTTP 400 (BAD REQUEST)            | If any of required parameters will be absent in request body.              |
| HTTP 404 (NOT FOUND)              | When station at given ID not exist.                                        |

Sample request:

```bash
curl -H "Content-Type: application/json" -H "Accept: application/json" -X POST -d '{"show_id": 1, "start_time": 1463510400000}' "http://localhost:8080/stations/1/schedule"
```

Sample response:

```json
{
    "brief": "Everyday weather forecast.",
    "duration": 10,
    "name": "Weather Forecast",
    "start_time": 1463510400000,
    "time_left": 0,
    "time_passed": 0
}
```

Re-schedule program
-------------------

Url:

```apib
/stations/{id}/schedule
```

Query parameters:

| Parameter | Description                                           |
|:----------|:------------------------------------------------------|
| **id**    | An ID of the station which schedule will be modified. |

Body parameters:

| Parameter        | Required | Description                                                     |
|:-----------------|:--------:|:----------------------------------------------------------------|
| **current_time** | yes      | Current starting time that identifies program in given station. |
| **show_id**      | no*      | Defines a show that will be scheduled.                          |
| **new_time**     | no*      | Defines new starting time.                                      |

\* Although parameters are optional, you have to provide at least one of them.

Response codes:

| Code                              | Description                                                              |
|:----------------------------------|:-------------------------------------------------------------------------|
| HTTP 200 (OK)                     | When reschedule operation ends successfully.                             |
| HTTP 400 (BAD REQUEST)            | If any of required parameters will be absent in request body.<br> When program at given <i>current_time</i> is not scheduled in station at given <i>id</i>. |
| HTTP 404 (NOT FOUND)              | When station at given ID not exist.                                        |

Sample request:

```bash
curl -H "Content-Type: application/json" -H "Accept: application/json" -X PATCH -d '{"current_time": 1463510400000, "show_id": 3}' "http://localhost:8080/stations/1/schedule"
```

Sample response:

```json
{
    "brief": "Group of buddies goes through massive mayhem.",
    "duration": 25,
    "name": "Friends",
    "start_time": 1463510400000,
    "time_left": 0,
    "time_passed": 0
}
```

Unschedule program
------------------

Url:

```apib
/stations/{id}/schedule
```

Query parameters:

| Parameter | Description                                           |
|:----------|:------------------------------------------------------|
| **id**    | An ID of the station which schedule will be modified. |

Body parameters:

| Parameter        | Required | Description                                                     |
|:-----------------|:--------:|:----------------------------------------------------------------|
| **current_time** | yes      | Current starting time that identifies program in given station. |

Response codes:

| Code                              | Description                                                                |
|:----------------------------------|:---------------------------------------------------------------------------|
| HTTP 204 (NO CONTENT)             | When unschedule operation ends successfully.                               |
| HTTP 400 (BAD REQUEST)            | When program at given <i>current_time</i> is not scheduled in station at given <i>id</i>. |
| HTTP 404 (NOT FOUND)              | When station at given ID or program at given credentials not exists.       |

Sample request:

```bash
curl -H "Content-Type: application/json" -H "Accept: application/json" -X DELETE -d '{"current_time": 1463497200000}' "http://localhost:8080/stations/1/schedule"
```

No body will be returned in response.
