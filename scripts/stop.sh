#!/bin/bash

pwd &> /home/ec2-user/directory.txt & ps -ef | grep streaksaver | grep -v grep | awk '{print $2}' | xargs kill &