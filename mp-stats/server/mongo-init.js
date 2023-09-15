var td = new Date();
var statsDbName = "mp-stats";
var statsDB = db.getSiblingDB(statsDbName);
statsDB.log.insertOne({
    "time": td.toDateString() + ", " + td.toTimeString(),
    "message": "Database created with name: " + statsDbName
});

