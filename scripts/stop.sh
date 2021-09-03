#!/bin/bash

ps -ef | grep streaksaver | grep -v grep | awk '{print $2}' | xargs kill -9 &> kill.txt &