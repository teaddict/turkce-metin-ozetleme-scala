FROM mongo

COPY init.json /init.json
CMD mongoimport --host mongo --db summarydb --collection summary --type json --file /init.json --jsonArray