#!/bin/sh

appcfg.py upload_data \
	--config_file=map.yaml --filename=events.csv --kind=Event \ 
	--url=http://localhost:8888/remote_api --email=a@b.c --passin

appcfg.py upload_data \
	--config_file=map.yaml --filename=membership.csv --kind=Membership \
	 --url=http://localhost:8888/remote_api --email=a@b.c --passin
						
appcfg.py upload_data \
	--config_file=map.yaml --filename=member.csv --kind=Member \
	 --url=http://localhost:8888/remote_api --email=a@b.c --passin
