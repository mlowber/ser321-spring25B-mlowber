# Movie Guessing Game (Client-Server)

This is a simple Java-based client-server game where a single player guesses movie titles based on pixelated images. The server manages game logic, timing, image delivery, and a persistent leaderboard. The client receives images and responds with guesses or commands like "next", "skip", or "remaining".

---

## Requirements Checklist

| Feature | Implemented |
|--------|-------------|
| Gradle run commands | Yes |
| `start` → Server says hello | Yes |
| `name` → Server greets user | Yes |
| Main menu: play, leaderboard, quit | Yes |
| Length selection: short/medium/long | Yes |
| Server sends base64-encoded images | Yes |
| Guess validation on server | Yes |
| Image difficulty increases with "next" | Yes |
| "skip" works with limited use | Yes |
| "remaining" shows skips left | Yes |
| Timer-based game ending | Yes |
| Score calculation | Yes |
| Leaderboard display | Yes |
| Leaderboard persistence to `leaderboard.json` | Yes |
| JSON protocol with header & payload | Yes |
| Robust error handling | Yes |
| Graceful quit with image | Yes |
| Port configurable via command-line args | Yes |

---

## Protocol Specification

Each message is a JSON object sent over a TCP socket.
Messages have a `type` (header) and optional `value`, `message`, `score`, `leaderboard`, or `image` fields (payload).

### Request Flow

start → name → choice → length → guess/next/skip/remaining

### Request Types

| Type | Client Sends | Server Responds |
|------|--------------|-----------------|
| `start` | — | `"type": "hello"`, ask for name, send welcome image |
| `name` | `"value": <username>` | Welcome message, menu image |
| `choice` | `"value": play/leaderboard/quit` | Menu response or leaderboard |
| `length` | `"value": short/medium/long` | Starts timer, sends first image |
| `guess` | `"value": <title>` | Correct/incorrect response, image update |
| `next` | — | Higher resolution image, or message if max clarity |
| `skip` | — | New movie, or error if no skips left |
| `remaining` | — | Skips remaining count |
| `timeout` | — | Final score, return to menu |
| `error` | Malformed or unexpected input | JSON with `"type": "error"` and message |

### Response Fields

- `type`: Always present. Indicates message purpose.
- `value`: Message string shown to user.
- `message`: Optional additional message.
- `image`: Base64-encoded PNG image.
- `imageFormat`: Always `"png"`.
- `leaderboard`: JSONArray with name/score pairs.
- `score`: Sent on timeout or game end.

---

## Robustness

- Handles malformed JSON with graceful errors.
- Commands validated on the server to prevent crashes.
- Persistent game state resets when needed (e.g., after leaderboard or timeout).
- Images are base64-encoded dynamically and safely transmitted.
- Leaderboard is written to disk after every new high score.
- Supports changing port via CLI args: `java SockServer 9000` and `java ClientGui localhost 9000`.

---

## What would change if UDP was used?

If UDP replaced TCP:
- Reliability would no longer be guaranteed.
- We'd need manual acknowledgment and retry logic.
- Image transmission would require fragmentation or compression.
- Sequence numbers or timestamps would be needed to track state.
- Simpler interactions like guesses could work, but file/image transfers would be fragile.

---

## Demo Video

Link: _https://youtu.be/Uv0YBEHsEXE_  
Length: 4–7 minutes

---

## Leaderboard File

Scores are stored in `leaderboard.json` on the server. They are updated after each correct guess or game end.

Example format:

```json
{
  "Alice": 266.66,
  "Bob": 150.00
}
```
---