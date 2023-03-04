var td = new Date();
var dbName = "mp-stats";
db.log.insertOne([
    {"time": td.toDateString() + " " + td.toTimeString()},
    {"message": "Database created with name: " + dbName}
]);
db.createUser(
    {
        user: "root",
        pwd: "root",
        roles: [
            {
                role: "readWrite",
                db: dbName
            }
        ]
    }
);
