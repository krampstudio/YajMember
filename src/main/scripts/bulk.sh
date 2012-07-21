#!/bin/sh

appcfg.py upload_data --config_file=map.yaml --filename=events.csv --kind=Event --url=http://localhost:8888/remote_api
