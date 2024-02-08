#!/bin/bash

# Remove former distributions
rm dist/redacted_py-*
rm dist/redacted-py-*

# Publish library to PyPI

python3 -m build
python3 -m twine upload dist/*
