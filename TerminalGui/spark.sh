#!/bin/bash
curl -H "Content-Type: application/json" -X POST -d '{"collection":"collection6","x":"20","y":"20","timestep":"1000"}' http://localhost:9500/grid -o ~/desktop/simulation.json

