#!/bin/bash
set -e

docker run -it --rm -v ${PWD}:/docs -p 8000:8000 mkdocs-material-weight serve -a 0.0.0.0:8000
