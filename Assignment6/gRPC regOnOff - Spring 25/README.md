# gRPC Distributed Systems Assignment

## 1. Project Description and Requirements Fulfilled

This project implements a gRPC-based node and client that support the following services:

* **Built-in services**: Echo, Joke, Sort, CoffeePot
* **Custom services (Task 2)**: Key–Value Store (KVStore) and To-Do List (Todo)

It fulfills the assignment requirements:

* Interactive menu-driven client (Task 1)
* Auto-run mode calling all services without registry (Task 1)
* Two new services with server-side state (Task 2)
* Deployment on AWS EC2 with public IP (Task 3)

## 2. How to Run the Program

1. Build and generate stubs:

   ```bash
   gradle clean build
   ```
2. Start the server (Node) locally:

   ```bash
   gradle runNode
   ```
3. Run the client in interactive mode:

   ```bash
   gradle runClient
   ```
4. Run the client in auto-run mode:

   ```bash
   gradle runClient -Pauto=1
   ```

## 3. How to Use the Client How to Use the Client

* After running `gradle runClient` (interactive), you will see a numbered menu.
* Enter a number to select a service:

    * **Echo**: type a message, press Enter
    * **Joke**: getJoke asks for count; setJoke asks for text
    * **Sort**: enter comma-separated integers
    * **CoffeePot**: sub-menu for brew, status, getCup, or run all
    * **KVStore**: sub-menu for put (key and value) and get (key)
    * **To-Do List**: sub-menu for add task (text), list tasks, mark done (id)
* Enter `0` at any menu level to go back or exit.

## 4. Requirements Checklist

* [x] Interactive, menu-driven client (Task 1)
* [x] Auto-run mode without registry (Task 1)
* [x] Built-in gRPC services implemented (Task 1)
* [x] Two custom services (KVStore, Todo) with server state (Task 2)
* [x] Deployed on AWS EC2 with public IP and background run (Task 3)

## 5. Screencast

Include a short video demonstrating:

1. Building and running locally
2. Interactive menu use
3. Auto-run mode output
4. Remote client connecting to AWS EC2 node

Link: https://youtu.be/pclTS7yPCwI
