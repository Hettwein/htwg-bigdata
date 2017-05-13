#!/bin/bash
curl -H "Content-Type: application/json" -X GET -d '{"collection":"collection7","x":"80","y":"80","timestep":"2000"}' http://localhost:9500/grid -o ~/Schreibtisch/simulation.json

