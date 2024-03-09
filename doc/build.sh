#!/bin/bash
set -e

docker run -it --rm -v ${PWD}:/docs mkdocs-material-weight build --site-dir output
