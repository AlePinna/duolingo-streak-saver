#!/bin/bash

service mongod start & java -jar /home/ec2-user/streaksaver-1.0.0.jar &> streaksaver.log &