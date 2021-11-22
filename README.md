# duolingo-streak-saver

## Overview

As an OCD Duolingo user, I needed to find a way to save my precious streak even when I forget to buy a streak freeze manually, hence this project.

The app is composed of two parts:
- A scheduled batch that buys a streak freeze for every user present in the database.
- REST APIs to save and delete user credentials, as well to perform some basic admin tasks.

The scheduled batch is run by default every night at 00:10.
User credentials are stored with encrypted passwords.


## Requirements

- Java >= 11
- Maven >= 3.0
- MongoDB >= 3.6

The Maven build creates a runnable .jar, to be run without command line arguments.

