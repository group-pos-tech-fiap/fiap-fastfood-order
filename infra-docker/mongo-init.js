db = db.getSiblingDB('db-fiap-fastfood-order');

db.createUser(
    {
        user: "admin",
        pwd: "FIAPauth123_",
        roles: [
            {
                role: "readWrite",
                db: "db-fiap-fastfood-order"
            }
        ]
    }
)

db.createCollection("Order");