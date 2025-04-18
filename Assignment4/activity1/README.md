# Assignment 4 Activity 1

## Description
The initial `Performer` code only supports adding strings to a list.  
This activity implements four operations over TCP/JSON:
- **Add** (selected = 1): Adds a new string to the list and returns the updated list.
- **Display** (selected = 3): Returns the full list of strings.
- **Count** (selected = 4): Returns the number of strings in the list.
- **Quit** (selected = 0): Closes the connection.

## Protocol

### Requests
```json
{
  "selected": <int: 1=add, 3=display, 4=count, 0=quit>,
  "data":     <depends on operation>
}
```
- **data**
   - For **add**: the string to add.
   - For **display** and **count**: send `""` (empty string).
   - For **quit**: ignored (can be `""` or omitted).

### Responses

- **Success**:
  ```json
  {
    "type": "<add|display|count|quit>",
    "data": <String|int|null>
  }
  ```
- **Error**:
  ```json
  {
    "type": "error",
    "message": "<error description>"
  }
  ```

## Gradle Tasks & Defaults

- **Default host**: `localhost`
- **Default port**: `8000`
- **Default maxThreads** (for Task 3): `4`

| Task       | Description                                  | Default arguments           |
|------------|----------------------------------------------|-----------------------------|
| `runTask1` | Single‑threaded `Server`                     | `port=8000`                 |
| `runTask2` | Unbounded multi‑threaded `ThreadedServer`    | `port=8000`                 |
| `runTask3` | Bounded thread‑pool `ThreadPoolServer`       | `port=8000`, `maxThreads=4` |
| `runClient`| Client connecting to `host:port`             | `host=localhost`, `port=8000` |

### Usage Examples

- **All defaults**
  ```bash
  gradle runTask1
  gradle runTask2
  gradle runTask3
  gradle runClient
  ```

- **Override port and threads**
  ```bash
  gradle runTask2 -Pport=9099
  gradle runTask3 -Pport=9099 -PmaxThreads=8
  gradle runClient -Pport=9099
  ```

## Screencast

Include **one** short screencast (≤ 4 minutes) that demonstrates:

1. **Task 1** (`runTask1`): two clients connecting sequentially and performing add/display/count/quit.
2. **Task 2** (`runTask2`): two clients issuing `add` simultaneously, showing overlapping “Start add”/“end add.”
3. **Task 3** (`runTask3 -PmaxThreads=2`): three clients connecting; the third waits until one of the first two quits.
4. **Display** and **Count** operations returning correct, up‑to‑date results.
5. Shared state remains intact (no lost or overwritten entries).

Screencast link:  
https://youtu.be/PrVJwWzwNBk
