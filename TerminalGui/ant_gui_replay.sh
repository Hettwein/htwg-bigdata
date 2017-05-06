#!/bin/bash
while true; do 
	curl -X GET http://127.0.0.1:9100/replay
	sleep 1
	echo "----------------------------------------------------------"
done
