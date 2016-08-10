This endpoint accepts and returns the following content types:

- application/json
- application/xml

Create show
-----------

Url:

```apib
/shows
```

Body parameters:

| Parameter     | Required | Description         |
|:--------------|:--------:|:--------------------|
| **name**      | yes      | Show's name.        |
| **brief**     | yes      | Show's description. |
| **duration**  | yes      | Show's duration.    |

Response codes:

| Code                              | Description                                                                |
|:----------------------------------|:---------------------------------------------------------------------------|
| HTTP 200 (OK)                     | When add operation ends successfully.                                      |

Sample request:

```bash
curl -H "Accept: application/json" -H "Content-Type: application/json" -X POST "http://localhost:8080/shows" -d '{"name": "Myth Busters", "brief": "Test brief", "duration": 45}'
```

Sample response:

```json
{
    "brief": "Test brief",
    "duration": 45,
    "name": "Myth Busters"
}
```

Retrieve shows
--------------

Url:

```apib
/shows
```

Response codes:

| Code                              | Description                                                                |
|:----------------------------------|:---------------------------------------------------------------------------|
| HTTP 200 (OK)                     | When add operation ends successfully.                                      |

Sample request:

```bash
curl -H "Accept: application/json" -H "Content-Type: application/json" -X GET "http://localhost:8080/shows"
```

Sample response:

```json
[
    {
        "brief": "Everyday weather forecast.",
        "duration": 10,
        "name": "Weather Forecast"
    },
    {
        "brief": "Popular entertainment program.",
        "duration": 25,
        "name": "Melody Trivia"
    },
    {
        "brief": "Group of buddies goes through massive mayhem.",
        "duration": 25,
        "name": "Friends"
    }
]
```

Retrieve show
-------------

Url:

```apib
/shows/{id}
```

Query parameters:

| Parameter | Description                              |
|:----------|:-----------------------------------------|
| **id**    | An ID of the show that will be returned. |

Response codes:

| Code                              | Description                                                                |
|:----------------------------------|:---------------------------------------------------------------------------|
| HTTP 200 (OK)                     | When add operation ends successfully.                                      |
| HTTP 404 (NOT FOUND)              | If show at given <i>id</i> not exist.                                      |

Sample request:

```bash
curl -H "Accept: application/json" -H "Content-Type: application/json" -X GET "http://localhost:8080/shows/2"
```

Sample response:

```json
{
    "brief": "Popular entertainment program.",
    "duration": 25,
    "name": "Melody Trivia"
}
```

Update show
-----------

Url:

```apib
/shows/{id}
```

Query parameters:

| Parameter | Description                              |
|:----------|:-----------------------------------------|
| **id**    | An ID of the show that will be updated.  |

Body parameters:

| Parameter     | Required | Description         |
|:--------------|:--------:|:--------------------|
| **name**      | no*      | Show's name.        |
| **bief**      | no*      | Show's description. |
| **duration**  | no*      | Show's duration.    |

\* Although all parameters are optional, you have to provide at least one of them.

Response codes:

| Code                              | Description                                                                |
|:----------------------------------|:---------------------------------------------------------------------------|
| HTTP 200 (OK)                     | When update operation ends successfully.                                   |
| HTTP 400 (BAD REQUEST)            | If none of optional parameters will be provided.                           |
| HTTP 404 (NOT FOUND)              | If show at given <i>id</i> not exist.                                      |

Sample request:

```bash
curl -H "Accept: application/json" -H "Content-Type: application/json" -X PATCH -d '{"duration": 55}' "http://localhost:8080/shows/8"
```

Sample response:
```json
{
    "brief": "Test brief",
    "duration": 55,
    "id": 8,
    "name": "Myth Busters"
}
```

Delete show
-----------

Url:

```apib
/shows/{id}
```

Query parameters:

| Parameter | Description                              |
|:----------|:-----------------------------------------|
| **id**    | An ID of the show that will be deleted.  |

Response codes:

| Code                              | Description                                                            |
|:----------------------------------|:-----------------------------------------------------------------------|
| HTTP 204 (NO CONTENT)             | Returned always. Hence, if show at given <i>id</i> already not exists. |

Sample request:

```bash
curl -X DELETE "http://localhost:8080/shows/1"
```

No body will be returned in response.
