# Assignment 5 - Distributed Algorithms
**Author:** Matthew Lowber  
**Class:** SER 321 Spring 2025

---

## a) Commands to Run

You need to **start the components in this order**:

1. **Start 3 Nodes on different ports:**

    ```bash
    gradle runNode6000
    gradle runNode6001
    gradle runNode6002
    ```

    Each command should be run in a separate terminal.

    You can optionally simulate a faulty node using:

    ```bash
    gradle runNode6000 -PFault=1
    ```

2. **Start the Leader:**

    ```bash
    gradle runLeader
    ```

3. **Start the Client:**

    ```bash
    gradle runClient
    ```

---

## b) Program Purpose and Functionality

This project implements a simple distributed system to compute the sum of a list of numbers provided by a Client.  
The computation is distributed across multiple Nodes, coordinated by a Leader.  
The system also includes basic fault simulation and a consensus verification step to validate results.

Key functionality:
- Distributed computation across multiple Nodes
- Threaded parallel communication
- Fault tolerance testing
- Consensus check for result correctness

---

## c) Protocol Description

The system uses a simple **custom TCP socket protocol** based on Java Object Streams:

- **Client → Leader:**  
  Sends a List of Integers and a delay time (in milliseconds).
  
- **Leader → Nodes:**  
  Splits the list into chunks and sends each chunk along with the delay.

- **Nodes → Leader:**  
  Each Node calculates the sum of its chunk and returns the partial sum.

- **Leader (Consensus Check):**  
  After collecting partial sums, the Leader asks each Node to verify another Node's result by recomputing the sum and responding true/false.

If all Nodes agree during consensus, the final distributed sum is accepted.  
Otherwise, an error is reported.

---

## d) Intended Workflow (What Was Implemented)

The intended and implemented workflow:

1. Client sends list and delay to Leader.
2. Leader splits list and sends chunks to 3 different Nodes.
3. Nodes compute partial sums with a delay between additions.
4. Leader collects partial sums using threaded connections.
5. Leader runs a second threaded round to perform consensus checking among Nodes.
6. Leader finalizes and prints the distributed total if consensus succeeds.
7. Leader compares the distributed result with a local single-threaded sum for performance comparison.

---

## e) Requirements Fulfilled

| Feature | Status |
|:---|:---|
| Buildable Gradle Project | x |
| Client can send numbers and delay | x |
| Leader splits list and distributes tasks | x |
| 3 Nodes run and compute partial sums | x |
| Threaded communication for Nodes | x |
| Simulate faulty nodes with `-pFault=1` | x |
| Consensus check implemented | x |
| Leader compares distributed vs local sum | x |
| Error handling for consensus failure | x |

---

## f) Screencast Plan (Demo Checklist)

Screencast Link: https://youtu.be/8qxI7WBYOVI

In my screencast video, I will demonstrate:

- Building and running the project using Gradle commands.
- Starting 3 Nodes on ports 6000, 6001, and 6002.
- Running the Leader and waiting for the Client.
- Running the Client, inputting a list and delay.
- Showing that the Leader:
  - Splits the list
  - Distributes the chunks
  - Receives and combines partial sums
- Showing Consensus success (all nodes agree).
- Showing Distributed sum vs Local sum timing comparison.
- Simulating a Faulty Node (`-pFault=1`) and showing Consensus failure message.

---
