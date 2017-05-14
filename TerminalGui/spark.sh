#!/bin/bash
curl -H "Content-Type: application/json" -X POST -d '{"collection":"collection5","x":"50","y":"50","timestep":"3000"}' http://localhost:9500/grid -o ~/desktop/simulation.json

